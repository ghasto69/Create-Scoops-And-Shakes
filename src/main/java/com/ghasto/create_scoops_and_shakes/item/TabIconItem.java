package com.ghasto.create_scoops_and_shakes.item;

import com.ghasto.create_scoops_and_shakes.util.DontShowInTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class TabIconItem extends BlockItem implements DontShowInTab {
    public TabIconItem(Block block, Properties properties) {
        super(block, properties);
    }
}
