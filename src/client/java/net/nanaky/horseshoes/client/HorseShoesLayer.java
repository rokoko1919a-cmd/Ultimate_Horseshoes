package net.nanaky.horseshoes.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.nanaky.horseshoes.items.HorseshoesItem;
import net.nanaky.horseshoes.mixin.AbstractEquineModelAccessor;
import net.minecraft.client.model.animal.equine.AbstractEquineModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.EquineRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

public class HorseShoesLayer<S extends EquineRenderState>
        extends RenderLayer<S, AbstractEquineModel<S>> {

    private final HorseShoesModel shoesModel;

    public HorseShoesLayer(RenderLayerParent<S, ?> parent, HorseShoesModel shoesModel) {
        super((RenderLayerParent<S, AbstractEquineModel<S>>) parent);
        this.shoesModel = shoesModel;
    }


    
    @Override
    public void submit(PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector,
                       int packedLight,
                       S renderState,
                       float yRot,
                       float xRot) {
        if (renderState.isInvisible) return;

        ItemStack shoes = ((HorseShoesRenderStateAccess) renderState).horseshoes$getHorseshoeItem();
        if (shoes.isEmpty() || !(shoes.getItem() instanceof HorseshoesItem horseshoesItem)) return;

        Identifier texture = horseshoesItem.getEntityTexture();
        AbstractEquineModelAccessor equineModel =
                (AbstractEquineModelAccessor) this.getParentModel();
        this.shoesModel.queueSync(equineModel);

        submitNodeCollector.order(2)
        .<S>submitModel(
                this.shoesModel,
                renderState,
                poseStack,
                RenderTypes.entityCutout(texture),
                packedLight,
                LivingEntityRenderer.getOverlayCoords(renderState, 0.0F),
                -1,        // color (-1 = white/no tint)
                null,      // TextureAtlasSprite
                renderState.outlineColor,
                null       // CrumblingOverlay
        );
    }
}