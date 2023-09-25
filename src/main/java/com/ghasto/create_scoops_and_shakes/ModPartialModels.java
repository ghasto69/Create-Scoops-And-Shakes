package com.ghasto.create_scoops_and_shakes;

import com.jozufozu.flywheel.core.PartialModel;

public class ModPartialModels {
    public static final PartialModel BLAZE_FREEZING_ACTIVE = block("blaze_burner/blaze/blaze_freezing_active"),
            BLAZE_FREEZING = block("blaze_burner/blaze/blaze_freezing"),
            BLAZE_COOLED_ACTIVE = block("blaze_burner/blaze/blaze_cooled_active"),
            BLAZE_COOLED = block("blaze_burner/blaze/blaze_cooled"),
            BLAZE_BURNER_FREEZING_RODS = block("blaze_burner/freezing_rods_small"),
            BLAZE_BURNER_FREEZING_RODS_2 = block("blaze_burner/freezing_rods_large");

    private static PartialModel block(String path) {
        return new PartialModel(CreateScoopsAndShakes.id("block/" + path));
    }
    public static void register() {}
}
