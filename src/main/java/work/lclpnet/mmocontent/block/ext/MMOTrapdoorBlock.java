package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.TrapdoorBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class MMOTrapdoorBlock extends TrapdoorBlock implements IMMOBlock {

    public MMOTrapdoorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
