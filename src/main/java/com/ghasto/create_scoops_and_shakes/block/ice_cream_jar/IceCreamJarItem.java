package com.ghasto.create_scoops_and_shakes.block.ice_cream_jar;

import java.util.List;

import io.github.fabricators_of_create.porting_lib.transfer.fluid.item.FluidHandlerItemStack;
import io.github.fabricators_of_create.porting_lib.util.FluidStack;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.base.FullItemFluidStorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import org.jetbrains.annotations.Nullable;

import com.ghasto.create_scoops_and_shakes.ModBlockEntities;
import com.ghasto.create_scoops_and_shakes.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class IceCreamJarItem extends BlockItem{

	public IceCreamJarItem(Block block, Properties properties) {
		super(block, properties);
	}
	private ContainerItemContext context;
	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		IceCreamJarBlockEntity be = new IceCreamJarBlockEntity(ModBlockEntities.ICE_CREAM_JAR.get(), BlockPos.ZERO, ModBlocks.ICE_CREAM_JAR.getDefaultState());
		if(stack.getTag() == null)
			return;
		be.readClient(stack.getTag());
		tooltipComponents.add(Component.literal(be.fluidTankBehaviour.getPrimaryTank().getRenderedFluid().getAmount() / 81 + "Mb ")
				.append(be.fluidTankBehaviour.getPrimaryTank().getRenderedFluid().getDisplayName()));
	}
}
