package com.anastas1s12.jjs.client.sorcerermode;

import com.anastas1s12.jjs.sorcerermode.SorcererModeData;
import net.minecraft.client.player.LocalPlayer;

/**
 * Client-side manager for sorcerer mode state and networking.
 */
public class ClientSorcererModeManager {
    private static SorcererModeData smData = null;
    private static boolean playFirstTimeAnimation = false;

    /**
     * Initializes client-side sorcerer mode networking
     */
    public static void initialize() {
        // Will be called to register network receivers
    }

    /**
     * Sets the sorcerer mode data for the client
     */
    public static void setSorcererModeData(SorcererModeData data) {
        smData = data;
    }

    /**
     * Gets the current sorcerer mode data
     */
    public static SorcererModeData getSorcererModeData() {
        return smData;
    }

    /**
     * Checks if player is currently in sorcerer mode
     */
    public static boolean isInSorcererMode() {
        return smData != null && smData.isInSorcererMode();
    }

    /**
     * Check if local player is in sorcerer mode (CLIENT-SIDE validation overload)
     */
    public static boolean isInSorcererMode(LocalPlayer player) {
        if (player == null) return false;
        return isInSorcererMode();
    }

    /**
     * Set sorcerer mode state dynamically (client-side)
     * Called when receiving basic update triggers
     */
    public static void setInSorcererMode(LocalPlayer player, boolean inMode) {
        if (player == null) return;
        if (smData == null) {
            smData = new SorcererModeData();
        }
        smData.setInSorcererMode(inMode); // Assumes setInSorcererMode exists on your common data class
    }

    /**
     * Checks if player has entered sorcerer mode before
     */
    public static boolean hasEnteredSorcererModeBefore() {
        return smData != null && smData.hasEnteredSorcererModeBefore();
    }

    /**
     * Sets whether to play the first-time animation
     */
    public static void setPlayFirstTimeAnimation(boolean play) {
        playFirstTimeAnimation = play;
    }

    /**
     * Returns whether to play the first-time animation
     */
    public static boolean shouldPlayFirstTimeAnimation() {
        return playFirstTimeAnimation;
    }

    /**
     * Resets sorcerer mode data (for disconnection or logging out)
     */
    public static void reset() {
        smData = null;
        playFirstTimeAnimation = false;
    }
}
