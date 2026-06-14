package com.anastas1s12.jjs.networking.payload.c2s;

import com.anastas1s12.jjs.JujutsuSorcery;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Networking payload for assigning abilities to hotbar slots
 * Client -> Server: "I want to assign ability X to slot Y"
 */
public record AssignAbilityPayload(int slotIndex, ResourceLocation abilityId, boolean clearSlot) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<AssignAbilityPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "assign_ability"));

    // Custom StreamCodec to replicate your original nullable/conditional logic perfectly
    public static final StreamCodec<FriendlyByteBuf, AssignAbilityPayload> CODEC = StreamCodec.of(
            (buf, payload) -> {
                buf.writeInt(payload.slotIndex());
                buf.writeBoolean(payload.clearSlot());
                if (!payload.clearSlot()) {
                    buf.writeResourceLocation(payload.abilityId() != null ? payload.abilityId() : ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "empty"));
                }
            },
            buf -> {
                int slotIndex = buf.readInt();
                boolean clearSlot = buf.readBoolean();
                ResourceLocation abilityId = null;
                if (!clearSlot) {
                    abilityId = buf.readResourceLocation();
                }
                return new AssignAbilityPayload(slotIndex, abilityId, clearSlot);
            }
    );

    // Convenience constructor for assigning an ability
    public AssignAbilityPayload(int slotIndex, ResourceLocation abilityId) {
        this(slotIndex, abilityId, false);
    }

    // Convenience constructor for clearing a slot
    public AssignAbilityPayload(int slotIndex, boolean clearSlot) {
        this(slotIndex, null, clearSlot);
    }

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
