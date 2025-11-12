package mod.arcomit.surpass.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.client.ClientConfig;
import mods.flammpfeil.slashblade.client.renderer.model.BladeFirstPersonRender;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Constant;

@Mixin(value = BladeFirstPersonRender.class)
public abstract class BladeFirstPersonRenderMixin {

    @ModifyConstant(
            method = "render",
            constant = @Constant(floatValue = -0.5F),
            remap = false
    )
    private float modifyTranslateZ(float original) {
        return !ClientConfig.ENABLES_GENERATION1_FIRST_PERSON_RENDER.get() ? original : -0.7F;
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionf;)V",
                    ordinal = 1
            )
    )
    private void cancelXRotation(PoseStack poseStack, Quaternionf quaternion) {
        // 如果配置未开启，则应用原始的旋转
        if (!ClientConfig.ENABLES_GENERATION1_FIRST_PERSON_RENDER.get()) {
            poseStack.mulPose(quaternion);
        }
    }
}

