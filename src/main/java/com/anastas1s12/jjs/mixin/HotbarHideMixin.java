package com.anastas1s12.jjs.mixin;

import com.anastas1s12.jjs.client.sorcerermode.SorcererModeUIHandler;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin to hide the hotbar when in sorcerer mode
 */
@Mixin(Gui.class)
public class HotbarHideMixin {

    @Inject(method = "renderHotbarAndDecorations", at = @At("HEAD"), cancellable = true)
    private void jjs$hideHotbarInSorcererMode(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (SorcererModeUIHandler.isInSorcererMode()) {
            ci.cancel();
        }
    }
}
