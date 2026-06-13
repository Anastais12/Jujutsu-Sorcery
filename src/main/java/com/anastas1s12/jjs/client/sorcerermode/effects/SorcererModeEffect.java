package com.anastas1s12.jjs.client.sorcerermode.effects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

/**
 * Template for creating custom sorcerer mode effects
 * Copy this file and extend it to add custom visual effects
 * 
 * Example: CandleFlameEffect, PurpleAuraEffect, etc.
 */
public abstract class SorcererModeEffect {
    protected Minecraft client;
    protected LocalPlayer player;
    protected float progress; // 0 to 1, animation progress
    
    public SorcererModeEffect() {
        this.client = Minecraft.getInstance();
    }
    
    /**
     * Called when effect starts (entering sorcerer mode)
     */
    public abstract void onStart();
    
    /**
     * Called every tick while effect is active
     * 
     * @param deltaTime Time since last tick (in seconds for dT, or use ticks)
     */
    public abstract void onUpdate(float deltaTime);
    
    /**
     * Called when effect ends (exiting sorcerer mode)
     */
    public abstract void onEnd();
    
    /**
     * Returns whether effect is currently active
     */
    public abstract boolean isActive();
    
    /**
     * Sets animation progress (0 to 1)
     */
    public void setProgress(float progress) {
        this.progress = Math.max(0, Math.min(1, progress));
    }
    
    /**
     * Gets animation progress
     */
    public float getProgress() {
        return progress;
    }
}

