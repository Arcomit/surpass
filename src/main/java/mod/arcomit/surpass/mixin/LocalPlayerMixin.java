package mod.arcomit.surpass.mixin;

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

/**
 * Mixin to enable omnidirectional sprinting
 * @author Arcomit
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {

    @Redirect(method = "aiStep", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"))
    private boolean redirectIsUsingItemInAiStep(LocalPlayer player) {
        if (Config.enablesBladeNoSlowDown && player.getMainHandItem().getItem() instanceof ItemSlashBlade) {
            return false;
        }
        return player.isUsingItem();
    }
}

