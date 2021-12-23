package work.lclpnet.mmocontent.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.ext.*;

import java.util.Objects;

public class MMOBlockRegistrar {

    protected final Block block;
    protected boolean slab = false, stairs = false, wall = false, verticalSlab = false, pane = false;

    public MMOBlockRegistrar(Block block) {
        this.block = Objects.requireNonNull(block);
    }

    public MMOBlockRegistrar(AbstractBlock.Settings settings) {
        this(new MMOBlock(Objects.requireNonNull(settings)));
    }

    public MMOBlockRegistrar withSlab() {
        this.slab = true;
        return this;
    }

    public MMOBlockRegistrar withStairs() {
        this.stairs = true;
        return this;
    }

    public MMOBlockRegistrar withWall() {
        this.wall = true;
        return this;
    }

    public MMOBlockRegistrar withVerticalSlab() {
        this.verticalSlab = true;
        return this;
    }

    public MMOBlockRegistrar withPane() {
        this.pane = true;
        return this;
    }

    public void register(Identifier blockId) {
        register(blockId, ItemGroup.BUILDING_BLOCKS);
    }

    public void register(final Identifier blockId, ItemGroup group) {
        registerBlock(blockId, block);

        final String namespace = blockId.getNamespace(), name = blockId.getPath();

        final FabricItemSettings blockItemSettings = new FabricItemSettings().group(group);
        BlockItem item = block instanceof IMMOBlock ? ((IMMOBlock) block).provideBlockItem(blockItemSettings) : new BlockItem(block, blockItemSettings);
        if (item != null) registerBlockItem(blockId, item);

        if (slab || verticalSlab) {
            SlabBlock slabBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideSlab(block) : new MMOSlabBlock(block);
            if (slab) {
                final Identifier slabId = new Identifier(namespace, String.format("%s_slab", name));
                registerBlock(slabId, slabBlock);
                registerBlockItem(slabId, new BlockItem(slabBlock, new FabricItemSettings().group(group)));
            }
            if (verticalSlab) {
                final Identifier verticalSlabId = new Identifier(namespace, name + "_vertical_slab");
                MMOVerticalSlabBlock verticalSlab = block instanceof IBlockOverride ? ((IBlockOverride) block).provideVerticalSlab(slabBlock) : new MMOVerticalSlabBlock(slabBlock);
                registerBlock(verticalSlabId, verticalSlab);
                registerBlockItem(verticalSlabId, new BlockItem(verticalSlab, new FabricItemSettings().group(group)));
            }
        }
        if (stairs) {
            final Identifier stairsId = new Identifier(namespace, String.format("%s_stairs", name));
            StairsBlock stairs = block instanceof IBlockOverride ? ((IBlockOverride) block).provideStairs(block) : new MMOStairsBlock(block);
            registerBlock(stairsId, stairs);
            registerBlockItem(stairsId, new BlockItem(stairs, new FabricItemSettings().group(group)));
        }
        if (wall) {
            final Identifier wallId = new Identifier(namespace, String.format("%s_wall", name));
            WallBlock wall = block instanceof IBlockOverride ? ((IBlockOverride) block).provideWall(block) : new MMOWallBlock(block);
            registerBlock(wallId, wall);
            registerBlockItem(wallId, new BlockItem(wall, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        }
        if (pane) {
            final Identifier paneId = new Identifier(namespace, String.format("%s_pane", name));
            PaneBlock pane = block instanceof IBlockOverride ? ((IBlockOverride) block).providePane(block) : new MMOInheritedPaneBlock(block);
            registerBlock(paneId, pane);
            registerBlockItem(paneId, new BlockItem(pane, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        }
    }

    private void registerBlockItem(Identifier blockId, BlockItem blockItem) {
        Registry.register(Registry.ITEM, blockId, blockItem);
    }

    private void registerBlock(Identifier identifier, Block block) {
        Registry.register(Registry.BLOCK, identifier, block);
    }
}
