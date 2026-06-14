package com.anastas1s12.jjs.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks ability cooldowns per player
 * Stores: last use time of each ability
 */
public class AbilityCooldownData {
    private final Map<ResourceLocation, Long> cooldowns = new HashMap<>();
    private static final String COOLDOWNS_KEY = "AbilityCooldowns";

    public void recordAbilityUse(ResourceLocation abilityId, long worldTime) {
        cooldowns.put(abilityId, worldTime);
    }

    public boolean isOnCooldown(ResourceLocation abilityId, long worldTime, int cooldownTicks) {
        Long lastUse = cooldowns.get(abilityId);
        if (lastUse == null) return false;

        long timeSinceUse = worldTime - lastUse;
        return timeSinceUse < cooldownTicks;
    }

    public int getRemainingCooldown(ResourceLocation abilityId, long worldTime, int cooldownTicks) {
        Long lastUse = cooldowns.get(abilityId);
        if (lastUse == null) return 0;

        long timeSinceUse = worldTime - lastUse;
        int remaining = cooldownTicks - (int) timeSinceUse;
        return Math.max(0, remaining);
    }

    public float getCooldownProgress(ResourceLocation abilityId, long worldTime, int cooldownTicks) {
        Long lastUse = cooldowns.get(abilityId);
        if (lastUse == null) return 1.0f; // Not on cooldown

        long timeSinceUse = worldTime - lastUse;
        return Math.min(1.0f, (float) timeSinceUse / cooldownTicks);
    }

    public void writeToNbt(CompoundTag tag) {
        ListTag listTag = new ListTag();

        for (Map.Entry<ResourceLocation, Long> entry : cooldowns.entrySet()) {
            CompoundTag cooldownTag = new CompoundTag();
            cooldownTag.putString("ability", entry.getKey().toString());
            cooldownTag.putLong("lastUse", entry.getValue());
            listTag.add(cooldownTag);
        }

        tag.put(COOLDOWNS_KEY, listTag);
    }

    public void readFromNbt(CompoundTag tag) {
        cooldowns.clear();

        if (!tag.contains(COOLDOWNS_KEY)) return;

        ListTag listTag = tag.getList(COOLDOWNS_KEY, Tag.TAG_COMPOUND);
        for (int i = 0; i < listTag.size(); i++) {
            CompoundTag cooldownTag = listTag.getCompound(i);
            ResourceLocation abilityId = parse(cooldownTag.getString("ability"));
            long lastUse = cooldownTag.getLong("lastUse");
            cooldowns.put(abilityId, lastUse);
        }
    }

    private static ResourceLocation parse(String s) {
        if (s == null) return ResourceLocation.fromNamespaceAndPath("", "");
        int idx = s.indexOf(":");
        if (idx <= 0) return ResourceLocation.fromNamespaceAndPath(s, "");
        String ns = s.substring(0, idx);
        String path = s.substring(idx + 1);
        return ResourceLocation.fromNamespaceAndPath(ns, path);
    }

    public void clear() {
        cooldowns.clear();
    }
}
