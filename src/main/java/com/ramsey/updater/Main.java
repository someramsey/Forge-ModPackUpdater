package com.ramsey.updater;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.*;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;


@Mod(Main.MODID)
public class Main {
    public static final String MODID = "updater";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Main() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onScreenOpen(ScreenEvent.Opening event) {
            if (event.getNewScreen() instanceof TitleScreen) {
                event.setNewScreen(new ConfirmScreen((confirmed) -> {
                    if (!confirmed) {
                        Minecraft.getInstance().stop();
                    }

                    Minecraft.getInstance().setScreen(new DownloadScreen());
                }, Component.translatable("gui.updater.title"), Component.translatable("gui.updater.message"), Component.translatable("gui.updater.confirm"), Component.translatable("gui.updater.cancel")));
            }
        }
    }
}
