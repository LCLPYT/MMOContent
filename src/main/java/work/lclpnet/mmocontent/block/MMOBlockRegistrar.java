package work.lclpnet.mmocontent.block;

import com.google.common.base.Functions;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import work.lclpnet.mmocontent.block.ext.IBlockOverride;
import work.lclpnet.mmocontent.block.ext.IMMOBlock;
import work.lclpnet.mmocontent.block.ext.MMOBlock;
import work.lclpnet.mmocontent.block.ext.MMOVerticalSlabBlock;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.Objects;
import java.util.function.Function;

public class MMOBlockRegistrar {

    private static final IBlockOverride DEFAULT_PROVIDER = new IBlockOverride() {};

    protected final Block block;
    protected boolean slab = false;
    protected boolean stairs = false;
    protected boolean wall = false;
    protected boolean verticalSlab = false;
    protected boolean pane = false;
    protected boolean fence = false;
    protected boolean fenceGate = false;
    protected boolean door = false;
    protected boolean trapdoor = false;
    protected PressurePlateBlock.ActivationRule pressurePlate = null;
    // button
    protected boolean button = false;
    protected boolean buttonWooden = false;

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

    public MMOBlockRegistrar withFence() {
        this.fence = true;
        return this;
    }

    public MMOBlockRegistrar withFenceGate() {
        this.fenceGate = true;
        return this;
    }

    public MMOBlockRegistrar withDoor() {
        this.door = true;
        return this;
    }

    public MMOBlockRegistrar withTrapdoor() {
        this.trapdoor = true;
        return this;
    }

    public MMOBlockRegistrar withPressurePlate(PressurePlateBlock.ActivationRule type) {
        this.pressurePlate = type;
        return this;
    }

    public MMOBlockRegistrar withButton(boolean wooden) {
        this.button = true;
        this.buttonWooden = wooden;
        return this;
    }

    public Result register(Identifier blockId) {
        return register(blockId, ItemGroup.BUILDING_BLOCKS);
    }

    public Result register(final Identifier blockId, ItemGroup group) {
        return register(blockId, group, Functions.identity());
    }

    /**
     * Registers every block variant set by this builder.
     *
     * @param blockId The identifier of the base block to register, variant names will be derived from it.
     * @param group The {@link ItemGroup} to register items to.
     * @param basePathTransformer A function that transforms the <code>blockId</code> for the base block. Useful, for example,
     *                            if one want's to register planks and matching stairs, etc. one would set <code>blockId</code> to
     *                            <code>"woodname"</code> and the basePathTransformer to <code>id -> id + "_planks"</code>.
     *                            This way, the stairs would be named consistently: <code>"woodname_stairs"</code>.
     * @return The result, containing all registered variants.
     */
    public Result register(final Identifier blockId, ItemGroup group, Function<String, String> basePathTransformer) {
        return register(blockId, group, basePathTransformer, true);
    }

