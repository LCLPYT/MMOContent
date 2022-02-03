package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Block;
import net.minecraft.block.WallBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.block.BlockStatesUtil;

public class MMOWallBlock extends WallBlock implements IMMOBlock {

    public MMOWallBlock(Block parent) {
        this(BlockStatesUtil.copyState(parent));
    }

    public MMOWallBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
