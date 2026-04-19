package net.nanaky.horseshoes.client;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EquineRenderState;

public class HorseShoesDonkeyModel extends HorseShoesModel {

    private static final float DONKEY_Y_OFFSET       = -1.6F;
    private static final float DONKEY_X_INSET        =  1.4F;
    private static final float DONKEY_FRONT_Z_OFFSET =  1.75F;
    private static final float DONKEY_HIND_Z_OFFSET  = -1.5F;

    public HorseShoesDonkeyModel() {
        super(createDonkeyLayer().bakeRoot());
    }

    @Override
    public void setupAnim(EquineRenderState state) {
        if (pendingSync == null) return;

        ModelPart lfl = pendingSync.horseshoes$getLeftFrontLeg();
        ModelPart rfl = pendingSync.horseshoes$getRightFrontLeg();
        ModelPart lhl = pendingSync.horseshoes$getLeftHindLeg();
        ModelPart rhl = pendingSync.horseshoes$getRightHindLeg();

        this.leftFrontShoe.xRot    = lfl.xRot;
        this.leftFrontShoe.yRot    = lfl.yRot;
        this.leftFrontShoe.zRot    = lfl.zRot;
        this.leftFrontShoe.visible = lfl.visible;

        this.rightFrontShoe.xRot    = rfl.xRot;
        this.rightFrontShoe.yRot    = rfl.yRot;
        this.rightFrontShoe.zRot    = rfl.zRot;
        this.rightFrontShoe.visible = rfl.visible;

        this.leftHindShoe.xRot    = lhl.xRot;
        this.leftHindShoe.yRot    = lhl.yRot;
        this.leftHindShoe.zRot    = lhl.zRot;
        this.leftHindShoe.visible = lhl.visible;

        this.rightHindShoe.xRot    = rhl.xRot;
        this.rightHindShoe.yRot    = rhl.yRot;
        this.rightHindShoe.zRot    = rhl.zRot;
        this.rightHindShoe.visible = rhl.visible;

        float standing = state.standAnimation;
        float yShift = LEG_STANDING_Y_OFFSET * standing;
        float zShift = LEG_STANDING_Z_OFFSET * standing;

        this.leftFrontShoe.x  = -DONKEY_X_INSET;
        this.leftFrontShoe.y  = FRONT_LEG_REST_Y + DONKEY_Y_OFFSET - yShift;
        this.leftFrontShoe.z  = FRONT_LEG_REST_Z + FRONT_Z + DONKEY_FRONT_Z_OFFSET + zShift;

        this.rightFrontShoe.x =  DONKEY_X_INSET;
        this.rightFrontShoe.y = FRONT_LEG_REST_Y + DONKEY_Y_OFFSET - yShift;
        this.rightFrontShoe.z = FRONT_LEG_REST_Z + FRONT_Z + DONKEY_FRONT_Z_OFFSET + zShift;

        this.leftHindShoe.x  = -DONKEY_X_INSET;
        this.leftHindShoe.y  = HIND_LEG_REST_Y + DONKEY_Y_OFFSET - yShift;
        this.leftHindShoe.z  = HIND_LEG_REST_Z + HIND_Z + DONKEY_HIND_Z_OFFSET - zShift;

        this.rightHindShoe.x =  DONKEY_X_INSET;
        this.rightHindShoe.y = HIND_LEG_REST_Y + DONKEY_Y_OFFSET - yShift;
        this.rightHindShoe.z = HIND_LEG_REST_Z + HIND_Z + DONKEY_HIND_Z_OFFSET - zShift;

        this.pendingSync = null;
    }

    public static LayerDefinition createDonkeyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeDeformation deform = new CubeDeformation(0.57F);
        float shift = 0.25F;

        root.addOrReplaceChild("left_front_shoe",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F + shift + 0.5F, 10F, -1.9F + 0.5F, 3, 1, 3, deform),
                PartPose.offset(5.0F, 14.0F, -10.0F));
        root.addOrReplaceChild("right_front_shoe",
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-1.0F - shift + 0.5F, 10F, -1.9F + 0.5F, 3, 1, 3, deform),
                PartPose.offset(-5.0F, 14.0F, -10.0F));
        root.addOrReplaceChild("left_hind_shoe",
                CubeListBuilder.create().texOffs(16, 0)
                        .addBox(-3.0F + shift + 0.5F, 10F, -1.0F + 0.5F, 3, 1, 3, deform),
                PartPose.offset(5.0F, 14.0F, 7.0F));
        root.addOrReplaceChild("right_hind_shoe",
                CubeListBuilder.create().texOffs(16, 6)
                        .addBox(-1.0F - shift + 0.5F, 10F, -1.0F + 0.5F, 3, 1, 3, deform),
                PartPose.offset(-5.0F, 14.0F, 7.0F));

        return LayerDefinition.create(mesh, 32, 16);
    }
}