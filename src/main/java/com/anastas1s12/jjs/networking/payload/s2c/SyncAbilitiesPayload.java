// This file previously contained a custom payload codec for abilities. Ability syncing now uses
// a raw packet via AbilityNetworking, so this file is kept as an (unused) placeholder to avoid
// large refactors elsewhere.
package com.anastas1s12.jjs.networking.payload.s2c;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.abilities.AbilityData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncAbilitiesPayload(String assignedTechnique, String knownAbilitiesCsv, String cooldownsCsv, String hotbarCsv) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncAbilitiesPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "sync_abilities"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncAbilitiesPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, SyncAbilitiesPayload::assignedTechnique,
            ByteBufCodecs.STRING_UTF8, SyncAbilitiesPayload::knownAbilitiesCsv,
            ByteBufCodecs.STRING_UTF8, SyncAbilitiesPayload::cooldownsCsv,
            ByteBufCodecs.STRING_UTF8, SyncAbilitiesPayload::hotbarCsv,
            SyncAbilitiesPayload::new
    );

    public SyncAbilitiesPayload(AbilityData data) {
        this(
                data.getAssignedTechnique() == null ? "" : data.getAssignedTechnique().toString(),
                buildKnownCsv(data),
                buildCooldownsCsv(data),
                buildHotbarCsv(data)
        );
    }

    private static String buildKnownCsv(AbilityData data) {
        var sb = new StringBuilder();
        boolean first = true;
        for (var rl : data.getKnownAbilities()) {
            if (!first) sb.append(','); first = false;
            sb.append(rl.toString());
        }
        return sb.toString();
    }

    private static String buildCooldownsCsv(AbilityData data) {
        var sb = new StringBuilder();
        boolean first = true;
        for (var e : data.getAllCooldowns().entrySet()) {
            if (!first) sb.append(';'); first = false;
            sb.append(e.getKey().toString()).append('=').append(e.getValue());
        }
        return sb.toString();
    }

    private static String buildHotbarCsv(AbilityData data) {
        var sb = new StringBuilder();
        boolean first = true;
        for (var rl : data.getHotbar()) {
            if (!first) sb.append(','); first = false;
            sb.append(rl == null ? "" : rl.toString());
        }
        return sb.toString();
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}



