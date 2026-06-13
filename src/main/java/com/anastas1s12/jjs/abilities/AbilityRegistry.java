package com.anastas1s12.jjs.abilities;

import com.anastas1s12.jjs.JujutsuSorcery;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple static registry for abilities. Abilities are registered at mod initialization.
 */
public class AbilityRegistry {
    private static final Map<ResourceLocation, Ability> REGISTRY = new HashMap<>();

    public static void initialize() {
        // Register example abilities here (replace or extend with JSON/resource-driven loader later)
        register(new Ability(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "flare_strike"),
                "Flare Strike",
                "A focused burst of cursed energy that deals damage in a small area.",
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/ability/flare_strike.png"),
                50, // cost
                20, // cooldown ticks (1 second at 20tps)
                com.anastas1s12.jjs.abilities.AbilityType.LEARNABLE
        ));

        register(new Ability(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "shadow_dash"),
                "Shadow Dash",
                "Dash forward quickly, ignoring collisions for a moment.",
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/ability/shadow_dash.png"),
                30,
                40,
                com.anastas1s12.jjs.abilities.AbilityType.INNATE
        ));
    }

    public static void register(Ability ability) {
        REGISTRY.put(ability.getId(), ability);
    }

    public static Ability get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Map<ResourceLocation, Ability> getAll() {
        return Collections.unmodifiableMap(REGISTRY);
    }
}


