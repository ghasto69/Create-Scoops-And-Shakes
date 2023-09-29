package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import com.simibubi.create.foundation.fluid.CombinedTankWrapper;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class IceCreamJarBlockEntity extends SmartBlockEntity implements SidedStorageBlockEntity {
	Storage<FluidVariant> fluidStorage;
	SmartFluidTankBehaviour fluidTankBehaviour;
	boolean contentsChanged;
	public IceCreamJarBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		fluidTankBehaviour = new SmartFluidTankBehaviour
				(SmartFluidTankBehaviour.TYPE, this, 1, (FluidConstants.BUCKET/2), true)
				.whenFluidUpdates(() -> contentsChanged = true);
		fluidStorage = fluidTankBehaviour.getCapability();
		behaviours.add(fluidTankBehaviour);
	}

	@Nullable
	@Override
	public Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
		return fluidStorage;
	}

	@Override
	public void tick() {
		super.tick();
		if (!contentsChanged)
			return;

		contentsChanged = false;
		sendData();
	}
}
