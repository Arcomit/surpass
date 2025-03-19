package mod.arcomit.surpass.mixin;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.client.event.ConfigSyncEvent;
import mod.arcomit.surpass.event.ConfigPersistenceEvent;
import mod.arcomit.surpass.utils.FindEntityHelper;
import mods.flammpfeil.slashblade.ability.LockOnManager;
import mods.flammpfeil.slashblade.capability.inputstate.CapabilityInputState;
import mods.flammpfeil.slashblade.event.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.InputCommand;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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

    @Inject(
            method = "onEntityUpdate",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private void disableLockOnLogic(TickEvent.RenderTickEvent event, CallbackInfo ci) {
        ci.cancel();
        //我在写屎山，我讨厌Mixin
        if (event.phase != TickEvent.Phase.START)
            return;

        final Minecraft mcinstance = Minecraft.getInstance();
        if (mcinstance.player == null)
            return;

        LocalPlayer player = mcinstance.player;

        ItemStack stack = player.getMainHandItem();
        if (stack.isEmpty())
            return;
        if (!(stack.getItem() instanceof ItemSlashBlade))
            return;

        stack.getCapability(ItemSlashBlade.BLADESTATE).ifPresent(s -> {

            Entity target = s.getTargetEntity(player.level());

            if (target == null)
                return;
            if (!target.isAlive())
                return;

            LivingEntity entity = player;

            if (!entity.level().isClientSide())
                return;
            if (!entity.getCapability(CapabilityInputState.INPUT_STATE)
                    .filter(input -> input.getCommands().contains(InputCommand.SNEAK)).isPresent())
                return;

            float partialTicks = mcinstance.getFrameTime();

            float oldYawHead = entity.yHeadRot;
            float oldYawOffset = entity.yBodyRot;
            float oldPitch = entity.getXRot();
            float oldYaw = entity.getYRot();

            float prevYawHead = entity.yHeadRotO;
            float prevYawOffset = entity.yBodyRotO;
            float prevYaw = entity.yRotO;
            float prevPitch = entity.xRotO;

            entity.lookAt(EntityAnchorArgument.Anchor.EYES, target.position().add(0, target.getEyeHeight() / 2.0, 0));

            float step = (float) (0.125f * Config.targetLockRotationSpeed * partialTicks);

            step *= Math.min(1.0f, Math.abs(Mth.wrapDegrees(oldYaw - entity.yHeadRot) * 0.5));

            entity.setXRot(Mth.rotLerp(step, oldPitch, entity.getXRot()));
            entity.setYRot(Mth.rotLerp(step, oldYaw, entity.getYRot()));
            entity.setYHeadRot(Mth.rotLerp(step, oldYawHead, entity.getYHeadRot()));

            entity.yBodyRot = oldYawOffset;

            entity.yBodyRotO = prevYawOffset;
            entity.yHeadRotO = prevYawHead;
            entity.yRotO = prevYaw;
            entity.xRotO = prevPitch;
        });

    }
}