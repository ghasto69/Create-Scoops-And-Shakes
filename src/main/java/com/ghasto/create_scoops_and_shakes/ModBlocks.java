package com.ghasto.create_scoops_and_shakes;

import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlock;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarItem;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

public class ModBlocks {
	public static final BlockEntry<? extends Block>
	ICE_CREAM_JAR = CreateScoopsAndShakes.REGISTRATE.block("ice_cream_jar",IceCreamJarBlock::new)
			.item(IceCreamJarItem::new)
			.transform(CreateRegistrate.customRenderedItem(() -> IceCreamJarRenderer.IceCreamJarItemRenderer::new))
			.build()
			.properties(BlockBehaviour.Properties::noOcclusion)
			.loot((lt,block) -> {
				LootTable.Builder builder = LootTable.lootTable();
				lt.add(block, builder.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(block.asItem())
								.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
										.copy("Tanks", "BlockEntityTag.Tanks")
										.copy("Tanks", "Tanks")
								))));
			})
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p)-> {
				p.simpleBlock(c.get(), AssetLookup.standardModel(c,p));
			})
			.register();
	public static void register() {}
}
