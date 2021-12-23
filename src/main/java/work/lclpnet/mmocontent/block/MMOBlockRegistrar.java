package work.lclpnet.mmocontent.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.ext.*;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
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

        RegisteredBlock<SlabBlock> registeredSlab = null;
        RegisteredBlock<MMOVerticalSlabBlock> registeredVerticalSlab = null;
        if (slab || verticalSlab) {
            SlabBlock slabBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideSlab(block) : new MMOSlabBlock(block);
            if (slab) {
                final Identifier slabId = new Identifier(namespace, String.format("%s_slab", name));
                registerBlock(slabId, slabBlock);

                BlockItem slabItem = new BlockItem(slabBlock, new FabricItemSettings().group(group));
                registerBlockItem(slabId, slabItem);

                registeredSlab = new RegisteredBlock<>(slabBlock, slabItem);
            }
            if (verticalSlab) {
                final Identifier verticalSlabId = new Identifier(namespace, name + "_vertical_slab");
                MMOVerticalSlabBlock verticalSlabBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideVerticalSlab(slabBlock) : new MMOVerticalSlabBlock(slabBlock);
                registerBlock(verticalSlabId, verticalSlabBlock);

                BlockItem verticalSlabItem = new BlockItem(verticalSlabBlock, new FabricItemSettings().group(group));
                registerBlockItem(verticalSlabId, verticalSlabItem);

                registeredVerticalSlab = new RegisteredBlock<>(verticalSlabBlock, verticalSlabItem);
            }
        }

        RegisteredBlock<StairsBlock> registeredStairs = null;
        if (stairs) {
            final Identifier stairsId = new Identifier(namespace, String.format("%s_stairs", name));
            StairsBlock stairsBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideStairs(block) : new MMOStairsBlock(block);
            registerBlock(stairsId, stairsBlock);

            BlockItem stairsItem = new BlockItem(stairsBlock, new FabricItemSettings().group(group));
            registerBlockItem(stairsId, stairsItem);

            registeredStairs = new RegisteredBlock<>(stairsBlock, stairsItem);
        }

        RegisteredBlock<WallBlock> registeredWall = null;
        if (wall) {
            final Identifier wallId = new Identifier(namespace, String.format("%s_wall", name));
            WallBlock wallBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideWall(block) : new MMOWallBlock(block);
            registerBlock(wallId, wallBlock);

            BlockItem wallItem = new BlockItem(wallBlock, new FabricItemSettings().group(ItemGroup.DECORATIONS));
            registerBlockItem(wallId, wallItem);

            registeredWall = new RegisteredBlock<>(wallBlock, wallItem);
        }

        RegisteredBlock<PaneBlock> registeredPane = null;
        if (pane) {
            final Identifier paneId = new Identifier(namespace, String.format("%s_pane", name));
            PaneBlock paneBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).providePane(block) : new MMOInheritedPaneBlock(block);
            registerBlock(paneId, paneBlock);

            BlockItem paneItem = new BlockItem(paneBlock, new FabricItemSettings().group(ItemGroup.DECORATIONS));
            registerBlockItem(paneId, paneItem);

            registeredPane = new RegisteredBlock<>(paneBlock, paneItem);
        }

        return new Result(item, registeredSlab, registeredVerticalSlab, registeredStairs, registeredWall, registeredPane);
    }

    private void registerBlockItem(Identifier blockId, BlockItem blockItem) {
        Registry.register(Registry.ITEM, blockId, blockItem);
    }

    private void registerBlock(Identifier identifier, Block block) {
        Registry.register(Registry.BLOCK, identifier, block);
    }

    public static class Result {

        @Nullable
        public final BlockItem item;
        @Nullable
        public final RegisteredBlock<SlabBlock> slab;
        @Nullable
        public final RegisteredBlock<MMOVerticalSlabBlock> verticalSlab;
        @Nullable
        public final RegisteredBlock<StairsBlock> stairs;
        @Nullable
        public final RegisteredBlock<WallBlock> wall;
        @Nullable
        public final RegisteredBlock<PaneBlock> pane;

        @ParametersAreNullableByDefault
        public Result(BlockItem item,
                      RegisteredBlock<SlabBlock> slab,
                      RegisteredBlock<MMOVerticalSlabBlock> verticalSlab,
                      RegisteredBlock<StairsBlock> stairs,
                      RegisteredBlock<WallBlock> wall,
                      RegisteredBlock<PaneBlock> pane) {
            this.item = item;
            this.slab = slab;
            this.verticalSlab = verticalSlab;
            this.stairs = stairs;
            this.wall = wall;
            this.pane = pane;
        }
    }

    public static class RegisteredBlock<T extends Block> {

        public final T block;
        @Nullable
        public final BlockItem item;

        public RegisteredBlock(T block, @Nullable BlockItem item) {
            this.block = block;
            this.item = item;
        }
    }
}