    /**
     * Registers every block variant set by this builder.
     *
     * @param blockId The identifier of the base block to register, variant names will be derived from it.
     * @param group The {@link ItemGroup} to register items to.
     * @param basePathTransformer A function that transforms the <code>blockId</code> for the base block. Useful, for example,
     *                            if one want's to register planks and matching stairs, etc. one would set <code>blockId</code> to
     *                            <code>"woodname"</code> and the basePathTransformer to <code>id -> id + "_planks"</code>.
     *                            This way, the stairs would be named consistently: <code>"woodname_stairs"</code>.
     * @param registerBase Whether to register the base block. If false, only the variants will be registered.
     *                     This feature is especially useful when trying to add stairs/slabs to vanilla blocks.
     * @return The result, containing all registered variants.
     */
    public Result register(final Identifier blockId, ItemGroup group, Function<String, String> basePathTransformer, boolean registerBase) {
        final String namespace = blockId.getNamespace(), name = blockId.getPath();

        Identifier baseBlockId = new Identifier(namespace, basePathTransformer.apply(name));
        if (registerBase)
            registerBlock(baseBlockId, block);

        final BlockItem item;

        if (registerBase) {
            final FabricItemSettings blockItemSettings = new FabricItemSettings().group(group);
            item = block instanceof IMMOBlock ? ((IMMOBlock) block).provideBlockItem(blockItemSettings) : new BlockItem(block, blockItemSettings);
            if (item != null)
                registerBlockItem(baseBlockId, item);
        } else {
            item = null;
        }

        final IBlockOverride provider = block instanceof IBlockOverride ? (IBlockOverride) block : DEFAULT_PROVIDER;

        RegisteredBlock<SlabBlock> registeredSlab = null;
        RegisteredBlock<MMOVerticalSlabBlock> registeredVerticalSlab = null;
        if (slab || verticalSlab) {
            SlabBlock slabBlock = provider.provideSlab(block);
            if (slab) {
                final Identifier slabId = new Identifier(namespace, String.format("%s_slab", name));
                registerBlock(slabId, slabBlock);

                BlockItem slabItem = new BlockItem(slabBlock, new FabricItemSettings().group(group));
                registerBlockItem(slabId, slabItem);

                registeredSlab = new RegisteredBlock<>(slabBlock, slabItem);
            }
            if (verticalSlab) {
                final Identifier verticalSlabId = new Identifier(namespace, String.format("%s_vertical_slab", name));
                MMOVerticalSlabBlock verticalSlabBlock = provider.provideVerticalSlab(slabBlock);
                registerBlock(verticalSlabId, verticalSlabBlock);

                BlockItem verticalSlabItem = new BlockItem(verticalSlabBlock, new FabricItemSettings().group(group));
                registerBlockItem(verticalSlabId, verticalSlabItem);

                registeredVerticalSlab = new RegisteredBlock<>(verticalSlabBlock, verticalSlabItem);
            }
        }

        RegisteredBlock<StairsBlock> registeredStairs = null;
        if (stairs) {
            final Identifier stairsId = new Identifier(namespace, String.format("%s_stairs", name));
            StairsBlock stairsBlock = provider.provideStairs(block);
            registerBlock(stairsId, stairsBlock);

            BlockItem stairsItem = new BlockItem(stairsBlock, new FabricItemSettings().group(group));
            registerBlockItem(stairsId, stairsItem);

            registeredStairs = new RegisteredBlock<>(stairsBlock, stairsItem);
        }

        RegisteredBlock<WallBlock> registeredWall = null;
        if (wall) {
            final Identifier wallId = new Identifier(namespace, String.format("%s_wall", name));
            WallBlock wallBlock = provider.provideWall(block);
            registerBlock(wallId, wallBlock);

            BlockItem wallItem = new BlockItem(wallBlock, new FabricItemSettings().group(group));
            registerBlockItem(wallId, wallItem);

            registeredWall = new RegisteredBlock<>(wallBlock, wallItem);
        }

        RegisteredBlock<PaneBlock> registeredPane = null;
        if (pane) {
            final Identifier paneId = new Identifier(namespace, String.format("%s_pane", name));
            PaneBlock paneBlock = provider.providePane(block);
            registerBlock(paneId, paneBlock);

            BlockItem paneItem = new BlockItem(paneBlock, new FabricItemSettings().group(group));
            registerBlockItem(paneId, paneItem);

            registeredPane = new RegisteredBlock<>(paneBlock, paneItem);
        }

        RegisteredBlock<FenceBlock> registeredFence = null;
        if (fence) {
            final Identifier fenceId = new Identifier(namespace, String.format("%s_fence", name));
            FenceBlock fenceBlock = provider.provideFence(block);
            registerBlock(fenceId, fenceBlock);

            BlockItem fenceItem = new BlockItem(fenceBlock, new FabricItemSettings().group(group));
            registerBlockItem(fenceId, fenceItem);

            registeredFence = new RegisteredBlock<>(fenceBlock, fenceItem);
        }

        RegisteredBlock<FenceGateBlock> registeredFenceGate = null;
        if (fenceGate) {
            final Identifier fenceGateId = new Identifier(namespace, String.format("%s_fence_gate", name));
            FenceGateBlock fenceGateBlock = provider.provideFenceGate(block);
            registerBlock(fenceGateId, fenceGateBlock);

            BlockItem fenceGateItem = new BlockItem(fenceGateBlock, new FabricItemSettings().group(group));
            registerBlockItem(fenceGateId, fenceGateItem);

            registeredFenceGate = new RegisteredBlock<>(fenceGateBlock, fenceGateItem);
        }

        RegisteredBlock<DoorBlock> registeredDoor = null;
        if (door) {
            final Identifier doorId = new Identifier(namespace, String.format("%s_door", name));
            DoorBlock doorBlock = provider.provideDoor(block);
            registerBlock(doorId, doorBlock);

            BlockItem doorItem = new BlockItem(doorBlock, new FabricItemSettings().group(group));
            registerBlockItem(doorId, doorItem);

            registeredDoor = new RegisteredBlock<>(doorBlock, doorItem);
        }

        RegisteredBlock<TrapdoorBlock> registeredTrapdoor = null;
        if (trapdoor) {
            final Identifier trapdoorId = new Identifier(namespace, String.format("%s_trapdoor", name));
            TrapdoorBlock trapdoorBlock = provider.provideTrapdoor(block);
            registerBlock(trapdoorId, trapdoorBlock);

            BlockItem trapdoorItem = new BlockItem(trapdoorBlock, new FabricItemSettings().group(group));
            registerBlockItem(trapdoorId, trapdoorItem);

            registeredTrapdoor = new RegisteredBlock<>(trapdoorBlock, trapdoorItem);
        }

        RegisteredBlock<PressurePlateBlock> registeredPressurePlate = null;
        if (pressurePlate != null) {
            final Identifier pressurePlateId = new Identifier(namespace, String.format("%s_pressure_plate", name));
            PressurePlateBlock doorBlock = provider.providePressurePlate(pressurePlate, block);
            registerBlock(pressurePlateId, doorBlock);

            BlockItem pressurePlateItem = new BlockItem(doorBlock, new FabricItemSettings().group(group));
            registerBlockItem(pressurePlateId, pressurePlateItem);

            registeredPressurePlate = new RegisteredBlock<>(doorBlock, pressurePlateItem);
        }

        RegisteredBlock<AbstractButtonBlock> registeredButton = null;
        if (button) {
            final Identifier buttonId = new Identifier(namespace, String.format("%s_button", name));
            AbstractButtonBlock buttonBlock = provider.provideButton(buttonWooden, block);
            registerBlock(buttonId, buttonBlock);

            BlockItem buttonItem = new BlockItem(buttonBlock, new FabricItemSettings().group(group));
            registerBlockItem(buttonId, buttonItem);

            registeredButton = new RegisteredBlock<>(buttonBlock, buttonItem);
        }

        return new Result(item, registeredSlab, registeredVerticalSlab, registeredStairs, registeredWall, registeredPane,
                registeredFence, registeredFenceGate, registeredDoor, registeredTrapdoor, registeredPressurePlate, registeredButton);
    }

