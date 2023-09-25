package com.ghasto.create_scoops_and_shakes;

import com.ghasto.create_scoops_and_shakes.item.TabIconItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

public class ModItems {
    public static final ItemEntry<? extends Item> ICON_ITEM =
            REGISTRATE.item("icon_item", p -> new TabIconItem(AllBlocks.BLAZE_BURNER.get(), p))
                    .model(AssetLookup.customBlockItemModel("blaze_burner", "tab_icon"))
                    .recipe((c,p)->{ModRecipeGen.generateAll(p);})
                    .register();
    public static void register() {}
}

