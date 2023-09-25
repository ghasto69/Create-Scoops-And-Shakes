package com.ghasto.create_scoops_and_shakes.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import com.ghasto.create_scoops_and_shakes.ModPartialModels;
import com.ghasto.create_scoops_and_shakes.ModSpriteShifts;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerRenderer;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(BlazeBurnerRenderer.class)
public abstract class MixinBlazeBurnerRenderer {
    @Unique
    private static BlazeBurnerBlock.HeatLevel COOLED = ClassTinkerers.getEnum(BlazeBurnerBlock.HeatLevel.class, "COOLED");
    @Unique
    private static BlazeBurnerBlock.HeatLevel FREEZING = ClassTinkerers.getEnum(BlazeBurnerBlock.HeatLevel.class, "FREEZING");
    /**
     * @author
     * @reason
     */
    @Overwrite
    private static void renderShared(PoseStack ms, @Nullable PoseStack modelTransform, MultiBufferSource bufferSource,
                                     Level level, BlockState blockState, BlazeBurnerBlock.HeatLevel heatLevel, float animation, float horizontalAngle,
                                     boolean canDrawFlame, boolean drawGoggles, boolean drawHat, int hashCode) {

        boolean blockAbove = animation > 0.125f;
        float time = AnimationTickHolder.getRenderTime(level);
        float renderTick = time + (hashCode % 13) * 16f;
        float offsetMult = heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING) ? 64 : 16;
        float offset = Mth.sin((float) ((renderTick / 16f) % (2 * Math.PI))) / offsetMult;
        float offset1 = Mth.sin((float) ((renderTick / 16f + Math.PI) % (2 * Math.PI))) / offsetMult;
        float offset2 = Mth.sin((float) ((renderTick / 16f + Math.PI / 2) % (2 * Math.PI))) / offsetMult;
        float headY = offset - (animation * .75f);

        VertexConsumer solid = bufferSource.getBuffer(RenderType.solid());
        VertexConsumer cutout = bufferSource.getBuffer(RenderType.cutoutMipped());

        ms.pushPose();

        if (canDrawFlame && blockAbove) {
            SpriteShiftEntry spriteShift =
                    heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllSpriteShifts.SUPER_BURNER_FLAME : AllSpriteShifts.BURNER_FLAME;
            if(heatLevel.isAtLeast(COOLED)) {
                spriteShift = heatLevel == FREEZING ? ModSpriteShifts.FREEZING_BURNER_FLAME : AllSpriteShifts.SUPER_BURNER_FLAME;
            }

            float spriteWidth = spriteShift.getTarget()
                    .getU1()
                    - spriteShift.getTarget()
                    .getU0();

            float spriteHeight = spriteShift.getTarget()
                    .getV1()
                    - spriteShift.getTarget()
                    .getV0();

            float speed = 1 / 32f + 1 / 64f * heatLevel.ordinal();

            double vScroll = speed * time;
            vScroll = vScroll - Math.floor(vScroll);
            vScroll = vScroll * spriteHeight / 2;

            double uScroll = speed * time / 2;
            uScroll = uScroll - Math.floor(uScroll);
            uScroll = uScroll * spriteWidth / 2;

            SuperByteBuffer flameBuffer = CachedBufferer.partial(AllPartialModels.BLAZE_BURNER_FLAME, blockState);
            if (modelTransform != null)
                flameBuffer.transform(modelTransform);
            flameBuffer.shiftUVScrolling(spriteShift, (float) uScroll, (float) vScroll);
            draw(flameBuffer, horizontalAngle, ms, cutout);
        }

