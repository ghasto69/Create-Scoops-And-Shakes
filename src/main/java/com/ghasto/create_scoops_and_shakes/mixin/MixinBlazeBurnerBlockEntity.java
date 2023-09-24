package com.ghasto.create_scoops_and_shakes.mixin;


import com.chocohead.mm.api.ClassTinkerers;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity;

import com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity.FuelType;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;

import io.github.fabricators_of_create.porting_lib.transfer.callbacks.TransactionCallback;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlazeBurnerBlockEntity.class)
public abstract class MixinBlazeBurnerBlockEntity extends SmartBlockEntity {
	@Shadow
	protected FuelType activeFuel;

	@Shadow
	protected int remainingBurnTime;

	@Shadow protected boolean isCreative;

	@Shadow public abstract BlazeBurnerBlock.HeatLevel getHeatLevelFromBlock();

	@Shadow public abstract void spawnParticleBurst(boolean soulFlame);

	@Shadow protected abstract void playSound();

	@Shadow protected abstract void setBlockHeat(BlazeBurnerBlock.HeatLevel heat);

	@Shadow public abstract void updateBlockState();

	@Shadow @Final public static int INSERTION_THRESHOLD;

	@Shadow @Final public static int MAX_HEAT_CAPACITY;

	public MixinBlazeBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	/**
	 * @author Ghasto
	 * @reason Feed blaze burner snowballs to make it COOLED, or feed it ice cream to make it FREEZING
	 */
	@Overwrite
	protected boolean tryUpdateFuel(ItemStack itemStack, boolean forceOverflow, TransactionContext ctx) {
		if (isCreative)
			return false;

		FuelType newFuel = FuelType.NONE;
		int newBurnTime;
		if(itemStack.is(Items.SNOWBALL)) {
			newFuel = ClassTinkerers.getEnum(FuelType.class, "COOLED");
			newBurnTime = 1600;
		} else if (itemStack.is(Items.DIAMOND)) {
			newFuel = ClassTinkerers.getEnum(FuelType.class, "COOLED");
			newBurnTime = 3200;
		} else if (AllTags.AllItemTags.BLAZE_BURNER_FUEL_SPECIAL.matches(itemStack)) {
			newBurnTime = 3200;
			newFuel = FuelType.SPECIAL;
		} else {
			Integer fuel = FuelRegistry.INSTANCE.get(itemStack.getItem());
			newBurnTime = fuel == null ? 0 : fuel;
			if (newBurnTime > 0) {
				newFuel = FuelType.NORMAL;
			} else if (AllTags.AllItemTags.BLAZE_BURNER_FUEL_REGULAR.matches(itemStack)) {
				newBurnTime = 1600; // Same as coal
				newFuel = FuelType.NORMAL;
			}
		}

		if (newFuel == FuelType.NONE)
			return false;
		if (newFuel.ordinal() < activeFuel.ordinal())
			return false;

		if (newFuel == activeFuel) {
			if (remainingBurnTime <= INSERTION_THRESHOLD) {
				newBurnTime += remainingBurnTime;
			} else if (forceOverflow && newFuel == FuelType.NORMAL) {
				if (remainingBurnTime < MAX_HEAT_CAPACITY) {
					newBurnTime = Math.min(remainingBurnTime + newBurnTime, MAX_HEAT_CAPACITY);
				} else {
					newBurnTime = remainingBurnTime;
				}
			} else {
				return false;
			}
		}

		FuelType finalNewFuel = newFuel;
		int finalNewBurnTime = newBurnTime;
		TransactionCallback.onSuccess(ctx, () -> {
			activeFuel = finalNewFuel;
			remainingBurnTime = finalNewBurnTime;
			if (level.isClientSide) {
				boolean soulFlame = false;
				if(activeFuel == FuelType.SPECIAL) {
					soulFlame = true;
				}
				if(activeFuel == ClassTinkerers.getEnum(FuelType.class, "COOLED") && remainingBurnTime > 1600)
				spawnParticleBurst(soulFlame);
				return;
			}
			BlazeBurnerBlock.HeatLevel prev = getHeatLevelFromBlock();
			playSound();
			updateBlockState();

			if (prev != getHeatLevelFromBlock())
				level.playSound(null, worldPosition, SoundEvents.BLAZE_AMBIENT, SoundSource.BLOCKS,
						.125f + level.random.nextFloat() * .125f, 1.15f - level.random.nextFloat() * .25f);
		});

		return true;
	}
	/**
	 * @author Ghasto
	 * @reason Cooling & Freezing
	 */
	@Overwrite(remap = false)
	protected BlazeBurnerBlock.HeatLevel getHeatLevel() {
		BlazeBurnerBlock.HeatLevel level = BlazeBurnerBlock.HeatLevel.SMOULDERING;
		if(activeFuel == FuelType.SPECIAL) {
			level = BlazeBurnerBlock.HeatLevel.SEETHING;
		} else if (activeFuel == ClassTinkerers.getEnum(FuelType.class, "COOLED") && remainingBurnTime > 1600) {
			level = ClassTinkerers.getEnum(BlazeBurnerBlock.HeatLevel.class, "FREEZING");
		} else if (activeFuel == ClassTinkerers.getEnum(FuelType.class, "COOLED") && remainingBurnTime <= 1600) {
			level = ClassTinkerers.getEnum(BlazeBurnerBlock.HeatLevel.class, "COOLED");
		} else if(activeFuel == FuelType.NORMAL) {
			boolean lowPercent = (double) remainingBurnTime / MAX_HEAT_CAPACITY < 0.0125;
			level = lowPercent ? BlazeBurnerBlock.HeatLevel.FADING : BlazeBurnerBlock.HeatLevel.KINDLED;
		}
		return level;
	}
	@Inject(method = "tick", at = @At("TAIL"), remap = false)
	public void create_scoops_and_shakes$tick(CallbackInfo ci) {
		if(remainingBurnTime <= 1600) {
			if(activeFuel == ClassTinkerers.getEnum(FuelType.class, "COOLED")) {
				updateBlockState();
			}
		}
	}
}
