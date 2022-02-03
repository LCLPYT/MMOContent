package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.DoorBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class MMODoorBlock extends DoorBlock implements IMMOBlock {

    public MMODoorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
