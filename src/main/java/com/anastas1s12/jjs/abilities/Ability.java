package com.anastas1s12.jjs.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * Represents a single ability with basic metadata used by the ability system.
 */
public class Ability {
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final ResourceLocation icon; // resource location to an icon texture
    private final int cursedEnergyCost;
    private final int cooldownTicks;
    private final AbilityType type;

    public Ability(ResourceLocation id, String name, String description, ResourceLocation icon, int cursedEnergyCost, int cooldownTicks) {
        this(id, name, description, icon, cursedEnergyCost, cooldownTicks, AbilityType.NONE);
    }

    public Ability(ResourceLocation id, String name, String description, ResourceLocation icon, int cursedEnergyCost, int cooldownTicks, AbilityType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.cursedEnergyCost = Math.max(0, cursedEnergyCost);
        this.cooldownTicks = Math.max(0, cooldownTicks);
        this.type = type == null ? AbilityType.NONE : type;
    }

    public ResourceLocation getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public int getCursedEnergyCost() {
        return cursedEnergyCost;
    }

    public int getCooldownTicks() {
        return cooldownTicks;
    }

    public AbilityType getType() {
        return type;
    }

    // Optional helper: write minimal representation to NBT (id only usually sufficient)
    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id.toString());
        tag.putString("name", name);
        tag.putString("description", description);
        tag.putString("icon", icon.toString());
        tag.putInt("cost", cursedEnergyCost);
        tag.putInt("cooldown", cooldownTicks);
        tag.putString("type", type == null ? "none" : type.name());
        return tag;
    }

    public static Ability fromNbt(CompoundTag tag) {
        ResourceLocation id = parse(tag.getString("id"));
        String name = tag.getString("name");
        String description = tag.getString("description");
        ResourceLocation icon = parse(tag.getString("icon"));
        int cost = tag.getInt("cost");
        int cooldown = tag.getInt("cooldown");
        AbilityType type = AbilityType.NONE;
        try {
            String t = tag.getString("type");
            if (t != null && !t.isEmpty()) type = AbilityType.valueOf(t);
        } catch (Exception ignored) {}
        return new Ability(id, name, description, icon, cost, cooldown, type);
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


