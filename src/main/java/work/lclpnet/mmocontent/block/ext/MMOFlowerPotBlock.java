package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Block;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class MMOFlowerPotBlock extends FlowerPotBlock implements IMMOBlock {

    public MMOFlowerPotBlock(Block content, Settings settings) {
        super(content, settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return null;
    }
}
