# MMOContent

[![Gradle Publish](https://github.com/LCLPYT/MMOContent/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/LCLPYT/MMOContent/actions/workflows/gradle-publish.yml)

MMOContent is a Minecraft modification programming library.

## Download
Stable builds of MMOContent are available at the [GitHub Releases tab](https://github.com/LCLPYT/MMOContent/releases).

However, you can find all versions of the mod in the [LCLPNetwork Artifactory](https://repo.lclpnet.work/#artifact~internal/work.lclpnet.mods/mmocontent).

## Implementation
### Gradle Dependency
In your `build.gradle` build script, add this repository and dependency:
```groovy
repositories {
    maven {
        url "https://repo.lclpnet.work/repository/internal"
    }
    // other repositories here
}

dependencies {
    modImplementation "work.lclpnet.mods:mmocontent:${project.mmocontent_version}" // replace with version
}
```

### `fabric.mod.json` Dependency
In your `fabric.mod.json`, add MMOContent as dependency:
```jsonc
  // ...
  "depends": {
    "mmocontent": ">=1.0.0" // replace with desired version (semver enabled)
  },
  // ...
```


### Networking / Packets
Create a handler class called something like `MCNetworking` (customized for your mod).
Define the following field / methods:
```java
public class MCNetworking {
    private static MMOPacketRegistrar registrar = null;
    
    public static void registerPackets() {
        if (registrar != null) return; // already registered

        registrar = new MMOPacketRegistrar(LogManager.getLogger());
        // register your packets here
        // registrar.register(MMOEntitySpawnS2CPacket.ID, new MMOEntitySpawnS2CPacket.Decoder());
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientPacketHandlers() {
        registrar.registerClientPacketHandlers();
        registrar = null; // should be called last on client
    }

    public static void registerServerPacketHandlers() {
        registrar.registerServerPacketHandlers();
        if (!Env.isClient()) registrar = null; // not needed any further on a dedicated server
    }
} 
```

In your common entry, call:
```java
    @Override
    public void onInitialize() {
        MCNetworking.registerPackets();
        MCNetworking.registerServerPacketHandlers();
        // ...
    }
```

In your client entry, call:
```java
    @Override
    public void onInitializeClient() {
        MCNetworking.registerPackets();
        MCNetworking.registerClientPacketHandlers();
        // ...
    }
```

### Sounds
For sounds, you can create a class called something like `MMOSounds` (customized for your mod).
Put this content inside:
```java
    private static List<SoundEvent> sounds = new ArrayList<>();

    // register sounds here
    // public static final SoundEvent ENTITY_TEST_DIE = register("entity.test.die");

    public static SoundEvent register(String name) {
        Identifier loc = new Identifier(YourMod.MOD_ID, name);
        SoundEvent event = new SoundEvent(loc);
        sounds.add(event);
        return event;
    }

    public static void init() {
        if (sounds == null) throw new IllegalStateException("Sounds are already initialized");
        sounds.forEach(sound -> Registry.register(Registry.SOUND_EVENT, ((SoundEventAccessor) sound).getId(), sound));
        sounds = null;
    }
```
Remember to call the `init()` method inside your common entry:
```java
    @Override
    public void onInitialize() {
        // ...
        MMOSounds.init();
    }
```

### Blocks
MMOContent provides a comfort `MMOBlockRegistrar` class for registering blocks.

To create a new stone-like block with slab and stairs variants, use the following code in your mod's common initializer:
```java
MMOBlock block = new MMOBlock(AbstractBlock.Settings.of(Material.STONE).sounds(BlockSoundGroup.STONE));
new MMOBlockRegistrar(block)
        .withSlab().withStairs()
        .register(new Identifier("modid", "custom_stone"));
```
As you can see, `MMOBlockRegistrar` uses a builder-like pattern to collect all variants to register.
There are a lot of variants you can add; view them all in your IDE.

In this example, `MMOBlock` is used as block instance, but you are free to use any descendant of `net.minecraft.block.Block`.
There is even a constructor that allows you to pass an instance of `net.minecraft.block.AbstractBlock.Settings` directly.
For more information on `Settings`, consult the [Fabric wiki](https://fabricmc.net/wiki/tutorial:blocks#registering_your_block).

#### Vertical Slabs
If you want to add vertical slabs to your blocks, you can use `MMOBlockRegistrar::withVerticalSlab` to add one from the builder.
In case you want to register a vertical slab block without a builder, you can instantiate `MMOVerticalSlabBlock` directly, passing an instance of `net.minecraft.block.SlabBlock` from with the vertical slab will be derived.

After registering the block, you must create a blockstate and two model files:
```jsonc
// blockstates/custom_stone_vertical_slab.json
{
  "variants": {
    "type=north": { "model": "modid:block/custom_stone_vertical_slab", "y": 0, "uvlock": true },
    "type=south": { "model": "modid:block/custom_stone_vertical_slab", "y": 180, "uvlock": true },
    "type=east": { "model": "modid:block/custom_stone_vertical_slab", "y": 90, "uvlock": true },
    "type=west": { "model": "modid:block/custom_stone_vertical_slab", "y": 270, "uvlock": true },
    "type=double": { "model": "modid:block/custom_stone" }
  }
}

// model/block/custom_stone_vertical_slab.json
{
  "parent": "modid:block/vertical_slab",
  "textures": {
    "bottom": "modid:block/custom_stone",
    "top": "modid:block/custom_stone",
    "side": "modid:block/custom_stone"
  }
}

// model/item/custom_stone_vertical_slab.json
{
    "parent": "modid:block/custom_stone_vertical_slab"
}
```
Where `modid` is the id of your mod, and `custom_stone` is your block name.
The parent model `modid:block/vertical_slab` has to be created too, but you can copy it [directly from MMOContent](https://github.com/LCLPYT/MMOContent/blob/main/src/main/resources/assets/mmocontent/model/block/vertical_slab.json) (put it in your `model/block` directory).<br>
Referencing it directly (`"parent": "mmocontent:block/vertical_slab"`) is a bad idea, since it is not guaranteed that MMOContent is loaded before your mod, which can lead to errors.

### Commands
To register commands, you can extend `AbstractCommand`:
```java
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class EchoCommand extends AbstractCommand {

    public EchoCommand() {
        super("echo");
        // optionally add command aliases here
        // addAlias("loopback", "another_alias", ...);
    }

    @Override
    protected LiteralArgumentBuilder<ServerCommandSource> build(LiteralArgumentBuilder<ServerCommandSource> builder) {
        return builder
                .then(CommandManager.argument("text", StringArgumentType.greedyString())
                        .executes(EchoCommand::echo));
    }

    private static int echo(CommandContext<ServerCommandSource> ctx) {
        final String text = StringArgumentType.getString(ctx, "text");
        ctx.getSource().sendFeedback(new LiteralText(text), false);
        return 0;
    }
}
```
Pass the command name to the super constructor.
The `build()` method transforms a given root brigadier `LiteralArgumentBuilder` and populates the command tree.
From there on, you apply default Mojang brigadier library logic.

To register your commands, you can adapt to this pattern:
```java
import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

public class MyCommands {

    private static volatile boolean listenerRegistered = false;

    public static void register() {
        if (listenerRegistered) return;
        listenerRegistered = true;

        CommandRegistrationCallback.EVENT.register(MyCommands::register);
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher, boolean dedicated) {
        // register new commands here
        new EchoCommand().register(dispatcher);
    }
}
```
Remember to call `MyCommands.register()` in your `ModInitializer#onInitialize`.

#### Custom Argument Types
To register custom argument types, you may use MMOArgumentTypes.register():
```java
MMOArgumentTypes.register(new Identifier("modid", "my_argument_type"), MyArgumentType.class, serializer);
```
Where `serializer` is an ArgumentSerializer that serializes your argument type. For simple argument types, you can use
`ConstantArgumentSerializer`. For more information, consult the [Fabric wiki](https://fabricmc.net/wiki/tutorial:commands#custom_argument_types).

### Config
MMOContent provides `ConfigHelper` to aid you creating configurations.
The `ConfigHelper` mainly provides two methods: `load()` and `save()` which are able to JSON deserialize / serialize 
POJOs using the [GSON](https://github.com/google/gson) library.

You can adapt such a singleton-like pattern:
```java
public class MyConfig {
    
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("modid").resolve("config.json");
    
    private static MyConfig config = null;

    /* Prevent instantiation with new MyConfig() */
    protected MyConfig() {}

    /* actual configuration properties; all types supported by GSON are allowed */
    public boolean someBoolean = false;
    public int someInt = 0;
    public String someString = "";
    public UIConfig ui = new UIConfig();
    // ...

    /* static getters and setters */
    public static boolean shouldRenderUi() {
        return config.ui.enableRender;
    }

    public static void shouldRenderUi(boolean enable) {
        config.ui.enableRender = enable;

        onChange();  // notify config change
        save();      // save config asynchronously
    }

    /* optional update callback method (useful for reacting to general config changes) */
    public static void onChange() {
        // implement behaviour here
    }

    /* IO logic boilerplate */
    public static CompletableFuture<Void> load() {
        return ConfigHelper.load(CONFIG_PATH, MyConfig.class, MyConfig::new).thenAccept(conf -> {
            config = conf;
            onChange();
        });
    }

    public static CompletableFuture<Void> save() {
        return ConfigHelper.save(CONFIG_PATH, config);
    }

    /* optional way to define sub-configurations (deeper JSON paths; e.g. {"ui":{"enableRender":true}}) */
    public static class UIConfig {
        public boolean enableRender = true;
    }
}
```