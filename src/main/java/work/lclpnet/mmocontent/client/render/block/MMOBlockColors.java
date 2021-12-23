package work.lclpnet.mmocontent.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.item.ItemConvertible;
import work.lclpnet.mmocontent.event.BlockColorCallback;
import work.lclpnet.mmocontent.event.ItemColorCallback;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class MMOBlockColors {

    public static final Map<BlockColorProvider, Block[]> blockColors = new HashMap<>();
    public static final Map<ItemColorProvider, ItemConvertible[]> itemColors = new HashMap<>();

    public static void registerBlockColor(BlockColorProvider provider, Block... blocks) {
        blockColors.put(provider, blocks);
    }

    public static void registerItemColor(ItemColorProvider provider, ItemConvertible... items) {
        itemColors.put(provider, items);
    }

    public static void registerBlockColors(BlockColors colors) {
        blockColors.forEach(colors::registerColorProvider);
        BlockColorCallback.EVENT.invoker().register(colors);
    }

    public static void registerItemColors(ItemColors colors) {
        itemColors.forEach(colors::register);
        ItemColorCallback.EVENT.invoker().register(colors);
    }
}