        PartialModel blazeModel;
        if(heatLevel.isAtLeast(FREEZING)) {
            blazeModel = blockAbove ? ModPartialModels.BLAZE_FREEZING_ACTIVE : ModPartialModels.BLAZE_FREEZING;
        } else if(heatLevel.isAtLeast(COOLED)) {
            blazeModel = blockAbove ? ModPartialModels.BLAZE_COOLED_ACTIVE : ModPartialModels.BLAZE_COOLED;
        } else if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.SEETHING)) {
            blazeModel = blockAbove ? AllPartialModels.BLAZE_SUPER_ACTIVE : AllPartialModels.BLAZE_SUPER;
        } else if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            blazeModel = blockAbove && heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.KINDLED) ? AllPartialModels.BLAZE_ACTIVE
                    : AllPartialModels.BLAZE_IDLE;
        } else {
            blazeModel = AllPartialModels.BLAZE_INERT;
        }

        SuperByteBuffer blazeBuffer = CachedBufferer.partial(blazeModel, blockState);
        if (modelTransform != null)
            blazeBuffer.transform(modelTransform);
        blazeBuffer.translate(0, headY, 0);
        draw(blazeBuffer, horizontalAngle, ms, solid);

        if (drawGoggles) {
            PartialModel gogglesModel = blazeModel == AllPartialModels.BLAZE_INERT
                    ? AllPartialModels.BLAZE_GOGGLES_SMALL : AllPartialModels.BLAZE_GOGGLES;

            SuperByteBuffer gogglesBuffer = CachedBufferer.partial(gogglesModel, blockState);
            if (modelTransform != null)
                gogglesBuffer.transform(modelTransform);
            gogglesBuffer.translate(0, headY + 8 / 16f, 0);
            draw(gogglesBuffer, horizontalAngle, ms, solid);
        }

        if (drawHat) {
            SuperByteBuffer hatBuffer = CachedBufferer.partial(AllPartialModels.TRAIN_HAT, blockState);
            if (modelTransform != null)
                hatBuffer.transform(modelTransform);
            hatBuffer.translate(0, headY, 0);
            if (blazeModel == AllPartialModels.BLAZE_INERT) {
                hatBuffer.translateY(0.5f)
                        .centre()
                        .scale(0.75f)
                        .unCentre();
            } else {
                hatBuffer.translateY(0.75f);
            }
            hatBuffer
                    .rotateCentered(Direction.UP, horizontalAngle + Mth.PI)
                    .translate(0.5f, 0, 0.5f)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);
        }

        if (heatLevel.isAtLeast(BlazeBurnerBlock.HeatLevel.FADING)) {
            PartialModel rodsModel = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllPartialModels.BLAZE_BURNER_SUPER_RODS
                    : AllPartialModels.BLAZE_BURNER_RODS;
            PartialModel rodsModel2 = heatLevel == BlazeBurnerBlock.HeatLevel.SEETHING ? AllPartialModels.BLAZE_BURNER_SUPER_RODS_2
                    : AllPartialModels.BLAZE_BURNER_RODS_2;
            if(heatLevel.isAtLeast(COOLED)) {
                rodsModel = heatLevel == FREEZING ? ModPartialModels.BLAZE_BURNER_FREEZING_RODS
                        : AllPartialModels.BLAZE_BURNER_SUPER_RODS;
                rodsModel2 = heatLevel == FREEZING ? ModPartialModels.BLAZE_BURNER_FREEZING_RODS_2
                        : AllPartialModels.BLAZE_BURNER_SUPER_RODS_2;
            }

            SuperByteBuffer rodsBuffer = CachedBufferer.partial(rodsModel, blockState);
            if (modelTransform != null)
                rodsBuffer.transform(modelTransform);
            rodsBuffer.translate(0, offset1 + animation + .125f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);

            SuperByteBuffer rodsBuffer2 = CachedBufferer.partial(rodsModel2, blockState);
            if (modelTransform != null)
                rodsBuffer2.transform(modelTransform);
            rodsBuffer2.translate(0, offset2 + animation - 3 / 16f, 0)
                    .light(LightTexture.FULL_BRIGHT)
                    .renderInto(ms, solid);
        }

        ms.popPose();
    }
    @Unique
    private static void draw(SuperByteBuffer buffer, float horizontalAngle, PoseStack ms, VertexConsumer vc) {
        buffer.rotateCentered(Direction.UP, horizontalAngle)
                .light(LightTexture.FULL_BRIGHT)
                .renderInto(ms, vc);
    }
}
