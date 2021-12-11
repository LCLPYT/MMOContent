package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.AbstractBlock;

public class MMOGlassBlock extends MMOBlock {

    public MMOGlassBlock(AbstractBlock.Settings settings) {
        super(settings.nonOpaque()
                .allowsSpawning((state, world, pos, entityType) -> false)
                .solidBlock((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false));
    }
}
