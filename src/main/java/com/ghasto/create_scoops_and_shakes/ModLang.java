package com.ghasto.create_scoops_and_shakes;

import static com.ghasto.create_scoops_and_shakes.CreateScoopsAndShakes.REGISTRATE;

public class ModLang {
    public static void generate() {
        lang("create.recipe.heat_requirement.freezing", "Frozen");
        lang("create.recipe.heat_requirement.cooling", "Cooled");
    }
    public static void lang(String key, String translation) {
        REGISTRATE.addRawLang(key, translation);
    }
}
