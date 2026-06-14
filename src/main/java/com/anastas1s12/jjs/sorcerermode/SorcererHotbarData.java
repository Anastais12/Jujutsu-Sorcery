package com.anastas1s12.jjs.sorcerermode;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

/**
 * Stores which ability is assigned to each hotbar slot
 * 7 total slots (like a single row of hotbar)
 */
public class SorcererHotbarData {
    private static final int SLOT_COUNT = 7;
    private final ResourceLocation[] slots = new ResourceLocation[SLOT_COUNT];
    private int selectedSlot = 0;

    private static final String HOTBAR_KEY = "SorcererHotbar";
    private static final String SELECTED_KEY = "SelectedSlot";

    public SorcererHotbarData() {
        // Initialize all slots as empty
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots[i] = null;
        }
    }

    /**
     * Assign ability to slot (0-6)
     */
    public void setSlot(int slot, ResourceLocation abilityId) {
        if (slot >= 0 && slot < SLOT_COUNT) {
            slots[slot] = abilityId;
        }
    }

    /**
     * Get ability from slot (returns null if empty)
     */
    public ResourceLocation getSlot(int slot) {
        if (slot >= 0 && slot < SLOT_COUNT) {
            return slots[slot];
        }
        return null;
    }

    /**
     * Clear a slot
     */
    public void clearSlot(int slot) {
        if (slot >= 0 && slot < SLOT_COUNT) {
            slots[slot] = null;
        }
    }

    /**
     * Get selected slot (0-6)
     */
    public int getSelectedSlot() {
        return selectedSlot;
    }

    /**
     * Set selected slot
     */
    public void setSelectedSlot(int slot) {
        if (slot >= 0 && slot < SLOT_COUNT) {
            selectedSlot = slot;
        }
    }

    /**
     * Cycle selection with mouse wheel (up = previous, down = next)
     */
    public void cycleSelection(int direction) {
        selectedSlot += direction;
        if (selectedSlot < 0) selectedSlot = SLOT_COUNT - 1;
        if (selectedSlot >= SLOT_COUNT) selectedSlot = 0;
    }

    /**
     * Get ability in currently selected slot
     */
    public ResourceLocation getSelectedAbility() {
        return slots[selectedSlot];
    }

    /**
     * Check if slot is empty
     */
    public boolean isSlotEmpty(int slot) {
        return slot < 0 || slot >= SLOT_COUNT || slots[slot] == null;
    }

    public int getSlotCount() {
        return SLOT_COUNT;
    }

    // NBT Serialization
    public void writeToNbt(CompoundTag tag) {
        ListTag listTag = new ListTag();

        for (int i = 0; i < SLOT_COUNT; i++) {
            CompoundTag slotTag = new CompoundTag();
            if (slots[i] != null) {
                slotTag.putString("ability", slots[i].toString());
            } else {
                slotTag.putString("ability", "");
            }
            listTag.add(slotTag);
        }

        tag.put(HOTBAR_KEY, listTag);
        tag.putInt(SELECTED_KEY, selectedSlot);
    }

    public void readFromNbt(CompoundTag tag) {
        // Initialize
        for (int i = 0; i < SLOT_COUNT; i++) {
            slots[i] = null;
        }
        selectedSlot = 0;

        if (!tag.contains(HOTBAR_KEY)) return;

        ListTag listTag = tag.getList(HOTBAR_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < Math.min(listTag.size(), SLOT_COUNT); i++) {
            CompoundTag slotTag = listTag.getCompound(i);
            String abilityStr = slotTag.getString("ability");

            if (!abilityStr.isEmpty()) {
                slots[i] = parse(abilityStr);
            }
        }

        if (tag.contains(SELECTED_KEY)) {
            selectedSlot = tag.getInt(SELECTED_KEY);
        }
    }

    private static ResourceLocation parse(String s) {
        if (s == null || s.isEmpty()) return null;
        int idx = s.indexOf(":");
        if (idx <= 0) return ResourceLocation.fromNamespaceAndPath(s, "");
        String ns = s.substring(0, idx);
        String path = s.substring(idx + 1);
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }
}
