package com.ramsey.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class UpdateHandler {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static PackInfo packInfo;
    public static boolean requiresUpdate;

    private static UpdateScreen updateScreen;
    private static Path destinationDir;

    public static void runInstallScript() {
        long currentPid = ProcessHandle.current().pid();

        String javaHome = System.getProperty("java.home");
        String javaBinPath = javaHome + "/bin/java";

        String currentJarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();

        String[] command = new String[]{
            javaBinPath,
            "-cp",
            currentJarPath,
            "com.ramsey.updater.PostUpdateTask",
            Long.toString(currentPid),
            destinationDir.resolve(packInfo.scriptPath()).toString()
        };

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.start();

            Main.LOGGER.info("Install script started");
        } catch (IOException exception) {
            Main.LOGGER.error("Failed to start install script", exception);
        }
    }

    public static void fetchPackInfo() {
        try {
            URL url = new URL(Config.FetchUrl.get());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(Config.FetchTimeout.get());
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            Gson gson = new Gson();
            JsonReader jsonReader = new JsonReader(inputStreamReader);
            JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

            String version = jsonObject.get("version").getAsString();
            String downloadUrl = jsonObject.get("url").getAsString();
            String installationScript = jsonObject.get("script").getAsString();

            connection.disconnect();
            inputStreamReader.close();
            jsonReader.close();

            UpdateHandler.packInfo = new PackInfo(version, downloadUrl, installationScript);
            UpdateHandler.requiresUpdate = !Config.LatestVersion.get().equals(version);
        } catch (IOException exception) {
            Main.LOGGER.error("Failed to check for update", exception);
        }
    }

    public static void installUpdate(UpdateScreen updateScreen) {
        Path gameDir = FMLPaths.GAMEDIR.get();
        UpdateHandler.destinationDir = gameDir.resolve(Config.RootDir.get());
        UpdateHandler.updateScreen = updateScreen;

        executor.submit(() -> {
            try {
                prepare();
                download();
                extract();
                updateScreen.updateComplete();
            } catch (Exception exception) {
                onFail(exception);
            }
        });
    }

    private static void prepare() throws IOException {
        if (!Files.exists(destinationDir)) {
            Files.createDirectories(destinationDir);
        }

        try (Stream<Path> stream = Files.walk(destinationDir)) {
            stream.filter(Files::isRegularFile).forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException exception) {
                    Main.LOGGER.error("Failed to delete file: {}", path, exception);
                }
            });
        }
    }

    private static void download() throws IOException {
        URL url = new URL(packInfo.url());
        Path path = destinationDir.resolve(Config.PackagePath.get());

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        int fileSize = connection.getContentLength();


        try (
            InputStream in = connection.getInputStream();
            OutputStream out = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        ) {
            byte[] buffer = new byte[1024];

            int bytesRead;
            int totalBytesRead = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                float progress = (float) Math.max(totalBytesRead, 1) / fileSize;
                updateScreen.displayProgress("Downloading (" + totalBytesRead + "/" + fileSize + ")", progress);
            }
        } finally {
            connection.disconnect();
        }
    }

    private static void extract() throws IOException {
        Path packagePath = destinationDir.resolve(Config.PackagePath.get());

        updateScreen.displayProgress("Extracting", 0);

        try (ZipFile zipFile = new ZipFile(packagePath.toFile())) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            int totalEntries = zipFile.size();
            int entryIndex = 0;

            while (entries.hasMoreElements()) {
                entryIndex++;
                ZipEntry entry = entries.nextElement();
                Path entryPath = destinationDir.resolve(entry.getName());

                if (entry.isDirectory()) {
                    Files.createDirectories(entryPath);
                } else {
                    try (InputStream in = zipFile.getInputStream(entry)) {
                        Files.copy(in, entryPath, StandardCopyOption.REPLACE_EXISTING);

                        float progress = (float) entryIndex / totalEntries;
                        updateScreen.displayProgress("Extracting (" + entryIndex + "/" + totalEntries + ")", progress);
                    }
                }
            }
        }

        updateScreen.displayProgress("Cleaning up", 1);

        Files.delete(packagePath);
    }

    private static void onFail(Exception exception) {
        String errorMessage = "Update failed (" + exception.getClass().getName() + ") " +
            exception.getMessage() + "\n" + Arrays.toString(exception.getStackTrace());

        Main.LOGGER.error(errorMessage);
        UpdateHandler.updateScreen.displayError(errorMessage);
    }
}
