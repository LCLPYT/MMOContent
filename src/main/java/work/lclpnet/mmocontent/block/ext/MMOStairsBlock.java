package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.block.BlockStatesUtil;

public class MMOStairsBlock extends StairsBlock implements IMMOBlock {

    public MMOStairsBlock(Block parent) {
        this(parent.getDefaultState(), BlockStatesUtil.copyState(parent));
    }

    public MMOStairsBlock(BlockState baseBlockState, Settings settings) {
        super(baseBlockState, settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
