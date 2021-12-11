package work.lclpnet.mmocontent.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.Material;
import work.lclpnet.mmocontent.block.ext.MMOFlowerPotBlock;
import work.lclpnet.mmocontent.util.IdentifierProvider;

import java.util.function.Function;

public class MMOPottedPlantUtil {

    public static FlowerPotBlock addPottedPlant(Block block, String name, IdentifierProvider identifierProvider) {
        return addPottedPlant(block, name, Function.identity(), identifierProvider);
    }

    public static FlowerPotBlock addPottedPlant(Block block, String name, Function<AbstractBlock.Settings, AbstractBlock.Settings> transformer, IdentifierProvider identifierProvider) {
        AbstractBlock.Settings settings = transformer.apply(AbstractBlock.Settings.of(Material.SUPPORTED)
                .breakInstantly()
                .nonOpaque());

        MMOFlowerPotBlock potted = new MMOFlowerPotBlock(block, settings);

        new MMOBlockRegistrar(potted)
                .register(identifierProvider.identifier(String.format("potted_%s", name)));

        return potted;
    }
}
