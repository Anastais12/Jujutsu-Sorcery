package com.anastas1s12.jjs.abilities;

import com.anastas1s12.jjs.JujutsuSorcery;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple registry for techniques. Techniques can contain multiple abilities.
 */
public class TechniqueRegistry {
    private static final Map<ResourceLocation, Technique> REGISTRY = new HashMap<>();

    public static void initialize() {
        // Example technique
        Technique flame = new Technique(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "flame_tech"), "Flame Technique", "A technique focused on flame abilities");
        flame.addAbility(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "flare_strike"));

        Technique shadow = new Technique(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "shadow_tech"), "Shadow Technique", "A technique focused on shadow abilities");
        shadow.addAbility(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "shadow_dash"));

        register(flame);
        register(shadow);
    }

    public static void register(Technique t) {
        REGISTRY.put(t.getId(), t);
    }

    public static Technique get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Map<ResourceLocation, Technique> getAll() {
        return Collections.unmodifiableMap(REGISTRY);
    }
}

