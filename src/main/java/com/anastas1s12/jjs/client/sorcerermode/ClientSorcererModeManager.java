package com.anastas1s12.jjs.client.sorcerermode;

import com.anastas1s12.jjs.sorcerermode.SorcererModeData;

/**
 * Client-side manager for sorcerer mode state
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
     * Resets sorcerer mode data (for disconnection)
     */
    public static void reset() {
        smData = null;
        playFirstTimeAnimation = false;
    }
}

