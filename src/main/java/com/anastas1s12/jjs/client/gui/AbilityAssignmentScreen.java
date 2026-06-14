package com.anastas1s12.jjs.client.gui;

import com.anastas1s12.jjs.abilities.AbilityRegistry;
import com.anastas1s12.jjs.abilities.Ability;
import com.anastas1s12.jjs.JujutsuSorcery;
import com.anastas1s12.jjs.networking.payload.c2s.AssignAbilityPayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom screen for assigning abilities to hotbar
 *
 * Layout:
 * - Left side: 18 available abilities from player's techniques
 * - Right side: 7 hotbar slots
 * - Bottom: Clear button
 * - Hover: Shows ability tooltip (name, description, CE cost, cooldown)
 *
 * User can:
 * - Click ability → click hotbar slot to assign
 * - Click ability → click Clear to remove from hotbar
 * - Scroll wheel to cycle hotbar selection
 */
public class AbilityAssignmentScreen extends Screen {
    private int leftPos;
    private int topPos;

    private static final int SCREEN_WIDTH = 256;
    private static final int SCREEN_HEIGHT = 256;

    // Texture locations (PLACEHOLDER COORDINATES - USER WILL ADJUST)
    private static final ResourceLocation SCREEN_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/ability_inventory/ability_inventory.png");
    private static final ResourceLocation HOTBAR_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(JujutsuSorcery.MOD_ID, "textures/gui/sorcerer_hotbar.png");

    // Ability inventory grid (18 slots for abilities)
    private static final int ABILITY_SLOT_COUNT = 18;
    private static final int ABILITY_SLOT_SIZE = 22; // Each slot is 22x22
    private static final int ABILITY_GRID_COLS = 9;
    private static final int ABILITY_GRID_ROWS = 2;

    // Hotbar slots (7 slots)
    private static final int HOTBAR_SLOT_COUNT = 7;
    private static final int HOTBAR_SLOT_SIZE = 22;

    // Random placeholder coordinates (USER WILL SET THESE TO ACTUAL VALUES)
    // Format: [slotIndex][0] = x, [slotIndex][1] = y
    private static final int[][] ABILITY_SLOT_COORDS = {
            // Row 1
            {16, 81}, {34, 81}, {52, 81}, {70, 81}, {88, 81}, {106, 81}, {124, 81}, {142, 81}, {160, 81},
            // Row 2
            {16, 99}, {34, 99}, {52, 99}, {70, 99}, {88, 99}, {106, 99}, {124, 99}, {142, 99}, {160, 99},
    };

    // Hotbar slot coordinates (7 slots horizontal)
    // PLACEHOLDER VALUES - USER WILL ADJUST
    private static final int[][] HOTBAR_SLOT_COORDS = {
            {21, 19}, {40, 19}, {59, 19}, {78, 19}, {97, 19}, {116, 19}, {135, 19}, {154, 19}
    };

    // Clear button coordinates (x, y) - PLACEHOLDER
    private static final int CLEAR_BUTTON_X = 148;
    private static final int CLEAR_BUTTON_Y = 9;
    private static final int CLEAR_BUTTON_WIDTH = 29;
    private static final int CLEAR_BUTTON_HEIGHT = 8;

    // Selection state
    private ResourceLocation selectedAbility = null;
    private int selectedHotbarSlot = -1;
    private int hoveredSlot = -1; // For tooltip

    // Ability data
    private List<ResourceLocation> availableAbilities = new ArrayList<>();

    // Tooltip
    private Component tooltipTitle = null;
    private List<Component> tooltipLines = new ArrayList<>();

    public AbilityAssignmentScreen() {
        super(Component.literal("Ability Assignment"));

        // Load all available abilities
        availableAbilities.addAll(AbilityRegistry.getAll().keySet());
    }

