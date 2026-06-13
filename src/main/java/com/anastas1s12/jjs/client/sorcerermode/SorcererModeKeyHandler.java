package com.anastas1s12.jjs.client.sorcerermode;

import com.anastas1s12.jjs.JujutsuSorcery;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

/**
 * Handles R key input for toggling sorcerer mode
 */
public class SorcererModeKeyHandler {
    private static KeyMapping toggleSorcererModeKey;
    private static boolean lastKeyPressed = false;
    
    public static void initialize() {
        // Register the key binding for toggling sorcerer mode
        toggleSorcererModeKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.jjs.toggle_sorcerer_mode",
                GLFW.GLFW_KEY_R,
                "category.jjs.sorcerer_mode"
        ));
        
        // Register tick event to check for key presses
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            handleKeybinds();
        });
    }
    
    private static void handleKeybinds() {
        Minecraft client = Minecraft.getInstance();
        
        // Only process key when in game and not in a screen
        if (client.level != null && client.screen == null) {
            boolean keyPressed = toggleSorcererModeKey.isDown();
            
            // Detect key press (transition from not pressed to pressed)
            if (keyPressed && !lastKeyPressed) {
                toggleSorcererMode();
            }
            
            lastKeyPressed = keyPressed;
        } else {
            lastKeyPressed = false;
        }
    }
    
    private static void toggleSorcererMode() {
        Minecraft client = Minecraft.getInstance();
        if (client.player != null) {
            SorcererModeClientEvents.toggleSorcererMode(client.player);
        }
    }
}

