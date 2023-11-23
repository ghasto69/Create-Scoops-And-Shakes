package com.ghasto.create_scoops_and_shakes.mixin;

import com.ghasto.create_scoops_and_shakes.ModBlockEntities;
import com.ghasto.create_scoops_and_shakes.ModBlocks;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.AllFluids;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.fluids.potion.PotionFluidHandler;
import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.simibubi.create.content.fluids.transfer.GenericItemFilling;

import com.simibubi.create.foundation.fluid.FluidHelper;

import io.github.fabricators_of_create.porting_lib.transfer.TransferUtil;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import net.minecraft.world.level.material.Fluid;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GenericItemFilling.class)
public abstract class MixinGenericItemFilling {
	@Shadow
	protected static boolean canFillGlassBottleInternally(FluidStack availableFluid) {
		return false;
	}

	@Shadow
	public static boolean isFluidHandlerValid(ItemStack stack, Storage<FluidVariant> fluidHandler) {
		return false;
	}
	/**
	 * @author
	 * @reason
	 */
	@Overwrite
	public static boolean canItemBeFilled(Level world, ItemStack stack) {
		if (stack.getItem() == Items.GLASS_BOTTLE)
			return true;
		if (stack.getItem() == Items.MILK_BUCKET)
			return false;
		if(stack.getItem() == ModBlocks.ICE_CREAM_JAR.asItem())
			return true;

		Storage<FluidVariant> tank = FluidStorage.ITEM.find(stack, ContainerItemContext.withInitial(stack));
		if (tank == null)
			return false;
		if (!isFluidHandlerValid(stack, tank))
			return false;
		return tank.supportsInsertion();
	}
	/**
	 * @author
	 * @reason
	 */
	@Overwrite(remap = false)
	public static ItemStack fillItem(Level world, long requiredAmount, ItemStack stack, FluidStack availableFluid) {
		FluidStack toFill = availableFluid.copy();
		toFill.setAmount(requiredAmount);
		availableFluid.shrink(requiredAmount);

		if (stack.getItem() == Items.GLASS_BOTTLE && canFillGlassBottleInternally(toFill)) {
			ItemStack fillBottle = ItemStack.EMPTY;
			Fluid fluid = toFill.getFluid();
			if (FluidHelper.isWater(fluid))
				fillBottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
			else if (fluid.isSame(AllFluids.TEA.get()))
				fillBottle = AllItems.BUILDERS_TEA.asStack();
			else
				fillBottle = PotionFluidHandler.fillBottle(stack, toFill);
			stack.shrink(1);
			return fillBottle;
		}
		ItemStack split = stack.copy();
		split.setCount(1);
		ContainerItemContext ctx = ContainerItemContext.withConstant(split);
		Storage<FluidVariant> tank = FluidStorage.ITEM.find(split, ctx);
		if (tank == null)
			return ItemStack.EMPTY;
		try (Transaction t = TransferUtil.getTransaction()) {
			tank.insert(toFill.getType(), toFill.getAmount(), t);
			t.commit();

			ItemStack container = ctx.getItemVariant().toStack((int) ctx.getAmount());
			stack.shrink(1);
			return container;
		}
	}
}
