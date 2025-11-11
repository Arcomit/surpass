package mod.arcomit.surpass.event;

import mod.arcomit.surpass.Surpass;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surpass.MODID)
public class ConfigPersistenceEvent {
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            CompoundTag oldData = event.getOriginal().getPersistentData();
            CompoundTag newData = event.getEntity().getPersistentData();
            // 将旧数据复制到新实体
            newData.put("enablesEnemyLockOn", oldData.get("enablesEnemyLockOn"));
            newData.put("enablesSneakingNoBackOff", oldData.get("enablesSneakingNoBackOff"));
        }
    }

    public static boolean getClientEnablesEnemyLockOn(ServerPlayer player) {
        return player.getPersistentData().getBoolean("enablesEnemyLockOn");
    }

    public static boolean getClientEnablesSneakingNoBackOff(ServerPlayer player) {
        return player.getPersistentData().getBoolean("enablesSneakingNoBackOff");
    }
}
