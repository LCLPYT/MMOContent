package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.PaneBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class MMOPaneBlock extends PaneBlock implements IMMOBlock {

    protected MMOPaneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
