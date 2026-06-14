package com.anastas1s12.jjs.abilities.techiques.template;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.abilities.Ability;
import com.anastas1s12.jjs.abilities.AbilityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * Shadow Dash - Dash forward quickly, ignoring collisions temporarily
 *
 * Effect: Teleports player forward in the direction they're looking
 * - Instant movement
 * - Gives brief speed boost
 * - Creates visual effect trail
 */
public class ShadowDash extends Ability {
    private static final double DASH_DISTANCE = 15.0; // How far to dash
    private static final int SPEED_DURATION = 10; // Ticks of speed after dash

    public ShadowDash() {
        super(
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "shadow_dash"),
                "Shadow Dash",
                "Dash forward quickly, ignoring obstacles for a moment.",
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/ability/shadow_dash.png"),
                30,  // CE cost
                40,  // Cooldown: 2 seconds at 20 tps
                AbilityType.INNATE,
                TriggerType.CLICK
        );
    }

    @Override
    public void execute(ServerPlayer player) {
        // Get player's look direction
        Vec3 lookDirection = player.getLookAngle();

        // Calculate new position
        Vec3 currentPos = player.position();
        Vec3 newPos = currentPos.add(
                lookDirection.x * DASH_DISTANCE,
                0,  // Don't move vertically
                lookDirection.z * DASH_DISTANCE
        );

        // Teleport player
        player.teleportTo(newPos.x, newPos.y, newPos.z);

        // Give brief speed boost
        var speedEffect = new net.minecraft.world.effect.MobEffectInstance(
                net.minecraft.world.effect.MobEffects.MOVEMENT_SPEED,
                SPEED_DURATION,
                1  // Amplifier (Speed II)
        );
        player.addEffect(speedEffect);

        // TODO: Add particle trail effect
        // TODO: Add sound effect
    }
}
