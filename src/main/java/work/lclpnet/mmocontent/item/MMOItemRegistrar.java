package work.lclpnet.mmocontent.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.client.render.block.MMOBlockColors;
import work.lclpnet.mmocontent.util.Env;
import work.lclpnet.mmocontent.util.IdentifierProvider;

import java.util.function.Function;

public class MMOItemRegistrar {

    protected final Function<FabricItemSettings, Item> itemFactory;

    public MMOItemRegistrar() {
        this(Item::new);
    }

    public MMOItemRegistrar(Function<FabricItemSettings, Item> itemFactory) {
        this.itemFactory = itemFactory;
    }

    public void register(Identifier itemId) {
        register(itemId, ItemGroup.BUILDING_BLOCKS);
    }

    public void register(final Identifier itemId, ItemGroup group) {
        final Item item = itemFactory.apply(new FabricItemSettings().group(group));
        Registry.register(Registry.ITEM, itemId, item);

        if (Env.isClient() && item instanceof ItemColorProvider) registerItemColor((ItemColorProvider) item, item);
    }

    @Environment(EnvType.CLIENT)
    private void registerItemColor(ItemColorProvider provider, ItemConvertible... items) {
        MMOBlockColors.registerItemColor(provider, items);
    }

    public static void registerSpawnEgg(EntityType<?> type, String entityName, int primaryColor, int secondaryColor, IdentifierProvider identifierProvider) {
        new MMOItemRegistrar(settings -> new SpawnEggItem(type, primaryColor, secondaryColor, settings))
                .register(identifierProvider.identifier(String.format("%s_spawn_egg", entityName)), ItemGroup.MISC);
    }
}
