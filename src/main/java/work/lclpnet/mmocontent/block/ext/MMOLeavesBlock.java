package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.LeavesBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class MMOLeavesBlock extends LeavesBlock implements IMMOBlock {

    public MMOLeavesBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
