package com.ghasto.create_scoops_and_shakes.block.blender;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.ghasto.create_scoops_and_shakes.ModRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
		if (getSpeed() < 96)
			return super.getMatchingRecipes();
		List<Recipe<Container>> blendingRecipes = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.BLENDING.getType());
		blendingRecipes.forEach(recipe -> {
			if (matchBasinRecipe(recipe)) {
				matchingRecipes.add(recipe);
			}
		});
		return matchingRecipes;
	}
}
