package com.anastas1s12.jjs.abilities;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.abilities.techiques.template.CursedReinforcement;
import com.anastas1s12.jjs.abilities.techiques.template.EnergyBlast;
import com.anastas1s12.jjs.abilities.techiques.template.FlareStrike;
import com.anastas1s12.jjs.abilities.techiques.template.ShadowDash;
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

        register(new FlareStrike());
        register(new ShadowDash());
        register(new CursedReinforcement());
        register(new EnergyBlast());

        JujutsuSorcery.LOGGER.info("Registered " + REGISTRY.size() + " abilities");
    }


    public static void register(Ability ability) {
        REGISTRY.put(ability.getId(), ability);
        JujutsuSorcery.LOGGER.debug("Registered ability: " + ability.getName());
    }

    public static Ability get(ResourceLocation id) {
        return REGISTRY.get(id);
    }

    public static Map<ResourceLocation, Ability> getAll() {
        return Collections.unmodifiableMap(REGISTRY);
    }
}


