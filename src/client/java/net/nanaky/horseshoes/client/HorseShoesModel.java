package net.nanaky.horseshoes.client;

import net.nanaky.horseshoes.mixin.AbstractEquineModelAccessor;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EquineRenderState;

public class HorseShoesModel extends EntityModel<EquineRenderState> {
    private static final String LEFT_FRONT_SHOE  = "left_front_shoe";
    private static final String RIGHT_FRONT_SHOE = "right_front_shoe";
    private static final String LEFT_HIND_SHOE   = "left_hind_shoe";
    private static final String RIGHT_HIND_SHOE  = "right_hind_shoe";

    protected static final float FRONT_Z = -0.875F;
    protected static final float HIND_Z  =  0.8125F;

    // Vanilla leg rest poses — must match AbstractEquineModel PartPose exactly
    protected static final float FRONT_LEG_REST_Y = 14.0F;
    protected static final float FRONT_LEG_REST_Z = -10.0F;
    protected static final float HIND_LEG_REST_Y  = 14.0F;
    protected static final float HIND_LEG_REST_Z  =  7.0F;

    // Vanilla standing animation constants — from AbstractEquineModel
    protected static final float LEG_STANDING_Y_OFFSET = 12.0F;
    protected static final float LEG_STANDING_Z_OFFSET =  4.0F;

    protected AbstractEquineModelAccessor pendingSync = null;

    protected final ModelPart leftFrontShoe;
    protected final ModelPart rightFrontShoe;
    protected final ModelPart leftHindShoe;
    protected final ModelPart rightHindShoe;

    public HorseShoesModel(ModelPart root) {
        super(root);
        this.leftFrontShoe  = root.getChild("left_front_shoe");
        this.rightFrontShoe = root.getChild("right_front_shoe");
        this.leftHindShoe   = root.getChild("left_hind_shoe");
        this.rightHindShoe  = root.getChild("right_hind_shoe");
    }

    public HorseShoesModel() {
        this(createLayer().bakeRoot());
    }

    public void queueSync(AbstractEquineModelAccessor horseModel) {
        this.pendingSync = horseModel;
    }

    @Override
    public void setupAnim(EquineRenderState state) {
        if (pendingSync == null) return;

        ModelPart lfl = pendingSync.horseshoes$getLeftFrontLeg();
        ModelPart rfl = pendingSync.horseshoes$getRightFrontLeg();
        ModelPart lhl = pendingSync.horseshoes$getLeftHindLeg();
        ModelPart rhl = pendingSync.horseshoes$getRightHindLeg();

        // Rotations — set fresh by vanilla every frame, reliable on server
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

        // Position computed purely from state — no ModelPart y/z reading at all
        // This is identical on LAN and dedicated server
        float standing = state.standAnimation;
        float yShift = LEG_STANDING_Y_OFFSET * standing;
        float zShift = LEG_STANDING_Z_OFFSET * standing;

        this.leftFrontShoe.y  = FRONT_LEG_REST_Y - yShift;
        this.leftFrontShoe.z  = FRONT_LEG_REST_Z + FRONT_Z + zShift;
        this.rightFrontShoe.y = FRONT_LEG_REST_Y - yShift;
        this.rightFrontShoe.z = FRONT_LEG_REST_Z + FRONT_Z + zShift;

        this.leftHindShoe.y  = HIND_LEG_REST_Y;
        this.leftHindShoe.z  = HIND_LEG_REST_Z + HIND_Z;
        this.rightHindShoe.y = HIND_LEG_REST_Y;
        this.rightHindShoe.z = HIND_LEG_REST_Z + HIND_Z;

        this.pendingSync = null;
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        CubeDeformation deform = new CubeDeformation(0.57F);
        float shift = 0.25F;

        // Original geometry — untouched
        root.addOrReplaceChild(LEFT_FRONT_SHOE,
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-3.0F + shift, 8.0F, -1.9F, 4, 2, 4, deform),
                PartPose.offset(4.0F, 14.0F, -10.0F));
        root.addOrReplaceChild(RIGHT_FRONT_SHOE,
                CubeListBuilder.create().texOffs(0, 6)
                        .addBox(-1.0F - shift, 8.0F, -1.9F, 4, 2, 4, deform),
                PartPose.offset(-4.0F, 14.0F, -10.0F));
        root.addOrReplaceChild(LEFT_HIND_SHOE,
                CubeListBuilder.create().texOffs(16, 0)
                        .addBox(-3.0F + shift, 8.0F, -1.0F, 4, 2, 4, deform),
                PartPose.offset(4.0F, 14.0F, 7.0F));
        root.addOrReplaceChild(RIGHT_HIND_SHOE,
                CubeListBuilder.create().texOffs(16, 6)
                        .addBox(-1.0F - shift, 8.0F, -1.0F, 4, 2, 4, deform),
                PartPose.offset(-4.0F, 14.0F, 7.0F));

        return LayerDefinition.create(mesh, 32, 16);
    }
}