package com.ramsey.updater.extractor;

import java.io.IOException;
import java.nio.file.Path;

public interface Extractor {
    void extract(Path source, Path destination, ProgressChangeListener callback) throws IOException;

    interface ProgressChangeListener {
        void progress(int currentEntry, int totalEntries);
    }
}
