package com.ghasto.create_scoops_and_shakes.block.blender;

import com.ghasto.create_scoops_and_shakes.ModRecipeTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.potion.PotionMixingRecipes;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;

import com.simibubi.create.content.kinetics.mixer.MixerInstance;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import com.simibubi.create.foundation.block.IBE;

import com.simibubi.create.infrastructure.config.AllConfigs;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("UnstableApiUsage")
public class BlenderBlockEntity extends MechanicalMixerBlockEntity {
	public BlenderBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected List<Recipe<?>> getMatchingRecipes() {
		List<Recipe<?>> matchingRecipes = new ArrayList<>();

		Optional<BasinBlockEntity> basin = getBasin();
		if (!basin.isPresent())
			return matchingRecipes;

		BasinBlockEntity basinBlockEntity = basin.get();
		if (basin.isEmpty())
			return matchingRecipes;

		Storage<ItemVariant> availableItems = basinBlockEntity
				.getItemStorage(null);
		if (availableItems == null)
			return matchingRecipes;
		if(getSpeed() < 96)
			return super.getMatchingRecipes();
		List<Recipe<Container>> blendingRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.BLENDING.getType());
		blendingRecipes.forEach(recipe -> {
			if(matchBasinRecipe(recipe)) {
				matchingRecipes.add(recipe);
			}
		});
		return matchingRecipes;
	}

	@Override
	protected Optional<BasinBlockEntity> getBasin() {
			if (level == null)
				return Optional.empty();
			BlockEntity basinBE = level.getBlockEntity(worldPosition.above());
			if (!(basinBE instanceof BasinBlockEntity))
				return Optional.empty();
			return Optional.of((BasinBlockEntity) basinBE);
		}

	@Override
	public void tick() {
		if(getBasin().isPresent()) {
			if(getMatchingRecipes().isEmpty())
				return;
			running = true;
		}
		super.tick();
	}
}
