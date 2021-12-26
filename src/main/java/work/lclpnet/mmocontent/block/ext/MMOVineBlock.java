package work.lclpnet.mmocontent.block.ext;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.VineBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class MMOVineBlock extends VineBlock implements IMMOBlock {

    public MMOVineBlock() {
        super(Settings.of(Material.REPLACEABLE_PLANT)
                .noCollision()
                .ticksRandomly()
                .strength(0.2F, 0.2F)
                .sounds(BlockSoundGroup.GRASS));
    }

    @Override
    public BlockItem provideBlockItem(Item.Settings settings) {
        return new BlockItem(this, settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        scheduledTick(state, world, pos, random);
    }
}
