# InfiniteFluids

## Basics

### What does this mod do?
In vanilla Minecraft, when a water block is next to at least two source blocks,
it becomes a source block itself, which allows collection of infinite water.
This mod offers control over which fluids have this behavior. Currently, it
makes lava act the same way in the Nether (as well as in mod-added dimensions
where doesWaterVaporize is true, if any). In future versions, this will have a
configuration and an API for controlling which fluids are infinite.

### How do I use this mod?
You need Minecraft Forge installed first. Once that's done, just drop
infinitefluids-*version*.jar in your Minecraft instance's mods/ directory.

## Development

### How do I compile this mod from source?
You need a JDK installed first. Start a command prompt or terminal in the
directory you downloaded the source to. If you're on Windows, type
`gradlew.bat build`. Otherwise, type `./gradlew build`. Once it's done, the mod
will be saved to build/libs/infinitefluids-*version*.jar.

### How do I develop this mod in Eclipse?
Start a command prompt or terminal in the directory you downloaded the source
to. If you're on Windows, type `gradlew.bat setupDecompWorkspace eclipse`.
Otherwise, type `./gradlew setupDecompWorkspace eclipse`. Once it's done, start
Eclipse and set the workspace to the "eclipse" subdirectory. Copy the dummy.jar
file to the run/mods/ directory.

### How can I contribute to this mod's development?
Send pull requests. Note that by doing so, you agree to release your
contributions under this mod's license.

## Licensing/Permissions

### What license is this released under?
It's released under the GPL v2 or later.

### Can I use this in my modpack?
Yes, even if you monetize it with adf.ly or something, and you don't need to
ask me for my permission first.
