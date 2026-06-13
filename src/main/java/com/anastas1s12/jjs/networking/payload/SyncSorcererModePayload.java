package com.anastas1s12.jjs.networking.payload;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.sorcerermode.SorcererModeData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Network payload for syncing sorcerer mode data to clients
 */
public record SyncSorcererModePayload(boolean isInSorcererMode, boolean hasEnteredSorcererModeBefore) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncSorcererModePayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "sync_sorcerer_mode")
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, SyncSorcererModePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SyncSorcererModePayload::isInSorcererMode,
            ByteBufCodecs.BOOL, SyncSorcererModePayload::hasEnteredSorcererModeBefore,
            SyncSorcererModePayload::new
    );
    
    /**
     * Creates a payload from SorcererModeData
     */
    public SyncSorcererModePayload(SorcererModeData data) {
        this(data.isInSorcererMode(), data.hasEnteredSorcererModeBefore());
    }
    
    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

