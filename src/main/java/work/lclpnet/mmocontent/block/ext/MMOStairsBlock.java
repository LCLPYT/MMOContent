package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Block;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.block.BlockStatesUtil;

public class MMOStairsBlock extends StairsBlock implements IMMOBlock {

    public MMOStairsBlock(Block parent) {
        super(parent.getDefaultState(), BlockStatesUtil.copyState(parent));
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
