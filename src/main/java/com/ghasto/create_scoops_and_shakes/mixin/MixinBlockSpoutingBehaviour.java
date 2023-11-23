package com.ghasto.create_scoops_and_shakes.mixin;

import com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.JarFillingBehaviour;
import com.simibubi.create.api.behaviour.BlockSpoutingBehaviour;

import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockSpoutingBehaviour.class)
public abstract class MixinBlockSpoutingBehaviour {
	@Shadow
	public static void addCustomSpoutInteraction(ResourceLocation resourceLocation, BlockSpoutingBehaviour movementBehaviour) {
	}

	@Inject(method = "registerDefaults", at = @At("HEAD"),remap = false)
	private static void registerDefaultsInject(CallbackInfo ci){
		addCustomSpoutInteraction(CreateScoopsAndShakes.id("jar_filling"), new JarFillingBehaviour());
	}
}
