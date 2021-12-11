package work.lclpnet.mmocontent.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;
import work.lclpnet.mmocontent.util.Env;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MMOPacketRegistrar {

    private Map<Identifier, IPacketDecoder<? extends MCPacket>> packetDecoderMap = new HashMap<>();
    private final Logger LOGGER;

    public MMOPacketRegistrar(Logger logger) {
        LOGGER = logger;
    }

    public void register(Identifier id, IPacketDecoder<? extends MCPacket> serializer) {
        Objects.requireNonNull(id);
        Objects.requireNonNull(serializer);

        packetDecoderMap.put(id, serializer);
    }

    @Environment(EnvType.CLIENT)
    public void registerClientPacketHandlers() {
        packetDecoderMap.forEach((id, serializer) -> ClientPlayNetworking.registerGlobalReceiver(id,
                (client, handler, buf, responseSender) -> {
                    try {
                        serializer.handleClient(serializer.decode(buf), client, handler, responseSender);
                    } catch (IOException e) {
                        LOGGER.error("Error decoding packet", e);
                    }
                })
        );
        packetDecoderMap = null; // registerClient should be called last
    }

    public void registerServerPacketHandlers() {
        packetDecoderMap.forEach((id, serializer) -> ServerPlayNetworking.registerGlobalReceiver(id,
                (server, player, handler, buf, responseSender) -> {
                    try {
                        serializer.handleServer(serializer.decode(buf), server, player, handler, responseSender);
                    } catch (IOException e) {
                        LOGGER.error("Error decoding packet", e);
                    }
                })
        );
    }
}
