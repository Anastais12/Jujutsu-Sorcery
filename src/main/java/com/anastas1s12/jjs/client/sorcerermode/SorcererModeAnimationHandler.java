package com.anastas1s12.jjs.client.sorcerermode;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

/**
 * Handles animations for sorcerer mode (hands, camera, etc.)
 */
public class SorcererModeAnimationHandler {
    private static int firstTimeAnimationTick = 0;
    private static final int FIRST_TIME_ANIMATION_DURATION = 40; // Duration in ticks
    private static boolean playingFirstTimeAnimation = false;
    
    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            updateAnimations();
        });
    }
    
    /**
     * Updates animations based on sorcerer mode state
     */
    private static void updateAnimations() {
        Minecraft client = Minecraft.getInstance();
        
        if (client.player == null) {
            return;
        }
        
        // Check if we should play first-time animation
        if (ClientSorcererModeManager.shouldPlayFirstTimeAnimation() && !playingFirstTimeAnimation) {
            startFirstTimeAnimation(client);
        }
        
        // Update animation progress
        if (playingFirstTimeAnimation) {
            updateFirstTimeAnimation(client);
        }
    }
    
    /**
     * Starts the first-time entering animation
     */
    private static void startFirstTimeAnimation(Minecraft client) {
        playingFirstTimeAnimation = true;
        firstTimeAnimationTick = 0;
        
        // Play hand animation
        if (client.player != null) {
            client.player.swing(InteractionHand.MAIN_HAND);
        }
    }
    
    /**
     * Updates the first-time animation
     */
    private static void updateFirstTimeAnimation(Minecraft client) {
        firstTimeAnimationTick++;
        
        if (firstTimeAnimationTick >= FIRST_TIME_ANIMATION_DURATION) {
            playingFirstTimeAnimation = false;
            firstTimeAnimationTick = 0;
            ClientSorcererModeManager.setPlayFirstTimeAnimation(false);
        }
        
        // Custom hand animation during sorcerer mode transition
        updateHandAnimation(client, (float) firstTimeAnimationTick / FIRST_TIME_ANIMATION_DURATION);
    }
    
    /**
     * Updates hand animation with progress (0 to 1)
     */
    private static void updateHandAnimation(Minecraft client, float progress) {
        if (client.player == null) {
            return;
        }
        
        // Custom hand pose can be modified here
        // You can adjust camera position, hand rotation, etc.
        // This is a placeholder for more complex hand animations
    }
    
    /**
     * Returns whether currently playing first-time animation
     */
    public static boolean isPlayingFirstTimeAnimation() {
        return playingFirstTimeAnimation;
    }
    
    /**
     * Returns first-time animation progress (0 to 1)
     */
    public static float getFirstTimeAnimationProgress() {
        return playingFirstTimeAnimation ? (float) firstTimeAnimationTick / FIRST_TIME_ANIMATION_DURATION : 0f;
    }
}

