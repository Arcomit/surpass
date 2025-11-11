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
    private static final ForgeConfigSpec.BooleanValue ENABLES_AIR_TRICK_DODGE;
    private static final ForgeConfigSpec.DoubleValue AIR_TRICK_DODGE_SPEED_MULTIPLIER;
    private static final ForgeConfigSpec.BooleanValue ENABLES_INTERRUPT_COMBOS;
    private static final ForgeConfigSpec.DoubleValue SLOW_DOWN_ATTENUATION_VALUE;
    private static final ForgeConfigSpec.DoubleValue SLOW_DOWN_ATTENUATION_MAX_VALUE;
    private static final ForgeConfigSpec.BooleanValue ENABLES_FORTUNE_DROP;
    private static final ForgeConfigSpec.BooleanValue ENABLES_CHANGE_CARRY_TYPE;
    private static final ForgeConfigSpec.BooleanValue ENABLES_BIND_OWNER;
    private static final ForgeConfigSpec.BooleanValue ENABLES_S_RANK_BOOST;
    private static final ForgeConfigSpec.BooleanValue ENABLES_SNEAKING_NO_BACKOFF_SWITCH;
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
        // 索敌转视角速度
        LOCKON_ROTATION_SPEED = builder.comment("The rotation speed of the lock-on.[Default:1.0]")
                .defineInRange("lockonRotationSpeed", 1.0d,0.0D,8.0D);
        // 开启空中闪避功能？
        ENABLES_AIR_TRICK_DODGE = builder.comment("Enable air trick-dodge?[Default:true]")
                .define("enablesAirTrickDodge", true);
        // 空中闪避速度调整系数
        AIR_TRICK_DODGE_SPEED_MULTIPLIER = builder.comment("The speed multiplier of air trick-dodge.[Default:0.5]")
                .defineInRange("airTrickDodgeSpeedMultiplier", 0.5d,0.0D,1.0D);
        // 开启打断连招功能？
        ENABLES_INTERRUPT_COMBOS = builder.comment("Enable interrupt combos?[Default:true]")
                .define("enablesInterruptCombos", true);
        // 每次缓降衰减的值
        SLOW_DOWN_ATTENUATION_VALUE = builder.comment("Slow down attenuation value.[Default:0.05]")
                .defineInRange("slowDownAttenuationValue", 0.05d,0.0D,1.0D);
        // 缓降衰减最大值
        SLOW_DOWN_ATTENUATION_MAX_VALUE = builder.comment("Slow down attenuation max value.[Default:0.6]")
                .defineInRange("slowDownAttenuationMaxValue", 0.6d,0.0D,1.0D);
        // 开启时运掉落装备功能？
        ENABLES_FORTUNE_DROP = builder.comment("Enable fortune drop equipment?[Default:true]")
                .define("enablesFortuneDrop", true);
        // 开启携带类型切换功能？
        ENABLES_CHANGE_CARRY_TYPE = builder.comment("Enable the carry type switching function?[Default:true]")
                .define("enablesChangeCarryType", true);
        // 开启绑定主人功能？
        ENABLES_BIND_OWNER = builder.comment("Enable bind owner function?[Default:true]")
                .define("enablesBindOwner", true);
        // 开启S评分强化功能？
        ENABLES_S_RANK_BOOST = builder.comment("Enable S-Rank Boost function?[ Default:true]")
                .define("enablesSRankBoost", true);
        // 开启潜行时无保护切换功能？
        ENABLES_SNEAKING_NO_BACKOFF_SWITCH = builder.comment("Enable the sneaking no back-off switch?[Default:true]")
                .define("enablesSneakingNoBackOffSwitch", true);
        builder.pop();
        SPEC = builder.build();
    }

    public static boolean enablesEnemyLockOnSwitch;
    public static double crosshairMaxDist;
    public static double crosshairMaxAngle;
    public static double nearestMaxDist;
    public static double targetLockRotationSpeed;
    public static boolean enablesAirTrickDodge;
    public static double airTrickDodgeSpeedMultiplier;
    public static boolean enablesInterruptCombos;
    public static double slowDownAttenuationValue;
    public static double slowDownAttenuationMaxValue;
    public static boolean enablesFortuneDrop;
    public static boolean enablesChangeCarryType;
    public static boolean enablesBindOwner;
    public static boolean enablesSRankBoost;
    public static boolean enablesSneakingNoBackOffSwitch;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) return;
        enablesEnemyLockOnSwitch = ENABLES_ENEMY_LOCKON_SWITCH.get();
        crosshairMaxDist = CROSSHAIR_MAX_DIST.get();
        crosshairMaxAngle = CROSSHAIR_MAX_ANGLE.get();
        nearestMaxDist = NEAREST_MAX_DIST.get();
        targetLockRotationSpeed = LOCKON_ROTATION_SPEED.get();
        enablesAirTrickDodge = ENABLES_AIR_TRICK_DODGE.get();
        airTrickDodgeSpeedMultiplier = AIR_TRICK_DODGE_SPEED_MULTIPLIER.get();
        enablesInterruptCombos = ENABLES_INTERRUPT_COMBOS.get();
        slowDownAttenuationValue = SLOW_DOWN_ATTENUATION_VALUE.get();
        slowDownAttenuationMaxValue = SLOW_DOWN_ATTENUATION_MAX_VALUE.get();
        enablesFortuneDrop = ENABLES_FORTUNE_DROP.get();
        enablesChangeCarryType = ENABLES_CHANGE_CARRY_TYPE.get();
        enablesBindOwner = ENABLES_BIND_OWNER.get();
        enablesSRankBoost = ENABLES_S_RANK_BOOST.get();
        enablesSneakingNoBackOffSwitch = ENABLES_SNEAKING_NO_BACKOFF_SWITCH.get();
    }
}
