package mod.arcomit.surpass.utils;

import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;

import java.util.Comparator;
import java.util.List;

/**
 * 实体查找辅助工具类
 * 提供基于准心和距离的实体搜索功能
 */
public class FindEntityHelper {

    /**
     * 寻找离玩家准心最近的实体
     * 
     * @param player   玩家实体
     * @param maxAngle 搜索角度（度数）
     * @param maxDist  最大索敌距离
     * @return 最接近准心的实体，未找到则返回 null
     */
    public static Entity findClosestToCrosshair(Player player, double maxAngle, double maxDist) {
        final Vec3 eyePos = player.getEyePosition(1.0f);
        final Vec3 lookVec = player.getLookAngle().normalize();
        final double cosThreshold = Math.cos(Math.toRadians(maxAngle));
        final double maxDistSqr = maxDist * maxDist;

        List<Entity> candidates = getInitialCandidates(player, eyePos, maxDist, maxDistSqr);

        return candidates.stream()
                .map(FindEntityHelper::getTopParent)
                .distinct()
                .filter(entity -> isLivingEntityInAngle(entity, eyePos, lookVec, cosThreshold))
                .filter(entity -> TargetSelector.test.test(player, (LivingEntity) entity))
                .filter(entity -> hasLineOfSight(player, eyePos, entity))
                .min(Comparator.comparingDouble(entity -> 
                        calculateCrosshairDistance(entity, eyePos, lookVec)))
                .orElse(null);
    }

    /**
     * 寻找离玩家最近的实体
     * 
     * @param player 玩家实体
     * @param radius 搜索半径
     * @return 最近的实体，未找到则返回 null
     */
    public static Entity findNearestEntity(Player player, double radius) {
        if (player == null || !player.isAlive()) {
            return null;
        }

        AABB searchArea = createSearchArea(player, radius);
        List<Entity> candidates = player.level().getEntities(player, searchArea, 
                entity -> isValidCandidate(entity, player));

        return candidates.stream()
                .map(FindEntityHelper::getTopParent)
                .distinct()
                .filter(entity -> entity instanceof LivingEntity)
                .filter(entity -> TargetSelector.test.test(player, (LivingEntity) entity))
                .min(Comparator.comparingDouble(player::distanceToSqr))
                .orElse(null);
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 递归获取 PartEntity 的最顶层父实体
     *
     * @param entity 要检查的实体
     * @return 最顶层的父实体，如果输入不是 PartEntity 则返回自身
     */
    private static Entity getTopParent(Entity entity) {
        if (entity instanceof PartEntity<?>) {
            Entity parent = ((PartEntity<?>) entity).getParent();
            if (parent != null) {
                return getTopParent(parent);
            }
        }
        return entity;
    }

    /**
     * 获取初始候选实体列表
     */
    private static List<Entity> getInitialCandidates(Player player, Vec3 eyePos, 
                                                      double maxDist, double maxDistSqr) {
        return player.level().getEntitiesOfClass(
                Entity.class,
                new AABB(eyePos, eyePos).inflate(maxDist),
                entity -> entity.isAlive()
                        && entity.isPickable()
                        && eyePos.distanceToSqr(entity.position()) <= maxDistSqr
        );
    }

    /**
     * 检查实体是否在视角范围内且为 LivingEntity
     */
    private static boolean isLivingEntityInAngle(Entity entity, Vec3 eyePos, 
                                                  Vec3 lookVec, double cosThreshold) {
        if (!(entity instanceof LivingEntity)) {
            return false;
        }

        Vec3 entityPos = entity.getBoundingBox().getCenter();
        Vec3 toEntity = entityPos.subtract(eyePos);
        double distSqr = toEntity.lengthSqr();
        
        if (distSqr < 0.0001) {
            return false;
        }

        double projection = toEntity.dot(lookVec);
        double actualCos = projection / Math.sqrt(distSqr);
        
        return actualCos >= cosThreshold;
    }

    /**
     * 检查玩家与实体之间是否有视线（无方块阻挡）
     */
    private static boolean hasLineOfSight(Player player, Vec3 eyePos, Entity entity) {
        Vec3 entityPos = entity.getBoundingBox().getCenter();
        ClipContext context = new ClipContext(
                eyePos, entityPos,
                ClipContext.Block.VISUAL,
                ClipContext.Fluid.NONE,
                player
        );
        
        return player.level().clip(context).getType() != HitResult.Type.BLOCK;
    }

    /**
     * 计算实体到准心视线的垂直距离（平方）
     */
    private static double calculateCrosshairDistance(Entity entity, Vec3 eyePos, Vec3 lookVec) {
        Vec3 entityPos = entity.getBoundingBox().getCenter();
        Vec3 toEntity = entityPos.subtract(eyePos);
        double projection = lookVec.dot(toEntity);
        Vec3 closestPoint = eyePos.add(lookVec.scale(projection));
        
        return entityPos.distanceToSqr(closestPoint);
    }

    /**
     * 创建以玩家为中心的搜索区域
     */
    private static AABB createSearchArea(Player player, double radius) {
        return new AABB(
                player.getX() - radius, player.getY() - radius, player.getZ() - radius,
                player.getX() + radius, player.getY() + radius, player.getZ() + radius
        );
    }

    /**
     * 检查实体是否为有效的候选目标
     */
    private static boolean isValidCandidate(Entity entity, Player player) {
        if (entity == player || !entity.isAlive() || !entity.isPickable()) {
            return false;
        }

        Entity topParent = getTopParent(entity);
        return topParent instanceof LivingEntity 
                && TargetSelector.test.test(player, (LivingEntity) topParent);
    }
}
