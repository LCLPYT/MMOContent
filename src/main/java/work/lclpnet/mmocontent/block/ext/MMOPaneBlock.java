package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.PaneBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import work.lclpnet.mmocontent.client.render.block.MMORenderLayers;
import work.lclpnet.mmocontent.util.Env;

public class MMOPaneBlock extends PaneBlock implements IMMOBlock {

    public MMOPaneBlock(Settings settings, boolean registerRenderLayer) {
        super(settings);

        if (registerRenderLayer && Env.isClient()) registerRenderLayer();
    }

    protected void registerRenderLayer() {
        MMORenderLayers.setBlockRenderType(this, RenderLayer.getCutout());
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
