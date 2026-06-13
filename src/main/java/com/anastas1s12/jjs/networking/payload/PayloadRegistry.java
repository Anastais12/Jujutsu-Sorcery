package com.anastas1s12.jjs.networking.payload;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class PayloadRegistry {
    public static void registerPayloads() {
        PayloadTypeRegistry.playS2C().register(
                SyncCursedEnergyPayload.TYPE,
                SyncCursedEnergyPayload.CODEC
        );
        PayloadTypeRegistry.playS2C().register(
                SyncSorcererModePayload.TYPE,
                SyncSorcererModePayload.CODEC
        );
        PayloadTypeRegistry.playS2C().register(
                SyncAbilitiesPayload.TYPE,
                SyncAbilitiesPayload.CODEC
        );
    }
}
