package com.ghasto.create_scoops_and_shakes;

import static net.minecraft.world.item.Items.BUCKET;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.FluidBuilder;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.util.entry.FluidEntry;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.EmptyItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.Fluid;

@SuppressWarnings("UnstableApiUsage")
public class ModFluids {
	public static final FluidEntry<SimpleFlowableFluid.Flowing>
			VANILLA_ICE_CREAM = iceCreamFluid("vanilla").register(),
			CHOCOLATE_ICE_CREAM = iceCreamFluid("chocolate").register(),
			BERRY_ICE_CREAM = iceCreamFluid("berry").register();


	public static FluidBuilder<SimpleFlowableFluid.Flowing, CreateRegistrate> standardFluid(String name) {
 		return CreateScoopsAndShakes.REGISTRATE.fluid(name, CreateScoopsAndShakes.id("block/fluid/" + name + "_still"), CreateScoopsAndShakes.id("block/fluid/" + name + "_flow"));
	}
	public static FluidBuilder<SimpleFlowableFluid.Flowing, CreateRegistrate> iceCreamFluid(String name) {
		return standardFluid(name + "_ice_cream")
				.fluidProperties(p -> p.levelDecreasePerBlock(2)
						.tickRate(25)
						.flowSpeed(4)
						.blastResistance(100f))
				.onRegisterAfter(Registries.ITEM, icecream -> {
					Fluid source = icecream.getSource();
					// transfer values
					FluidStorage.combinedItemApiProvider(source.getBucket()).register(context ->
							new FullItemFluidStorage(context, bucket -> ItemVariant.of(BUCKET), FluidVariant.of(source), FluidConstants.BUCKET));
					FluidStorage.combinedItemApiProvider(BUCKET).register(context ->
							new EmptyItemFluidStorage(context, bucket -> ItemVariant.of(source.getBucket()), source, FluidConstants.BUCKET));
				});
	}
	public static void register() {}
}
