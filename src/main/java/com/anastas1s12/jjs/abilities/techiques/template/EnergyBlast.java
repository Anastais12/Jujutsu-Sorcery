package com.anastas1s12.jjs.abilities.techiques.template;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.abilities.Ability;
import com.anastas1s12.jjs.abilities.AbilityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.phys.Vec3;

/**
 * Energy Blast - Fire a projectile of cursed energy
 *
 * Effect: Launches a projectile that damages on impact
 * - Uses SmallFireball as base (no explosion, just damage)
 * - Can be dodged/blocked
 * - Travels in look direction
 */
public class EnergyBlast extends Ability {
    private static final double PROJECTILE_SPEED = 1.0; // Speed multiplier
    private static final float PROJECTILE_DAMAGE = 6.0f; // Damage on hit

    public EnergyBlast() {
        super(
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "energy_blast"),
                "Energy Blast",
                "Launch a projectile of cursed energy that damages enemies on impact.",
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/ability/energy_blast.png"),
                40,  // CE cost
                15,  // Cooldown: 0.75 seconds at 20 tps (rapid-fire ability)
                AbilityType.LEARNABLE,
                TriggerType.CLICK
        );
    }

    @Override
    public void execute(ServerPlayer player) {
        Vec3 lookDirection = player.getLookAngle();
        Vec3 spawnPos = player.getEyePosition();

        Vec3 powerVector = new Vec3(
                lookDirection.x * PROJECTILE_SPEED,
                lookDirection.y * PROJECTILE_SPEED,
                lookDirection.z * PROJECTILE_SPEED
        );

        SmallFireball projectile = new SmallFireball(
                player.serverLevel(),
                player,
                powerVector
        );

        projectile.moveTo(spawnPos.x, spawnPos.y, spawnPos.z, player.getYRot(), player.getXRot());

        projectile.setPos(spawnPos);

        player.serverLevel().addFreshEntity(projectile);

        // TODO: Replace with custom cursed energy projectile
        // TODO: Add particle trail
        // TODO: Add sound effect
    }
}
