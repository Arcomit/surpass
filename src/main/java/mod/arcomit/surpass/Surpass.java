package mod.arcomit.surpass;

import com.mojang.logging.LogUtils;
import mod.arcomit.surpass.client.ClientConfig;
import mod.arcomit.surpass.network.NetworkHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@SuppressWarnings("removal")
@Mod(Surpass.MODID)
public class Surpass {
    public static final String MODID = "surpass";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Surpass() {
        LOGGER.info("Surpass is loaded!");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        NetworkHandler.register();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(Surpass.MODID, path);
    }
}
