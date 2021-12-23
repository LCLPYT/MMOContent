package work.lclpnet.mmocontent.client.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.item.ItemColors;
import work.lclpnet.mmocontent.asm.mixin.client.MinecraftClientAccessor;

public class ClientCommon {

    public static ItemColors getItemColors() {
        return ((MinecraftClientAccessor) MinecraftClient.getInstance()).getItemColors();
    }
}
