package com.ramsey.updater;

import net.minecraft.client.gui.screens.ProgressScreen;

public class DownloadScreen extends ProgressScreen {
    private ProgressBar progressBar;

    public DownloadScreen() {
        super(false);
    }

    @Override
    protected void init() {
        this.progressBar = new ProgressBar(100, 120, this.width - 200, 10);
        this.addRenderableOnly(progressBar);
    }

    public void setProgress(float progress) {
        super.progressStagePercentage((int) (progress * 100));
        this.progressBar.progress = progress;
    }
}
