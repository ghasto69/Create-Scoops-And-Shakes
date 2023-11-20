package com.ghasto.create_scoops_and_shakes;

import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.block.render.SpriteShiftEntry;
import com.simibubi.create.foundation.block.render.SpriteShifter;

public class ModSpriteShifts {
    public static final SpriteShiftEntry COOLER_EFFECT =
            get("block/breeze_cooler_effect", "block/breeze_cooler_effect_scroll"),
            COOLER_EFFECT_FREEZING = get("block/breeze_cooler_effect", "block/freezing_breeze_cooler_effect_scroll");
    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(CreateScoopsAndShakes.id(originalLocation), CreateScoopsAndShakes.id(targetLocation));
    }

    public static void init() {}
}
