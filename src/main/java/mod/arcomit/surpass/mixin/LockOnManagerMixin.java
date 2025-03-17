package mod.arcomit.surpass.mixin;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.client.event.ConfigSyncEvent;
import mod.arcomit.surpass.event.ConfigPersistenceEvent;
import mod.arcomit.surpass.utils.FindEntityHelper;
import mods.flammpfeil.slashblade.ability.LockOnManager;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LockOnManager.class)
public abstract class LockOnManagerMixin {

    @Inject(
            method = "onInputChange(Lmods/flammpfeil/slashblade/event/InputCommandEvent;)V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void disableLockOnLogic(InputCommandEvent event, CallbackInfo ci) {
        ci.cancel();

        ServerPlayer player = event.getEntity();
        if (Config.enablesEnemyLockOnSwitch && !ConfigPersistenceEvent.getClientEnablesEnemyLockOn(player)) return;
        // set target
        ItemStack stack = event.getEntity().getMainHandItem();
        if (stack.isEmpty())
            return;
        if (!(stack.getItem() instanceof ItemSlashBlade))
            return;

        Entity targetEntity;

        if (event.getOld().contains(InputCommand.SNEAK) == event.getCurrent().contains(InputCommand.SNEAK))
            return;

        if ((event.getOld().contains(InputCommand.SNEAK) && !event.getCurrent().contains(InputCommand.SNEAK))) {
            // remove target
            targetEntity = null;
        } else {
            // search target
            targetEntity = FindEntityHelper.findClosestToCrosshair(player, Config.crosshairMaxAngle,Config.crosshairMaxDist);
            if (targetEntity == null) {
                targetEntity = FindEntityHelper.findNearestEntity(player,Config.crosshairMaxAngle);
            }

        }
        Entity finalTargetEntity = targetEntity;
        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {
            s.setTargetEntityId(finalTargetEntity);
        });

    }
}