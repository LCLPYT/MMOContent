package work.lclpnet.mmocontent.asm.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import work.lclpnet.mmocontent.client.render.LateBindingRenderLayers;

import java.util.Map;

@Mixin(RenderLayers.class)
public class MixinRenderLayers {

    @Final
    @Shadow
    private static Map<Block, RenderLayer> BLOCKS;

    @Inject(method = "<clinit>*", at = @At("RETURN"))
    private static void onInitialize(CallbackInfo info) {
        LateBindingRenderLayers.init(BLOCKS::put);
    }
}
