package com.ghasto.create_scoops_and_shakes;

import com.simibubi.create.AllFluids;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;

import com.simibubi.create.foundation.fluid.SmartFluidTank;
import com.simibubi.create.foundation.utility.NBTHelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.material.Fluid;

import org.apache.commons.lang3.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;

public class CreateScoopsAndShakesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModPartialModels.register();ModSpriteShifts.init();
	}
}
