package com.anastas1s12.jjs.mixin;

import com.anastas1s12.jjs.sorcerermode.SorcererModeManager;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Server-side mixin that prevents hotbar interactions while a player is in sorcerer mode.
 *
 * This cancels packets that change the carried hotbar slot and container click packets
 * so players cannot manipulate the hotbar while sorcerer mode is active.
 */
@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerMixin {

    @Shadow
    public ServerPlayer player;

    @Inject(method = "handleSetCarriedItem", at = @At("HEAD"), cancellable = true)
    private void jjs$onHandleSetCarriedItem(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
        if (player != null && SorcererModeManager.getSorcererModeData(player).isInSorcererMode()) {
            ci.cancel();
        }
    }

    @Inject(method = "handleContainerClick", at = @At("HEAD"), cancellable = true)
    private void jjs$onHandleContainerClick(ServerboundContainerClickPacket packet, CallbackInfo ci) {
        if (player != null && SorcererModeManager.getSorcererModeData(player).isInSorcererMode()) {
            ci.cancel();
        }
    }
}

