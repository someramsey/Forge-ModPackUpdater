package com.ramsey.updater;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public abstract class UpdateChecker {
    public static UpdateState updateState;
    public static PackInfo packInfo;

    public static void checkUpdateState() {
        try {
            fetchPackInfo();

            String latestVersion = packInfo.version();
            String installedVersion = getInstalledVersion();

            if (Objects.equals(installedVersion, latestVersion)) {
                updateState = UpdateState.UpToDate;
            } else {
                updateState = UpdateState.UpdateAvailable;
            }
        } catch (IOException exception) {
            updateState = UpdateState.FailedToFetch;
            Main.LOGGER.error("Failed to check version match state", exception);
        }
    }

    private static String getInstalledVersion() throws IOException {
        if (!Files.exists(Config.versionFilePath)) {
            return null;
        }

        return Files.readString(Config.versionFilePath);
    }

    private static void fetchPackInfo() throws IOException {
        URL url = new URL(Config.fetchUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");
        connection.setConnectTimeout(Config.fetchTimeout);

        InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

        Gson gson = new Gson();

        JsonReader jsonReader = new JsonReader(inputStreamReader);
        JsonObject jsonObject = gson.fromJson(jsonReader, JsonObject.class);

        String version = jsonObject.get("version").getAsString();
        String downloadUrl = jsonObject.get("url").getAsString();

        connection.disconnect();
        inputStreamReader.close();
        jsonReader.close();

        packInfo = new PackInfo(version, downloadUrl);
    }

    public enum UpdateState {
        UpToDate,
        UpdateAvailable,
        FailedToFetch
    }

    public record PackInfo(String version, String url) {
        public PackInfo {
            Objects.requireNonNull(version, "Invalid Body: Version cannot be null");
            Objects.requireNonNull(url, "Invalid Body: Url cannot be null");
        }
    }
}
