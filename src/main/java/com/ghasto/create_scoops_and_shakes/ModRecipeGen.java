package com.ghasto.create_scoops_and_shakes;

import com.chocohead.mm.api.ClassTinkerers;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.mixer.MixingRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeSerializer;
import com.simibubi.create.foundation.data.recipe.CrushingRecipeGen;
import com.simibubi.create.foundation.data.recipe.FillingRecipeGen;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.tterrag.registrate.fabric.SimpleFlowableFluid;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;

import com.tterrag.registrate.util.entry.FluidEntry;

import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import io.github.tropheusj.milk.Milk;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalFluidTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.COOLING_CONDITION;
import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.FREEZING_CONDITION;
import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.FREEZING_LEVEL;
import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

@SuppressWarnings("UnstableApiUsage")
public class ModRecipeGen {
    public static void generateAll(RegistrateRecipeProvider p) {
		getMixingBuilder("vanilla_ice_cream")
				.require(FluidIngredient.fromTag(ConventionalFluidTags.MILK, FluidConstants.BUCKET))
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.output(ModFluids.VANILLA_ICE_CREAM.get(), FluidConstants.BUCKET)
				.duration(150)
				.requiresHeat(FREEZING_CONDITION)
				.build(p);

		iceCreamRecipe("chocolate", ModFluids.CHOCOLATE_ICE_CREAM.get(), Items.COCOA_BEANS, p);
		iceCreamRecipe("berry", ModFluids.BERRY_ICE_CREAM.get(), Items.SWEET_BERRIES, p);

		iceCreamJarFilling("vanilla", ModFluids.VANILLA_ICE_CREAM, p);
    }
	public static Ingredient itemIngredient(ItemLike item, int count) {
		return Ingredient.of(new ItemStack(item, count));
	}
    private static ProcessingRecipeBuilder<MixingRecipe> getMixingBuilder(String id) {
        ProcessingRecipeSerializer<MixingRecipe> MixingSerialiser = AllRecipeTypes.MIXING.getSerializer();
        return new ProcessingRecipeBuilder<MixingRecipe>(MixingSerialiser.getFactory(), CreateScoopsAndShakes.id(id));
    }
	private static void iceCreamJarFilling(String id, FluidEntry<SimpleFlowableFluid.Flowing> fluid, RegistrateRecipeProvider p) {
		getFillingBuilder("jar_filling_"+id+"_ice_cream")
				.require(FluidIngredient.fromFluid(fluid.get(), 40500))
				.require(ModBlocks.ICE_CREAM_JAR)
				.output(getFilledJar(new FluidStack(fluid.get(), 40500)))
				.build(p);
	}
	public static ItemStack getFilledJar(FluidStack fluid) {
		ItemStack item = ModBlocks.ICE_CREAM_JAR.asStack();
		IceCreamJarBlockEntity be = new IceCreamJarBlockEntity(ModBlockEntities.ICE_CREAM_JAR.get(), BlockPos.ZERO, ModBlocks.ICE_CREAM_JAR.getDefaultState());
		be.fluidTankBehaviour.getPrimaryHandler().setFluid(fluid);
		CompoundTag data = new CompoundTag();
		be.write(data,false);
		item.setTag(data);
		item.getOrCreateTagElement("BlockEntityTag").put("Tanks", data.get("Tanks"));
		MutableComponent hovername = fluid.getDisplayName().copy().append(" ").append(item.getHoverName());
		item.setHoverName(hovername);
		return item;
	}
	private static ProcessingRecipeBuilder<FillingRecipe> getFillingBuilder(String id){
		ProcessingRecipeSerializer<FillingRecipe> fillingSerialiser = AllRecipeTypes.FILLING.getSerializer();
		return new ProcessingRecipeBuilder<FillingRecipe>(fillingSerialiser.getFactory(), CreateScoopsAndShakes.id(id));
	}
	private static void iceCreamRecipe(String name, Fluid result, Item specialIngredient,RegistrateRecipeProvider p){
		getMixingBuilder(name+"_ice_cream")
				.require(FluidIngredient.fromTag(ConventionalFluidTags.MILK, FluidConstants.BUCKET))
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.require(Items.SUGAR)
				.require(specialIngredient)
				.require(specialIngredient)
				.require(specialIngredient)
				.output(new FluidStack(result, FluidConstants.BUCKET))
				.duration(150)
				.requiresHeat(FREEZING_CONDITION)
				.build(p);
		getMixingBuilder(name+"_ice_cream"+"_from_vanilla")
				.require(FluidIngredient.fromFluid(ModFluids.VANILLA_ICE_CREAM.get(), FluidConstants.BUCKET))
				.require(specialIngredient)
				.require(specialIngredient)
				.require(specialIngredient)
				.requiresHeat(COOLING_CONDITION)
				.output(new FluidStack(result, FluidConstants.BUCKET))
				.duration(250)
				.build(p);
	}
}
