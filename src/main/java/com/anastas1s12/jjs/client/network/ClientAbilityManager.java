package com.anastas1s12.jjs.client.network;

import net.minecraft.resources.ResourceLocation;
import com.anastas1s12.jjs.networking.payload.s2c.SyncAbilitiesPayload;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Client-side holder for ability state synced from the server.
 */
public class ClientAbilityManager {
    private static final int HOTBAR_SIZE = 8;
    private static Set<ResourceLocation> known = new HashSet<>();
    private static Map<ResourceLocation, Integer> cooldowns = new HashMap<>();
    private static ResourceLocation assignedTechnique = null;
    private static ResourceLocation[] hotbar = new ResourceLocation[HOTBAR_SIZE];

    public static void updateFromPayload(SyncAbilitiesPayload payload) {
        updateFromRaw(payload.assignedTechnique(), payload.knownAbilitiesCsv(), payload.cooldownsCsv(), payload.hotbarCsv());
    }

    public static void updateFromRaw(String tech, String knownCsv, String cdCsv, String hotCsv) {
        assignedTechnique = (tech == null || tech.isEmpty()) ? null : ResourceLocation.tryParse(tech);

        known.clear();
        cooldowns.clear();

        if (knownCsv != null && !knownCsv.isEmpty()) {
            String[] parts = knownCsv.split(",");
            for (String p : parts) {
                if (p == null || p.isEmpty()) continue;
                ResourceLocation rl = ResourceLocation.tryParse(p);
                if (rl != null) known.add(rl);
            }
        }

        if (cdCsv != null && !cdCsv.isEmpty()) {
            String[] pairs = cdCsv.split(";");
            for (String pair : pairs) {
                if (pair == null || pair.isEmpty()) continue;
                String[] kv = pair.split("=");
                if (kv.length != 2) continue;
                ResourceLocation rl = ResourceLocation.tryParse(kv[0]);
                try {
                    int v = Integer.parseInt(kv[1]);
                    if (rl != null && v > 0) cooldowns.put(rl, v);
                } catch (NumberFormatException ignored) {}
            }
        }

        if (hotCsv != null) {
            String[] slots = hotCsv.split(",");
            for (int i = 0; i < Math.min(slots.length, hotbar.length); i++) {
                String s = slots[i];
                hotbar[i] = (s == null || s.isEmpty()) ? null : ResourceLocation.tryParse(s);
            }
        }
    }

    public static boolean clientKnows(ResourceLocation id) {
        return known.contains(id);
    }

    public static int getCooldown(ResourceLocation id) {
        return cooldowns.getOrDefault(id, 0);
    }

    public static ResourceLocation getHotbarSlot(int slot) {
        if (slot < 0 || slot >= hotbar.length) return null;
        return hotbar[slot];
    }

    public static ResourceLocation getAssignedTechnique() {
        return assignedTechnique;
    }
}


