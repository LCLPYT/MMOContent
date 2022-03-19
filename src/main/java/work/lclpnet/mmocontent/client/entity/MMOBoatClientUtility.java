package work.lclpnet.mmocontent.client.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.BoatEntity;
import work.lclpnet.mmocontent.entity.MMOBoatEntity;

@Environment(EnvType.CLIENT)
public class MMOBoatClientUtility {

    private static boolean clientInitialized = false;

    /**
     * By default, the MMOBoatEntity entity disabled.
     * Call this method while initialization to enable.
     */
    public static void enableMMOBoatClientIntegration() {
        if (clientInitialized) return;

        if (MMOBoatEntity.boatEntityType == null)
            throw new IllegalStateException("MMOBoatEntity should be enabled before enabling it on the client");

        EntityRendererRegistry.register(MMOBoatEntity.boatEntityType, BoatEntityRenderer::new);

        @SuppressWarnings("unchecked")
        MMOClientEntities.EntityFactory<MMOBoatEntity> factory = (type, world) -> new MMOBoatEntity((EntityType<? extends BoatEntity>) type, world);
        MMOClientEntities.registerNonLiving(MMOBoatEntity.boatEntityType, factory);

        clientInitialized = true;
    }
}
