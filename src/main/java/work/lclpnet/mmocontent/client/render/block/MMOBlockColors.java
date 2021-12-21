package work.lclpnet.mmocontent.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import work.lclpnet.mmocontent.block.ext.IBlockColorProvider;
import work.lclpnet.mmocontent.block.ext.IItemColorProvider;
import work.lclpnet.mmocontent.event.BlockColorCallback;
import work.lclpnet.mmocontent.event.ItemColorCallback;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class MMOBlockColors {

    public static final List<IBlockColorProvider> blockColorProviders = new ArrayList<>();
    public static final List<IItemColorProvider> itemColorProviders = new ArrayList<>();

    public static void registerBlockColorProvider(IBlockColorProvider provider) {
        blockColorProviders.add(provider);
    }

    public static void registerBlockColors(BlockColors colors) {
        blockColorProviders.forEach(provider -> provider.registerBlockColor(colors));
        BlockColorCallback.EVENT.invoker().register(colors);
    }

    public static void registerItemColorProvider(IItemColorProvider provider) {
        itemColorProviders.add(provider);
    }

    public static void registerItemColors(ItemColors colors) {
        itemColorProviders.forEach(provider -> provider.registerItemColor(colors));
        ItemColorCallback.EVENT.invoker().register(colors);
    }
}
