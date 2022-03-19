package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.SignBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.SignType;

import javax.annotation.Nullable;

public class MMOSignBlock extends SignBlock implements IMMOBlock {

    public MMOSignBlock(Settings settings, SignType signType) {
        super(settings, signType);
    }

    @Override
    public @Nullable BlockItem provideBlockItem(Item.Settings settings) {
        return null;
    }
}
