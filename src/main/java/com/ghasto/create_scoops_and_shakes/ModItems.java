package com.ghasto.create_scoops_and_shakes;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

import com.ghasto.create_scoops_and_shakes.util.DontShowInTabItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;

import net.minecraft.world.item.Item;

public class ModItems {
    public static final ItemEntry<? extends Item> ICON_ITEM =
            REGISTRATE.item("icon_item", DontShowInTabItem::new)
                    .model(AssetLookup.customGenericItemModel("blaze_burner", "tab_icon"))
                    .recipe((c,p)->{ModRecipeGen.generateAll(p);})
                    .register();
    public static void register() {}
}

