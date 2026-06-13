package com.anastas1s12.jjs.abilities;

import com.anastas1s12.jjs.JujutsuSorcery;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import java.util.Set;
import com.anastas1s12.jjs.networking.payload.SyncAbilitiesPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

/**
 * Sends ability state to clients using a simple raw packet (FriendlyByteBuf).
 */
public class AbilityNetworking {
    public static final ResourceLocation SYNC_ID = ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "sync_abilities");

    public static void sendSyncPacket(ServerPlayer player, AbilityData data) {
        SyncAbilitiesPayload payload = new SyncAbilitiesPayload(data);
        ServerPlayNetworking.send(player, payload);
    }
}


