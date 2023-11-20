package com.ghasto.create_scoops_and_shakes.item.ice_cream_cone;

import org.jetbrains.annotations.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IceCreamConeItem extends Item {
	int scoops_count = 0;
	public IceCreamConeItem(Properties properties) {
		super(properties);

	}

	@Nullable
	@Override
	public FoodProperties getFoodProperties() {
        return new FoodProperties.Builder()
				.nutrition(scoops_count)
				.saturationMod(scoops_count*0.5f)
				.build();
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		int scoopsCountTemporary = 0;
		if(stack.getTag() != null) {
			if (stack.getTag().contains("scoop1"))
				scoopsCountTemporary++;
			if (stack.getTag().contains("scoop2"))
				scoopsCountTemporary++;
			if (stack.getTag().contains("scoop3"))
				scoopsCountTemporary++;
		}
		scoops_count = scoopsCountTemporary;
	}
}
