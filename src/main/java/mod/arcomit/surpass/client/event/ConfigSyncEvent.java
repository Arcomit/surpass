package mod.arcomit.surpass.client.event;

import mod.arcomit.surpass.Surpass;
import mod.arcomit.surpass.network.ConfigSyncPacket;
import mod.arcomit.surpass.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surpass.MODID, value = Dist.CLIENT)
public class ConfigSyncEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(ClientPlayerNetworkEvent.LoggingIn event) {
        ConfigSyncPacket packet = new ConfigSyncPacket();
        NetworkHandler.INSTANCE.sendToServer(packet);//同步配置到服务器
    }
}
