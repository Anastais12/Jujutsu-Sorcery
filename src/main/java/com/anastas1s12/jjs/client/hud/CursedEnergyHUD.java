package com.anastas1s12.jjs.client.hud;

import com.anastas1s12.jjs.client.network.ClientCursedEnergyManager;
import com.anastas1s12.jjs.cursed_energy.CursedEnergyData;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

/**
 * Manages HUD rendering for cursed energy
 */
public class CursedEnergyHUD {
    private static CursedEnergyData ceData = null;
    
    /**
     * Initializes the cursed energy HUD
     */
    public static void initialize() {
        HudRenderCallback.EVENT.register(CursedEnergyHUD::onHudRender);
    }
    
    /**
     * HUD render callback
     */
    private static void onHudRender(GuiGraphics drawContext, DeltaTracker tickDelta) {
        CursedEnergyData data = ClientCursedEnergyManager.getCursedEnergyData();
        if (data != null) {
            CursedEnergyRenderer.render(drawContext, data, drawContext.guiWidth(), drawContext.guiHeight());
        }
    }
    
    /**
     * Updates the cursed energy data
     */
    public static void updateData(CursedEnergyData data) {
        ceData = data;
    }
    
    /**
     * Gets the current cursed energy data
     */
    public static CursedEnergyData getData() {
        return ceData;
    }
    
    /**
     * Resets the HUD data
     */
    public static void reset() {
        ceData = null;
        CursedEnergyRenderer.reset();
    }
}

