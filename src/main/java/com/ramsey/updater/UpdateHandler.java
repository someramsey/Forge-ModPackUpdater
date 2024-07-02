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
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class UpdateHandler {
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static VersionInfo versionInfo;

    public static boolean requiresUpdate;

    public static void checkRequiresUpdate() {
        executor.submit(UpdateHandler::checkUpdate);
    }

    private static void checkUpdate() {
        try {
            versionInfo = getVersionInfo();
            //TODO: Check
        } catch (IOException exception) {
            Main.LOGGER.error("Failed to check for update", exception);
        }
    }

    private void download() throws IOException {
        String url = "https://www.dropbox.com/scl/fi/5rltym7xax4jwheiuy5vs/Create-Custom.rar?rlkey=x45y4qww3583cmx993enn1zbj&st=4hlddsqs&dl=1";
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

//                updateScreen.updateProgress(totalBytesRead, fileSize);
            }
        } finally {
            connection.disconnect();
        }
    }

    private static VersionInfo getVersionInfo() throws IOException {
        URL url = new URL(Config.AccessUrl.get());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());
        JsonReader jsonReader = new JsonReader(inputStreamReader);
        Gson gson = new Gson();

        JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

        connection.disconnect();


        String version = jsonObject.get("version").getAsString();
        String downloadUrl = jsonObject.get("url").getAsString();

        return new VersionInfo(version, downloadUrl);
    }
}
