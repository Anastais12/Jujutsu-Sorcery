package com.anastas1s12.jjs.networking.handler;

import com.anastas1s12.jjs.client.hud.CursedEnergyHUD;
import com.anastas1s12.jjs.cursed_energy.CursedEnergyData;
import com.anastas1s12.jjs.networking.payload.SyncCursedEnergyPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Handles client-side network callbacks for cursed energy payloads
 */
public class ClientPayloadHandler {
    
    /**
     * Initializes client payload handlers
     * Call this from ClientCursedEnergyManager.initialize()
     */
    public static void registerHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(SyncCursedEnergyPayload.TYPE, 
            (payload, context) -> {
                // Decode payload into CursedEnergyData
                CursedEnergyData ceData = new CursedEnergyData(
                    payload.currentCE(),
                    payload.maxCE()
                );
                
                // Update HUD with new data
                CursedEnergyHUD.updateData(ceData);
            });
    }
}

