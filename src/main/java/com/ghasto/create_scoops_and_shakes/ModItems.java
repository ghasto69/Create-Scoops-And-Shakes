package com.ghasto.create_scoops_and_shakes;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

import com.ghasto.create_scoops_and_shakes.block.breeze_cooler.BreezeCoolerBlockItem;
import com.ghasto.create_scoops_and_shakes.util.DontShowInTabItem;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItems {
    public static final ItemEntry<? extends Item> ICON_ITEM =
            REGISTRATE.item("icon_item", DontShowInTabItem::new)
                    .model(AssetLookup.customGenericItemModel("blaze_burner", "tab_icon"))
                    .recipe((c,p)->{ModRecipeGen.generateAll(p);})
                    .register(),
			EMPTY_BREEZE_COOLER = REGISTRATE.item("empty_breeze_cooler", BreezeCoolerBlockItem::empty)
					.model(AssetLookup.customBlockItemModel("breeze_cooler", "block"))
					.register();

    public static void register() {}
}

