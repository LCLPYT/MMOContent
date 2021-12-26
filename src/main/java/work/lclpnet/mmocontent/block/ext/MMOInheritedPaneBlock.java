package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Block;
import work.lclpnet.mmocontent.block.BlockStatesUtil;
import work.lclpnet.mmocontent.client.render.block.MMORenderLayers;
import work.lclpnet.mmocontent.util.Env;

public class MMOInheritedPaneBlock extends MMOPaneBlock {

    public MMOInheritedPaneBlock(Block parent) {
        super(BlockStatesUtil.copyState(parent));

        if (Env.isClient()) MMORenderLayers.inheritRenderLayer(this, parent);
    }
}
