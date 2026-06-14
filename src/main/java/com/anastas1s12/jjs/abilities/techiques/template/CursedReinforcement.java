package com.anastas1s12.jjs.abilities.techiques.template;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.abilities.Ability;
import com.anastas1s12.jjs.abilities.AbilityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

/**
 * Cursed Reinforcement - Buff player defense temporarily
 *
 * Effect: Applies protective buffs to the player
 * - Resistance effect (reduce damage)
 * - Absorption effect (extra health)
 * - Visual effect particles
 */
public class CursedReinforcement extends Ability {
    private static final int BUFF_DURATION = 200; // 10 seconds at 20 tps
    private static final int RESISTANCE_LEVEL = 1; // Resistance II = reduces 80% damage
    private static final int ABSORPTION_HEARTS = 4; // 2 extra hearts worth of absorption

    public CursedReinforcement() {
        super(
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "cursed_reinforcement"),
                "Cursed Reinforcement",
                "Reinforce your body with cursed energy to reduce incoming damage.",
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/ability/cursed_reinforcement.png"),
                60,  // CE cost
                60,  // Cooldown: 3 seconds at 20 tps
                AbilityType.LEARNABLE,
                TriggerType.CLICK
        );
    }

    @Override
    public void execute(ServerPlayer player) {
        // Apply resistance (reduces incoming damage)
        var resistanceEffect = new MobEffectInstance(
                MobEffects.DAMAGE_RESISTANCE,
                BUFF_DURATION,
                RESISTANCE_LEVEL,
                false, // not ambient
                true   // show particles
        );
        player.addEffect(resistanceEffect);

        // Apply absorption (extra health temporarily)
        var absorptionEffect = new MobEffectInstance(
                MobEffects.ABSORPTION,
                BUFF_DURATION,
                ABSORPTION_HEARTS - 1, // Amplifier is 0-indexed
                false, // not ambient
                true   // show particles
        );
        player.addEffect(absorptionEffect);

        // TODO: Add purple aura particle effect
        // TODO: Add reinforcement sound effect
    }
}
