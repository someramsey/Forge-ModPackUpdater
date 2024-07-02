package com.ramsey.updater;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadHandler {
    private final DownloadScreen downloadScreen;
    private final ExecutorService executor;

    public DownloadHandler(DownloadScreen downloadScreen) {
        this.downloadScreen = downloadScreen;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void start() {
        executor.execute(this::downloadTask);
    }

    private void downloadTask() {
        try {
            String url = "https://www.dropbox.com/scl/fi/5rltym7xax4jwheiuy5vs/Create-Custom.rar?rlkey=x45y4qww3583cmx993enn1zbj&st=4hlddsqs&dl=1";
            String destination = "C:\\Users\\RamizKöfte\\Desktop\\dw\\pack.rar";

            URL website = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) website.openConnection();

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

                    downloadScreen.updateProgress("(" + totalBytesRead + " / " + fileSize + ")");
                }
            }
        } catch (Exception exception) {
            downloadScreen.displayError(exception.getMessage());
            Main.LOGGER.error("Failed to download the file", exception);
        }
    }
}
