package com.ghasto.create_scoops_and_shakes;

import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;

public class ModSpriteShifts {
    public static final SpriteShiftEntry BURNER_FLAME_COOLED =
            get("block/blaze_burner_flame_cooled", "block/blaze_burner_flame_cooled_scroll"),
            FREEZING_BURNER_FLAME = get("block/blaze_burner_flame_cooled", "block/blaze_burner_flame_freezing_scroll");
    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(CreateScoopsAndShakes.id(originalLocation), CreateScoopsAndShakes.id(targetLocation));
    }

    public static void init() {}
}
