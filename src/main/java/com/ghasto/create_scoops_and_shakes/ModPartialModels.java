package com.ghasto.create_scoops_and_shakes;

import com.jozufozu.flywheel.core.PartialModel;
import com.simibubi.create.AllPartialModels;

public class ModPartialModels {
    public static final PartialModel breeze_INERT = block("breeze_cooler/breeze/inert"), breeze_FREEZING_ACTIVE = block("breeze_cooler/breeze/freezing_active"),
			breeze_GOGGLES = block("breeze_cooler/goggles"), breeze_GOGGLES_SMALL = block("breeze_cooler/goggles_small"),
			breeze_IDLE = block("breeze_cooler/breeze/idle"), breeze_ACTIVE = block("breeze_cooler/breeze/active"),
			breeze_FREEZING = block("breeze_cooler/breeze/freezing"), breeze_cooler_effect = block("breeze_cooler/effect"),
			breeze_cooler_RODS = block("breeze_cooler/rods_small"),
			breeze_cooler_RODS_2 = block("breeze_cooler/rods_large"),
			breeze_cooler_SUPER_RODS = block("breeze_cooler/superheated_rods_small"),
			breeze_cooler_SUPER_RODS_2 = block("breeze_cooler/superheated_rods_large");

    private static PartialModel block(String path) {
        return new PartialModel(CreateScoopsAndShakes.id("block/" + path));
    }
    public static void register() {}
}
