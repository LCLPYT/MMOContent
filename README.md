# MMOContent
MMOContent is a Minecraft modification programming library.

## Implementation
### Sounds
For sounds, you can create a class called something like `MMOSounds` (customized for your case, of course).
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
Remember to call the `init()` method inside your common entry point:
```java
    @Override
    public void onInitialize() {
        // ...
        MMOSounds.init();
    }
```
