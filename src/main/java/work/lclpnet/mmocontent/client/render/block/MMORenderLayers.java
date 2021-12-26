package work.lclpnet.mmocontent.client.render.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

/**
 * Utility class to register block render types.
 */
@Environment(EnvType.CLIENT)
public class MMORenderLayers {

    public static void setBlockRenderType(Block b, RenderLayer renderType) {
        BlockRenderLayerMap.INSTANCE.putBlock(b, renderType);
    }
}
