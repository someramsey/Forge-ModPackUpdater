package com.ramsey.updater;

import net.minecraft.client.gui.screens.ProgressScreen;

public class DownloadScreen extends ProgressScreen {
    public DownloadScreen() {
        super(false);
    }

    @Override
    protected void init() {
        this.addRenderableOnly(new ProgressBar(100, 120, this.width - 200, 10));
    }
}
