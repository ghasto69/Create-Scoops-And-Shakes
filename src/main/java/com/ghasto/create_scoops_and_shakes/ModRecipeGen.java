package com.ghasto.create_scoops_and_shakes;

import com.chocohead.mm.api.ClassTinkerers;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CrushingRecipeGen;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import io.github.tropheusj.milk.Milk;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalFluidTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.FREEZING_CONDITION;
import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.FREEZING_LEVEL;

@SuppressWarnings("UnstableApiUsage")
public class ModRecipeGen {
    public static void generateAll(RegistrateRecipeProvider p) {
		getMixingBuilder("vanilla_ice_cream")
				.require(FluidIngredient.fromTag(ConventionalFluidTags.MILK, FluidConstants.BUCKET))
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.output(new FluidStack(ModFluids.VANILLA_ICE_CREAM.get(), FluidConstants.BUCKET))
				.duration(150)
				.requiresHeat(FREEZING_CONDITION)
				.build(p);

    }
	public static Ingredient itemIngredient(ItemLike item, int count) {
		return Ingredient.of(new ItemStack(item, count));
	}
    private static ProcessingRecipeBuilder<MixingRecipe> getMixingBuilder(String id) {
        ProcessingRecipeSerializer<MixingRecipe> MixingSerialiser = AllRecipeTypes.MIXING.getSerializer();
        return new ProcessingRecipeBuilder<MixingRecipe>(MixingSerialiser.getFactory(), CreateScoopsAndShakes.id(id));
    }
}
