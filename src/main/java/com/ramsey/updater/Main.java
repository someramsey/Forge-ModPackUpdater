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
    private static final Logger LOGGER = LogUtils.getLogger();

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

            minecraft.setScreen(downloadScreen);

            new Thread(() -> {
                try {
                    download(downloadScreen);
                } catch (IOException exception) {
                    LOGGER.error("Failed to download the file", exception);
                }
            }).start();
        }

        private static void download(DownloadScreen downloadScreen) throws IOException {
            URL website = new URL("https://www.dropbox.com/scl/fi/5rltym7xax4jwheiuy5vs/Create-Custom.rar?rlkey=x45y4qww3583cmx993enn1zbj&st=4hlddsqs&dl=1");
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();
            int fileSize = connection.getContentLength();

            try (
                InputStream in = connection.getInputStream();
                OutputStream out = Files.newOutputStream(Path.of("C:\\Users\\RamizKöfte\\Desktop\\dw\\pack.rar"), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
            ) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                int totalBytesRead = 0;

                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                    totalBytesRead += bytesRead;

                    float progress = (float) totalBytesRead / fileSize;




                    downloadScreen.updateProgress(totalBytesRead + " / " + fileSize + " bytes");
                }
            }
        }
    }

}
