package com.ghasto.create_scoops_and_shakes;

import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.ghasto.create_scoops_and_shakes.util.DontShowInTab;
import com.tterrag.registrate.util.entry.RegistryEntry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.NAME;
import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;
import static com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer.IceCreamJarItemRenderer.displayFilledJar;

public class ModCreativeModTabs {
	public static final RegistryEntry<CreativeModeTab> TAB = REGISTRATE.simple("tab", Registries.CREATIVE_MODE_TAB, () ->
			FabricItemGroup.builder()
					.title(Component.literal(NAME).withStyle(ChatFormatting.AQUA, ChatFormatting.BOLD))
					.icon(() -> ModItems.ICON_ITEM.asStack())
					.displayItems((c,p) -> {
						addCustomTabEntries(p);
						REGISTRATE.getAll(Registries.ITEM).forEach(e -> {
							if(!(e.get() instanceof DontShowInTab)) {
								p.accept(e.get());
							}
						});
					})
					.build());
	public static void register() {}
	private static void addCustomTabEntries(CreativeModeTab.Output p) {
		displayFilledJar(ModFluids.VANILLA_ICE_CREAM, p);
		displayFilledJar(ModFluids.BERRY_ICE_CREAM, p);
		displayFilledJar(ModFluids.CHOCOLATE_ICE_CREAM, p);
	}

}
