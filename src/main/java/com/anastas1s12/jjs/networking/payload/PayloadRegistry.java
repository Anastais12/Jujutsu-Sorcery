package com.anastas1s12.jjs.networking.payload;

import com.anastas1s12.jjs.networking.payload.c2s.AssignAbilityPayload;
import com.anastas1s12.jjs.networking.payload.s2c.SyncAbilitiesPayload;
import com.anastas1s12.jjs.networking.payload.s2c.SyncCursedEnergyPayload;
import com.anastas1s12.jjs.networking.payload.s2c.SyncSorcererModePayload;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;


public class PayloadRegistry {

    public static void registerS2CPayloads() {
        PayloadTypeRegistry.playS2C().register(SyncCursedEnergyPayload.TYPE, SyncCursedEnergyPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncSorcererModePayload.TYPE, SyncSorcererModePayload.CODEC);
        PayloadTypeRegistry.playS2C().register(SyncAbilitiesPayload.TYPE, SyncAbilitiesPayload.CODEC);
        PayloadTypeRegistry.playS2C().register(AssignAbilityPayload.TYPE, AssignAbilityPayload.CODEC);

    }

    public static void registerC2SPayloads() {

    }
}
