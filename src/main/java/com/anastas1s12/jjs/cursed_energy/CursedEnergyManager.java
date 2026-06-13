package com.anastas1s12.jjs.cursed_energy;

import com.anastas1s12.jjs.utils.IPlayerDataAccessor;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Manages cursed energy for players on the server side
 */
public class CursedEnergyManager {
    private static final String CURSED_ENERGY_NBT_KEY = "CursedEnergyData";
    
    /**
     * Initialize the cursed energy system
     */
    public static void initialize() {
        // Sync cursed energy when player joins
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            syncPlayerCursedEnergy(handler.player);
        });
    }
    
    /**
     * Gets or creates cursed energy data for a player
     */
    public static CursedEnergyData getCursedEnergyData(Player player) {
        // Cast to the interface instead of the mixin class
        CompoundTag playerNbt = ((IPlayerDataAccessor) player).jjs$getPersistentData();

        if (playerNbt.contains(CURSED_ENERGY_NBT_KEY)) {
            return CursedEnergyData.readFromNbt(
                    playerNbt.getCompound(CURSED_ENERGY_NBT_KEY)
            );
        }

        // Create new data if it doesn't exist
        CursedEnergyData data = new CursedEnergyData();
        setCursedEnergyData(player, data);
        return data;
    }
    
    /**
     * Saves cursed energy data for a player
     */
    public static void setCursedEnergyData(Player player, CursedEnergyData data) {
        CompoundTag playerNbt = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        CompoundTag ceNbt = new CompoundTag();
        data.writeToNbt(ceNbt);
        playerNbt.put(CURSED_ENERGY_NBT_KEY, ceNbt);
    }
    
    /**
     * Syncs cursed energy to client
     */
    public static void syncPlayerCursedEnergy(ServerPlayer player) {
        if (player instanceof ServerPlayer serverPlayer) {
            CursedEnergyData data = getCursedEnergyData(player);
            CursedEnergyNetworking.sendSyncPacket(serverPlayer, data);
        }
    }
    
    /**
     * Adds cursed energy to a player
     */
    public static void addCursedEnergy(Player player, int amount) {
        CursedEnergyData data = getCursedEnergyData(player);
        data.addCE(amount);
        setCursedEnergyData(player, data);
        syncPlayerCursedEnergy((ServerPlayer) player);
    }
    
    /**
     * Removes cursed energy from a player
     */
    public static void removeCursedEnergy(Player player, int amount) {
        CursedEnergyData data = getCursedEnergyData(player);
        data.removeCE(amount);
        setCursedEnergyData(player, data);
        syncPlayerCursedEnergy((ServerPlayer) player);
    }
    
    /**
     * Sets max cursed energy for a player
     */
    public static void setMaxCursedEnergy(Player player, int maxCE) {
        CursedEnergyData data = getCursedEnergyData(player);
        data.setMaxCE(maxCE);
        setCursedEnergyData(player, data);
        syncPlayerCursedEnergy((ServerPlayer) player);
    }
}


