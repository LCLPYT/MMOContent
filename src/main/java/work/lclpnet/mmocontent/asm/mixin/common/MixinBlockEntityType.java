package work.lclpnet.mmocontent.asm.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.HashSet;
import java.util.Set;

@Mixin(BlockEntityType.Builder.class)
public class MixinBlockEntityType {

    @ModifyArg(
            method = "build",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/entity/BlockEntityType;<init>(Lnet/minecraft/block/entity/BlockEntityType$BlockEntityFactory;Ljava/util/Set;Lcom/mojang/datafixers/types/Type;)V"
            ),
            index = 1
    )
    public Set<Block> adjustBlocks(Set<Block> blocks) {
        // make sign block list mutable
        if (blocks.contains(Blocks.OAK_SIGN)) return new HashSet<>(blocks);

        return blocks;
    }
}
