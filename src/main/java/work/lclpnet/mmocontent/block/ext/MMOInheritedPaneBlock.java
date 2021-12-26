package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Block;
import work.lclpnet.mmocontent.block.BlockStatesUtil;

public class MMOInheritedPaneBlock extends MMOPaneBlock {

    public MMOInheritedPaneBlock(Block parent) {
        super(BlockStatesUtil.copyState(parent));
    }
}
