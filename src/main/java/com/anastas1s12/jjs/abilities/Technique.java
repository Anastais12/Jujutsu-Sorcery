package com.anastas1s12.jjs.abilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a technique (a grouping of abilities) that can be assigned to a player.
 */
public class Technique {
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final List<ResourceLocation> abilities = new ArrayList<>();

    public Technique(ResourceLocation id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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

    public void addAbility(ResourceLocation abilityId) {
        if (!abilities.contains(abilityId)) abilities.add(abilityId);
    }

    public List<ResourceLocation> getAbilities() {
        return abilities;
    }

    public CompoundTag toNbt() {
        CompoundTag t = new CompoundTag();
        t.putString("id", id.toString());
        t.putString("name", name);
        t.putString("description", description);
        ListTag list = new ListTag();
        for (ResourceLocation rl : abilities) list.add(StringTag.valueOf(rl.toString()));
        t.put("abilities", list);
        return t;
    }

    public static Technique fromNbt(CompoundTag tag) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(tag.getString("id").split(":")[0], tag.getString("id").split(":")[1]);
        Technique tech = new Technique(id, tag.getString("name"), tag.getString("description"));
        ListTag list = tag.getList("abilities", 8);
        for (int i = 0; i < list.size(); i++) {
            tech.addAbility(ResourceLocation.fromNamespaceAndPath(list.getString(i).split(":")[0], list.getString(i).split(":")[1]));
        }
        return tech;
    }
}

