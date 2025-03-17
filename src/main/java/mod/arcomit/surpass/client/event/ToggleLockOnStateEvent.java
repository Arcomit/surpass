package mod.arcomit.surpass.client.event;

import mod.arcomit.surpass.Surpass;
import mod.arcomit.surpass.client.ClientConfig;
import mod.arcomit.surpass.client.registry.Keys;
import mod.arcomit.surpass.network.ConfigSyncPacket;
import mod.arcomit.surpass.network.NetworkHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surpass.MODID, value = Dist.CLIENT)
public class ToggleLockOnStateEvent {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;

        while(Keys.TOGGLE_ENEMY_LOCKON_ON_OFF.consumeClick()) {
            ClientConfig.ENABLES_ENEMY_LOCKON.set(!ClientConfig.ENABLES_ENEMY_LOCKON.get());
            ClientConfig.ENABLES_ENEMY_LOCKON.save();
            ConfigSyncPacket packet = new ConfigSyncPacket();
            NetworkHandler.INSTANCE.sendToServer(packet);//同步配置到服务器
        }
    }
}
