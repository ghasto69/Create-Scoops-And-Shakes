package com.ghasto.create_scoops_and_shakes.block.blender;

import com.ghasto.create_scoops_and_shakes.ModRecipeTypes;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;

public class BlendingRecipe extends BasinRecipe {
	public BlendingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(ModRecipeTypes.BLENDING, params);
	}
}
