package com.ghasto.create_scoops_and_shakes;

import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlock;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarItem;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.crusher.CrushingWheelBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.entry.BlockEntry;

import io.github.fabricators_of_create.porting_lib.util.NBTSerializer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.Supplier;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;

public class ModBlocks {
	public static final BlockEntry<? extends Block>
	ICE_CREAM_JAR = REGISTRATE.block("ice_cream_jar",IceCreamJarBlock::new)
			.item(IceCreamJarItem::new)
			.build()
			.properties(BlockBehaviour.Properties::noOcclusion)
			.loot((lt,block) -> {
				LootTable.Builder builder = LootTable.lootTable();
				lt.add(block, builder.withPool(LootPool.lootPool()
						.setRolls(ConstantValue.exactly(1))
						.add(LootItem.lootTableItem(block.asItem())
								.apply(CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
										.copy("Tanks", "BlockEntityTag.Tanks")))));
			})
			.addLayer(() -> RenderType::cutoutMipped)
			.blockstate((c, p)-> {
				p.simpleBlock(c.get(), AssetLookup.standardModel(c,p));
			})
			.register();
	public static void register() {}
}
