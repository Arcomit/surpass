package mod.arcomit.surpass.client.event;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.Surpass;
import mod.arcomit.surpass.client.ClientConfig;
import mod.arcomit.surpass.client.registry.Keys;
import mod.arcomit.surpass.network.ConfigSyncPacket;
import mod.arcomit.surpass.network.InterruptCombosPacket;
import mod.arcomit.surpass.network.NetworkHandler;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Surpass.MODID, value = Dist.CLIENT)
public class InterruptCombosEvent {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if(event.phase != TickEvent.Phase.END) return;
        Player player = Minecraft.getInstance().player;
        if(player == null) return;
        if(!Config.enablesInterruptCombos){
            player.displayClientMessage(
                    Component.translatable("surpass.interrupt_combos",
                            Component.translatable("surpass.disable")),
                    true
            );
            return;
        }

        while(Keys.INTERRUPT_COMBOS.consumeClick()) {
            ItemStack stack = player.getMainHandItem();
            if (stack.getItem() instanceof ItemSlashBlade){
                InterruptCombosPacket packet = new InterruptCombosPacket();
                NetworkHandler.INSTANCE.sendToServer(packet);
            }
        }
    }
}
