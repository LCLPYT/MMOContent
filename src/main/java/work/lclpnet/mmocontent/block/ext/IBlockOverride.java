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
        return new MMOInheritedPaneBlock(baseBlock);
    }

    default FenceBlock provideFence(Block baseBlock) {
        return new MMOFenceBlock(BlockStatesUtil.copyState(baseBlock)
                .strength(2.0F, 3.0F));
    }

    default FenceGateBlock provideFenceGate(Block baseBlock) {
        return new MMOFenceGateBlock(BlockStatesUtil.copyState(baseBlock)
                .strength(2.0F, 3.0F));
    }

    default DoorBlock provideDoor(Block baseBlock) {
        return new MMODoorBlock(BlockStatesUtil.copyState(baseBlock)
                .nonOpaque()
                .strength(3.0F));
    }

    default TrapdoorBlock provideTrapdoor(Block baseBlock) {
        return new MMOTrapdoorBlock(BlockStatesUtil.copyState(baseBlock)
                .nonOpaque()
                .strength(3.0F));
    }

    default PressurePlateBlock providePressurePlate(PressurePlateBlock.ActivationRule type, Block baseBlock) {
        return new MMOPressurePlateBlock(type, BlockStatesUtil.copyState(baseBlock)
                .noCollision()
                .strength(0.5F));
    }

    default AbstractButtonBlock provideButton(boolean wooden, Block baseBlock) {
        return new MMOButtonBlock.Impl(wooden, BlockStatesUtil.copyState(baseBlock)
                .noCollision()
                .strength(0.5F));
    }
}
