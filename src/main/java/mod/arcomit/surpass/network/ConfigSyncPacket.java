package mod.arcomit.surpass.network;

import mod.arcomit.surpass.client.ClientConfig;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ConfigSyncPacket {

    private boolean enablesEnemyLockOn;

    public ConfigSyncPacket() {
        enablesEnemyLockOn = ClientConfig.ENABLES_ENEMY_LOCKON.get();
    }

    public boolean getEnablesEnemyLockOn() {
        return enablesEnemyLockOn;
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBoolean(enablesEnemyLockOn);
    }

    public static ConfigSyncPacket decode(FriendlyByteBuf buffer) {
        return new ConfigSyncPacket();
    }

    // 处理器类
    public static class Handler {
        public static void handle(ConfigSyncPacket msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                // 服务端接收处理逻辑
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    // 存储到玩家的持久化数据中
                    player.getPersistentData().putBoolean("enablesEnemyLockOn", msg.getEnablesEnemyLockOn());
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}

