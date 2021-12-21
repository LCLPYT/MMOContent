package work.lclpnet.mmocontent.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.color.item.ItemColors;

public interface ItemColorCallback {

    Event<ItemColorCallback> EVENT = EventFactory.createArrayBacked(ItemColorCallback.class,
            listeners -> itemColorMap -> {
                for (ItemColorCallback listener : listeners)
                    listener.register(itemColorMap);
            });

    void register(ItemColors itemColorMap);
}
