package com.ghasto.create_scoops_and_shakes;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;
import static com.simibubi.create.AllInteractionBehaviours.interactionBehaviour;
import static com.simibubi.create.AllMovementBehaviours.movementBehaviour;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

import com.ghasto.create_scoops_and_shakes.block.breeze_cooler.BreezeCoolerBlock;
import com.ghasto.create_scoops_and_shakes.block.breeze_cooler.BreezeCoolerBlockItem;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlock;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarItem;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.processing.burner.BlazeBurnerInteractionBehaviour;
import com.simibubi.create.content.processing.burner.BlazeBurnerMovementBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

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
	public static final BlockEntry<BreezeCoolerBlock> BREEZE_COOLER =
			REGISTRATE.block("breeze_cooler", BreezeCoolerBlock::new)
					.initialProperties(SharedProperties::softMetal)
					.properties(p -> p.mapColor(MapColor.COLOR_GRAY))
					.properties(p -> p.lightLevel(BreezeCoolerBlock::getLight))
					.transform(pickaxeOnly())
					.addLayer(() -> RenderType::cutoutMipped)
					.tag(AllTags.AllBlockTags.FAN_TRANSPARENT.tag)
					.loot((lt, block) -> lt.add(block, BreezeCoolerBlock.buildLootTable()))
					.blockstate((c, p) -> p.simpleBlock(c.getEntry(), AssetLookup.partialBaseModel(c, p)))
					.onRegister(movementBehaviour(new BlazeBurnerMovementBehaviour()))
					.onRegister(interactionBehaviour(new BlazeBurnerInteractionBehaviour()))
					.item(BreezeCoolerBlockItem::withBreeze)
					.model(AssetLookup.customBlockItemModel("breeze_cooler", "block"))
					.build()
					.register();

	public static void register() {}

}
