package com.ghasto.create_scoops_and_shakes;

import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.content.processing.basin.BasinRenderer;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

import net.minecraft.world.level.block.entity.BlockEntity;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

public class ModBlockEntities {
	public static final BlockEntityEntry<IceCreamJarBlockEntity>
	ICE_CREAM_JAR = REGISTRATE.blockEntity("ice_cream_jar", IceCreamJarBlockEntity::new)
			.validBlock(ModBlocks.ICE_CREAM_JAR)
			.renderer(() -> IceCreamJarRenderer::new)
			.register();
	public static void register() {}
}
