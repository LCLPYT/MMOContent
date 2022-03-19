package work.lclpnet.mmocontent.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import work.lclpnet.mmocontent.MMOContent;
import work.lclpnet.mmocontent.asm.mixin.common.BoatEntityAccessor;
import work.lclpnet.mmocontent.networking.MMONetworking;

import javax.annotation.Nullable;

public class MMOBoatEntity extends BoatEntity {

    private static final TrackedData<NbtCompound> TYPE = DataTracker.registerData(BoatEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    public MMOBoatEntity(World world, double x, double y, double z) {
        this(boatEntityType, world);
        this.setPosition(x, y, z);
        this.setVelocity(Vec3d.ZERO);
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;
    }

    public MMOBoatEntity(EntityType<? extends BoatEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TYPE, new NbtCompound());
    }

    @Nullable
    public MMOBoatType getMMOBoatType() {
        return MMOBoatType.fromTag(this.dataTracker.get(TYPE));
    }

    public void setMMOBoatType(MMOBoatType type) {
        final NbtCompound tag = new NbtCompound();
        type.toTag(tag);
        this.dataTracker.set(TYPE, tag);
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
        BoatEntityAccessor accessor = (BoatEntityAccessor) this;
        accessor.setFallVelocity(this.getVelocity().y);

        if (!this.hasVehicle()) {
            if (onGround) {
                if (this.fallDistance > 3.0F) {
                    if (accessor.getLocation() != Location.ON_LAND) {
                        this.fallDistance = 0.0F;
                        return;
                    }

                    this.handleFallDamage(this.fallDistance, 1.0F, DamageSource.FALL);
                    if (!this.world.isClient && !this.isRemoved()) {
                        this.kill();
                        if (this.world.getGameRules().getBoolean(GameRules.DO_ENTITY_DROPS)) {
                            int j;

                            final MMOBoatType boatType = getMMOBoatType();
                            if (boatType != null) {
                                for (j = 0; j < 3; ++j) {
                                    this.dropItem(boatType.baseBlock);
                                }
                            }

                            for(j = 0; j < 2; ++j) {
                                this.dropItem(Items.STICK);
                            }
                        }
                    }
                }

                this.fallDistance = 0.0F;
            } else if (!this.world.getFluidState(this.getBlockPos().down()).isIn(FluidTags.WATER) && heightDifference < 0.0D) {
                this.fallDistance = (float)((double)this.fallDistance - heightDifference);
            }
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        super.writeCustomDataToNbt(tag);

        MMOBoatType type = getMMOBoatType();
        if (type != null) type.toTag(tag);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        super.readCustomDataFromNbt(tag);

        MMOBoatType type = MMOBoatType.fromTag(tag);
        if (type != null) setMMOBoatType(type);
    }

    @Override
    public Item asItem() {
        final MMOBoatType type = getMMOBoatType();
        return type != null ? type.boatItem : super.asItem();
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return MMONetworking.createMMOSpawnPacket(this);
    }

    /* Library stuff */

    public static EntityType<MMOBoatEntity> boatEntityType = null;

    /**
     * By default, the MMOBoatEntity entity disabled.
     * Call this method while initialization to enable.
     */
    public static void enableMMOBoatIntegration() {
        if (boatEntityType != null) return;  // already enabled

        boatEntityType = Registry.register(Registry.ENTITY_TYPE,
                MMOContent.identifier("boat"),
                FabricEntityTypeBuilder.create(SpawnGroup.MISC, (EntityType.EntityFactory<MMOBoatEntity>) MMOBoatEntity::new)
                        .dimensions(EntityDimensions.changing(1.375F, 0.5625F))
                        .trackRangeChunks(10)
                        .build());
    }
}
