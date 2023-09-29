package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import com.ghasto.create_scoops_and_shakes.ModBlockEntities;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IceCreamJarBlock extends Block implements IBE<IceCreamJarBlockEntity> {

	public IceCreamJarBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<IceCreamJarBlockEntity> getBlockEntityClass() {
		return IceCreamJarBlockEntity.class;
	}
	@Override
	public BlockEntityType<? extends IceCreamJarBlockEntity> getBlockEntityType() {
		return ModBlockEntities.ICE_CREAM_JAR.get();
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}
