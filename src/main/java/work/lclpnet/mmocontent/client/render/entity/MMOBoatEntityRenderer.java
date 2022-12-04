package work.lclpnet.mmocontent.client.render.entity;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import work.lclpnet.mmocontent.entity.MMOBoatEntity;
import work.lclpnet.mmocontent.entity.MMOBoatType;

import java.util.HashMap;
import java.util.Map;

/**
 * A modified version of {@link net.minecraft.client.render.entity.BoatEntityRenderer}.
 */
public class MMOBoatEntityRenderer extends EntityRenderer<MMOBoatEntity> {

    private final BoatEntityModel model;
    private final Map<MMOBoatType, Identifier> textures = new HashMap<>();

    public MMOBoatEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.shadowRadius = 0.8f;
        model = new BoatEntityModel(context.getPart(EntityModelLayers.createBoat(BoatEntity.Type.OAK)), false);
    }

    @Override
    public Identifier getTexture(MMOBoatEntity boatEntity) {
        MMOBoatType type = boatEntity.getMMOBoatType();
        if (type == null)  // return oak boat texture
            return new Identifier(String.format("textures/entity/boat/%s.png", BoatEntity.Type.OAK.getName()));

        return textures.computeIfAbsent(type, t -> {
            String path = String.format("textures/entity/boat/%s.png", t.identifier.getPath());
            return new Identifier(t.identifier.getNamespace(), path);
        });
    }

    @Override
    public void render(MMOBoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.translate(0.0, 0.375, 0.0);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - f));
        float h = (float)boatEntity.getDamageWobbleTicks() - g;
        float j = boatEntity.getDamageWobbleStrength() - g;
        if (j < 0.0f)
            j = 0.0f;

        if (h > 0.0f)
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.sin(h) * h * j / 10.0f * (float)boatEntity.getDamageWobbleSide()));

        if (!MathHelper.approximatelyEquals(boatEntity.interpolateBubbleWobble(g), 0.0f))
            matrixStack.multiply(new Quaternion(new Vec3f(1.0f, 0.0f, 1.0f), boatEntity.interpolateBubbleWobble(g), true));

        Identifier identifier = getTexture(boatEntity);
        matrixStack.scale(-1.0f, -1.0f, 1.0f);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0f));
        model.setAngles(boatEntity, g, 0.0f, -0.1f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(model.getLayer(identifier));
        model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);

        if (!boatEntity.isSubmergedInWater()) {
            VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
            model.getWaterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
        }

        matrixStack.pop();
        super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
