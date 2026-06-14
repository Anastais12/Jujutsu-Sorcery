package com.anastas1s12.jjs.cursed_energy;

import com.anastas1s12.jjs.networking.payload.s2c.SyncCursedEnergyPayload;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Handles networking for cursed energy synchronization
 */
public class CursedEnergyNetworking {

    /**
     * Sends cursed energy sync packet to a player
     */
    public static void sendSyncPacket(ServerPlayer player, CursedEnergyData data) {
        ServerPlayNetworking.send(player, new SyncCursedEnergyPayload(data));
    }

    /**
     * Sends cursed energy sync packet to all players
     */
    public static void broadcastSyncPacket(Player player, CursedEnergyData data) {
        if (player.getServer() != null) {
            SyncCursedEnergyPayload payload = new SyncCursedEnergyPayload(data);
            for (ServerPlayer serverPlayer : player.getServer().getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(serverPlayer, payload);
            }
        }
    }
}
