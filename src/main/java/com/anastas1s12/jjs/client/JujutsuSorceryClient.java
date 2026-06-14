package com.anastas1s12.jjs.client;

import com.anastas1s12.jjs.client.hud.CursedEnergyHUD;
import com.anastas1s12.jjs.client.hud.SorcererHotbarRenderer;
import com.anastas1s12.jjs.client.network.ClientCursedEnergyManager;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeAnimationHandler;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeClientEvents;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeKeyHandler;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeUIHandler;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;

public class JujutsuSorceryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCursedEnergyManager.initialize();
        CursedEnergyHUD.initialize();

        HudRenderCallback.EVENT.register((guiGraphics, tickCounter) -> {
            SorcererHotbarRenderer.render(guiGraphics, Minecraft.getInstance());
        });

        SorcererModeKeyHandler.initialize();
        SorcererModeClientEvents.initialize();
        SorcererModeUIHandler.initialize();
        SorcererModeAnimationHandler.initialize();
    }
}
