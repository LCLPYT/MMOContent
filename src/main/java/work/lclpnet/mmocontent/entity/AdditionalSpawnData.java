package work.lclpnet.mmocontent.entity;

import net.minecraft.network.PacketByteBuf;

public interface AdditionalSpawnData {

    void writeSpawnData(PacketByteBuf buffer);

    void readSpawnData(PacketByteBuf buffer);
}
