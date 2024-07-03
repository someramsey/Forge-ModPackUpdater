package com.ramsey.updater;

import java.util.Objects;

public record PackInfo(String version, String url, String scriptPath) {
    public PackInfo {
        Objects.requireNonNull(version, "Invalid Body: Version cannot be null");
        Objects.requireNonNull(url, "Invalid Body: Url cannot be null");
        Objects.requireNonNull(scriptPath, "Invalid Body: Installation Script cannot be null");
    }
}
