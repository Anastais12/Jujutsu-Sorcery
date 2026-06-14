package com.anastas1s12.jjs.abilities.techiques.template;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.abilities.Ability;
import com.anastas1s12.jjs.abilities.AbilityType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.core.registries.Registries;

/**
 * Flare Strike - A focused burst of cursed energy that deals area damage
 *
 * Effect: Creates an explosion of cursed energy around the player
 * - Damages all nearby entities (except player)
 * - Creates particle effects
 * - Knockback effect
 */
public class FlareStrike extends Ability {
    private static final float BASE_DAMAGE = 8.0f;
    private static final double RANGE = 5.0; // Radius of effect
    private static final float KNOCKBACK = 1.5f;

    public FlareStrike() {
        super(
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "flare_strike"),
                "Flare Strike",
                "A focused burst of cursed energy that deals damage in a nearby area.",
                ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/ability/flare_strike.png"),
                50,  // CE cost
                20,  // Cooldown: 1 second at 20 tps
                AbilityType.LEARNABLE,
                TriggerType.CLICK
        );
    }

    @Override
    public void execute(ServerPlayer player) {
        Vec3 playerPos = player.position();

        // Create explosion effect
        // TODO: Add particle effect for cursed energy burst

        // Get all entities in range (except player)
        AABB range = new AABB(
                playerPos.x - RANGE, playerPos.y - RANGE, playerPos.z - RANGE,
                playerPos.x + RANGE, playerPos.y + RANGE, playerPos.z + RANGE
        );

        var level = player.serverLevel();
        var entities = level.getEntities(player, range);

        // Damage entities
        DamageSource damageSource = level.damageSources().generic();

        for (Entity entity : entities) {
            if (entity == player) continue; // Don't hurt self

            // Calculate damage with distance falloff
            double distance = player.distanceTo(entity);
            float damage = (float) (BASE_DAMAGE * (1.0 - (distance / RANGE)));
            damage = Math.max(1.0f, damage); // Minimum 1 damage

            // Apply damage
            entity.hurt(damageSource, damage);

            // Apply knockback
            Vec3 direction = entity.position().subtract(playerPos).normalize();
            entity.push(direction.x * KNOCKBACK, KNOCKBACK * 0.5, direction.z * KNOCKBACK);
        }

        // Play sound
        // TODO: Add sound effect
    }
}
