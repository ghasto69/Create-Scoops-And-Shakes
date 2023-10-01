package com.ghasto.create_scoops_and_shakes;

import com.chocohead.mm.api.ClassTinkerers;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import io.github.tropheusj.milk.Milk;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;

public class ModRecipeGen {
    public static void generateAll(RegistrateRecipeProvider p) {
        getMixingBuilder("honey_and_milk_to_chocolate")
                .require(FluidIngredient.fromFluid(AllFluids.HONEY.get(), FluidConstants.fromBucketFraction(1, 2)))
                .require(FluidIngredient.fromFluid(Milk.STILL_MILK, FluidConstants.fromBucketFraction(1, 2)))
                .output(new FluidStack(AllFluids.CHOCOLATE.get(), FluidConstants.BUCKET))
                .requiresHeat(ClassTinkerers.getEnum(HeatCondition.class, "FREEZING"))
                .build(p);
    }
    private static ProcessingRecipeBuilder<MixingRecipe> getMixingBuilder(String id) {
        ProcessingRecipeSerializer<MixingRecipe> MixingSerialiser = AllRecipeTypes.MIXING.getSerializer();
        return new ProcessingRecipeBuilder<MixingRecipe>(MixingSerialiser.getFactory(), CreateScoopsAndShakes.id(id));
    }
}
