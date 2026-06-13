package com.anastas1s12.jjs.client.sorcerermode;

import com.anastas1s12.jjs.networking.payload.SyncSorcererModePayload;
import com.anastas1s12.jjs.sorcerermode.SorcererModeData;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.player.LocalPlayer;

/**
 * Client-side event handlers for sorcerer mode
 */
public class SorcererModeClientEvents {
    
    /**
     * Initializes client-side sorcerer mode event handlers
     */
    public static void initialize() {
        // Register network receiver for sorcerer mode sync
        ClientPlayNetworking.registerGlobalReceiver(SyncSorcererModePayload.TYPE,
                (payload, context) -> {
                    SorcererModeData data = new SorcererModeData(
                            payload.isInSorcererMode(),
                            payload.hasEnteredSorcererModeBefore()
                    );
                    ClientSorcererModeManager.setSorcererModeData(data);
                    
                    // Check if this is first time entering
                    if (payload.isInSorcererMode() && !ClientSorcererModeManager.hasEnteredSorcererModeBefore()) {
                        ClientSorcererModeManager.setPlayFirstTimeAnimation(true);
                    }
                    
                    // Update HUD and visuals
                    SorcererModeUIHandler.updateSorcererModeState(payload.isInSorcererMode());
                });
    }
    
    /**
     * Toggles sorcerer mode on the client
     */
    public static void toggleSorcererMode(LocalPlayer player) {
        // Send command packet to server to toggle sorcerer mode
        // Using built-in Minecraft messaging system
        if (player.connection != null) {
            // We'll handle this through a command instead
            player.connection.sendCommand("jjs sorcerer toggle");
        }
    }
}


