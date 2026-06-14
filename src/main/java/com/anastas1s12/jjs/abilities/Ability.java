package com.anastas1s12.jjs.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * Represents a single ability with basic metadata used by the ability system.
 */
public class Ability {

    public enum TriggerType {
        CLICK,
        HOLD,
        TOGGLE,
        PASSIVE
    }

    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final ResourceLocation icon;
    private final int cursedEnergyCost;
    private final int cooldownTicks;
    private final AbilityType type;
    private final TriggerType triggerType;

    public Ability(ResourceLocation id, String name, String description, ResourceLocation icon,
                   int cursedEnergyCost, int cooldownTicks) {
        this(id, name, description, icon, cursedEnergyCost, cooldownTicks, AbilityType.NONE, TriggerType.CLICK);
    }

    public Ability(ResourceLocation id, String name, String description, ResourceLocation icon,
                   int cursedEnergyCost, int cooldownTicks, AbilityType type) {
        this(id, name, description, icon, cursedEnergyCost, cooldownTicks, type, TriggerType.CLICK);
    }

    public Ability(ResourceLocation id, String name, String description, ResourceLocation icon,
                   int cursedEnergyCost, int cooldownTicks, AbilityType type, TriggerType triggerType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.cursedEnergyCost = Math.max(0, cursedEnergyCost);
        this.cooldownTicks = Math.max(0, cooldownTicks);
        this.type = type == null ? AbilityType.NONE : type;
        this.triggerType = triggerType == null ? TriggerType.CLICK : triggerType;
    }

    // Getters
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

    public TriggerType getTriggerType() {
        return triggerType;
    }

    /**
     * Override this to implement ability effects
     */
    public void execute(ServerPlayer player) {
        // Base implementation - override in subclasses
    }

    /**
     * Called when ability is held (for HOLD trigger type)
     */
    public void onHold(ServerPlayer player, int holdTicks) {
        // Override for hold abilities
    }

    /**
     * Called when ability is released (for HOLD trigger type)
     */
    public void onRelease(ServerPlayer player, int heldTicks) {
        // Override for hold abilities
    }

    // NBT Serialization
    public CompoundTag toNbt() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", id.toString());
        tag.putString("name", name);
        tag.putString("description", description);
        tag.putString("icon", icon.toString());
        tag.putInt("cost", cursedEnergyCost);
        tag.putInt("cooldown", cooldownTicks);
        tag.putString("type", type == null ? "none" : type.name());
        tag.putString("triggerType", triggerType.name());
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

        TriggerType triggerType = TriggerType.CLICK;
        try {
            String t = tag.getString("triggerType");
            if (t != null && !t.isEmpty()) triggerType = TriggerType.valueOf(t);
        } catch (Exception ignored) {}

        return new Ability(id, name, description, icon, cost, cooldown, type, triggerType);
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


