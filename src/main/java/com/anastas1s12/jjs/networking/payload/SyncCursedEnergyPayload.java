package com.anastas1s12.jjs.networking.payload;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.cursed_energy.CursedEnergyData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Network payload for syncing cursed energy data to clients
 */
public record SyncCursedEnergyPayload(int currentCE, int maxCE) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncCursedEnergyPayload> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "sync_cursed_energy"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCursedEnergyPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, SyncCursedEnergyPayload::currentCE, ByteBufCodecs.INT, SyncCursedEnergyPayload::maxCE, SyncCursedEnergyPayload::new);
    /**
     * Creates a payload from CursedEnergyData
     */
    public SyncCursedEnergyPayload(CursedEnergyData data) {
        this(data.getCurrentCE(), data.getMaxCE());
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

