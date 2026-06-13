package com.anastas1s12.jjs.abilities;

import com.anastas1s12.jjs.cursed_energy.CursedEnergyData;
import com.anastas1s12.jjs.cursed_energy.CursedEnergyManager;
import com.anastas1s12.jjs.utils.IPlayerDataAccessor;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import com.anastas1s12.jjs.abilities.Technique;
import net.minecraft.world.entity.player.Player;

/**
 * Server-side manager for player abilities. Stores per-player AbilityData in the player's persistent NBT.
 */
public class AbilityManager {
    private static final String ABILITY_NBT_KEY = "JJS_AbilityData";

    public static void initialize() {
        // Ensure data exists for players when they join
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ensureAbilityDataExists(handler.player);
            // Sync abilities immediately when player joins
            if (handler.player instanceof ServerPlayer sp) {
                AbilityNetworking.sendSyncPacket(sp, getAbilityData(sp));
            }
        });
    }

    private static void ensureAbilityDataExists(Player player) {
        CompoundTag playerNbt = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        if (!playerNbt.contains(ABILITY_NBT_KEY)) {
            AbilityData data = new AbilityData();
            setAbilityData(player, data);
        }
    }

    public static AbilityData getAbilityData(Player player) {
        CompoundTag playerNbt = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        if (playerNbt.contains(ABILITY_NBT_KEY)) {
            return AbilityData.readFromNbt(playerNbt.getCompound(ABILITY_NBT_KEY));
        }
        AbilityData data = new AbilityData();
        setAbilityData(player, data);
        return data;
    }

    public static void setAbilityData(Player player, AbilityData data) {
        CompoundTag playerNbt = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        playerNbt.put(ABILITY_NBT_KEY, data.writeToNbt());
        if (player instanceof ServerPlayer serverPlayer) {
            AbilityNetworking.sendSyncPacket(serverPlayer, data);
        }
    }

    public static void learnAbility(Player player, ResourceLocation abilityId) {
        AbilityData data = getAbilityData(player);
        data.learnAbility(abilityId);
        setAbilityData(player, data);
    }

    public static void setAbilityCooldown(Player player, ResourceLocation abilityId, int ticks) {
        AbilityData data = getAbilityData(player);
        data.setCooldown(abilityId, ticks);
        setAbilityData(player, data);
    }

    /**
     * Assigns a technique to the player, learns all abilities in the technique, and syncs.
     */
    public static boolean assignTechnique(Player player, ResourceLocation techniqueId) {
        Technique tech = TechniqueRegistry.get(techniqueId);
        if (tech == null) return false;
        AbilityData data = getAbilityData(player);
        data.setAssignedTechnique(techniqueId);
        for (ResourceLocation abilityId : tech.getAbilities()) {
            data.learnAbility(abilityId);
        }
        setAbilityData(player, data);
        return true;
    }

    /**
     * Attempt to use an ability for the player. This will check if the player knows the ability,
     * has enough cursed energy, and isn't on cooldown. If successful, it will deduct CE and set cooldown.
     * Returns true if the ability was used.
     */
    public static boolean tryUseAbility(Player player, Ability ability) {
        if (ability == null) return false;
        AbilityData data = getAbilityData(player);
        if (!data.knowsAbility(ability.getId())) return false;

        // Check cooldown
        int remaining = data.getCooldown(ability.getId());
        if (remaining > 0) return false;

        // Check cursed energy
        CursedEnergyData ceData = CursedEnergyManager.getCursedEnergyData(player);
        if (ceData.getCurrentCE() < ability.getCursedEnergyCost()) return false;

        // Consume CE and set cooldown
        CursedEnergyManager.removeCursedEnergy(player, ability.getCursedEnergyCost());
        data.setCooldown(ability.getId(), ability.getCooldownTicks());
        setAbilityData(player, data);
        // TODO: trigger ability effect (to be implemented by gameplay code)
        return true;
    }
}



