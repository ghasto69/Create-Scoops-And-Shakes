package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import com.ghasto.create_scoops_and_shakes.ModBlockEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GlassBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.NotNull;

public class IceCreamJarBlock extends Block implements IBE<IceCreamJarBlockEntity> {
	public static final VoxelShape SHAPE = box(4, 0, 4, 13, 9, 13);
	public IceCreamJarBlock(Properties properties) {
		super(properties);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
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
		return RenderShape.MODEL;
	}

}
