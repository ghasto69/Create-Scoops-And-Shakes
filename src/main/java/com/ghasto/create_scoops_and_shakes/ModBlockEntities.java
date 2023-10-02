package com.ghasto.create_scoops_and_shakes;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

import com.ghasto.create_scoops_and_shakes.block.blender.BlenderBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarBlockEntity;
import com.ghasto.create_scoops_and_shakes.block.ice_cream_jar.IceCreamJarRenderer;
import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.mixer.MechanicalMixerRenderer;
import com.simibubi.create.content.kinetics.mixer.MixerInstance;
import com.tterrag.registrate.util.entry.BlockEntityEntry;

public class ModBlockEntities {
	public static final BlockEntityEntry<IceCreamJarBlockEntity>
	ICE_CREAM_JAR = REGISTRATE.blockEntity("ice_cream_jar", IceCreamJarBlockEntity::new)
			.validBlock(ModBlocks.ICE_CREAM_JAR)
			.renderer(() -> IceCreamJarRenderer.IceCreamJarBlockRenderer::new)
			.register();
	public static final BlockEntityEntry<BlenderBlockEntity>
	BLENDER = REGISTRATE.blockEntity("blender", BlenderBlockEntity::new)
			.instance(() -> MixerInstance::new)
			.validBlocks(ModBlocks.BLENDER)
			.renderer(() -> MechanicalMixerRenderer::new)
			.register();
	public static void register() {}
}
