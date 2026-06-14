package com.anastas1s12.jjs.client.hud;

import com.anastas1s12.jjs.abilities.AbilityRegistry;
import com.anastas1s12.jjs.abilities.Ability;
import com.anastas1s12.jjs.abilities.AbilityExecutor;
import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.client.sorcerermode.ClientSorcererModeManager;
import com.anastas1s12.jjs.sorcerermode.SorcererModeManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

/**
 * Renders the sorcerer hotbar on top of the game screen (not a screen, just HUD elements)
 *
 * Features:
 * - 7 ability slots
 * - Selection indicator (moves with mouse wheel)
 * - CE progress bar under each slot (14x11 texture)
 * - Cooldown overlay on abilities
 * - Tooltip on hover
 */
public class SorcererHotbarRenderer {
    private static final int SLOT_COUNT = 7;
    private static final int SLOT_SIZE = 16; // Slot is 22x22
    private static final int CE_BAR_WIDTH = 14;
    private static final int CE_BAR_HEIGHT = 11;

    // Textures
    private static final ResourceLocation HOTBAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/sorcerer_hotbar/sorcerer_hotbar.png");
    private static final ResourceLocation SELECTION_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/sorcerer_hotbar/selection.png"); // 22x22
    private static final ResourceLocation CE_BAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/sorcerer_hotbar/hotbar_ce.png"); // 14x11 each

    /**
     * PLACEHOLDER COORDINATES (USER WILL ADJUST THESE)
     * Format: [slotIndex][0] = x, [slotIndex][1] = y (top-left corner of slot)
     */
    private static final int[][] HOTBAR_SLOT_COORDS = {
            {19, 21},     // Slot 0
            {46, 21},     // Slot 1
            {75, 21},     // Slot 2
            {103, 21},    // Slot 3
            {130, 21},    // Slot 4
            {159, 21},    // Slot 5
            {187, 21}     // Slot 6
    };

    /**
     * CE bar coordinates (bottom-left corner of each bar)
     * PLACEHOLDER - USER WILL ADJUST
     */
    private static final int[][] CE_BAR_COORDS = {
            {1, 11},     // Bar 0
            {16, 11},     // Bar 1
            {32, 11},     // Bar 2
            {48, 11},    // Bar 3
            {64, 11},    // Bar 4
            {80, 11},    // Bar 5
            {96, 11}     // Bar 6
    };

    /**
     * Render the sorcerer hotbar HUD
     * Call from HudRenderEvent
     */
    public static void render(GuiGraphics guiGraphics, Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        // Only render if in sorcerer mode
        if (!ClientSorcererModeManager.isInSorcererMode(player)) {
            return;
        }

        // Draw each hotbar slot
        for (int i = 0; i < SLOT_COUNT; i++) {
            drawHotbarSlot(guiGraphics, minecraft, player, i);
        }
    }

    private static void drawHotbarSlot(GuiGraphics guiGraphics, Minecraft minecraft, LocalPlayer player, int slotIndex) {
        int[] slotCoords = HOTBAR_SLOT_COORDS[slotIndex];
        int[] ceBarCoords = CE_BAR_COORDS[slotIndex];
        int slotX = slotCoords[0];
        int slotY = slotCoords[1];

        // Draw slot background
        guiGraphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0xFF6600FF); // Purple background

        // Draw slot border
        guiGraphics.fill(slotX, slotY, slotX + SLOT_SIZE, slotY + 1, 0xFF00FF00); // Top border (green)
        guiGraphics.fill(slotX, slotY + SLOT_SIZE - 1, slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0xFF00FF00); // Bottom
        guiGraphics.fill(slotX, slotY, slotX + 1, slotY + SLOT_SIZE, 0xFF00FF00); // Left
        guiGraphics.fill(slotX + SLOT_SIZE - 1, slotY, slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0xFF00FF00); // Right

        // TODO: Draw ability icon from AbilityRegistry if assigned
        // ResourceLocation abilityId = SorcererModeManager.getHotbarAbility(player, slotIndex);
        // if (abilityId != null) {
        //     Ability ability = AbilityRegistry.get(abilityId);
        //     guiGraphics.blit(ability.getIcon(), slotX + 2, slotY + 2, 0, 0, 18, 18, 18, 18);
        // }

        drawAbilityCostBadge(guiGraphics, minecraft, player, slotIndex, slotX, slotY);

        drawCEProgressBar(guiGraphics, minecraft, player, slotIndex, ceBarCoords[0], ceBarCoords[1]);

        drawCooldownOverlay(guiGraphics, minecraft, player, slotIndex, slotX, slotY);

