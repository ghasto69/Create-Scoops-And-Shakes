package com.ghasto.create_scoops_and_shakes;

import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlock;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarItem;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

public class ModBlocks {
	public static final BlockEntry<? extends Block>
	ICE_CREAM_JAR = REGISTRATE.block("ice_cream_jar",IceCreamJarBlock::new)
			.item(IceCreamJarItem::new).build()
			.properties(BlockBehaviour.Properties::noOcclusion)
			.register();
	public static void register() {}
}
