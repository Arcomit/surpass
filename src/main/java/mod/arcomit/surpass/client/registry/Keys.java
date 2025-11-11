package mod.arcomit.surpass.client.registry;

import com.mojang.blaze3d.platform.InputConstants;
import mod.arcomit.surpass.Surpass;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = Surpass.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Keys {
    //按键-开关索敌(Toggle enemy lock-on on/off)
    public static final KeyMapping TOGGLE_ENEMY_LOCKON_ON_OFF = new KeyMapping(
            "key."+ Surpass.MODID +".toggle_lockon_on_off",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_5,
            "category."+ Surpass.MODID
    );

    public static final KeyMapping TOGGLE_SNEAKING_NO_BACKOFF_ON_OFF = new KeyMapping(
            "key."+ Surpass.MODID +".toggle_sneaking_no_backoff_on_off",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM,
            -1,
            "category."+ Surpass.MODID
    );

    public static final KeyMapping INTERRUPT_COMBOS = new KeyMapping(
            "key."+ Surpass.MODID +".interrupt_combos",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_4,
            "category."+ Surpass.MODID
    );

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_ENEMY_LOCKON_ON_OFF);
        event.register(TOGGLE_SNEAKING_NO_BACKOFF_ON_OFF);
        event.register(INTERRUPT_COMBOS);
    }
}
