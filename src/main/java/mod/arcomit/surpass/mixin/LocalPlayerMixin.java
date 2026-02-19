package mod.arcomit.surpass.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import mod.arcomit.surpass.Config;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @WrapOperation(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
            )
    )
    private boolean surpass$wrapIsUsingItemInAiStep(LocalPlayer player, Operation<Boolean> original) {
        boolean originalResult = original.call(player);

        if (Config.enablesBladeNoSlowDown && originalResult) {
            if (player != null && player.getMainHandItem() != null) {
                if (player.getMainHandItem().getItem() instanceof ItemSlashBlade) {
                    return false;
                }
            }
        }

        return originalResult;
    }
}

