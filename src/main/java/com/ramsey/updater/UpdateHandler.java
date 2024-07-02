package com.ramsey.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class UpdateHandler {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static VersionInfo versionInfo;

    public static boolean requiresUpdate;

    public static void checkRequiresUpdate() {
        try {
            versionInfo = getVersionInfo();

            String latestVersion = Config.LatestVersion.get();
            requiresUpdate = !latestVersion.equals(versionInfo.version());
        } catch (IOException exception) {
            Main.LOGGER.error("Failed to check for update", exception);
        }
    }

    public static void startUpdate(UpdateScreen updateScreen) {
        executor.submit(() -> {
            try {
                download(updateScreen);
            } catch (IOException exception) {
                Main.LOGGER.error("Update failed", exception);
            }
        });
    }

    private static void download(UpdateScreen updateScreen) throws IOException {
        String url = versionInfo.url();
        String destination = "C:\\Users\\RamizKöfte\\Desktop\\dw\\pack.rar";

        URL connectionUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();

        int fileSize = connection.getContentLength();

        try (
            InputStream in = connection.getInputStream();
            OutputStream out = Files.newOutputStream(Path.of(destination), StandardOpenOption.CREATE, StandardOpenOption.WRITE)
        ) {
            byte[] buffer = new byte[1024];

            int bytesRead;
            int totalBytesRead = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;

                float progress = (float) totalBytesRead / fileSize;
                updateScreen.displayProgress("Downloading (" + totalBytesRead + "/" + fileSize + ")", progress);
            }
        } finally {
            connection.disconnect();
        }
    }

    private static VersionInfo getVersionInfo() throws IOException {
        URL url = new URL(Config.FetchUrl.get());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setConnectTimeout(Config.FetchTimeout.get());
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

        Gson gson = new Gson();
        JsonReader jsonReader = new JsonReader(inputStreamReader);
        JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

        connection.disconnect();

        String version = jsonObject.get("version").getAsString();
        String downloadUrl = jsonObject.get("url").getAsString();

        return new VersionInfo(version, downloadUrl);
    }
}
