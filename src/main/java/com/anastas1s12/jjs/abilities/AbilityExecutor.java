package com.anastas1s12.jjs.abilities;

import com.anastas1s12.jjs.cursed_energy.CursedEnergyManager;
import com.anastas1s12.jjs.utils.IPlayerDataAccessor;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;

/**
 * Handles ability execution: CE drain, cooldown, and effects
 */
public class AbilityExecutor {

    private static final String COOLDOWN_DATA_KEY = "AbilityCooldownData";

    /**
     * Attempts to execute an ability for a player
     * Returns true if successful, false if not enough CE or on cooldown
     */
    public static boolean executeAbility(ServerPlayer player, ResourceLocation abilityId) {
        Ability ability = AbilityRegistry.get(abilityId);
        if (ability == null) {
            return false; // Ability doesn't exist
        }

        // Check CE requirement
        var ceData = CursedEnergyManager.getCursedEnergyData(player);
        if (ceData.getCurrentCE() < ability.getCursedEnergyCost()) {
            return false; // Not enough CE
        }

        // Check cooldown
        AbilityCooldownData cooldowns = getCooldownData(player);
        long worldTime = player.serverLevel().getGameTime();
        if (cooldowns.isOnCooldown(abilityId, worldTime, ability.getCooldownTicks())) {
            return false;
        }

        CursedEnergyManager.removeCursedEnergy(player, ability.getCursedEnergyCost());

        cooldowns.recordAbilityUse(abilityId, worldTime);
        saveCooldownData(player, cooldowns);

        ability.execute(player);

        CursedEnergyManager.syncToClient(player);

        return true;
    }

    /**
     * Called when player starts holding an ability (for HOLD trigger type)
     */
    public static void onAbilityHoldStart(ServerPlayer player, ResourceLocation abilityId) {
        Ability ability = AbilityRegistry.get(abilityId);
        if (ability == null || ability.getTriggerType() != Ability.TriggerType.HOLD) {
            return;
        }

        var ceData = CursedEnergyManager.getCursedEnergyData(player);
        if (ceData.getCurrentCE() < ability.getCursedEnergyCost()) {
            return; // Not enough CE
        }
    }

    /**
     * Called each tick while ability is held
     */
    public static void onAbilityHold(ServerPlayer player, ResourceLocation abilityId, int holdTicks) {
        Ability ability = AbilityRegistry.get(abilityId);
        if (ability == null) return;

        ability.onHold(player, holdTicks);
    }

    /**
     * Called when player releases a held ability
     */
    public static void onAbilityHoldRelease(ServerPlayer player, ResourceLocation abilityId, int heldTicks) {
        Ability ability = AbilityRegistry.get(abilityId);
        if (ability == null || ability.getTriggerType() != Ability.TriggerType.HOLD) {
            return;
        }

        // Check CE requirement
        var ceData = CursedEnergyManager.getCursedEnergyData(player);
        if (ceData.getCurrentCE() < ability.getCursedEnergyCost()) {
            return; // Not enough CE
        }

        // Check cooldown
        AbilityCooldownData cooldowns = getCooldownData(player);
        long worldTime = player.serverLevel().getGameTime();
        if (cooldowns.isOnCooldown(abilityId, worldTime, ability.getCooldownTicks())) {
            return; // Still on cooldown
        }

        // Execute
        CursedEnergyManager.removeCursedEnergy(player, ability.getCursedEnergyCost());
        cooldowns.recordAbilityUse(abilityId, worldTime);
        saveCooldownData(player, cooldowns);
        ability.onRelease(player, heldTicks);
        CursedEnergyManager.syncToClient(player);
    }

    /**
     * Get cooldown data for a player
     */
    public static AbilityCooldownData getCooldownData(ServerPlayer player) {
        CompoundTag playerData = ((IPlayerDataAccessor) player).jjs$getPersistentData();
        AbilityCooldownData cooldowns = new AbilityCooldownData();

        if (playerData.contains(COOLDOWN_DATA_KEY)) {
            cooldowns.readFromNbt(playerData.getCompound(COOLDOWN_DATA_KEY));
        }

        return cooldowns;
    }

    /**
     * Save cooldown data for a player
     */
    public static void saveCooldownData(ServerPlayer player, AbilityCooldownData cooldowns) {
        CompoundTag playerData = ((IPlayerDataAccessor) player).jjs$getPersistentData();

        CompoundTag cooldownTag = new CompoundTag();
        cooldowns.writeToNbt(cooldownTag);
        playerData.put(COOLDOWN_DATA_KEY, cooldownTag);
    }


    /**
     * Get cooldown progress (0.0 = just used, 1.0 = ready)
     */
    public static float getCooldownProgress(ServerPlayer player, ResourceLocation abilityId, Ability ability) {
        if (ability == null) return 1.0f;

        AbilityCooldownData cooldowns = getCooldownData(player);
        long worldTime = player.serverLevel().getGameTime();
        return cooldowns.getCooldownProgress(abilityId, worldTime, ability.getCooldownTicks());
    }

    /**
     * Get remaining cooldown ticks
     */
    public static int getRemainingCooldown(ServerPlayer player, ResourceLocation abilityId, Ability ability) {
        if (ability == null) return 0;

        AbilityCooldownData cooldowns = getCooldownData(player);
        long worldTime = player.serverLevel().getGameTime();
        return cooldowns.getRemainingCooldown(abilityId, worldTime, ability.getCooldownTicks());
    }
}
