package work.lclpnet.mmocontent.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class BlockStatesUtil {

    public static AbstractBlock.Settings copyState(Block parent) {
        AbstractBlock.Settings props = AbstractBlock.Settings.copy(parent);
        if (parent instanceof IVariantsShouldBeEmissive) props = props.emissiveLighting((s, r, p) -> true);
        return props;
    }

    public interface IVariantsShouldBeEmissive {}
}
