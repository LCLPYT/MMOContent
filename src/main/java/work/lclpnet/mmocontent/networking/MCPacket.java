package work.lclpnet.mmocontent.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import work.lclpnet.mmocontent.client.networking.MMOClientNetworking;

import java.io.IOException;

public abstract class MCPacket {

    private final Identifier identifier;

    public MCPacket(Identifier identifier) {
        this.identifier = identifier;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public abstract void encodeTo(PacketByteBuf buffer) throws IOException;

    public void sendTo(ServerPlayerEntity player) {
        MMONetworking.sendPacketTo(this, player);
    }

    @Environment(EnvType.CLIENT)
    public void sendToServer() {
        MMOClientNetworking.sendPacketToServer(this);
    }
}
