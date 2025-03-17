package mod.arcomit.surpass.client;

import mod.arcomit.surpass.Surpass;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Surpass.MODID, bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public class ClientConfig {
    // client
    public static ForgeConfigSpec.BooleanValue ENABLES_ENEMY_LOCKON;

    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Auto-Targeting Setting");
        ENABLES_ENEMY_LOCKON = builder.comment("Enable the enemy lock-on?[Default:true]")
                .define("enablesEnemyLockOn", true);
        builder.pop();
        SPEC = builder.build();
    }
}
