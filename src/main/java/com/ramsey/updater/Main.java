package com.ramsey.updater;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.jline.utils.Log;
import org.slf4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


@Mod(Main.MODID)
public class Main {
    public static final String MODID = "updater";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Main() throws IOException {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    @Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onScreenOpen(ScreenEvent.Opening event) {
            if (event.getNewScreen() instanceof TitleScreen) {
                event.setNewScreen(new ConfirmScreen(ClientEvents::confirm,
                    Component.translatable("gui.updater.title"),
                    Component.translatable("gui.updater.message"),
                    Component.translatable("gui.updater.confirm"),
                    Component.translatable("gui.updater.cancel")
                ));
            }
        }

        private static void confirm(boolean confirmed) {
            Minecraft minecraft = Minecraft.getInstance();

            if (!confirmed) {
                minecraft.stop();
            }

            DownloadScreen downloadScreen = new DownloadScreen();
            DownloadHandler downloadHandler = new DownloadHandler(downloadScreen);
            
            minecraft.setScreen(downloadScreen);

            downloadHandler.start();
        }
    }

}
