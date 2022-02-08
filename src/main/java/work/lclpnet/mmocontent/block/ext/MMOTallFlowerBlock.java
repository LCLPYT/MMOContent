package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.TallFlowerBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class MMOTallFlowerBlock extends TallFlowerBlock implements IMMOBlock {

    public MMOTallFlowerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
