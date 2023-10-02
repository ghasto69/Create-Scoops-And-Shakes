package com.ghasto.create_scoops_and_shakes.block.blender;

import com.ghasto.create_scoops_and_shakes.ModBlockEntities;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;

import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlenderBlock extends MechanicalMixerBlock {
	public BlenderBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntityType<? extends MechanicalMixerBlockEntity> getBlockEntityType() {
		return ModBlockEntities.BLENDER.get();
	}
}
