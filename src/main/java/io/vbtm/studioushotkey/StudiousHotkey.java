package io.vbtm.studioushotkey;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;

/**
 * One keybind that opens Studious Keybinds' {@code KeyboardScreen} from in-game.
 * <p>
 * Studious Keybinds only reaches its keyboard through Options > Controls > Keyboard. In a large modpack the
 * keyboard is the fastest way for a player to learn "what does this key do", so it deserves a hotkey.
 * The screen class is looked up by reflection so this jar has no compile-time dependency on Studious Keybinds
 * (whose license is unspecified); the mods.toml dependency guarantees it is present at runtime.
 */
@Mod(value = StudiousHotkey.MOD_ID, dist = Dist.CLIENT)
public final class StudiousHotkey {
    public static final String MOD_ID = "studioushotkey";
    private static final Logger LOGGER = LoggerFactory.getLogger(StudiousHotkey.class);
    private static final String KEYBOARD_SCREEN = "com.studiousgoose.keybinds.client.KeyboardScreen";

    /** Default H: unused by vanilla, memorable ("help"/"hotkeys"), and in-game only so it never eats a typed H. */
    public static final KeyMapping OPEN_KEYBOARD = new KeyMapping(
            "key.studioushotkey.open",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_H,
            "key.categories.studioushotkey");

    public StudiousHotkey(IEventBus modBus) {
        modBus.addListener(StudiousHotkey::onRegisterKeyMappings);
        NeoForge.EVENT_BUS.addListener(StudiousHotkey::onClientTick);
    }

    private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_KEYBOARD);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        boolean pressed = false;
        while (OPEN_KEYBOARD.consumeClick()) {
            pressed = true;
        }
        if (pressed && mc.screen == null && mc.player != null) {
            openKeyboard(mc);
        }
    }

    private static void openKeyboard(Minecraft mc) {
        try {
            Class<?> screenClass = Class.forName(KEYBOARD_SCREEN, true, StudiousHotkey.class.getClassLoader());
            Constructor<?> ctor = screenClass.getConstructor(Screen.class);
            // A null parent makes the screen's own Done/Esc handling return straight to the game.
            mc.setScreen((Screen) ctor.newInstance((Screen) null));
        } catch (ReflectiveOperationException | ClassCastException e) {
            LOGGER.error("Could not open Studious Keybinds' keyboard screen", e);
            if (mc.player != null) {
                mc.player.displayClientMessage(Component.translatable("studioushotkey.error.missing"), false);
            }
        }
    }
}
