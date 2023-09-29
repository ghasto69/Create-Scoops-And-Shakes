package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.processing.basin.BasinRenderer;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;

import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;

import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class IceCreamJarRenderer extends SmartBlockEntityRenderer<IceCreamJarBlockEntity> {
	public IceCreamJarRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}
	@Override
	protected void renderSafe(IceCreamJarBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
		SmartFluidTankBehaviour fluidTank = be.getBehaviour(SmartFluidTankBehaviour.TYPE);
		FluidStack renderedFluid = fluidTank.getPrimaryTank().getRenderedFluid();
		LerpedFloat fluidLevel = fluidTank.getPrimaryTank().getFluidLevel();
		float xMin = 2 / 16f;
		float xMax = 14 / 16f;
		final float yMin = 2 / 16f;
		final float yMax = yMin + 12 / 16f * fluidLevel.getValue();
		final float zMin = 2 / 16f;
		final float zMax = 14 / 16f;
		FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax,bufferSource,ms,light,false);
	}
}
