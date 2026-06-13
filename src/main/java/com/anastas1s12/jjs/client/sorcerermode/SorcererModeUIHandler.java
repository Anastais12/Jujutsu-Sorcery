package com.anastas1s12.jjs.client.sorcerermode;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Manages UI elements when in sorcerer mode
 */
public class SorcererModeUIHandler {
    private static boolean isInSorcererMode = false;
    private static float animationProgress = 0f;
    private static final float ANIMATION_SPEED = 0.1f; // Adjust for transition speed
    // The hotbar slot that is locked while in sorcerer mode
    private static int lockedSelectedHotbar = -1;
    
    public static void initialize() {
        // Register HUD render event to hide/show hotbar
        HudRenderCallback.EVENT.register((guiGraphics, partialTick) -> {
            updateHotbarVisibility();
        });
        
        // Register client tick to update animations
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            updateAnimations();
        });
    }
    
    /**
     * Updates the sorcerer mode state in the UI
     */
    public static void updateSorcererModeState(boolean inSorcererMode) {
        isInSorcererMode = inSorcererMode;
        
        // Trigger entering sorcerer mode effects
        if (inSorcererMode) {
            onEnterSorcererMode();
        } else {
            onExitSorcererMode();
        }
    }
    
    /**
     * Called when entering sorcerer mode
     */
    private static void onEnterSorcererMode() {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            // Lock current selected hotbar slot so the player cannot change it client-side
            try {
                lockedSelectedHotbar = client.player.getInventory().selected;
            } catch (Throwable ignored) {
                lockedSelectedHotbar = -1;
            }
            // Optionally notify server (server-side mixin blocks changes)
        }
        
        // Start animation
        animationProgress = 0f;
    }
    
    /**
     * Called when exiting sorcerer mode
     */
    private static void onExitSorcererMode() {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            // Re-enable hotbar (should be automatic when leaving sorcerer mode)
        }
        
        // Reset animation
        animationProgress = 0f;
    }
    
    /**
     * Updates hotbar visibility based on sorcerer mode state
     */
    private static void updateHotbarVisibility() {
        // This will prevent hotbar rendering when in sorcerer mode
        // The actual hiding is handled by mixin on HUD rendering
    }
    
    /**
     * Updates animations for sorcerer mode
     */
    private static void updateAnimations() {
        if (isInSorcererMode && animationProgress < 1f) {
            animationProgress += ANIMATION_SPEED;
            if (animationProgress > 1f) {
                animationProgress = 1f;
            }
        } else if (!isInSorcererMode && animationProgress > 0f) {
            animationProgress -= ANIMATION_SPEED;
            if (animationProgress < 0f) {
                animationProgress = 0f;
            }
        }

        // While in sorcerer mode, enforce the locked hotbar selection on the client
        Minecraft client = Minecraft.getInstance();
        if (isInSorcererMode && lockedSelectedHotbar >= 0 && client.player != null) {
            try {
                if (client.player.getInventory().selected != lockedSelectedHotbar) {
                    client.player.getInventory().selected = lockedSelectedHotbar;
                }
            } catch (Throwable ignored) {
            }
        }
    }
    
    /**
     * Returns whether currently in sorcerer mode
     */
    public static boolean isInSorcererMode() {
        return isInSorcererMode;
    }
    
    /**
     * Returns animation progress (0 to 1)
     */
    public static float getAnimationProgress() {
        return animationProgress;
    }
}

