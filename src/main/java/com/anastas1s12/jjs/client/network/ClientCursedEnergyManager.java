package com.anastas1s12.jjs.client.network;

import com.anastas1s12.jjs.cursed_energy.CursedEnergyData;
import com.anastas1s12.jjs.networking.payload.s2c.SyncCursedEnergyPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import com.anastas1s12.jjs.networking.payload.s2c.SyncAbilitiesPayload;

/**
 * Client-side manager for cursed energy data
 */
public class ClientCursedEnergyManager {
    private static CursedEnergyData ceData = null;
    
    /**
     * Initializes client-side cursed energy networking
     */
    public static void initialize() {
        ClientPlayNetworking.registerGlobalReceiver(SyncCursedEnergyPayload.TYPE,
            (payload, context) -> {
                ceData = new CursedEnergyData(payload.currentCE(), payload.maxCE());
                com.anastas1s12.jjs.client.hud.CursedEnergyHUD.updateData(ceData);
            });
        // Register ability sync receiver (custom payload)
        ClientPlayNetworking.registerGlobalReceiver(SyncAbilitiesPayload.TYPE,
                (payload, context) -> {
                    com.anastas1s12.jjs.client.network.ClientAbilityManager.updateFromPayload(payload);
                }
        );
    }
    
    /**
     * Gets the current cursed energy data
     */
    public static CursedEnergyData getCursedEnergyData() {
        return ceData;
    }
    
    /**
     * Resets cursed energy data (for disconnection)
     */
    public static void reset() {
        ceData = null;
    }
}


