package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.Material;
import net.minecraft.block.VineBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;

public class MMOVineBlock extends VineBlock implements IMMOBlock {

    public MMOVineBlock() {
        this(Settings.of(Material.REPLACEABLE_PLANT)
                .noCollision()
                .ticksRandomly()
                .strength(0.2F, 0.2F)
                .sounds(BlockSoundGroup.GRASS));
    }

    public MMOVineBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }
}
