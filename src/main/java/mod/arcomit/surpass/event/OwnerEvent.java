package mod.arcomit.surpass.event;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.Surpass;
import mods.flammpfeil.slashblade.entity.BladeStandEntity;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.registry.SlashBladeItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-10 02:08
 * @Description: TODO
 */
@Mod.EventBusSubscriber(modid = Surpass.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OwnerEvent {
    @SubscribeEvent
    public static void eventBindOwner(SlashBladeEvent.BladeStandAttackEvent event) {
        if (!Config.enablesBindOwner) {
            return;
        }
        if (!(event.getDamageSource().getEntity() instanceof ServerPlayer player)) {
            return;
        }
        var blade = event.getBlade();
        if (blade.isEmpty()) {
            return;
        }
        var stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() != SlashBladeItems.PROUDSOUL_TRAPEZOHEDRON.get()) {
            return;
        }

        var tag = blade.getOrCreateTag();
        var world = player.level();
        var bladeStand = event.getBladeStand();
        RandomSource random = player.getRandom();
        if (tag.contains("Surpass.Owner")) {
            if (tag.getString("Surpass.Owner").equals(player.getName().getString())) {
                tag.remove("Surpass.Owner");
                spawnUnbindEffects(world, bladeStand, random);
                event.setCanceled(true);
            }
            return;
        }
        stack.shrink(1);
        tag.putString("Surpass.Owner", player.getName().getString());
//        tag.putString("Surpass.Owner", "12416317");
        spawnBindEffects(world, bladeStand, random);
        event.setCanceled(true);
    }

    private static void spawnBindEffects(Level world, BladeStandEntity bladeStand, RandomSource random) {
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

    private static void spawnUnbindEffects(Level world, BladeStandEntity bladeStand, RandomSource random) {
        if (!(world instanceof ServerLevel serverLevel)) {
            return;
        }
        // 音效
        serverLevel.playSound(
                bladeStand,
                bladeStand.getPos(),
                SoundEvents.ENDER_DRAGON_FLAP,
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
                        ParticleTypes.SMOKE,
                        x, y, z,
                        0,
                        xDist, yDist + 0.2D, zDist,
                        1);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void eventHurt(SlashBladeEvent.BladeStandAttackEvent event) {
        if (!Config.enablesBindOwner) {
            return;
        }
        var blade = event.getBlade();
        if (blade.isEmpty()) {
            return;
        }
        var tag = blade.getTag();
        if (tag == null || !tag.contains("Surpass.Owner")) {
            return;
        }
        if (event.getDamageSource().getEntity() instanceof ServerPlayer player && player.getName().getString().equals(tag.getString("Surpass.Owner"))) {
            return;
        }
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void eventOnUpdate(SlashBladeEvent.UpdateEvent event) {
        var blade = event.getBlade();
        if (blade.isEmpty()) {
            return;
        }
        var tag = blade.getTag();
        if (tag == null || !tag.contains("Surpass.Owner")) {
            return;
        }
        var ownerName = tag.getString("Surpass.Owner");
        var currentHolder = event.getEntity();
        var bladeState = event.getSlashBladeState();
        if (!Config.enablesBindOwner) {// 没开启自动恢复旧封印状态
            if (tag.contains("Surpass.oldSealed")) {
                bladeState.setSealed(tag.getBoolean("Surpass.oldSealed"));
                tag.remove("Surpass.oldSealed");
            }
            return;
        }
        if (!(currentHolder instanceof Player)) {
            return;
        }
        if (currentHolder.getName().getString().equals(ownerName)) {
            if (tag.contains("Surpass.oldSealed")) {
                bladeState.setSealed(tag.getBoolean("Surpass.oldSealed"));
                tag.remove("Surpass.oldSealed");
            }
            return;
        }
        tag.putBoolean("Surpass.oldSealed",bladeState.isSealed());
        bladeState.setSealed(true);
    }
}
