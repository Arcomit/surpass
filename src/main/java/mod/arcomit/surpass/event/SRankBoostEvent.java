package mod.arcomit.surpass.event;

import mod.arcomit.surpass.Config;
import mods.flammpfeil.slashblade.capability.concentrationrank.CapabilityConcentrationRank;
import mods.flammpfeil.slashblade.event.BladeMotionEvent;
import mods.flammpfeil.slashblade.event.SlashBladeEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.registry.ComboStateRegistry;
import mods.flammpfeil.slashblade.slasharts.Drive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static mods.flammpfeil.slashblade.registry.combo.ComboState.getElapsed;

/**
 * @Author: Arcomit
 * @CreateTime: 2025-11-11 15:06
 * @Description: TODO
 */
@Mod.EventBusSubscriber(modid = "surpass", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SRankBoostEvent {

    @SubscribeEvent
    public static void onTestEvent(SlashBladeEvent.UpdateEvent event){
        if (!Config.enablesSRankBoost) return;
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            boolean isSelected = event.isSelected();
            ItemStack blade = event.getBlade();
            if (ItemSlashBlade.isInMainhand(blade, isSelected, livingEntity)) {
                var state = event.getSlashBladeState();
                ResourceLocation combo = state.resolvCurrentComboState(livingEntity);
                long elapsed = getElapsed(livingEntity);
                int rank = livingEntity.getCapability(CapabilityConcentrationRank.RANK_POINT)
                        .map(r -> r.getRank(livingEntity.level().getGameTime()).level)
                        .orElse(0);
                if (rank >= 5 && combo.equals(ComboStateRegistry.COMBO_A4.getId()) && elapsed == 9) {
                    Drive.doSlash(livingEntity, 47.5F, 10, Vec3.ZERO, false, 1.0f, 0.25f);
                }
            }
        }
    }
}