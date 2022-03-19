package work.lclpnet.mmocontent.entity;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MMOBoatType {

    public final Identifier identifier;
    public final Block baseBlock;
    public Item boatItem = Items.OAK_BOAT;

    protected MMOBoatType(Identifier identifier, Block baseBlock) {
        this.identifier = Objects.requireNonNull(identifier);
        this.baseBlock = Objects.requireNonNull(baseBlock);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MMOBoatType mBoatType = (MMOBoatType) o;
        return Objects.equals(identifier, mBoatType.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    public void toTag(NbtCompound tag) {
        tag.putString("MBoatType", this.identifier.toString());
    }

    private static final Map<Identifier, MMOBoatType> types = new HashMap<>();

    public static MMOBoatType register(Identifier identifier, Block baseBlock) {
        MMOBoatType type = new MMOBoatType(identifier, baseBlock);
        types.put(identifier, type);
        return type;
    }

    @Nullable
    public static MMOBoatType get(Identifier identifier) {
        return types.get(identifier);
    }

    public static Collection<MMOBoatType> getBoatTypes() {
        return types.values();
    }

    @Nullable
    public static MMOBoatType fromTag(NbtCompound tag) {
        if (!tag.contains("MBoatType")) return null;

        Identifier id = new Identifier(tag.getString("MBoatType"));
        return types.get(id);
    }
}
