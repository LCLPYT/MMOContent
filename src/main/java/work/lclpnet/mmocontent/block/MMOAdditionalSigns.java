package work.lclpnet.mmocontent.block;

import net.minecraft.block.Block;
import net.minecraft.block.SignBlock;
import net.minecraft.block.WallSignBlock;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.SignType;
import work.lclpnet.mmocontent.asm.mixin.common.BlockEntityTypeAccessor;
import work.lclpnet.mmocontent.asm.mixin.common.SignTypeAccessor;
import work.lclpnet.mmocontent.util.IdentifierProvider;

import java.util.Set;

public class MMOAdditionalSigns {

    public static void registerAdditionalSign(SignBlock standing, WallSignBlock wall) {
        Set<Block> blocks = ((BlockEntityTypeAccessor) BlockEntityType.SIGN).getBlocks();
        // blocks is made mutable in MixinBlockEntityType for SIGN

        blocks.add(standing);
        blocks.add(wall);
    }

    public static SignType registerSignType(String name, IdentifierProvider identifierProvider) {
        return SignTypeAccessor.invokeRegister(new MMOSignType(identifierProvider.identifier(name)));
    }
}
