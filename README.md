# MMOContent

[![Gradle Publish](https://github.com/LCLPYT/MMOContent/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/LCLPYT/MMOContent/actions/workflows/gradle-publish.yml)

MMOContent is a Minecraft modification programming library.

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
