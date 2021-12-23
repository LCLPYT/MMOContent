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

    public Result register(Identifier blockId) {
        return register(blockId, ItemGroup.BUILDING_BLOCKS);
    }

    public Result register(final Identifier blockId, ItemGroup group) {
        registerBlock(blockId, block);

        final String namespace = blockId.getNamespace(), name = blockId.getPath();

        final FabricItemSettings blockItemSettings = new FabricItemSettings().group(group);
        BlockItem item = block instanceof IMMOBlock ? ((IMMOBlock) block).provideBlockItem(blockItemSettings) : new BlockItem(block, blockItemSettings);
        if (item != null) registerBlockItem(blockId, item);

        SlabBlock slabBlock = null;
        MMOVerticalSlabBlock verticalSlabBlock = null;
        if (slab || verticalSlab) {
            slabBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideSlab(block) : new MMOSlabBlock(block);
            if (slab) {
                final Identifier slabId = new Identifier(namespace, String.format("%s_slab", name));
                registerBlock(slabId, slabBlock);
                registerBlockItem(slabId, new BlockItem(slabBlock, new FabricItemSettings().group(group)));
            }
            if (verticalSlab) {
                final Identifier verticalSlabId = new Identifier(namespace, name + "_vertical_slab");
                verticalSlabBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideVerticalSlab(slabBlock) : new MMOVerticalSlabBlock(slabBlock);
                registerBlock(verticalSlabId, verticalSlabBlock);
                registerBlockItem(verticalSlabId, new BlockItem(verticalSlabBlock, new FabricItemSettings().group(group)));
            }
        }

        StairsBlock stairsBlock = null;
        if (stairs) {
            final Identifier stairsId = new Identifier(namespace, String.format("%s_stairs", name));
            stairsBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideStairs(block) : new MMOStairsBlock(block);
            registerBlock(stairsId, stairsBlock);
            registerBlockItem(stairsId, new BlockItem(stairsBlock, new FabricItemSettings().group(group)));
        }

        WallBlock wallBlock = null;
        if (wall) {
            final Identifier wallId = new Identifier(namespace, String.format("%s_wall", name));
            wallBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideWall(block) : new MMOWallBlock(block);
            registerBlock(wallId, wallBlock);
            registerBlockItem(wallId, new BlockItem(wallBlock, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        }

        PaneBlock paneBlock = null;
        if (pane) {
            final Identifier paneId = new Identifier(namespace, String.format("%s_pane", name));
            paneBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).providePane(block) : new MMOInheritedPaneBlock(block);
            registerBlock(paneId, paneBlock);
            registerBlockItem(paneId, new BlockItem(paneBlock, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        }

        return new Result(item, slabBlock, verticalSlabBlock, stairsBlock, wallBlock, paneBlock);
    }

    private void registerBlockItem(Identifier blockId, BlockItem blockItem) {
        Registry.register(Registry.ITEM, blockId, blockItem);
    }

    private void registerBlock(Identifier identifier, Block block) {
        Registry.register(Registry.BLOCK, identifier, block);
    }

    public static class Result {

        public final BlockItem item;
        public final SlabBlock slab;
        public final MMOVerticalSlabBlock verticalSlab;
        public final StairsBlock stairs;
        public final WallBlock wall;
        public final PaneBlock pane;

        public Result(BlockItem item, SlabBlock slab, MMOVerticalSlabBlock verticalSlab, StairsBlock stairs, WallBlock wall, PaneBlock pane) {
            this.item = item;
            this.slab = slab;
            this.verticalSlab = verticalSlab;
            this.stairs = stairs;
            this.wall = wall;
            this.pane = pane;
        }
    }
}
