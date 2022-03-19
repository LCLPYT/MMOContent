package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.WallSignBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.SignType;
import org.jetbrains.annotations.Nullable;

public class MMOWallSignBlock extends WallSignBlock implements IMMOBlock {

    public MMOWallSignBlock(Settings settings, SignType signType) {
        super(settings, signType);
    }

    @Override
    public @Nullable BlockItem provideBlockItem(Item.Settings settings) {
        return null;
    }
}
