package com.anastas1s12.jjs.sorcerermode;

import net.minecraft.nbt.CompoundTag;

/**
 * Represents sorcerer mode state data for a player
 */
public class SorcererModeData {
    private boolean isInSorcererMode;
    private boolean hasEnteredSorcererModeBefore;
    
    public SorcererModeData() {
        this.isInSorcererMode = false;
        this.hasEnteredSorcererModeBefore = false;
    }
    
    public SorcererModeData(boolean isInSorcererMode, boolean hasEnteredSorcererModeBefore) {
        this.isInSorcererMode = isInSorcererMode;
        this.hasEnteredSorcererModeBefore = hasEnteredSorcererModeBefore;
    }
    
    /**
     * Writes sorcerer mode data to NBT
     */
    public void writeToNbt(CompoundTag tag) {
        tag.putBoolean("IsInSorcererMode", isInSorcererMode);
        tag.putBoolean("HasEnteredSorcererModeBefore", hasEnteredSorcererModeBefore);
    }
    
    /**
     * Reads sorcerer mode data from NBT
     */
    public static SorcererModeData readFromNbt(CompoundTag tag) {
        boolean isInSorcererMode = tag.getBoolean("IsInSorcererMode");
        boolean hasEnteredSorcererModeBefore = tag.getBoolean("HasEnteredSorcererModeBefore");
        return new SorcererModeData(isInSorcererMode, hasEnteredSorcererModeBefore);
    }
    
    // Getters and Setters
    public boolean isInSorcererMode() {
        return isInSorcererMode;
    }
    
    public void setInSorcererMode(boolean inSorcererMode) {
        isInSorcererMode = inSorcererMode;
    }
    
    public boolean hasEnteredSorcererModeBefore() {
        return hasEnteredSorcererModeBefore;
    }
    
    public void setHasEnteredSorcererModeBefore(boolean hasEntered) {
        hasEnteredSorcererModeBefore = hasEntered;
    }
    
    @Override
    public String toString() {
        return String.format("SorcererMode{inMode=%s, hasEntered=%s}", isInSorcererMode, hasEnteredSorcererModeBefore);
    }
}

