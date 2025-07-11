package mod.arcomit.surpass.mixin;

import mod.arcomit.surpass.Config;
import mods.flammpfeil.slashblade.event.handler.FallHandler;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(FallHandler.class)
public class FallHandlerMixin {
    @ModifyVariable(
            method = "fallDecrease(Lnet/minecraft/world/entity/LivingEntity;)V",
            at = @At(
                    value = "STORE",
                    target = "F"
            ),
            ordinal = 0,   // 第一个FLOAT类型变量存储
            remap = false
    )
    private static float overrideCurrentRatio(float original, LivingEntity user) {
        float currentRatio = user.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE).map((state) -> {
            float decRatio = state.getFallDecreaseRate();

            float newDecRatio = decRatio + ((float)Config.slowDownAttenuationValue);
            newDecRatio = Math.min(((float)Config.slowDownAttenuationMaxValue), newDecRatio);
            state.setFallDecreaseRate(newDecRatio);
            return decRatio;
        }).orElseGet(() -> 1.0f);
        return currentRatio;
    }
}
