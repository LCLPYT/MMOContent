package work.lclpnet.mmocontent.client;

import net.fabricmc.api.ClientModInitializer;
import work.lclpnet.mmocontent.client.render.block.MMORenderLayers;

public class MMOContentClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        MMORenderLayers.init();
    }
}
