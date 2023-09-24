package com.ghasto.create_scoops_and_shakes.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.recipe.HeatCondition;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(HeatCondition.class)
public class MixinHeatCondition {
	/**
	 * @author Ghasto
	 * @reason Add COOLED and FREEZING heat conditions
	 */
	@Overwrite(remap = false)
	public boolean testBlazeBurner(HeatLevel level) {
		if ((HeatCondition) (Object) this == ClassTinkerers.getEnum(HeatCondition.class, "FREEZING"))
			return level == ClassTinkerers.getEnum(HeatLevel.class, "FREEZING");
		if ((HeatCondition) (Object) this == ClassTinkerers.getEnum(HeatCondition.class, "COOLED"))
			return level == ClassTinkerers.getEnum(HeatLevel.class, "COOLED");
		if ((HeatCondition) (Object) this == HeatCondition.SUPERHEATED)
			return level == HeatLevel.SEETHING;
		if ((HeatCondition) (Object)this ==  HeatCondition.HEATED)
			return level != HeatLevel.NONE && level != HeatLevel.SMOULDERING && level != ClassTinkerers.getEnum(HeatLevel.class, "COOLED") && level != ClassTinkerers.getEnum(HeatLevel.class, "FREEZING");
		return true;
	}
}
