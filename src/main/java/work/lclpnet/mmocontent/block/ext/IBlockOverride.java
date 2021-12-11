package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.*;
import work.lclpnet.mmocontent.block.BlockStatesUtil;

public interface IBlockOverride {

    default SlabBlock provideSlab(Block baseBlock) {
        return new MMOSlabBlock(baseBlock);
    }

    default StairsBlock provideStairs(Block baseBlock) {
        return new MMOStairsBlock(baseBlock);
    }

    default MMOVerticalSlabBlock provideVerticalSlab(SlabBlock baseBlock) {
        return new MMOVerticalSlabBlock(baseBlock);
    }

    default WallBlock provideWall(Block baseBlock) {
        return new MMOWallBlock(baseBlock);
    }

    default PaneBlock providePane(Block baseBlock) {
        return new MMOPaneBlock(BlockStatesUtil.copyState(baseBlock), true);
    }
}
