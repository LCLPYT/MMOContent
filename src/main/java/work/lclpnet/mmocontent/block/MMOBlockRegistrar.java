package work.lclpnet.mmocontent.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.ext.*;
import work.lclpnet.mmocontent.client.render.block.MMOBlockColors;
import work.lclpnet.mmocontent.util.Env;
import work.lclpnet.mmocontent.util.IdentifierProvider;

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

    public void register(String name, IdentifierProvider identifierProvider) {
        register(name, ItemGroup.BUILDING_BLOCKS, identifierProvider);
    }

    public void register(String name, ItemGroup group, IdentifierProvider identifierProvider) {
        final Identifier blockId = identifierProvider.identifier(name);
        registerBlock(blockId, block);

        final FabricItemSettings blockItemSettings = new FabricItemSettings().group(group);
        BlockItem item = block instanceof IMMOBlock ? ((IMMOBlock) block).provideBlockItem(blockItemSettings) : new BlockItem(block, blockItemSettings);
        if (item != null) registerBlockItem(blockId, item);

        if (slab || verticalSlab) {
            SlabBlock slabBlock = block instanceof IBlockOverride ? ((IBlockOverride) block).provideSlab(block) : new MMOSlabBlock(block);
            if (slab) {
                final Identifier slabId = identifierProvider.identifier(name + "_slab");
                registerBlock(slabId, slabBlock);
                registerBlockItem(slabId, new BlockItem(slabBlock, new FabricItemSettings().group(group)));
            }
            if (verticalSlab) {
                final Identifier verticalSlabId = identifierProvider.identifier(name + "_vertical_slab");
                MMOVerticalSlabBlock verticalSlab = block instanceof IBlockOverride ? ((IBlockOverride) block).provideVerticalSlab(slabBlock) : new MMOVerticalSlabBlock(slabBlock);
                registerBlock(verticalSlabId, verticalSlab);
                registerBlockItem(verticalSlabId, new BlockItem(verticalSlab, new FabricItemSettings().group(group)));
            }
        }
        if (stairs) {
            final Identifier stairsId = identifierProvider.identifier(name + "_stairs");
            StairsBlock stairs = block instanceof IBlockOverride ? ((IBlockOverride) block).provideStairs(block) : new MMOStairsBlock(block);
            registerBlock(stairsId, stairs);
            registerBlockItem(stairsId, new BlockItem(stairs, new FabricItemSettings().group(group)));
        }
        if (wall) {
            final Identifier wallId = identifierProvider.identifier(name + "_wall");
            WallBlock wall = block instanceof IBlockOverride ? ((IBlockOverride) block).provideWall(block) : new MMOWallBlock(block);
            registerBlock(wallId, wall);
            registerBlockItem(wallId, new BlockItem(wall, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        }
        if (pane) {
            final Identifier paneId = identifierProvider.identifier(name + "_pane");
            PaneBlock pane = block instanceof IBlockOverride ? ((IBlockOverride) block).providePane(block) : new MMOInheritedPaneBlock(block);
            registerBlock(paneId, pane);
            registerBlockItem(paneId, new BlockItem(pane, new FabricItemSettings().group(ItemGroup.DECORATIONS)));
        }
    }

    private void registerBlockItem(Identifier blockId, BlockItem blockItem) {
        Registry.register(Registry.ITEM, blockId, blockItem);
        if (Env.isClient()) {
            Block block = blockItem.getBlock();
            if (block instanceof IItemColorProvider) registerItemColor((IItemColorProvider) block);
        }
    }

    private void registerBlock(Identifier identifier, Block block) {
        Registry.register(Registry.BLOCK, identifier, block);
        if (Env.isClient() && block instanceof IBlockColorProvider) registerBlockColor((IBlockColorProvider) block);
    }

    @Environment(EnvType.CLIENT)
    private void registerItemColor(IItemColorProvider provider) {
        MMOBlockColors.registerItemColorProvider(provider);
    }

    @Environment(EnvType.CLIENT)
    private void registerBlockColor(IBlockColorProvider provider) {
        MMOBlockColors.registerBlockColorProvider(provider);
    }
}