        // Draw selection indicator
        // TODO: Get selected slot from SorcererModeManager
        // if (SorcererModeManager.getSelectedHotbarSlot(player) == slotIndex) {
        //     drawSelectionIndicator(guiGraphics, slotX, slotY);
        // }
    }

    private static void drawAbilityCostBadge(GuiGraphics guiGraphics, Minecraft minecraft, LocalPlayer player,
                                             int slotIndex, int slotX, int slotY) {
        // TODO: Get ability from hotbar
        // ResourceLocation abilityId = SorcererModeManager.getHotbarAbility(player, slotIndex);
        // if (abilityId == null) return;
        //
        // Ability ability = AbilityRegistry.get(abilityId);
        // if (ability == null) return;
        //
        // // Draw CE cost in top-right corner of slot
        // int cost = ability.getCursedEnergyCost();
        // String costStr = String.valueOf(cost);
        // guiGraphics.drawString(minecraft.font, costStr, slotX + SLOT_SIZE - 10, slotY + 2, 0xFF00FFFF);
    }

    private static void drawCEProgressBar(GuiGraphics guiGraphics, Minecraft minecraft, LocalPlayer player,
                                          int slotIndex, int barX, int barY) {
        // Get player's current CE (from client-side cache)
        // TODO: Get CE data from client cache
        // int currentCE = ClientCursedEnergyManager.getCurrentCE();
        // int maxCE = ClientCursedEnergyManager.getMaxCE();
        // float percentage = (float) currentCE / maxCE;

        // Draw bar background
        guiGraphics.fill(barX, barY, barX + CE_BAR_WIDTH, barY + CE_BAR_HEIGHT, 0xFF330066); // Dark purple background

        // Draw bar fill (purple gradient) - for now, just solid color
        // float fillWidth = CE_BAR_WIDTH * percentage;
        // guiGraphics.fill(barX, barY, barX + (int)fillWidth, barY + CE_BAR_HEIGHT, 0xFF6600FF);

        // TODO: Blit CE bar texture for better look
        // guiGraphics.blit(CE_BAR_TEXTURE, barX, barY, 0, 0, (int)fillWidth, CE_BAR_HEIGHT, CE_BAR_WIDTH, CE_BAR_HEIGHT);
    }

    private static void drawCooldownOverlay(GuiGraphics guiGraphics, Minecraft minecraft, LocalPlayer player,
                                            int slotIndex, int slotX, int slotY) {
        // TODO: Check if ability is on cooldown
        // ResourceLocation abilityId = SorcererModeManager.getHotbarAbility(player, slotIndex);
        // if (abilityId == null) return;
        //
        // Ability ability = AbilityRegistry.get(abilityId);
        // float cooldownProgress = AbilityExecutor.getCooldownProgress(player, abilityId, ability);
        //
        // if (cooldownProgress < 1.0f) {
        //     // Draw semi-transparent dark overlay
        //     float overlayHeight = SLOT_SIZE * (1.0f - cooldownProgress);
        //     guiGraphics.fill(slotX, slotY + SLOT_SIZE - (int)overlayHeight,
        //                     slotX + SLOT_SIZE, slotY + SLOT_SIZE, 0xAA000000);
        //
        //     // Draw cooldown text
        //     int remainingTicks = AbilityExecutor.getRemainingCooldown(player, abilityId, ability);
        //     float remainingSeconds = remainingTicks / 20.0f;
        //     String cooldownStr = String.format("%.1f", remainingSeconds);
        //     guiGraphics.drawString(minecraft.font, cooldownStr, slotX + 6, slotY + 8, 0xFFFFFFFF);
        // }
    }

    private static void drawSelectionIndicator(GuiGraphics guiGraphics, int slotX, int slotY) {
        // Draw thick yellow border around selected slot
        guiGraphics.fill(slotX - 2, slotY - 2, slotX + SLOT_SIZE + 2, slotY - 1, 0xFFFFFF00); // Top
        guiGraphics.fill(slotX - 2, slotY + SLOT_SIZE + 1, slotX + SLOT_SIZE + 2, slotY + SLOT_SIZE + 2, 0xFFFFFF00); // Bottom
        guiGraphics.fill(slotX - 2, slotY - 2, slotX - 1, slotY + SLOT_SIZE + 2, 0xFFFFFF00); // Left
        guiGraphics.fill(slotX + SLOT_SIZE + 1, slotY - 2, slotX + SLOT_SIZE + 2, slotY + SLOT_SIZE + 2, 0xFFFFFF00); // Right

        // TODO: Or blit selection texture here
        // guiGraphics.blit(SELECTION_TEXTURE, slotX - 2, slotY - 2, 0, 0, 26, 26, 26, 26);
    }
}
