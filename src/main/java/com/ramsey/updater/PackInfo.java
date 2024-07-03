package com.ramsey.updater;

import java.util.Objects;

public record VersionInfo(String version, String url) {
    public VersionInfo {
        Objects.requireNonNull(version, "Invalid Body: Version cannot be null");
        Objects.requireNonNull(url, "Invalid Body: Url cannot be null");
    }
}
