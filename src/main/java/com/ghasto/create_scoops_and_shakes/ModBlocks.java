package com.ghasto.create_scoops_and_shakes;

import com.ghasto.create_scoops_and_shakes.block.blender.BlenderBlock;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlock;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarItem;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlock;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class ModBlocks {
	public static final BlockEntry<? extends Block>
	ICE_CREAM_JAR = REGISTRATE.block("ice_cream_jar",IceCreamJarBlock::new)
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
	public static final BlockEntry<BlenderBlock> BLENDER =
			REGISTRATE.block("blender", BlenderBlock::new)
					.initialProperties(SharedProperties::stone)
					.properties(p -> p.mapColor(MapColor.STONE))
					.properties(BlockBehaviour.Properties::noOcclusion)
					.transform(axeOrPickaxe())
					.blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
					.addLayer(() -> RenderType::cutoutMipped)
					.transform(BlockStressDefaults.setImpact(4.0))
					//.item(AssemblyOperatorBlockItem::new)
					//.transform(customItemModel())
					.register();
	public static void register() {}
}
