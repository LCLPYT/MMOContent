package work.lclpnet.mmocontent.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LateBindingRenderLayers {

    private static final Set<Consumer<BiConsumer<Block, RenderLayer>>> lateBinders = new HashSet<>();

    public static void bindLate(Consumer<BiConsumer<Block, RenderLayer>> lateBinder) {
        lateBinders.add(lateBinder);
    }

    public static void init(BiConsumer<Block, RenderLayer> binder) {
        lateBinders.forEach(lateBinder -> lateBinder.accept(binder));
        lateBinders.clear(); // free binders for garbage collection
    }
}
