package com.ghasto.create_scoops_and_shakes;

import com.simibubi.create.AllTags;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class ModTags {
	public enum ModItemTags{
		;
		private TagKey<Item> tag;
		ModItemTags() {
			tag = TagKey.create(BuiltInRegistries.ITEM.key(), CreateScoopsAndShakes.id(name().toLowerCase()));
		}
		public TagKey<Item> get() {
			return tag;
		}
	}
	public enum ModFluidTags{
		ICE_CREAM
		;
		private TagKey<Fluid> tag;
		ModFluidTags() {
			tag = TagKey.create(BuiltInRegistries.FLUID.key(), CreateScoopsAndShakes.id(name().toLowerCase()));
		}
		public TagKey<Fluid> get() {
			return tag;
		}
	}
}
