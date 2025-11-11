package mod.arcomit.surpass.mixin;

import mod.arcomit.surpass.Config;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Mixin to modify ItemSlashBlade's appendSwordType method to add owner information
 */
@Mixin(ItemSlashBlade.class)
public class ItemSlashBladeMixin {

    @OnlyIn(Dist.CLIENT)
    @Inject(
            method = "appendSwordType",
            at = @At("RETURN"),
            cancellable = true,
            remap = false
    )
    private void addOwnerInfo(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        if (!Config.enablesBindOwner) {
            return;
        }
        var tag = stack.getTag();
        if (tag == null || !tag.contains("Surpass.Owner")) {
            return;
        }

        String ownerName = tag.getString("Surpass.Owner");

        // 获取当前玩家UUID
        var player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        // 判断是否为所有者
        boolean isOwner = player.getName().getString().equals(ownerName);

        tooltip.add(Component.translatable("surpass.sword.owner.name", ownerName)
                .withStyle(ChatFormatting.GOLD));
        if (!isOwner) {
            tooltip.add(Component.translatable("surpass.sword.owner.other")
                    .withStyle(ChatFormatting.RED));
        }
    }
}

