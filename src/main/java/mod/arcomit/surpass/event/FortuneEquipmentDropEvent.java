package mod.arcomit.surpass.event;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.Surpass;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// 时运掉落装备
@Mod.EventBusSubscriber(modid = Surpass.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FortuneEquipmentDropEvent {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if(!Config.enablesFortuneDrop) return;

        LivingEntity entity = event.getEntity();

        if (entity instanceof  Player ) return;

        DamageSource source = event.getSource();

        // 检查是否由玩家造成的伤害
        if (!(source.getEntity() instanceof Player player)) {
            return;
        }

        // 获取玩家手持物品
        ItemStack weaponStack = player.getMainHandItem();

        // 检查是否为拔刀剑（ItemSlashBlade）
        if (!(weaponStack.getItem() instanceof ItemSlashBlade)) {
            return;
        }

        // 获取时运等级
        int lootingLevel = weaponStack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

        // 如果没有时运附魔，直接返回
        if (lootingLevel <= 0) {
            return;
        }

        // 时运1：强制掉落主手物品
        if (lootingLevel >= 1) {
            ItemStack mainHandItem = entity.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!mainHandItem.isEmpty()) {
                // 强制100%掉落主手物品
                entity.spawnAtLocation(mainHandItem.copy());
                entity.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }

        // 时运2及以上：强制掉落主手物品和所有装备
        if (lootingLevel >= 2) {
            // 掉落副手物品
            ItemStack offHandItem = entity.getItemBySlot(EquipmentSlot.OFFHAND);
            if (!offHandItem.isEmpty()) {
                entity.spawnAtLocation(offHandItem.copy());
                entity.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
            }

            // 掉落头盔
            ItemStack headItem = entity.getItemBySlot(EquipmentSlot.HEAD);
            if (!headItem.isEmpty()) {
                entity.spawnAtLocation(headItem.copy());
                entity.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
            }

            // 掉落胸甲
            ItemStack chestItem = entity.getItemBySlot(EquipmentSlot.CHEST);
            if (!chestItem.isEmpty()) {
                entity.spawnAtLocation(chestItem.copy());
                entity.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
            }

            // 掉落护腿
            ItemStack legsItem = entity.getItemBySlot(EquipmentSlot.LEGS);
            if (!legsItem.isEmpty()) {
                entity.spawnAtLocation(legsItem.copy());
                entity.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
            }

            // 掉落靴子
            ItemStack feetItem = entity.getItemBySlot(EquipmentSlot.FEET);
            if (!feetItem.isEmpty()) {
                entity.spawnAtLocation(feetItem.copy());
                entity.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
            }
        }
    }
}

