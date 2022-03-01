package work.lclpnet.mmocontent.asm.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import work.lclpnet.mmocontent.util.MMOUtil;

@Mixin(AxeItem.class)
public class MixinAxeItem {

    @Inject(
            method = "useOnBlock",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/AxeItem;getStrippedState(Lnet/minecraft/block/BlockState;)Ljava/util/Optional;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            cancellable = true
    )
    public void onStripLog(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir, World world,
                           BlockPos blockPos, PlayerEntity playerEntity, BlockState blockState) {
        Block block = MMOUtil.getStrippedBlock(blockState.getBlock());
        if (block == null) return;

        world.playSound(playerEntity, blockPos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!world.isClient) {
            world.setBlockState(blockPos, block.getDefaultState().with(PillarBlock.AXIS, blockState.get(PillarBlock.AXIS)), 11);
            if (playerEntity != null)
                context.getStack().damage(1, (LivingEntity) playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
        }

        cir.setReturnValue(ActionResult.success(world.isClient));
    }
}
