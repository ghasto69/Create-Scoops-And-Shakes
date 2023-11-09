package com.ghasto.create_scoops_and_shakes.block.breeze_cooler;

import com.ghasto.create_scoops_and_shakes.ModBlockEntities;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import com.simibubi.create.foundation.block.IBE;

import com.simibubi.create.foundation.utility.AdventureUtil;
import com.simibubi.create.foundation.utility.Lang;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BreezeCoolerBlock extends HorizontalDirectionalBlock implements IBE<BreezeCoolerBlockEntity>, IWrenchable {

	public static final EnumProperty<CoolerLevel> COOLER_LEVEL = EnumProperty.create("blaze", CoolerLevel.class);

	public BreezeCoolerBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(COOLER_LEVEL, CoolerLevel.NONE));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(COOLER_LEVEL, FACING);
	}

	@Override
	public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
		if (world.isClientSide)
			return;
		BlockEntity blockEntity = world.getBlockEntity(pos.above());
		if (!(blockEntity instanceof BasinBlockEntity))
			return;
		BasinBlockEntity basin = (BasinBlockEntity) blockEntity;
		basin.notifyChangeOfContents();
	}

	@Override
	public Class<BreezeCoolerBlockEntity> getBlockEntityClass() {
		return BreezeCoolerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends BreezeCoolerBlockEntity> getBlockEntityType() {
		return ModBlockEntities.BREEZE_COOLER.get();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		if (state.getValue(COOLER_LEVEL) == CoolerLevel.NONE)
			return null;
		return IBE.super.newBlockEntity(pos, state);
	}

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand,
								 BlockHitResult blockRayTraceResult) {
		ItemStack heldItem = player.getItemInHand(hand);
		CoolerLevel COOLING = state.getValue(COOLER_LEVEL);

		if (AllItems.GOGGLES.isIn(heldItem) && COOLING != CoolerLevel.NONE)
			return onBlockEntityUse(world, pos, bbte -> {
				if (bbte.goggles)
					return InteractionResult.PASS;
				bbte.goggles = true;
				bbte.notifyUpdate();
				return InteractionResult.SUCCESS;
			});

		if (AdventureUtil.isAdventure(player))
			return InteractionResult.PASS;

		if (heldItem.isEmpty() && COOLING != CoolerLevel.NONE)
			return onBlockEntityUse(world, pos, bbte -> {
				if (!bbte.goggles)
					return InteractionResult.PASS;
				bbte.goggles = false;
				bbte.notifyUpdate();
				return InteractionResult.SUCCESS;
			});

//		if (COOLING == CoolerLevel.NONE) {
//			if (heldItem.getItem() instanceof FlintAndSteelItem) {
//				world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F,
//						world.random.nextFloat() * 0.4F + 0.8F);
//				if (world.isClientSide)
//					return InteractionResult.SUCCESS;
//				heldItem.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
//				world.setBlockAndUpdate(pos, AllBlocks.LIT_BLAZE_BURNER.getDefaultState());
//				return InteractionResult.SUCCESS;
//			}
//			return InteractionResult.PASS;
//		}

		boolean doNotConsume = player.isCreative();
		boolean forceOverflow = !(player.isFake());
		try (Transaction t = TransferUtil.getTransaction()) {
			InteractionResultHolder<ItemStack> res =
					tryInsert(state, world, pos, heldItem, doNotConsume, forceOverflow, t);
			t.commit();
			ItemStack leftover = res.getObject();
			if (!world.isClientSide && !doNotConsume && !leftover.isEmpty()) {
				if (heldItem.isEmpty()) {
					player.setItemInHand(hand, leftover);
				} else if (!player.getInventory()
						.add(leftover)) {
					player.drop(leftover, false);
				}
			}

			return res.getResult() == InteractionResult.SUCCESS ? InteractionResult.SUCCESS : InteractionResult.PASS;
		}
	}

	public static InteractionResultHolder<ItemStack> tryInsert(BlockState state, Level world, BlockPos pos,
															   ItemStack stack, boolean doNotConsume, boolean forceOverflow, TransactionContext ctx) {
		if (!state.hasBlockEntity())
			return InteractionResultHolder.fail(ItemStack.EMPTY);

		BlockEntity be = world.getBlockEntity(pos);
		if (!(be instanceof BreezeCoolerBlockEntity))
			return InteractionResultHolder.fail(ItemStack.EMPTY);
		BreezeCoolerBlockEntity burnerBE = (BreezeCoolerBlockEntity) be;

		if (burnerBE.isCreativeFuel(stack)) {
			TransactionCallback.onSuccess(ctx, burnerBE::applyCreativeFuel);
			return InteractionResultHolder.success(ItemStack.EMPTY);
		}
		if (!burnerBE.tryUpdateFuel(stack, forceOverflow, ctx))
			return InteractionResultHolder.fail(ItemStack.EMPTY);

		if (!doNotConsume) {
			ItemStack container = stack.getRecipeRemainder();
			if (!world.isClientSide) {
				stack.shrink(1);
			}
			return InteractionResultHolder.success(container);
		}
		return InteractionResultHolder.success(ItemStack.EMPTY);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		ItemStack stack = context.getItemInHand();
		Item item = stack.getItem();
		BlockState defaultState = defaultBlockState();
		if (!(item instanceof BlazeBurnerBlockItem))
			return defaultState;
		CoolerLevel initialCOOLING =
				((BlazeBurnerBlockItem) item).hasCapturedBlaze() ? CoolerLevel.SMOULDERING : CoolerLevel.NONE;
		return defaultState.setValue(COOLER_LEVEL, initialCOOLING)
				.setValue(FACING, context.getHorizontalDirection()
						.getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
		return AllShapes.HEATER_BLOCK_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
										CollisionContext p_220071_4_) {
		if (p_220071_4_ == CollisionContext.empty())
			return AllShapes.HEATER_BLOCK_SPECIAL_COLLISION_SHAPE;
		return getShape(p_220071_1_, p_220071_2_, p_220071_3_, p_220071_4_);
	}

	@Override
	public boolean hasAnalogOutputSignal(BlockState p_149740_1_) {
		return true;
	}

	@Override
	public int getAnalogOutputSignal(BlockState state, Level p_180641_2_, BlockPos p_180641_3_) {
		return Math.max(0, state.getValue(COOLER_LEVEL)
				.ordinal() - 1);
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Environment(EnvType.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (random.nextInt(10) != 0)
			return;
		if (!state.getValue(COOLER_LEVEL)
				.isAtLeast(CoolerLevel.SMOULDERING))
			return;
		world.playLocalSound((double) ((float) pos.getX() + 0.5F), (double) ((float) pos.getY() + 0.5F),
				(double) ((float) pos.getZ() + 0.5F), SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS,
				0.5F + random.nextFloat(), random.nextFloat() * 0.7F + 0.6F, false);
	}

	public static CoolerLevel getCoolerLevelOf(BlockState blockState) {
		return blockState.hasProperty(BreezeCoolerBlock.COOLER_LEVEL) ? blockState.getValue(BreezeCoolerBlock.COOLER_LEVEL)
				: CoolerLevel.NONE;
	}

	public static int getLight(BlockState state) {
		CoolerLevel level = state.getValue(COOLER_LEVEL);
		return switch (level) {
			case NONE -> 0;
			case SMOULDERING -> 8;
			default -> 15;
		};
	}

	public static LootTable.Builder buildLootTable() {
		LootItemCondition.Builder survivesExplosion = ExplosionCondition.survivesExplosion();
		com.simibubi.create.content.processing.burner.BlazeBurnerBlock block = AllBlocks.BLAZE_BURNER.get();
		LootTable.Builder builder = LootTable.lootTable();
		LootPool.Builder poolBuilder = LootPool.lootPool();
		for (CoolerLevel level : CoolerLevel.values()) {
			ItemLike drop = level == CoolerLevel.NONE ? AllItems.EMPTY_BLAZE_BURNER.get() : AllBlocks.BLAZE_BURNER.get();
			poolBuilder.add(LootItem.lootTableItem(drop)
					.when(survivesExplosion)
					.when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
							.setProperties(StatePropertiesPredicate.Builder.properties()
									.hasProperty(COOLER_LEVEL, level))));
		}
		builder.withPool(poolBuilder.setRolls(ConstantValue.exactly(1)));
		return builder;
	}

	public enum CoolerLevel implements StringRepresentable {
		NONE, SMOULDERING, FADING, KINDLED, FREEZING,;

		public static CoolerLevel byIndex(int index) {
			return values()[index];
		}

		public CoolerLevel nextActiveLevel() {
			return byIndex(ordinal() % (values().length - 1) + 1);
		}

		public boolean isAtLeast(CoolerLevel COOLINGLevel) {
			return this.ordinal() >= COOLINGLevel.ordinal();
		}

		@Override
		public String getSerializedName() {
			return Lang.asId(name());
		}
	}

}

