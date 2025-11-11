package mod.arcomit.surpass.event;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.Surpass;
import mods.flammpfeil.slashblade.client.renderer.CarryType;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-10 02:08
 * @Description: TODO
 */
@Mod.EventBusSubscriber(modid = Surpass.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChangeCarryTypeEvent {
    @SubscribeEvent
    public static void eventChangeCarryType(SlashBladeEvent.BladeStandAttackEvent event) {
        if  (!Config.enablesChangeCarryType) return;
        if (!(event.getDamageSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack blade = event.getBlade();
        if (blade.isEmpty()) {
            return;
        }
        if (stack.getItem() != Items.COPPER_INGOT) {
            return;
        }
        var world = player.level();
        var bladeStand = event.getBladeStand();
        var state = event.getSlashBladeState();
        RandomSource random = player.getRandom();

        stack.shrink(1);

        // 获取当前CarryType并设置为下一个类型
        CarryType currentType = state.getCarryType();
        CarryType nextType = getNextCarryType(currentType);
        state.setCarryType(nextType);

        spawnSucceedEffects(world, bladeStand, random);

        event.setCanceled(true);
    }

    /**
     * 获取下一个CarryType类型
     * 循环顺序: NONE -> DEFAULT -> PSO2 -> NINJA -> KATANA -> RNINJA -> NONE
     */
    private static CarryType getNextCarryType(CarryType currentType) {
        CarryType[] values = CarryType.values();
        int currentIndex = currentType.ordinal();
        int nextIndex = (currentIndex + 1) % values.length;
        return values[nextIndex];
    }

    private static void spawnSucceedEffects(Level world, BladeStandEntity bladeStand, RandomSource random) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }
        // 音效
        serverLevel.playSound(
                bladeStand,
                bladeStand.getPos(),
                SoundEvents.WITHER_SPAWN,
                SoundSource.BLOCKS,
                0.5f,
                0.8f
        );

        // 粒子效果
        for (int i = 0; i < 32; ++i) {
            double xDist = (random.nextFloat() * 2.0F - 1.0F);
            double yDist = (random.nextFloat() * 2.0F - 1.0F);
            double zDist = (random.nextFloat() * 2.0F - 1.0F);
            if (xDist * xDist + yDist * yDist + zDist * zDist <= 1.0D) {
                double x = bladeStand.getX(xDist / 4.0D);
                double y = bladeStand.getY(0.5D + yDist / 4.0D);
                double z = bladeStand.getZ(zDist / 4.0D);
                serverLevel.sendParticles(
                        ParticleTypes.PORTAL,
                        x, y, z,
                        0,
                        xDist, yDist + 0.2D, zDist,
                        1);
            }
        }
    }
}
