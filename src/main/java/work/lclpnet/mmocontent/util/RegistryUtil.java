package work.lclpnet.mmocontent.util;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;

public class RegistryUtil {

    @Nullable
    public static String getRegistryPath(Block block) {
        Identifier key = Registry.BLOCK.getId(block);
        String path = key.getPath();

        // check if block is in registry
        if (!block.equals(Blocks.AIR) && path.equals("air") && key.getNamespace().equals("minecraft")) return null;
        else return path;
    }
}
