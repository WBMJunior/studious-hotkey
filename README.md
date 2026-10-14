# Studious Hotkey

A tiny NeoForge 1.21.1 client mod that adds **one keybind (default `H`, in-game only)** which opens
[Studious Keybinds](https://www.curseforge.com/minecraft/mc-mods/studious-keybinds)' visual keyboard screen while you play.

Studious Keybinds draws a full keyboard with every action on every key, colour-coded, searchable and editable, but you can
only reach it through *Options > Controls > Keyboard*. In a large modpack that keyboard is the fastest answer to
"what does this key do?", so it deserves a hotkey. Press `H`, hover a key to see its actions, click to rebind, `Esc` to go back.

- Client-only. Requires Studious Keybinds (`studiouskeybinds`) 1.0.0 or newer; refuses to load without it.
- No mixins. Registers a `KeyMapping` (`Studious Hotkey` category) and opens the screen by reflection, so this jar carries no
  code from Studious Keybinds and needs none of it at build time.
- Rebind it like any other key. Modpacks can ship a default through Default Options / Configured Defaults.

## Build

```bash
gradle build
```

NeoForge ModDevGradle 2.0; Java 21. The jar lands in `build/libs/`. A copy of the Studious Keybinds jar in `libs/`
(git-ignored) is optional and only gives compile-time checks.

## Why not upstream?

It should be. See [UPSTREAM.md](UPSTREAM.md) for the suggestion sent to the Studious Keybinds author: a built-in
"open keyboard" keybind makes this mod unnecessary. Until then this fills the gap for the
[Vanilla But Then More](https://github.com/WBMJunior) modpack.

## License

MIT.
