package com.ghasto.create_scoops_and_shakes.block.breeze_cooler;

import com.chocohead.mm.api.ClassTinkerers;
import com.ghasto.create_scoops_and_shakes.ModFluids;
import com.ghasto.create_scoops_and_shakes.ModItems;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.fluids.tank.FluidTankBlock;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class BreezeCoolerBlockEntity extends SmartBlockEntity {

	public static final int MAX_HEAT_CAPACITY = 10000;
	public static final int INSERTION_THRESHOLD = 500;

	protected CoolerMode activeFuel;
	protected int remainingcoolingTime;
	protected LerpedFloat headAnimation;
	protected LerpedFloat headAngle;
	protected boolean isCreative;
	protected boolean goggles;
	protected boolean hat;

	public BreezeCoolerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		activeFuel = CoolerMode.NONE;
		remainingcoolingTime = 0;
		headAnimation = LerpedFloat.linear();
		headAngle = LerpedFloat.angular();
		isCreative = false;
		goggles = false;

		headAngle.startWithValue((AngleHelper.horizontalAngle(state.getOptionalValue(BreezeCoolerBlock.FACING)
				.orElse(Direction.SOUTH)) + 180) % 360);
	}

	public CoolerMode getActiveFuel() {
		return activeFuel;
	}

	public int getRemainingcoolingTime() {
		return remainingcoolingTime;
	}

	public boolean isCreative() {
		return isCreative;
	}

	@Override
	public void tick() {
		super.tick();

		if (level.isClientSide) {
			tickAnimation();
			if (!isVirtual())
				spawnParticles(getCoolerLevelFromBlock(), 1);
			return;
		}

		if (isCreative)
			return;

		if (remainingcoolingTime > 0)
			remainingcoolingTime--;

		if (activeFuel == CoolerMode.NORMAL)
			updateBlockState();
		if (remainingcoolingTime > 0)
			return;

		if (activeFuel == CoolerMode.SPECIAL) {
			activeFuel = CoolerMode.NORMAL;
			remainingcoolingTime = MAX_HEAT_CAPACITY / 2;
		} else
			activeFuel = CoolerMode.NONE;

		updateBlockState();
	}

	@Environment(EnvType.CLIENT)
	private void tickAnimation() {
		boolean active = getCoolerLevelFromBlock().isAtLeast(BreezeCoolerBlock.CoolerLevel.FADING) && isValidBlockAbove();

		if (!active) {
			float target = 0;
			LocalPlayer player = Minecraft.getInstance().player;
			if (player != null && !player.isInvisible()) {
				double x;
				double z;
				if (isVirtual()) {
					x = -4;
					z = -10;
				} else {
					x = player.getX();
					z = player.getZ();
				}
				double dx = x - (getBlockPos().getX() + 0.5);
				double dz = z - (getBlockPos().getZ() + 0.5);
				target = AngleHelper.deg(-Mth.atan2(dz, dx)) - 90;
			}
			target = headAngle.getValue() + AngleHelper.getShortestAngleDiff(headAngle.getValue(), target);
			headAngle.chase(target, .25f, LerpedFloat.Chaser.exp(5));
			headAngle.tickChaser();
		} else {
			headAngle.chase((AngleHelper.horizontalAngle(getBlockState().getOptionalValue(BreezeCoolerBlock.FACING)
					.orElse(Direction.SOUTH)) + 180) % 360, .125f, LerpedFloat.Chaser.EXP);
			headAngle.tickChaser();
		}

		headAnimation.chase(active ? 1 : 0, .25f, LerpedFloat.Chaser.exp(.25f));
		headAnimation.tickChaser();
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		if (!isCreative) {
			compound.putInt("fuelLevel", activeFuel.ordinal());
			compound.putInt("coolingTimeRemaining", remainingcoolingTime);
		} else
			compound.putBoolean("isCreative", true);
		if (goggles)
			compound.putBoolean("Goggles", true);
		if (hat)
			compound.putBoolean("TrainHat", true);
		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		activeFuel = CoolerMode.values()[compound.getInt("fuelLevel")];
		remainingcoolingTime = compound.getInt("coolingTimeRemaining");
		isCreative = compound.getBoolean("isCreative");
		goggles = compound.contains("Goggles");
		hat = compound.contains("TrainHat");
		super.read(compound, clientPacket);
	}

	public BreezeCoolerBlock.CoolerLevel getCoolerLevelFromBlock() {
		return BreezeCoolerBlock.getCoolerLevelOf(getBlockState());
	}

	public void updateBlockState() {
		setBlockHeat(getCoolerLevel());
	}

	protected void setBlockHeat(BreezeCoolerBlock.CoolerLevel heat) {
		BreezeCoolerBlock.CoolerLevel inBlockState = getCoolerLevelFromBlock();
		if (inBlockState == heat)
			return;
		level.setBlockAndUpdate(worldPosition, getBlockState().setValue(BreezeCoolerBlock.COOLER_LEVEL, heat));
		notifyUpdate();
	}

	/**
	 * @return true if the heater updated its cooling time and an item should be
	 *         consumed
	 */
	protected boolean tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, TransactionContext ctx) {
		if (isCreative)
			return false;

		CoolerMode newFuel = CoolerMode.NONE;
		int newcoolingTime;

		if (itemStack.is(Items.DIAMOND)) {
			newcoolingTime = 3200;
			newFuel = CoolerMode.SPECIAL;
		} else {
			Integer fuel = FuelRegistry.INSTANCE.get(itemStack.getItem());
			newcoolingTime = fuel == null ? 0 : fuel;
			if (newcoolingTime > 0) {
				newFuel = CoolerMode.NORMAL;
			} else if (itemStack.is(Items.SNOWBALL)) {
				newcoolingTime = 1600; // Same as coal
				newFuel = CoolerMode.NORMAL;
			}
		}

		if (newFuel == CoolerMode.NONE)
			return false;
		if (newFuel.ordinal() < activeFuel.ordinal())
			return false;

		if (newFuel == activeFuel) {
			if (remainingcoolingTime <= INSERTION_THRESHOLD) {
				newcoolingTime += remainingcoolingTime;
			} else if (forceOverflow && newFuel == CoolerMode.NORMAL) {
				if (remainingcoolingTime < MAX_HEAT_CAPACITY) {
					newcoolingTime = Math.min(remainingcoolingTime + newcoolingTime, MAX_HEAT_CAPACITY);
				} else {
					newcoolingTime = remainingcoolingTime;
				}
			} else {
				return false;
			}
		}

		CoolerMode finalNewFuel = newFuel;
		int finalNewcoolingTime = newcoolingTime;
		TransactionCallback.onSuccess(ctx, () -> {
			activeFuel = finalNewFuel;
			remainingcoolingTime = finalNewcoolingTime;
			if (level.isClientSide) {
				spawnParticleBurst(activeFuel == CoolerMode.SPECIAL);
				return;
			}
			BreezeCoolerBlock.CoolerLevel prev = getCoolerLevelFromBlock();
			playSound();
			updateBlockState();

			if (prev != getCoolerLevelFromBlock())
				level.playSound(null, worldPosition, SoundEvents.BLAZE_AMBIENT, SoundSource.BLOCKS,
						.125f + level.random.nextFloat() * .125f, 1.15f - level.random.nextFloat() * .25f);
		});

		return true;
	}

	protected void applyCreativeFuel() {
		activeFuel = CoolerMode.NONE;
		remainingcoolingTime = 0;
		isCreative = true;

		BreezeCoolerBlock.CoolerLevel next = getCoolerLevelFromBlock().nextActiveLevel();

		if (level.isClientSide) {
			spawnParticleBurst(next.isAtLeast(BreezeCoolerBlock.CoolerLevel.FREEZING));
			return;
		}

		playSound();
		if (next == BreezeCoolerBlock.CoolerLevel.FADING)
			next = next.nextActiveLevel();
		setBlockHeat(next);
	}

	public boolean isCreativeFuel(ItemStack stack) {
		return AllItems.CREATIVE_BLAZE_CAKE.isIn(stack);
	}

	public boolean isValidBlockAbove() {
		if (isVirtual())
			return false;
		BlockState blockState = level.getBlockState(worldPosition.above());
		return AllBlocks.BASIN.has(blockState) || blockState.getBlock() instanceof FluidTankBlock;
	}

	protected void playSound() {
		level.playSound(null, worldPosition, SoundEvents.BLAZE_SHOOT, SoundSource.BLOCKS,
				.125f + level.random.nextFloat() * .125f, .75f - level.random.nextFloat() * .25f);
	}

	protected BreezeCoolerBlock.CoolerLevel getCoolerLevel() {
		BreezeCoolerBlock.CoolerLevel level = BreezeCoolerBlock.CoolerLevel.SMOULDERING;
		switch (activeFuel) {
			case SPECIAL:
				level = BreezeCoolerBlock.CoolerLevel.FREEZING;
				break;
			case NORMAL:
				boolean lowPercent = (double) remainingcoolingTime / MAX_HEAT_CAPACITY < 0.0125;
				level = lowPercent ? BreezeCoolerBlock.CoolerLevel.FADING : BreezeCoolerBlock.CoolerLevel.KINDLED;
				break;
			default:
			case NONE:
				break;
		}
		return level;
	}

	protected void spawnParticles(BreezeCoolerBlock.CoolerLevel CoolerLevel, double burstMult) {
		if (level == null)
			return;
		if (CoolerLevel == BreezeCoolerBlock.CoolerLevel.NONE)
			return;

		RandomSource r = level.getRandom();

		Vec3 c = VecHelper.getCenterOf(worldPosition);
		Vec3 v = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .125f)
				.multiply(1, 0, 1));

		if (r.nextInt(4) != 0)
			return;

		boolean empty = level.getBlockState(worldPosition.above())
				.getCollisionShape(level, worldPosition.above())
				.isEmpty();

		if (empty || r.nextInt(8) == 0)
			level.addParticle(ParticleTypes.LARGE_SMOKE, v.x, v.y, v.z, 0, 0, 0);

		double yMotion = empty ? .0625f : r.nextDouble() * .0125f;
		Vec3 v2 = c.add(VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
						.multiply(1, .25f, 1)
						.normalize()
						.scale((empty ? .25f : .5) + r.nextDouble() * .125f))
				.add(0, .5, 0);

		if (CoolerLevel.isAtLeast(BreezeCoolerBlock.CoolerLevel.FREEZING)) {
			level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, v2.x, v2.y, v2.z, 0, yMotion, 0);
		} else if (CoolerLevel.isAtLeast(BreezeCoolerBlock.CoolerLevel.FADING)) {
			level.addParticle(ParticleTypes.FLAME, v2.x, v2.y, v2.z, 0, yMotion, 0);
		}
		return;
	}

	public void spawnParticleBurst(boolean soulFlame) {
		Vec3 c = VecHelper.getCenterOf(worldPosition);
		RandomSource r = level.random;
		for (int i = 0; i < 20; i++) {
			Vec3 offset = VecHelper.offsetRandomly(Vec3.ZERO, r, .5f)
					.multiply(1, .25f, 1)
					.normalize();
			Vec3 v = c.add(offset.scale(.5 + r.nextDouble() * .125f))
					.add(0, .125, 0);
			Vec3 m = offset.scale(1 / 32f);

			level.addParticle(soulFlame ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, v.x, v.y, v.z, m.x, m.y,
					m.z);
		}
	}

	public enum CoolerMode {
		NONE, NORMAL, SPECIAL
	}

}
