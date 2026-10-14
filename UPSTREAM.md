# Suggestion for Studious Keybinds: an in-game "open keyboard" keybind

Posted on the Studious Keybinds CurseForge page / issue tracker.

**What:** register one `KeyMapping` (suggested default `K`, `KeyConflictContext.IN_GAME`, its own category) that opens
`KeyboardScreen` with a `null` parent, so `Esc`/Done returns straight to the game.

**Why:** in modpacks with hundreds of actions the keyboard view is the best "what does this key do?" reference, but it is
three menus deep (Esc > Options > Controls > Keyboard). Pack authors want to tell players "press K to see your keys".

**Reference implementation:** https://github.com/WBMJunior/studious-hotkey - about 40 lines:

```java
public static final KeyMapping OPEN_KEYBOARD = new KeyMapping("key.studiouskeybinds.open_keyboard",
        KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, "key.categories.studiouskeybinds");

// RegisterKeyMappingsEvent (mod bus)
event.register(OPEN_KEYBOARD);

// ClientTickEvent.Post (game bus)
while (OPEN_KEYBOARD.consumeClick()) {
    if (mc.screen == null && mc.player != null) mc.setScreen(new KeyboardScreen(null));
}
```

Because `KeyboardScreen.onClose()` already does `minecraft.setScreen(parent)`, a `null` parent needs no other change.
Optional polish: show the hotkey in the keyboard screen's legend line, and let the key also *close* the screen
(toggle) when it is already open.
