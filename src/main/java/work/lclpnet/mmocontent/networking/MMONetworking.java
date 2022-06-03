package work.lclpnet.mmocontent.networking;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import work.lclpnet.mmocontent.networking.packet.MMOEntitySpawnS2CPacket;
import work.lclpnet.mmocontent.util.Env;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;

public class MMONetworking {

    private static final Logger LOGGER = LogManager.getLogger();
    private static MMOPacketRegistrar registrar = null;

    public static void registerPackets() {
        if (registrar != null) return; // already registered

        registrar = new MMOPacketRegistrar(LOGGER);
        registrar.register(MMOEntitySpawnS2CPacket.ID, new MMOEntitySpawnS2CPacket.Decoder());
    }

    @Environment(EnvType.CLIENT)
    public static void registerClientPacketHandlers() {
        registrar.registerClientPacketHandlers();
        registrar = null; // should be called last on client
    }

    public static void registerServerPacketHandlers() {
        registrar.registerServerPacketHandlers();
        if (!Env.isClient()) registrar = null; // not needed any further on a dedicated server
    }

    public static void sendPacketTo(MCPacket packet, ServerPlayerEntity player) {
        try {
            PacketByteBuf buf = toPacketBuffer(packet);
            ServerPlayNetworking.send(player, packet.getIdentifier(), buf);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public static void sendToAllTracking(Entity tracked, MCPacket packet) {
        Objects.requireNonNull(tracked);
        Objects.requireNonNull(packet);
        PlayerLookup.tracking(tracked).forEach(p -> sendPacketTo(packet, p));
    }

    public static void sendToAllTrackingIncludingSelf(LivingEntity living, MCPacket packet) {
        MMONetworking.sendToAllTracking(living, packet);

        // if the entity is a player, the packet will not be sent to the player, since players do not track themselves.
        if (living instanceof ServerPlayerEntity) MMONetworking.sendPacketTo(packet, (ServerPlayerEntity) living);
    }

    public static Packet<?> createVanillaS2CPacket(MCPacket packet) throws IOException {
        Objects.requireNonNull(packet, "Packet cannot be null");

        Identifier channelName = packet.getIdentifier();
        Objects.requireNonNull(channelName, "Channel name cannot be null");

        PacketByteBuf buf = toPacketBuffer(packet);
        return ServerPlayNetworking.createS2CPacket(channelName, buf);
    }

    public static Packet<?> createMMOSpawnPacket(Entity entity) {
        try {
            return createVanillaS2CPacket(new MMOEntitySpawnS2CPacket(entity));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create spawn packet", e);
        }
    }

    @Nonnull
    public static PacketByteBuf toPacketBuffer(MCPacket packet) throws IOException {
        PacketByteBuf buf = PacketByteBufs.create();
        packet.encodeTo(buf);
        return buf;
    }
}
