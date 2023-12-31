package com.ghasto.create_scoops_and_shakes;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

import com.ghasto.create_scoops_and_shakes.block.breeze_cooler.BreezeCoolerBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.breeze_cooler.BreezeCoolerRenderer;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {
	public static final BlockEntityEntry<IceCreamJarBlockEntity>
	ICE_CREAM_JAR = REGISTRATE.blockEntity("ice_cream_jar", IceCreamJarBlockEntity::new)
			.validBlock(ModBlocks.ICE_CREAM_JAR)
			.renderer(() -> IceCreamJarRenderer.IceCreamJarBlockRenderer::new)
			.register();
	public static final BlockEntityEntry<BreezeCoolerBlockEntity> BREEZE_COOLER = REGISTRATE
			.blockEntity("breeze_cooler", BreezeCoolerBlockEntity::new)
			.validBlocks(ModBlocks.BREEZE_COOLER)
			.renderer(() -> BreezeCoolerRenderer::new)
			.register();

	public static void register() {}
}
