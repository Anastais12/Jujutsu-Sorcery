package com.anastas1s12.jjs.sorcerermode;

import com.anastas1s12.jjs.networking.payload.s2c.SyncSorcererModePayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;

/**
 * Handles networking for sorcerer mode synchronization
 */
public class SorcererModeNetworking {
    
    /**
     * Sends sorcerer mode sync packet to a player
     */
    public static void sendSyncPacket(ServerPlayer player, SorcererModeData data) {
        ServerPlayNetworking.send(player, new SyncSorcererModePayload(data));
    }
}

