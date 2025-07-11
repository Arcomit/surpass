package mod.arcomit.surpass.event;

import mod.arcomit.surpass.Config;
import mod.arcomit.surpass.Surpass;
import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.ability.SlayerStyleArts;
import mods.flammpfeil.slashblade.ability.Untouchable;
import mods.flammpfeil.slashblade.capability.mobeffect.CapabilityMobEffect;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import mods.flammpfeil.slashblade.event.handler.InputCommandEvent;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.AdvancementHelper;
import mods.flammpfeil.slashblade.util.InputCommand;
import mods.flammpfeil.slashblade.util.NBTHelper;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumSet;

@Mod.EventBusSubscriber(modid = Surpass.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class AirTrickDodgeEvent {
    final static EnumSet<InputCommand> move = EnumSet.of(InputCommand.FORWARD, InputCommand.BACK, InputCommand.LEFT,
            InputCommand.RIGHT);

    static public final ResourceLocation ADVANCEMENT_TRICK_DODGE = new ResourceLocation(SlashBlade.MODID,
            "abilities/trick_dodge");

    final static int TRICKACTION_UNTOUCHABLE_TIME = 10;

    @SubscribeEvent
    public static void onInputChange(InputCommandEvent event) {

        EnumSet<InputCommand> old = event.getOld();
        EnumSet<InputCommand> current = event.getCurrent();
        ServerPlayer sender = event.getEntity();

        ItemStack stack = sender.getMainHandItem();
        if (!Config.enablesAirTrickDodge)
            return;
        if (stack.isEmpty())
            return;
        if (!(stack.getItem() instanceof ItemSlashBlade))
            return;

        if (!old.contains(InputCommand.SPRINT)) {

            boolean isHandled = false;
            if (!isHandled && !sender.onGround() && current.contains(InputCommand.SPRINT)
                    && current.stream().anyMatch(cc -> move.contains(cc))) {
                Level level = sender.level();
                int count = sender.getCapability(CapabilityMobEffect.MOB_EFFECT)
                        .map(ef -> ef.doAvoid(level.getGameTime())).orElse(0);

                if (0 < count) {
                    Untouchable.setUntouchable(sender, TRICKACTION_UNTOUCHABLE_TIME);

                    float moveForward = current.contains(InputCommand.FORWARD) == current.contains(InputCommand.BACK)
                            ? 0.0F
                            : (current.contains(InputCommand.FORWARD) ? 1.0F : -1.0F);
                    float moveStrafe = current.contains(InputCommand.LEFT) == current.contains(InputCommand.RIGHT)
                            ? 0.0F
                            : (current.contains(InputCommand.LEFT) ? 1.0F : -1.0F);
                    Vec3 input = new Vec3(moveStrafe, 0, moveForward);

                    sender.moveRelative((float) (3.0f * Config.airTrickDodgeSpeedMultiplier), input);

                    Vec3 motion = sender.getDeltaMovement();

                    sender.playNotifySound(SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 0.5f, 1.2f);

                    sender.move(MoverType.SELF, motion);
                    sender.isChangingDimension = true;

                    sender.connection.send(new ClientboundSetEntityMotionPacket(sender.getId(), new Vec3(motion.x * 0.5, motion.y, motion.z * 0.5)));

                    sender.getPersistentData().putInt("sb.avoid.counter", 2);
                    NBTHelper.putVector3d(sender.getPersistentData(), "sb.avoid.vec", sender.position());

                    AdvancementHelper.grantCriterion(sender, ADVANCEMENT_TRICK_DODGE);
                }

                isHandled = true;
            }
        }

    }
}
