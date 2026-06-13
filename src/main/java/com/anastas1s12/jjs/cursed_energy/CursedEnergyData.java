package com.anastas1s12.jjs.cursed_energy;


import net.minecraft.nbt.CompoundTag;

/**
 * Represents cursed energy data for a player
 */
public class CursedEnergyData {
    public static final int MIN_CE = 0;
    public static final int DEFAULT_MAX_CE = 1000;
    public static final int MAX_CE_LIMIT = 100000;
    
    private int currentCE;
    private int maxCE;
    
    public CursedEnergyData() {
        this.currentCE = DEFAULT_MAX_CE;
        this.maxCE = DEFAULT_MAX_CE;
    }
    
    public CursedEnergyData(int currentCE, int maxCE) {
        this.currentCE = Math.max(MIN_CE, Math.min(currentCE, maxCE));
        this.maxCE = Math.min(maxCE, MAX_CE_LIMIT);
    }
    
    /**
     * Writes cursed energy data to NBT
     */
    public void writeToNbt(CompoundTag tag) {
        tag.putInt("CurrentCE", currentCE);
        tag.putInt("MaxCE", maxCE);
    }
    
    /**
     * Reads cursed energy data from NBT
     */
    public static CursedEnergyData readFromNbt(CompoundTag tag) {
        int currentCE = tag.getInt("CurrentCE");
        int maxCE = tag.getInt("MaxCE");
        return new CursedEnergyData(currentCE, maxCE);
    }
    
    /**
     * Adds cursed energy to the player
     */
    public void addCE(int amount) {
        this.currentCE = Math.min(currentCE + Math.abs(amount), maxCE);
    }
    
    /**
     * Removes cursed energy from the player
     */
    public void removeCE(int amount) {
        this.currentCE = Math.max(currentCE - Math.abs(amount), MIN_CE);
    }
    
    /**
     * Sets the max cursed energy, capping at MAX_CE_LIMIT
     */
    public void setMaxCE(int maxCE) {
        this.maxCE = Math.min(Math.max(maxCE, 1), MAX_CE_LIMIT);
        // Ensure current CE doesn't exceed max
        if (currentCE > this.maxCE) {
            currentCE = this.maxCE;
        }
    }
    
    /**
     * Sets the current cursed energy
     */
    public void setCurrentCE(int currentCE) {
        this.currentCE = Math.max(MIN_CE, Math.min(currentCE, maxCE));
    }
    
    // Getters
    public int getCurrentCE() {
        return currentCE;
    }
    
    public int getMaxCE() {
        return maxCE;
    }
    
    public float getPercentage() {
        return maxCE > 0 ? (float) currentCE / maxCE : 0f;
    }
    
    @Override
    public String toString() {
        return String.format("CursedEnergy{current=%d, max=%d}", currentCE, maxCE);
    }
}

