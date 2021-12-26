package work.lclpnet.mmocontent.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import work.lclpnet.mmocontent.networking.MMONetworking;

@Environment(EnvType.CLIENT)
public class MMOContentClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MMONetworking.registerPackets();
        MMONetworking.registerClientPacketHandlers();
    }
}
