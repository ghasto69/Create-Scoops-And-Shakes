package com.ghasto.create_scoops_and_shakes.code_modification;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import org.slf4j.LoggerFactory;

public class EarlyRiser implements Runnable{
	@Override
	public void run() {
		LoggerFactory.getLogger("Create Scoops And Shakes Early Riser").info("Injecting COOLED and FREEZING into HeatLevel");
		EnumAdder heatlevel =  ClassTinkerers.enumBuilder("com.simibubi.create.content.processing.burner.BlazeBurnerBlock$HeatLevel");
		heatlevel.addEnum("COOLED").addEnum("FREEZING").build();
		ClassTinkerers.enumBuilder("com.simibubi.create.content.processing.burner.BlazeBurnerBlockEntity$FuelType").addEnum("COOLED").build();
		EnumAdder heatCondition = ClassTinkerers.enumBuilder("com.simibubi.create.content.processing.recipe.HeatCondition", int.class);
		heatCondition.addEnum("COOLED", 0xADD8E6).addEnum("FREEZING", 0x800080).build();
	}
}