    @Override
    protected void init() {
        super.init();
        // Center screen
        this.leftPos = (this.width - SCREEN_WIDTH) / 2;
        this.topPos = (this.height - SCREEN_HEIGHT) / 2;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Draw background
        this.renderTransparentBackground(guiGraphics);

        guiGraphics.blit(SCREEN_TEXTURE, this.leftPos, this.topPos, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, 256, 256);

        drawAbilitySlots(guiGraphics, mouseX, mouseY);

        // Draw hotbar slots
        drawHotbarSlots(guiGraphics, mouseX, mouseY);

        // Draw clear button
        drawClearButton(guiGraphics, mouseX, mouseY);

        // Draw selection indicators
        if (selectedAbility != null) {
            drawSelectionHighlight(guiGraphics, selectedAbility, 0xFFFF00); // Yellow highlight
        }

        // Draw tooltip if hovering
        if (hoveredSlot >= 0) {
            drawTooltip(guiGraphics, mouseX, mouseY);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void drawAbilitySlots(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int i = 0; i < Math.min(availableAbilities.size(), ABILITY_SLOT_COUNT); i++) {
            int[] coords = ABILITY_SLOT_COORDS[i];
            int x = this.leftPos + coords[0];
            int y = this.topPos + coords[1];

            ResourceLocation abilityId = availableAbilities.get(i);
            Ability ability = AbilityRegistry.get(abilityId);

            if (ability != null) {
                // Draw slot background (purple for now)
                guiGraphics.fill(x, y, x + ABILITY_SLOT_SIZE, y + ABILITY_SLOT_SIZE, 0xFF6600FF);

                // Draw ability icon (placeholder - you'll draw actual texture)
                // guiGraphics.blit(ability.getIcon(), x + 2, y + 2, 0, 0, 18, 18, 18, 18);

                // Draw border if hovered
                if (isMouseInSlot(mouseX, mouseY, x, y, ABILITY_SLOT_SIZE)) {
                    guiGraphics.fill(x, y, x + ABILITY_SLOT_SIZE, y + 1, 0xFFFFFF00); // Top
                    guiGraphics.fill(x, y + ABILITY_SLOT_SIZE - 1, x + ABILITY_SLOT_SIZE, y + ABILITY_SLOT_SIZE, 0xFFFFFF00); // Bottom
                    guiGraphics.fill(x, y, x + 1, y + ABILITY_SLOT_SIZE, 0xFFFFFF00); // Left
                    guiGraphics.fill(x + ABILITY_SLOT_SIZE - 1, y, x + ABILITY_SLOT_SIZE, y + ABILITY_SLOT_SIZE, 0xFFFFFF00); // Right
                    hoveredSlot = i;
                }
            }
        }
    }

    private void drawHotbarSlots(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        for (int i = 0; i < HOTBAR_SLOT_COUNT; i++) {
            int[] coords = HOTBAR_SLOT_COORDS[i];
            int x = this.leftPos + coords[0];
            int y = this.topPos + coords[1];

            // Draw slot background (darker purple)
            guiGraphics.fill(x, y, x + HOTBAR_SLOT_SIZE, y + HOTBAR_SLOT_SIZE, 0xFF440088);

            // TODO: Draw assigned ability icon if not empty

            // Highlight if hovering
            if (isMouseInSlot(mouseX, mouseY, x, y, HOTBAR_SLOT_SIZE)) {
                guiGraphics.fill(x, y, x + HOTBAR_SLOT_SIZE, y + 1, 0xFFFFFF00); // Top
                guiGraphics.fill(x, y + HOTBAR_SLOT_SIZE - 1, x + HOTBAR_SLOT_SIZE, y + HOTBAR_SLOT_SIZE, 0xFFFFFF00); // Bottom
                guiGraphics.fill(x, y, x + 1, y + HOTBAR_SLOT_SIZE, 0xFFFFFF00); // Left
                guiGraphics.fill(x + HOTBAR_SLOT_SIZE - 1, y, x + HOTBAR_SLOT_SIZE, y + HOTBAR_SLOT_SIZE, 0xFFFFFF00); // Right
            }
        }
    }

    private void drawClearButton(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = this.leftPos + CLEAR_BUTTON_X;
        int y = this.topPos + CLEAR_BUTTON_Y;

        // Draw button
        guiGraphics.fill(x, y, x + CLEAR_BUTTON_WIDTH, y + CLEAR_BUTTON_HEIGHT, 0xFFCC0000);

        // Highlight if hovering
        if (isMouseInSlot(mouseX, mouseY, x, y, CLEAR_BUTTON_WIDTH, CLEAR_BUTTON_HEIGHT)) {
            guiGraphics.fill(x, y, x + CLEAR_BUTTON_WIDTH, y + CLEAR_BUTTON_HEIGHT, 0xFFFF0000);
        }

        // Draw text
        guiGraphics.drawCenteredString(this.font, "Clear", x + CLEAR_BUTTON_WIDTH / 2, y + (CLEAR_BUTTON_HEIGHT - 8) / 2, 0xFFFFFFFF);
    }

    private void drawSelectionHighlight(GuiGraphics guiGraphics, ResourceLocation abilityId, int color) {
        int index = availableAbilities.indexOf(abilityId);
        if (index >= 0 && index < ABILITY_SLOT_COORDS.length) {
            int[] coords = ABILITY_SLOT_COORDS[index];
            int x = this.leftPos + coords[0];
            int y = this.topPos + coords[1];

            // Draw selection border (thicker)
            guiGraphics.fill(x - 2, y - 2, x + ABILITY_SLOT_SIZE + 2, y - 1, color);
            guiGraphics.fill(x - 2, y + ABILITY_SLOT_SIZE + 1, x + ABILITY_SLOT_SIZE + 2, y + ABILITY_SLOT_SIZE + 2, color);
            guiGraphics.fill(x - 2, y - 2, x - 1, y + ABILITY_SLOT_SIZE + 2, color);
            guiGraphics.fill(x + ABILITY_SLOT_SIZE + 1, y - 2, x + ABILITY_SLOT_SIZE + 2, y + ABILITY_SLOT_SIZE + 2, color);
        }
    }

    private void drawTooltip(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        if (hoveredSlot < 0 || hoveredSlot >= availableAbilities.size()) return;

        ResourceLocation abilityId = availableAbilities.get(hoveredSlot);
        Ability ability = AbilityRegistry.get(abilityId);
        if (ability == null) return;

        // Create tooltip
        List<Component> lines = new ArrayList<>();
        lines.add(Component.literal("§6" + ability.getName())); // Gold color
        lines.add(Component.literal("§7" + ability.getDescription())); // Gray color
        lines.add(Component.literal("§bCE Cost: §f" + ability.getCursedEnergyCost())); // Cyan cost
        lines.add(Component.literal("§eCooldown: §f" + ability.getCooldownTicks() + " ticks")); // Yellow cooldown

        // Draw tooltip background
        int tooltipWidth = 150;
        int tooltipHeight = lines.size() * 12 + 4;
        guiGraphics.fill(mouseX, mouseY, mouseX + tooltipWidth, mouseY + tooltipHeight, 0xFF000000);

        // Draw tooltip text
        for (int i = 0; i < lines.size(); i++) {
            guiGraphics.drawString(this.font, lines.get(i), mouseX + 4, mouseY + 2 + (i * 12), 0xFFFFFFFF);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        // Scroll wheel cycles hotbar selection
        int direction = scrollY > 0 ? -1 : 1; // Invert direction
        // TODO: Send packet to server to cycle selection
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // Check ability slots
        for (int i = 0; i < Math.min(availableAbilities.size(), ABILITY_SLOT_COUNT); i++) {
            int[] coords = ABILITY_SLOT_COORDS[i];
            int x = this.leftPos + coords[0];
            int y = this.topPos + coords[1];

            if (isMouseInSlot((int) mouseX, (int) mouseY, x, y, ABILITY_SLOT_SIZE)) {
                selectedAbility = availableAbilities.get(i);
                return true;
            }
        }

        // Check hotbar slots
        if (selectedAbility != null) {
            for (int i = 0; i < HOTBAR_SLOT_COUNT; i++) {
                int[] coords = HOTBAR_SLOT_COORDS[i];
                int x = this.leftPos + coords[0];
                int y = this.topPos + coords[1];

                if (isMouseInSlot((int) mouseX, (int) mouseY, x, y, HOTBAR_SLOT_SIZE)) {
                    // Send assignment packet
                    ClientPlayNetworking.send(new AssignAbilityPayload(i, selectedAbility));
                    selectedAbility = null; // Reset selection
                    return true;
                }
            }
        }

        // Check clear button
        if (selectedAbility != null) {
            int x = this.leftPos + CLEAR_BUTTON_X;
            int y = this.topPos + CLEAR_BUTTON_Y;

            if (isMouseInSlot((int) mouseX, (int) mouseY, x, y, CLEAR_BUTTON_WIDTH, CLEAR_BUTTON_HEIGHT)) {
                selectedAbility = null; // Clear selection
                return true;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isMouseInSlot(int mouseX, int mouseY, int x, int y, int size) {
        return isMouseInSlot(mouseX, mouseY, x, y, size, size);
    }

    private boolean isMouseInSlot(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Don't pause game
    }
}
