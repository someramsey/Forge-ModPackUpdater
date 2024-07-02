package com.ramsey.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Mod(Main.MODID)
public class Main {
    public static final String MODID = "updater";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() throws IOException {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        UpdateHandler.checkRequiresUpdate();
    }

    @Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onScreenOpen(ScreenEvent.Opening event) {
            if (event.getNewScreen() instanceof TitleScreen) {
                event.setNewScreen(new ConfirmScreen(ClientEvents::confirm,
                    Component.translatable("gui.updater.available.title"),
                    Component.translatable("gui.updater.available.message"),
                    Component.translatable("gui.updater.confirm"),
                    Component.translatable("gui.updater.refuse")
                ));
            }
        }

        private static void confirm(boolean confirmed) {
            Minecraft minecraft = Minecraft.getInstance();

            if (!confirmed) {
                minecraft.stop();
            }

            UpdateScreen updateScreen = new UpdateScreen();

            minecraft.setScreen(updateScreen);
        }
    }

}
