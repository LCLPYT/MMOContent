package work.lclpnet.mmocontent.util;

import net.minecraft.block.Block;

import java.util.HashMap;
import java.util.Map;

public class MMOUtil {

    private static final Map<Block, Block> strippedBlocks = new HashMap<>();

    public static Block getStrippedBlock(Block baseBlock) {
        return strippedBlocks.get(baseBlock);
    }

    public static void registerStrippedBlock(Block baseBlock, Block stripped) {
        strippedBlocks.put(baseBlock, stripped);
    }
}
