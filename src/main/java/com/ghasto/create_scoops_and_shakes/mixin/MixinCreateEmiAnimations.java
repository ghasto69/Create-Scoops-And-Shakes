package com.ghasto.create_scoops_and_shakes.mixin;

import com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes;
import com.ghasto.create_scoops_and_shakes.ModBlocks;
import com.ghasto.create_scoops_and_shakes.ModPartialModels;
import com.ghasto.create_scoops_and_shakes.ModSpriteShifts;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.compat.emi.CreateEmiAnimations;

import com.simibubi.create.compat.emi.recipes.basin.BasinEmiRecipe;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;

import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateEmiAnimations.class)
public class MixinCreateEmiAnimations {
	@Inject(method = "addBlazeBurner",remap = false, at = @At("HEAD"),cancellable = true)
	private static void inject(WidgetHolder widgets, int x, int y, BlazeBurnerBlock.HeatLevel heatLevel, CallbackInfo ci){
		if(heatLevel.isAtLeast(CreateScoopsAndShakes.COOLING_LEVEL)){
			addBreezeCooler(widgets, x, y, heatLevel);
			ci.cancel();
		}
	}
	private static void addBreezeCooler(WidgetHolder widgets, int x, int y, BlazeBurnerBlock.HeatLevel heatLevel) {
		widgets.addDrawable(x, y, 0, 0, (graphics, mouseX, mouseY, delta) -> {
			PoseStack matrixStack = graphics.pose();
			matrixStack.translate(0, 0, 200);
			matrixStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(-15.5f));
			matrixStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(22.5f));
			int scale = 23;

			float offset = (Mth.sin(AnimationTickHolder.getRenderTime() / 16f) + 0.5f) / 16f;

			CreateEmiAnimations.blockElement(ModBlocks.BREEZE_COOLER.getDefaultState()).atLocal(0, 1.65, 0)
					.scale(scale)
					.render(graphics);

			PartialModel breeze =
					heatLevel == CreateScoopsAndShakes.FREEZING_LEVEL ? ModPartialModels.breeze_FREEZING : ModPartialModels.breeze_ACTIVE;

			CreateEmiAnimations.blockElement(breeze).atLocal(1, 1.8, 1)
					.rotate(0, 180, 0)
					.scale(scale)
					.render(graphics);

			matrixStack.scale(scale, -scale, scale);
			matrixStack.translate(0, -1.8, 0);

			SpriteShiftEntry spriteShift =
					heatLevel == CreateScoopsAndShakes.FREEZING_LEVEL ? ModSpriteShifts.COOLER_EFFECT_FREEZING : ModSpriteShifts.COOLER_EFFECT;

			float spriteWidth = spriteShift.getTarget()
					.getU1()
					- spriteShift.getTarget()
					.getU0();

			float spriteHeight = spriteShift.getTarget()
					.getV1()
					- spriteShift.getTarget()
					.getV0();

			float time = AnimationTickHolder.getRenderTime(Minecraft.getInstance().level);
			float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();

			double vScroll = speed * time;
			vScroll = vScroll - Math.floor(vScroll);
			vScroll = vScroll * spriteHeight / 2;

			double uScroll = speed * time / 2;
			uScroll = uScroll - Math.floor(uScroll);
			uScroll = uScroll * spriteWidth / 2;

			Minecraft mc = Minecraft.getInstance();
			MultiBufferSource.BufferSource buffer = mc.renderBuffers()
					.bufferSource();
			VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
			CachedBufferer.partial(ModPartialModels.breeze_cooler_effect, Blocks.AIR.defaultBlockState())
					.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll)
					.light(LightTexture.FULL_BRIGHT)
					.renderInto(matrixStack, vb);
		});
	}
}
