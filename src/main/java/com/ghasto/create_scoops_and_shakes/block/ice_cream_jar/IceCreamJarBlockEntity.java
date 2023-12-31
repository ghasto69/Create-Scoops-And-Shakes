package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import com.simibubi.create.AllFluids;
import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;

import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SidedStorageBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class IceCreamJarBlockEntity extends SmartBlockEntity implements SidedStorageBlockEntity {
	Storage<FluidVariant> fluidStorage;
	public SmartFluidTankBehaviour fluidTankBehaviour;

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

	@Override
	public void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
	}

	@Nullable
	@Override
	public Storage<FluidVariant> getFluidStorage(@Nullable Direction face) {
		return fluidStorage;
	}

	@Override
	public void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
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
