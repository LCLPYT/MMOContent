package work.lclpnet.mmocontent.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.color.block.BlockColors;

public interface BlockColorCallback {

    Event<BlockColorCallback> EVENT = EventFactory.createArrayBacked(BlockColorCallback.class,
            listeners -> blockColorMap -> {
                for (BlockColorCallback listener : listeners)
                    listener.register(blockColorMap);
            });

    void register(BlockColors blockColorMap);
}
