package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.block.BlockStatesUtil;

public class MMOSlabBlock extends SlabBlock implements IMMOBlock {

    public MMOSlabBlock(Block parent) {
        super(BlockStatesUtil.copyState(parent));
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
