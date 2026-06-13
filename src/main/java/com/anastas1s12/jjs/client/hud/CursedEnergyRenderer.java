package com.anastas1s12.jjs.client.hud;

import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.cursed_energy.CursedEnergyData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

/**
 * Renderer for cursed energy HUD display
 * Shows 16x16 animated icon to the right of hotbar with CE text display
 */
public class CursedEnergyRenderer {
    private static final ResourceLocation CURSED_ENERGY_ICON =
            ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/hud/cursed_energy_icon.png");

    // Icon dimensions - 16x16 icon with 4 animation frames
    // Texture should be 16 wide x 64 tall (4 frames stacked vertically)
    private static final int ICON_WIDTH = 16;
    private static final int ICON_HEIGHT = 16;
    private static final int FRAME_HEIGHT = 16;  // Each frame is 16x16

    // HUD positioning - to the right of hotbar
    private static final int OFFSET_FROM_HOTBAR_RIGHT = 10;  // 10 pixels right of hotbar
    private static final int OFFSET_FROM_BOTTOM = 25;  // 25 pixels above hotbar

    // Text offset
    private static final int TEXT_OFFSET_X = 20;  // 20 pixels to the right of icon

    private static int animationFrame = 0;
    private static int animationTick = 0;
    private static final int ANIMATION_SPEED = 8;  // ticks per frame

    /**
     * Renders the cursed energy HUD - 16x16 animated icon + CE text
     */
    public static void render(GuiGraphics context, CursedEnergyData ceData, int screenWidth, int screenHeight) {
        if (ceData == null) return;

        // Update animation
        updateAnimation();

        // Position: right of hotbar, above it
        // Hotbar is centered at bottom, each slot is 18 pixels
        // Hotbar is 180 pixels wide, so center is at screenWidth/2
        // We position to the right of the rightmost hotbar slot
        int hotbarCenterX = screenWidth / 2;
        int hotbarRightEdge = hotbarCenterX + 90;  // Half of 180
        int x = hotbarRightEdge + OFFSET_FROM_HOTBAR_RIGHT;
        int y = screenHeight - OFFSET_FROM_BOTTOM;

        // Draw the animated icon
        renderCursedEnergyIcon(context, x, y, ceData);

        // Draw CE text to the right of icon
        renderCursedEnergyText(context, x + TEXT_OFFSET_X, y, ceData);
    }

    /**
     * Renders the 16x16 animated cursed energy icon
     * Texture should be 16x64 (4 frames of 16x16 stacked)
     */
    private static void renderCursedEnergyIcon(GuiGraphics context, int x, int y, CursedEnergyData ceData) {
        // Calculate texture V offset based on animation frame
        // Each frame is 16 pixels tall
        int textureVOffset = animationFrame * FRAME_HEIGHT;

        // Draw the current animation frame
        context.blit(CURSED_ENERGY_ICON,
                x, y,                      // Screen position
                0, textureVOffset,          // Texture U, V start (current frame)
                ICON_WIDTH, ICON_HEIGHT,   // Width, height on screen (16x16)
                ICON_WIDTH, 128);            // Texture dimensions (16x64 total)
    }

    /**
     * Renders the cursed energy text display next to icon
     */
    private static void renderCursedEnergyText(GuiGraphics context, int x, int y, CursedEnergyData ceData) {
        String ceText = formatCE(ceData.getCurrentCE());
        String maxCeText = formatCE(ceData.getMaxCE());
        String display = ceText + " / " + maxCeText;

        // Draw CE value
        context.drawString(Minecraft.getInstance().font, display, x, y, 0xB46CFF);

        // Draw percentage below
        String percentage = String.format("%.1f%%", ceData.getPercentage() * 100);
        context.drawString(Minecraft.getInstance().font, percentage, x, y + 10, 0x668BCE);
    }

    /**
     * Updates animation frame
     */
    private static void updateAnimation() {
        animationTick++;
        if (animationTick >= ANIMATION_SPEED) {
            animationTick = 0;
            animationFrame = (animationFrame + 1) % 4;  // 4 animation frames
        }
    }

    /**
     * Formats cursed energy value with thousands separator
     */
    private static String formatCE(int ce) {
        return String.format("%,d", ce);
    }

    /**
     * Resets animation state
     */
    public static void reset() {
        animationFrame = 0;
        animationTick = 0;
    }
}