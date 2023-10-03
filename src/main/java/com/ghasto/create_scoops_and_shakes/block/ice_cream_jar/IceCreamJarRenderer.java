package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import com.ghasto.create_scoops_and_shakes.ModBlockEntities;
import com.ghasto.create_scoops_and_shakes.ModBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.fluid.FluidRenderer;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class IceCreamJarRenderer {
	public static class IceCreamJarBlockRenderer extends SmartBlockEntityRenderer<IceCreamJarBlockEntity> {
		public IceCreamJarBlockRenderer(BlockEntityRendererProvider.Context context) {
			super(context);
		}

		@Override
		protected void renderSafe(IceCreamJarBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
			SmartFluidTankBehaviour fluidTank = be.getBehaviour(SmartFluidTankBehaviour.TYPE);
			FluidStack renderedFluid = fluidTank.getPrimaryTank().getRenderedFluid();
			LerpedFloat fluidLevel = fluidTank.getPrimaryTank().getFluidLevel();
			float xMin = 4.6f / 16f;
			float xMax = 11.4f / 16f;
			final float yMin = 1 / 16f;
			final float yMax = yMin + 5 / 16f * fluidLevel.getValue();
			final float zMin = 4.6f / 16f;
			final float zMax = 11.4f / 16f;
			FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, bufferSource, ms, light, true);
		}
	}
	public static class IceCreamJarItemRenderer extends CustomRenderedItemModelRenderer {
		@Override
		protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer, ItemDisplayContext transformType, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
			renderer.render(model.getOriginalModel(), light);
			IceCreamJarBlockEntity be = new IceCreamJarBlockEntity(ModBlockEntities.ICE_CREAM_JAR.get(), BlockPos.ZERO, ModBlocks.ICE_CREAM_JAR.getDefaultState());
			if(stack.getTag() == null)
				return;
			be.readClient(stack.getTag());
			SmartFluidTankBehaviour fluidTank = be.getBehaviour(SmartFluidTankBehaviour.TYPE);
			FluidStack renderedFluid = fluidTank.getPrimaryTank().getRenderedFluid();
			LerpedFloat fluidLevel = fluidTank.getPrimaryTank().getFluidLevel();
			float xMin = -3.4f / 16f;
			float xMax = 3.4f / 16f;
			final float yMin = -7 / 16f;
			final float yMax = -2 / 16f * fluidLevel.getValue(); //<-- Appears to not work
			final float zMin = -3.4f / 16f;
			final float zMax = 3.4f / 16f;
			FluidRenderer.renderFluidBox(renderedFluid, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, true);
		}
	}
}
