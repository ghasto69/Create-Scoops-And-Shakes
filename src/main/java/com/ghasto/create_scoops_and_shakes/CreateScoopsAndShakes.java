package com.ghasto.create_scoops_and_shakes;

import com.chocohead.mm.api.ClassTinkerers;
import com.ghasto.create_scoops_and_shakes.util.DontShowInTab;
import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.item.TooltipModifier;
import io.github.fabricators_of_create.porting_lib.data.ExistingFileHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CreateScoopsAndShakes implements ModInitializer, DataGeneratorEntrypoint {
	public static final String ID = "create_scoops_and_shakes";
	public static final String NAME = "Create Scoops And Shakes";
	public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(ID);
	static {
		REGISTRATE.setTooltipModifierFactory(item -> {
			return new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE)
					.andThen(TooltipModifier.mapNull(KineticStats.create(item)));
		});
	}
	public static final BlazeBurnerBlock.HeatLevel COOLING_LEVEL = ClassTinkerers.getEnum(BlazeBurnerBlock.HeatLevel.class, "COOLING");
	public static final BlazeBurnerBlock.HeatLevel FREEZING_LEVEL = ClassTinkerers.getEnum(BlazeBurnerBlock.HeatLevel.class, "FREEZING");
	public static final HeatCondition FREEZING_CONDITION = ClassTinkerers.getEnum(HeatCondition.class, "FREEZING");
	public static final HeatCondition COOLING_CONDITION = ClassTinkerers.getEnum(HeatCondition.class, "COOLING");
	@Override
	public void onInitialize() {
		LOGGER.info("Create addon mod [{}] is loading alongside Create [{}]!", NAME, Create.VERSION);
		Arrays.stream(BlazeBurnerBlock.HeatLevel.values()).toList().forEach(v -> {
			LOGGER.info("Blaze Burner Level: {}", v);
		});
		ModBlocks.register();
		ModItems.register();
		ModFluids.register();
		ModBlockEntities.register();
		ModRecipeTypes.register();
		REGISTRATE.simple("tab",Registries.CREATIVE_MODE_TAB, () ->
				FabricItemGroup.builder()
						.title(Component.literal(NAME).withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD))
						.icon(() -> ModItems.ICON_ITEM.asStack())
						.displayItems((c,p) -> {
							REGISTRATE.getAll(Registries.ITEM).forEach(e -> {
								if(!(e.get() instanceof DontShowInTab)) {
									p.accept(e.get());
								}
							});
							p.accept(Items.SNOWBALL);
						})
						.build()
		);
		REGISTRATE.register();
	}
	public static ResourceLocation id(String path) {
		return new ResourceLocation(ID, path);
	}
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		ExistingFileHelper existingFileHelper = ExistingFileHelper.withResourcesFromArg();
		REGISTRATE.setupDatagen(pack, existingFileHelper);
		ModLang.generate();
	}
}
