package work.lclpnet.mmocontent;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import work.lclpnet.mmocontent.networking.MMONetworking;

public class MMOContent implements ModInitializer {

    public static final String MOD_ID = "mmocontent";

    @Override
    public void onInitialize() {
        MMONetworking.registerPackets();
        MMONetworking.registerServerPacketHandlers();
    }

    public static Identifier identifier(String path) {
        return new Identifier(MOD_ID, path);
    }
}
