package com.anastas1s12.jjs.client;

import com.anastas1s12.jjs.client.hud.CursedEnergyHUD;
import com.anastas1s12.jjs.client.network.ClientCursedEnergyManager;
import com.anastas1s12.jjs.client.sorcerermode.ClientSorcererModeManager;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeAnimationHandler;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeClientEvents;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeKeyHandler;
import com.anastas1s12.jjs.client.sorcerermode.SorcererModeUIHandler;
import net.fabricmc.api.ClientModInitializer;

public class JujutsuSorceryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCursedEnergyManager.initialize();
        CursedEnergyHUD.initialize();
        
        // Initialize sorcerer mode systems
        SorcererModeKeyHandler.initialize();
        SorcererModeClientEvents.initialize();
        SorcererModeUIHandler.initialize();
        SorcererModeAnimationHandler.initialize();
    }
}
