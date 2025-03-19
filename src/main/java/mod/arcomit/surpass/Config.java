package mod.arcomit.surpass;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Surpass.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    // common
    private static final ForgeConfigSpec.BooleanValue ENABLES_ENEMY_LOCKON_SWITCH;
    private static final ForgeConfigSpec.DoubleValue CROSSHAIR_MAX_DIST;
    private static final ForgeConfigSpec.DoubleValue CROSSHAIR_MAX_ANGLE;
    private static final ForgeConfigSpec.DoubleValue NEAREST_MAX_DIST;
    private static final ForgeConfigSpec.DoubleValue LOCKON_ROTATION_SPEED;

    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.push("Auto-Targeting Setting");
        // 开启索敌切换功能？
        ENABLES_ENEMY_LOCKON_SWITCH = builder.comment("Enable the enemy lock-on switch?[Default:true]")
                .define("enablesEnemyLockOnSwitch", true);
        // 准星索敌距离
        CROSSHAIR_MAX_DIST = builder.comment("The maximum distance of the crosshair lock-on.[Default:100.0,Original Resharped:40.0]")
                .defineInRange("crosshairMaxDist", 100.0D,0.0D,200.0D);
        // 准星索敌辅助角度
        CROSSHAIR_MAX_ANGLE = builder.comment("The maximum angle of the crosshair lock-on.[Default:10.0,Original Resharped:0.0]")
                .defineInRange("crosshairMaxAngle", 10.0d,0.0D,180.0D);
        // 最近索敌距离
        NEAREST_MAX_DIST = builder.comment("The maximum distance of the nearest lock-on.[Default:32.0,Original Resharped:16.0]")
                .defineInRange("nearestMaxDist", 32.0d,0.0D,200.0D);
        // 最近索敌距离
        LOCKON_ROTATION_SPEED = builder.comment("The rotation speed of the lock-on.[Default:1.0]")
                .defineInRange("nearestMaxDist", 1.0d,0.0D,8.0D);
        builder.pop();
        SPEC = builder.build();
    }

    public static boolean enablesEnemyLockOnSwitch;
    public static double crosshairMaxDist;
    public static double crosshairMaxAngle;
    public static double nearestMaxDist;
    public static double targetLockRotationSpeed;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        enablesEnemyLockOnSwitch = ENABLES_ENEMY_LOCKON_SWITCH.get();
        crosshairMaxDist = CROSSHAIR_MAX_DIST.get();
        crosshairMaxAngle = CROSSHAIR_MAX_ANGLE.get();
        nearestMaxDist = NEAREST_MAX_DIST.get();
        targetLockRotationSpeed = LOCKON_ROTATION_SPEED.get();
    }
}
