package mod.arcomit.surpass.mixin;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.client.ClientConfig;
import mod.arcomit.surpass.event.ConfigPersistenceEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 禁用玩家潜行时的边缘保护机制，允许玩家从边缘掉落
 */
@Mixin(Player.class)
public class PlayerMixin {

    @Inject(
            method = "maybeBackOffFromEdge",
            at = @At("HEAD"),
            cancellable = true
    )
    protected void disableSneakEdgeProtection(Vec3 movement, MoverType moverType, CallbackInfoReturnable<Vec3> cir) {
        Player player = (Player) (Object) this;
        if (player instanceof ServerPlayer serverPlayer){
            if (!Config.enablesSneakingNoBackOffSwitch || !ConfigPersistenceEvent.getClientEnablesSneakingNoBackOff(serverPlayer)) return;
        }else {
            if (!Config.enablesSneakingNoBackOffSwitch || !ClientConfig.ENABLES_SNEAKING_NO_BACKOFF.get()) return;
        }

        if (player.isShiftKeyDown() && player.getMainHandItem().getItem() instanceof ItemSlashBlade) {
            cir.setReturnValue(movement);
        }
    }
}

