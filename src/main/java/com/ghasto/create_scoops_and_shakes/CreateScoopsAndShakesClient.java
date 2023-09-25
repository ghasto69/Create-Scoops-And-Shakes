package com.ghasto.create_scoops_and_shakes;

import net.fabricmc.api.ClientModInitializer;

public class CreateScoopsAndShakesClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModSpriteShifts.init();
        ModPartialModels.register();
    }
}
