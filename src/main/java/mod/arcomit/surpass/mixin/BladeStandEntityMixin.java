package mod.arcomit.surpass.mixin;

import mod.arcomit.surpass.Config;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin to prevent owned blades from being taken from blade stands by non-owners
 */
@Mixin(BladeStandEntity.class)
public abstract class BladeStandEntityMixin {

    @Inject(
            method = "interact",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void preventOwnerBladeRemoval(@NotNull Player player, @NotNull InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (!Config.enablesBindOwner) {
            return;
        }
        if (player.level().isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }

        BladeStandEntity bladeStand = (BladeStandEntity)(Object)this;
        ItemStack displayedBlade = bladeStand.getItem();

        if (displayedBlade.isEmpty()) {
            return;
        }

        var tag = displayedBlade.getTag();
        if (tag == null || !tag.contains("Surpass.Owner")) {
            return;
        }

        String ownerName = tag.getString("Surpass.Owner");
        String playerName = player.getName().getString();

        if (!playerName.equals(ownerName)) {
                player.hurt(player.damageSources().magic(), 1.0F);
                player.displayClientMessage(
                        Component.translatable("surpass.blade.owner.cannot_take")
                                .withStyle(ChatFormatting.RED),
                        true
                );
                cir.setReturnValue(InteractionResult.FAIL);
        }
    }
}

