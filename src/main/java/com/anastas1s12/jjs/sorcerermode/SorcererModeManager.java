package com.anastas1s12.jjs.sorcerermode;

import com.anastas1s12.jjs.utils.IPlayerDataAccessor;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Manages sorcerer mode state for players on the server side
 */
public class SorcererModeManager {
    private static final String SORCERER_MODE_NBT_KEY = "SorcererModeData";
    
    /**
     * Initialize the sorcerer mode system
     */
    public static void initialize() {
        // Sync sorcerer mode when player joins
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            syncPlayerSorcererMode(handler.player);
        });
    }
    
    /**
     * Gets or creates sorcerer mode data for a player
     */
    public static SorcererModeData getSorcererModeData(Player player) {
        CompoundTag playerNbt = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        
        if (playerNbt.contains(SORCERER_MODE_NBT_KEY)) {
            return SorcererModeData.readFromNbt(
                    playerNbt.getCompound(SORCERER_MODE_NBT_KEY)
            );
        }
        
        // Create new data if it doesn't exist
        SorcererModeData data = new SorcererModeData();
        setSorcererModeData(player, data);
        return data;
    }
    
    /**
     * Saves sorcerer mode data for a player
     */
    public static void setSorcererModeData(Player player, SorcererModeData data) {
        CompoundTag playerNbt = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        CompoundTag smNbt = new CompoundTag();
        data.writeToNbt(smNbt);
        playerNbt.put(SORCERER_MODE_NBT_KEY, smNbt);
    }
    
    /**
     * Syncs sorcerer mode state to client
     */
    public static void syncPlayerSorcererMode(ServerPlayer player) {
        if (player instanceof ServerPlayer serverPlayer) {
            SorcererModeData data = getSorcererModeData(player);
            SorcererModeNetworking.sendSyncPacket(serverPlayer, data);
        }
    }
    
    /**
     * Toggles sorcerer mode for a player
     */
    public static void toggleSorcererMode(Player player) {
        SorcererModeData data = getSorcererModeData(player);
        boolean newState = !data.isInSorcererMode();
        
        // If entering sorcerer mode for the first time
        if (newState && !data.hasEnteredSorcererModeBefore()) {
            data.setHasEnteredSorcererModeBefore(true);
        }
        
        data.setInSorcererMode(newState);
        setSorcererModeData(player, data);
        syncPlayerSorcererMode((ServerPlayer) player);
    }
    
    /**
     * Sets sorcerer mode state
     */
    public static void setSorcererMode(Player player, boolean state) {
        SorcererModeData data = getSorcererModeData(player);
        
        // If entering sorcerer mode for the first time
        if (state && !data.hasEnteredSorcererModeBefore()) {
            data.setHasEnteredSorcererModeBefore(true);
        }
        
        data.setInSorcererMode(state);
        setSorcererModeData(player, data);
        syncPlayerSorcererMode((ServerPlayer) player);
    }
}

