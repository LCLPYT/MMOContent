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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNullableByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class MMOBlockRegistrar {

    private static final IBlockOverride DEFAULT_PROVIDER = new IBlockOverride() {};

    public enum Component {
        SLAB,
        STAIRS,
        WALL,
        VERTICAL_SLAB,
        PANE,
        FENCE,
        FENCE_GATE,
        DOOR,
        TRAPDOOR,
        PRESSURE_PLATE,
        BUTTON
    }

    protected static class ComponentData {
        public final ItemGroup group;

        public ComponentData(ItemGroup group) {
            this.group = group;
        }
    }

    protected static class ButtonComponentData extends ComponentData {
        public final boolean wooden;

        public ButtonComponentData(ItemGroup group, boolean wooden) {
            super(group);
            this.wooden = wooden;
        }
    }

    protected static class PressurePlateComponentData extends ComponentData {
        public final PressurePlateBlock.ActivationRule activationRule;

        public PressurePlateComponentData(ItemGroup group, PressurePlateBlock.ActivationRule activationRule) {
            super(group);
            this.activationRule = activationRule;
        }
    }

    protected final Block block;
    protected final Map<Component, ComponentData> components = new HashMap<>();

    public MMOBlockRegistrar(Block block) {
        this.block = Objects.requireNonNull(block);
    }

    public MMOBlockRegistrar(AbstractBlock.Settings settings) {
        this(new MMOBlock(Objects.requireNonNull(settings)));
    }

    public MMOBlockRegistrar with(Component component) {
        return with(component, new ComponentData(null));
    }

    public MMOBlockRegistrar with(Component component, ItemGroup group) {
        this.components.put(component, new ComponentData(group));
        return this;
    }

    public MMOBlockRegistrar with(Component component, ComponentData data) {
        this.components.put(component, data);
        return this;
    }

    public MMOBlockRegistrar withSlab() {
        return with(Component.SLAB);
    }

    public MMOBlockRegistrar withSlab(ItemGroup group) {
        return with(Component.SLAB, group);
    }

    public MMOBlockRegistrar withStairs() {
        return with(Component.STAIRS);
    }

    public MMOBlockRegistrar withStairs(ItemGroup group) {
        return with(Component.STAIRS, group);
    }

    public MMOBlockRegistrar withWall() {
        return with(Component.WALL);
    }

    public MMOBlockRegistrar withWall(ItemGroup group) {
        return with(Component.WALL, group);
    }

    public MMOBlockRegistrar withVerticalSlab() {
        return with(Component.VERTICAL_SLAB);
    }

    public MMOBlockRegistrar withVerticalSlab(ItemGroup group) {
        return with(Component.VERTICAL_SLAB, group);
    }

    public MMOBlockRegistrar withPane() {
        return with(Component.PANE);
    }

    public MMOBlockRegistrar withPane(ItemGroup group) {
        return with(Component.PANE, group);
    }

    public MMOBlockRegistrar withFence() {
        return with(Component.FENCE);
    }

    public MMOBlockRegistrar withFence(ItemGroup group) {
        return with(Component.FENCE, group);
    }

    public MMOBlockRegistrar withFenceGate() {
        return with(Component.FENCE_GATE);
    }

    public MMOBlockRegistrar withFenceGate(ItemGroup group) {
        return with(Component.FENCE_GATE, group);
    }

    public MMOBlockRegistrar withDoor() {
        return with(Component.DOOR);
    }

    public MMOBlockRegistrar withDoor(ItemGroup group) {
        return with(Component.DOOR, group);
    }

    public MMOBlockRegistrar withTrapdoor() {
        return with(Component.TRAPDOOR);
    }

    public MMOBlockRegistrar withTrapdoor(ItemGroup group) {
        return with(Component.TRAPDOOR, group);
    }

    public MMOBlockRegistrar withPressurePlate(PressurePlateBlock.ActivationRule type) {
        return with(Component.PRESSURE_PLATE, new PressurePlateComponentData(null, type));
    }

    public MMOBlockRegistrar withPressurePlate(ItemGroup group, PressurePlateBlock.ActivationRule type) {
        return with(Component.PRESSURE_PLATE, new PressurePlateComponentData(group, type));
    }

    public MMOBlockRegistrar withButton(boolean wooden) {
        return with(Component.BUTTON, new ButtonComponentData(null, wooden));
    }

    public MMOBlockRegistrar withButton(ItemGroup group, boolean wooden) {
        return with(Component.BUTTON, new ButtonComponentData(group, wooden));
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected <T extends ComponentData> T getData(@Nonnull Component component) {
        return (T) components.get(component);
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

        AtomicReference<RegisteredBlock<SlabBlock>> registeredSlab = new AtomicReference<>(null);
        AtomicReference<RegisteredBlock<MMOVerticalSlabBlock>> registeredVerticalSlab = new AtomicReference<>(null);
        registerSlabAndVerticalSlab(namespace, name, group, provider, registeredSlab, registeredVerticalSlab);

        RegisteredBlock<StairsBlock> registeredStairs = registerStairs(namespace, name, group, provider);
        RegisteredBlock<WallBlock> registeredWall = registerWall(namespace, name, group, provider);
        RegisteredBlock<PaneBlock> registeredPane = registerPane(namespace, name, group, provider);
        RegisteredBlock<FenceBlock> registeredFence = registerFence(namespace, name, group, provider);
        RegisteredBlock<FenceGateBlock> registeredFenceGate = registerFenceGate(namespace, name, group, provider);
        RegisteredBlock<DoorBlock> registeredDoor = registerDoor(namespace, name, group, provider);
        RegisteredBlock<TrapdoorBlock> registeredTrapdoor = registerTrapdoor(namespace, name, group, provider);
        RegisteredBlock<PressurePlateBlock> registeredPressurePlate = registerPressurePlate(namespace, name, group, provider);
        RegisteredBlock<AbstractButtonBlock> registeredButton = registerButton(namespace, name, group, provider);

        return new Result(item, registeredSlab.get(), registeredVerticalSlab.get(), registeredStairs, registeredWall, registeredPane,
                registeredFence, registeredFenceGate, registeredDoor, registeredTrapdoor, registeredPressurePlate, registeredButton);
    }

    @Nullable
    private RegisteredBlock<AbstractButtonBlock> registerButton(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ButtonComponentData data = getData(Component.BUTTON);
        RegisteredBlock<AbstractButtonBlock> registeredButton = null;

        if (data != null) {
            final Identifier buttonId = new Identifier(namespace, String.format("%s_button", name));
            AbstractButtonBlock buttonBlock = provider.provideButton(data.wooden, block);
            registerBlock(buttonId, buttonBlock);

            BlockItem buttonItem = new BlockItem(buttonBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(buttonId, buttonItem);

            registeredButton = new RegisteredBlock<>(buttonBlock, buttonItem);
        }

        return registeredButton;
    }

    @Nullable
    private RegisteredBlock<PressurePlateBlock> registerPressurePlate(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final PressurePlateComponentData data = getData(Component.PRESSURE_PLATE);
        RegisteredBlock<PressurePlateBlock> registeredPressurePlate = null;

        if (data != null) {
            final Identifier pressurePlateId = new Identifier(namespace, String.format("%s_pressure_plate", name));
            PressurePlateBlock doorBlock = provider.providePressurePlate(data.activationRule, block);
            registerBlock(pressurePlateId, doorBlock);

            BlockItem pressurePlateItem = new BlockItem(doorBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(pressurePlateId, pressurePlateItem);

            registeredPressurePlate = new RegisteredBlock<>(doorBlock, pressurePlateItem);
        }

        return registeredPressurePlate;
    }

    @Nullable
    private RegisteredBlock<TrapdoorBlock> registerTrapdoor(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ComponentData data = getData(Component.TRAPDOOR);
        RegisteredBlock<TrapdoorBlock> registeredTrapdoor = null;

        if (data != null) {
            final Identifier trapdoorId = new Identifier(namespace, String.format("%s_trapdoor", name));
            TrapdoorBlock trapdoorBlock = provider.provideTrapdoor(block);
            registerBlock(trapdoorId, trapdoorBlock);

            BlockItem trapdoorItem = new BlockItem(trapdoorBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(trapdoorId, trapdoorItem);

            registeredTrapdoor = new RegisteredBlock<>(trapdoorBlock, trapdoorItem);
        }

        return registeredTrapdoor;
    }

    @Nullable
    private RegisteredBlock<DoorBlock> registerDoor(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ComponentData data = getData(Component.DOOR);
        RegisteredBlock<DoorBlock> registeredDoor = null;

        if (data != null) {
            final Identifier doorId = new Identifier(namespace, String.format("%s_door", name));
            DoorBlock doorBlock = provider.provideDoor(block);
            registerBlock(doorId, doorBlock);

            BlockItem doorItem = new BlockItem(doorBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(doorId, doorItem);

            registeredDoor = new RegisteredBlock<>(doorBlock, doorItem);
        }

        return registeredDoor;
    }

    @Nullable
    private RegisteredBlock<FenceGateBlock> registerFenceGate(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ComponentData data = getData(Component.FENCE_GATE);
        RegisteredBlock<FenceGateBlock> registeredFenceGate = null;

        if (data != null) {
            final Identifier fenceGateId = new Identifier(namespace, String.format("%s_fence_gate", name));
            FenceGateBlock fenceGateBlock = provider.provideFenceGate(block);
            registerBlock(fenceGateId, fenceGateBlock);

            BlockItem fenceGateItem = new BlockItem(fenceGateBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(fenceGateId, fenceGateItem);

            registeredFenceGate = new RegisteredBlock<>(fenceGateBlock, fenceGateItem);
        }

        return registeredFenceGate;
    }

    @Nullable
    private RegisteredBlock<FenceBlock> registerFence(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ComponentData data = getData(Component.FENCE);
        RegisteredBlock<FenceBlock> registeredFence = null;

        if (data != null) {
            final Identifier fenceId = new Identifier(namespace, String.format("%s_fence", name));
            FenceBlock fenceBlock = provider.provideFence(block);
            registerBlock(fenceId, fenceBlock);

            BlockItem fenceItem = new BlockItem(fenceBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(fenceId, fenceItem);

            registeredFence = new RegisteredBlock<>(fenceBlock, fenceItem);
        }

        return registeredFence;
    }

    @Nullable
    private RegisteredBlock<PaneBlock> registerPane(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ComponentData data = getData(Component.PANE);
        RegisteredBlock<PaneBlock> registeredPane = null;

        if (data != null) {
            final Identifier paneId = new Identifier(namespace, String.format("%s_pane", name));
            PaneBlock paneBlock = provider.providePane(block);
            registerBlock(paneId, paneBlock);

            BlockItem paneItem = new BlockItem(paneBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(paneId, paneItem);

            registeredPane = new RegisteredBlock<>(paneBlock, paneItem);
        }

        return registeredPane;
    }

    @Nullable
    private RegisteredBlock<WallBlock> registerWall(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ComponentData data = getData(Component.WALL);
        RegisteredBlock<WallBlock> registeredWall = null;

        if (data != null) {
            final Identifier wallId = new Identifier(namespace, String.format("%s_wall", name));
            WallBlock wallBlock = provider.provideWall(block);
            registerBlock(wallId, wallBlock);

            BlockItem wallItem = new BlockItem(wallBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(wallId, wallItem);

            registeredWall = new RegisteredBlock<>(wallBlock, wallItem);
        }

        return registeredWall;
    }

    @Nullable
    private RegisteredBlock<StairsBlock> registerStairs(String namespace, String name, ItemGroup group, IBlockOverride provider) {
        final ComponentData data = getData(Component.STAIRS);
        RegisteredBlock<StairsBlock> registeredStairs = null;

        if (data != null) {
            final Identifier stairsId = new Identifier(namespace, String.format("%s_stairs", name));
            StairsBlock stairsBlock = provider.provideStairs(block);
            registerBlock(stairsId, stairsBlock);

            BlockItem stairsItem = new BlockItem(stairsBlock, new FabricItemSettings().group(data.group != null ? data.group : group));
            registerBlockItem(stairsId, stairsItem);

            registeredStairs = new RegisteredBlock<>(stairsBlock, stairsItem);
        }

        return registeredStairs;
    }

    private void registerSlabAndVerticalSlab(String namespace, String name, ItemGroup group, IBlockOverride provider, AtomicReference<RegisteredBlock<SlabBlock>> registeredSlab, AtomicReference<RegisteredBlock<MMOVerticalSlabBlock>> registeredVerticalSlab) {
        final ComponentData slab = getData(Component.SLAB), verticalSlab = getData(Component.VERTICAL_SLAB);
        if (slab == null && verticalSlab == null) return;

        SlabBlock slabBlock = provider.provideSlab(block);
        if (slab != null) {
            final Identifier slabId = new Identifier(namespace, String.format("%s_slab", name));
            registerBlock(slabId, slabBlock);

            final BlockItem slabItem = new BlockItem(slabBlock, new FabricItemSettings().group(slab.group != null ? slab.group : group));
            registerBlockItem(slabId, slabItem);

            registeredSlab.set(new RegisteredBlock<>(slabBlock, slabItem));
        }

        if (verticalSlab != null) {
            final Identifier verticalSlabId = new Identifier(namespace, String.format("%s_vertical_slab", name));
            MMOVerticalSlabBlock verticalSlabBlock = provider.provideVerticalSlab(slabBlock);
            registerBlock(verticalSlabId, verticalSlabBlock);

            final BlockItem verticalSlabItem = new BlockItem(verticalSlabBlock,
                    new FabricItemSettings().group(verticalSlab.group != null ? verticalSlab.group : group));
            registerBlockItem(verticalSlabId, verticalSlabItem);

            registeredVerticalSlab.set(new RegisteredBlock<>(verticalSlabBlock, verticalSlabItem));
        }
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