    private void registerBlockItem(Identifier blockId, BlockItem blockItem) {
        Registry.register(Registry.ITEM, blockId, blockItem);
    }

    private void registerBlock(Identifier identifier, Block block) {
        Registry.register(Registry.BLOCK, identifier, block);
    }

    public record Result(@Nullable BlockItem item,
                         @Nullable RegisteredBlock<SlabBlock> slab,
                         @Nullable RegisteredBlock<MMOVerticalSlabBlock> verticalSlab,
                         @Nullable RegisteredBlock<StairsBlock> stairs,
                         @Nullable RegisteredBlock<WallBlock> wall,
                         @Nullable RegisteredBlock<PaneBlock> pane,
                         @Nullable RegisteredBlock<FenceBlock> fence,
                         @Nullable RegisteredBlock<FenceGateBlock> fenceGate,
                         @Nullable RegisteredBlock<DoorBlock> door,
                         @Nullable RegisteredBlock<TrapdoorBlock> trapdoor,
                         @Nullable RegisteredBlock<PressurePlateBlock> pressurePlate,
                         @Nullable RegisteredBlock<AbstractButtonBlock> button) {

        @ParametersAreNullableByDefault
        public Result(BlockItem item,
                      RegisteredBlock<SlabBlock> slab,
                      RegisteredBlock<MMOVerticalSlabBlock> verticalSlab,
                      RegisteredBlock<StairsBlock> stairs,
                      RegisteredBlock<WallBlock> wall,
                      RegisteredBlock<PaneBlock> pane,
                      RegisteredBlock<FenceBlock> fence,
                      RegisteredBlock<FenceGateBlock> fenceGate,
                      RegisteredBlock<DoorBlock> door,
                      RegisteredBlock<TrapdoorBlock> trapdoor,
                      RegisteredBlock<PressurePlateBlock> pressurePlate,
                      RegisteredBlock<AbstractButtonBlock> button) {
            this.item = item;
            this.slab = slab;
            this.verticalSlab = verticalSlab;
            this.stairs = stairs;
            this.wall = wall;
            this.pane = pane;
            this.fence = fence;
            this.fenceGate = fenceGate;
            this.door = door;
            this.trapdoor = trapdoor;
            this.pressurePlate = pressurePlate;
            this.button = button;
        }
    }

    public record RegisteredBlock<T extends Block>(T block, @Nullable BlockItem item) {

        public RegisteredBlock(T block, @Nullable BlockItem item) {
            this.block = block;
            this.item = item;
        }
    }
}
