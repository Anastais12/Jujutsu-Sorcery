package com.anastas1s12.jjs.sorcerermode;

import com.anastas1s12.jjs.utils.IPlayerDataAccessor;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

/**
 * Manages sorcerer mode state for players on the server side
 */
public class SorcererModeManager {
    private static final String SORCERER_MODE_NBT_KEY = "SorcererModeData";
    private static final String SORCERER_MODE_KEY = "InSorcererMode";
    private static final String HOTBAR_DATA_KEY = "SorcererHotbarData";
    
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

    /**
     * Check if a player is currently in sorcerer mode (SERVER)
     */
    public static boolean isInSorcererMode(ServerPlayer player) {
        if (player == null) return false;
        CompoundTag playerData = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        return playerData.getBoolean(SORCERER_MODE_KEY);
    }

    /**
     * Get hotbar data for a player
     */
    public static SorcererHotbarData getHotbarData(ServerPlayer player) {
        CompoundTag playerData = ((IPlayerDataAccessor) player).jjs$getPersistentData();

        SorcererHotbarData hotbar = new SorcererHotbarData();

        if (playerData.contains(HOTBAR_DATA_KEY)) {
            hotbar.readFromNbt(playerData.getCompound(HOTBAR_DATA_KEY));
        }

        return hotbar;
    }

    /**
     * Save hotbar data for a player
     */
    private static void saveHotbarData(ServerPlayer player, SorcererHotbarData hotbar) {
        CompoundTag playerData = ((IPlayerDataAccessor) player).jjs$getPersistentData();

        CompoundTag hotbarTag = new CompoundTag();
        hotbar.writeToNbt(hotbarTag);
        playerData.put(HOTBAR_DATA_KEY, hotbarTag);
    }

    /**
     * Assign ability to hotbar slot
     */
    public static void assignAbilityToHotbar(ServerPlayer player, int slotIndex, ResourceLocation abilityId) {
        if (abilityId == null) {
            clearHotbarSlot(player, slotIndex);
            return;
        }

        SorcererHotbarData hotbar = getHotbarData(player);
        hotbar.setSlot(slotIndex, abilityId);
        saveHotbarData(player, hotbar);

        // Sync to client
        // TODO: Send hotbar update packet to client
    }

    /**
     * Clear a hotbar slot
     */
    public static void clearHotbarSlot(ServerPlayer player, int slotIndex) {
        SorcererHotbarData hotbar = getHotbarData(player);
        hotbar.clearSlot(slotIndex);
        saveHotbarData(player, hotbar);

        // Sync to client
        // TODO: Send hotbar update packet to client
    }

    /**
     * Get ability in a hotbar slot
     */
    public static ResourceLocation getHotbarAbility(ServerPlayer player, int slotIndex) {
        SorcererHotbarData hotbar = getHotbarData(player);
        return hotbar.getSlot(slotIndex);
    }

    /**
     * Set selected slot (for mouse wheel cycling)
     */
    public static void setSelectedHotbarSlot(ServerPlayer player, int slotIndex) {
        SorcererHotbarData hotbar = getHotbarData(player);
        hotbar.setSelectedSlot(slotIndex);
        saveHotbarData(player, hotbar);
    }

    /**
     * Cycle hotbar selection (for mouse wheel)
     */
    public static void cycleHotbarSelection(ServerPlayer player, int direction) {
        SorcererHotbarData hotbar = getHotbarData(player);
        hotbar.cycleSelection(direction);
        saveHotbarData(player, hotbar);
    }

    /**
     * Get selected ability
     */
    public static ResourceLocation getSelectedAbility(ServerPlayer player) {
        SorcererHotbarData hotbar = getHotbarData(player);
        return hotbar.getSelectedAbility();
    }
}

