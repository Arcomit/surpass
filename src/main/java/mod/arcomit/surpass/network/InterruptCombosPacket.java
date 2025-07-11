package mod.arcomit.surpass.network;

import mod.arcomit.surpass.client.ClientConfig;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InterruptCombosPacket {

    public InterruptCombosPacket() {
    }

    public void encode(FriendlyByteBuf buffer) {}

    public static InterruptCombosPacket decode(FriendlyByteBuf buffer) {
        return new InterruptCombosPacket();
    }

    // 处理器类
    public static class Handler {
        public static void handle(InterruptCombosPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                // 服务端接收处理逻辑
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    player.getMainHandItem().getCapability(ItemSlashBlade.BLADESTATE)
                            .ifPresent(state -> state.updateComboSeq(player, state.getComboRoot()));
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
