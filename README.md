# InfiniteFluids

## Basics

### What does this mod do?
In vanilla Minecraft, when a water block is next to at least two source blocks,
it becomes a source block itself, which allows collection of infinite water.
This mod allows you to configure which fluids have this behavior.

### How do I use this mod?
You need Minecraft Forge installed first. Once that's done, just drop
infinitefluids-*version*.jar in your Minecraft instance's mods/ directory and
configure it to taste. (Configuration is not optional; if left unconfigured,
this mod won't change anything.)

### What settings does this mod have?
You can specify lists of fluids that will be infinite, with separate lists for
inside and outside the Nether (and any mod-added dimensions where water can't
be placed). Alternatively, you can invert the lists, so that all fluids are
infinite except for those specified.

### What name do I use for fluids in the configuration?
Use the name of the fluid's block that you'd use with /setblock. Examples:
- Vanilla water is "minecraft:water"
- Vanilla lava is "minecraft:lava"
- Tinkers' Construct liquid blue slime is "tconstruct:blueslime"

## Development

### How do I compile this mod from source?
You need a JDK installed first. Start a command prompt or terminal in the
directory you downloaded the source to. If you're on Windows, type
`gradlew.bat build`. Otherwise, type `./gradlew build`. Once it's done, the mod
will be saved to build/libs/infinitefluids-*version*.jar.

### How can I contribute to this mod's development?
Send pull requests. Note that by doing so, you agree to release your
contributions under this mod's license.

## Licensing/Permissions

### What license is this released under?
It's released under the GPL v2 or later.

### Can I use this in my modpack?
Yes, even if you monetize it with adf.ly or something, and you don't need to
ask me for my permission first.
