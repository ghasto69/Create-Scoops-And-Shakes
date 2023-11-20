package com.ghasto.create_scoops_and_shakes.mixin;

import com.chocohead.mm.api.ClassTinkerers;
import com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock.HeatLevel;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

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
		if ((HeatCondition) (Object) this == ClassTinkerers.getEnum(HeatCondition.class, "COOLING"))
			return level == ClassTinkerers.getEnum(HeatLevel.class, "COOLING");
		if ((HeatCondition) (Object) this == HeatCondition.SUPERHEATED)
			return level == HeatLevel.SEETHING;
		if ((HeatCondition) (Object)this ==  HeatCondition.HEATED)
			return level != HeatLevel.NONE && level != HeatLevel.SMOULDERING && level != ClassTinkerers.getEnum(HeatLevel.class, "COOLING") && level != ClassTinkerers.getEnum(HeatLevel.class, "FREEZING");
		return true;
	}
	/**
	 * @author Ghasto
	 * @reason Add COOLED and FREEZING heat conditions
	 */
	@Overwrite(remap = false)
	public BlazeBurnerBlock.HeatLevel visualizeAsBlazeBurner() {
		if((HeatCondition) (Object)this == CreateScoopsAndShakes.FREEZING_CONDITION)
			return CreateScoopsAndShakes.FREEZING_LEVEL;
		if((HeatCondition) (Object)this == CreateScoopsAndShakes.COOLING_CONDITION)
			return CreateScoopsAndShakes.COOLING_LEVEL;
		if ((HeatCondition) (Object)this == HeatCondition.SUPERHEATED)
			return HeatLevel.SEETHING;
		if ((HeatCondition) (Object)this == HeatCondition.HEATED)
			return HeatLevel.KINDLED;
		return HeatLevel.NONE;
	}
}
