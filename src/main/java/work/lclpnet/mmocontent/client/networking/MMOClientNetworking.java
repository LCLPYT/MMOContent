package work.lclpnet.mmocontent.client.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.mmocontent.networking.MCPacket;
import work.lclpnet.mmocontent.networking.MMONetworking;

import java.io.IOException;
import java.util.Objects;

import static work.lclpnet.mmocontent.networking.MMONetworking.toPacketBuffer;

@Environment(EnvType.CLIENT)
public class MMOClientNetworking {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void registerClientPacketHandlers() {
        MMONetworking.registerClientPacketHandlers();
    }

    public static void sendPacketToServer(MCPacket packet) {
        try {
            PacketByteBuf buf = toPacketBuffer(packet);
            ClientPlayNetworking.send(packet.getIdentifier(), buf);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public static Packet<?> createVanillaC2SPacket(MCPacket packet) throws IOException {
        Objects.requireNonNull(packet, "Packet cannot be null");

        Identifier channelName = packet.getIdentifier();
        Objects.requireNonNull(channelName, "Channel name cannot be null");

        PacketByteBuf buf = toPacketBuffer(packet);
        return ClientPlayNetworking.createC2SPacket(channelName, buf);
    }
}
