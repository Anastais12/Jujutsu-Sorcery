package com.anastas1s12.jjs.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Per-player ability data that is saved to the player's persistent data.
 * Stores known abilities and cooldowns (in ticks remaining).
 */
public class AbilityData {
    private final Set<ResourceLocation> knownAbilities = new HashSet<>();
    private final Map<ResourceLocation, Integer> cooldowns = new HashMap<>();
    private ResourceLocation assignedTechnique = null;
    private final ResourceLocation[] hotbar = new ResourceLocation[8];

    public AbilityData() {
    }

    public void learnAbility(ResourceLocation id) {
        knownAbilities.add(id);
    }

    public void setAssignedTechnique(ResourceLocation tech) {
        this.assignedTechnique = tech;
    }

    public ResourceLocation getAssignedTechnique() {
        return assignedTechnique;
    }

    public boolean knowsAbility(ResourceLocation id) {
        return knownAbilities.contains(id);
    }

    public Set<ResourceLocation> getKnownAbilities() {
        return knownAbilities;
    }

    public void setCooldown(ResourceLocation id, int ticks) {
        if (ticks <= 0) {
            cooldowns.remove(id);
        } else {
            cooldowns.put(id, Math.max(0, ticks));
        }
    }

    public int getCooldown(ResourceLocation id) {
        return cooldowns.getOrDefault(id, 0);
    }

    public Map<ResourceLocation, Integer> getAllCooldowns() {
        return cooldowns;
    }

    public void tickCooldowns() {
        // Decrease all cooldowns by 1 tick
        Map<ResourceLocation, Integer> copy = new HashMap<>(cooldowns);
        for (Map.Entry<ResourceLocation, Integer> e : copy.entrySet()) {
            int v = e.getValue() - 1;
            if (v <= 0) cooldowns.remove(e.getKey()); else cooldowns.put(e.getKey(), v);
        }
    }

    public void setHotbarSlot(int slot, ResourceLocation abilityId) {
        if (slot < 0 || slot >= hotbar.length) return;
        hotbar[slot] = abilityId;
    }

    public ResourceLocation getHotbarSlot(int slot) {
        if (slot < 0 || slot >= hotbar.length) return null;
        return hotbar[slot];
    }

    public ResourceLocation[] getHotbar() {
        return hotbar;
    }

    // Serialization helpers
    public CompoundTag writeToNbt() {
        CompoundTag tag = new CompoundTag();
        ListTag known = new ListTag();
        for (ResourceLocation rl : knownAbilities) {
            known.add(StringTag.valueOf(rl.toString()));
        }
        tag.put("KnownAbilities", known);

        CompoundTag cdTag = new CompoundTag();
        for (Map.Entry<ResourceLocation, Integer> e : cooldowns.entrySet()) {
            cdTag.putInt(e.getKey().toString(), e.getValue());
        }
        tag.put("Cooldowns", cdTag);
        if (assignedTechnique != null) tag.putString("AssignedTechnique", assignedTechnique.toString());

        ListTag hot = new ListTag();
        for (ResourceLocation rl : hotbar) {
            hot.add(StringTag.valueOf(rl == null ? "" : rl.toString()));
        }
        tag.put("Hotbar", hot);
        return tag;
    }

    public static AbilityData readFromNbt(CompoundTag tag) {
        AbilityData data = new AbilityData();
        ListTag known = tag.getList("KnownAbilities", 8); // 8 = StringTag
        for (int i = 0; i < known.size(); i++) {
            String s = known.getString(i);
            if (s != null && !s.isEmpty()) data.knownAbilities.add(parse(s));
        }

        CompoundTag cdTag = tag.getCompound("Cooldowns");
        for (String key : cdTag.getAllKeys()) {
            try {
                ResourceLocation rl = parse(key);
                int val = cdTag.getInt(key);
                if (val > 0) data.cooldowns.put(rl, val);
            } catch (Exception ignored) {
            }
        }

        if (tag.contains("AssignedTechnique")) {
            String s = tag.getString("AssignedTechnique");
            if (s != null && !s.isEmpty()) data.assignedTechnique = parse(s);
        }

        ListTag hot = tag.getList("Hotbar", 8);
        for (int i = 0; i < Math.min(hot.size(), data.hotbar.length); i++) {
            String s = hot.getString(i);
            if (s != null && !s.isEmpty()) data.hotbar[i] = parse(s);
        }

        return data;
    }

    private static ResourceLocation parse(String s) {
        if (s == null) return ResourceLocation.fromNamespaceAndPath("", "");
        int idx = s.indexOf(":");
        if (idx <= 0) return ResourceLocation.fromNamespaceAndPath(s, "");
        String ns = s.substring(0, idx);
        String path = s.substring(idx + 1);
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }
}


