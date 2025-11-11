package mod.arcomit.surpass.client.event;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.Surpass;
import mod.arcomit.surpass.client.ClientConfig;
import mod.arcomit.surpass.client.registry.Keys;
import mod.arcomit.surpass.network.ConfigSyncPacket;
import mod.arcomit.surpass.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surpass.MODID, value = Dist.CLIENT)
public class ToggleSneakingNoBackOffEvent {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;

        while(Keys.TOGGLE_SNEAKING_NO_BACKOFF_ON_OFF.consumeClick()) {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;
            if (!Config.enablesSneakingNoBackOffSwitch){
                player.displayClientMessage(
                        Component.translatable("surpass.sneaking_no_backoff_switch",
                                Component.translatable("surpass.disable")),
                        true
                );
                return;
            }

            ClientConfig.ENABLES_SNEAKING_NO_BACKOFF.set(!ClientConfig.ENABLES_SNEAKING_NO_BACKOFF.get());
            ClientConfig.ENABLES_SNEAKING_NO_BACKOFF.save();
            ConfigSyncPacket packet = new ConfigSyncPacket();
            NetworkHandler.INSTANCE.sendToServer(packet);//同步配置到服务器

            player.displayClientMessage(
                    Component.translatable("surpass.sneaking_no_backoff_switch",
                            Component.translatable(ClientConfig.ENABLES_SNEAKING_NO_BACKOFF.get() ?
                                    "surpass.on" : "surpass.off")),
                    true
            );
        }
    }
}
