package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class DownloadScreen extends Screen {
    private ProgressBar progressBar;
    private Component message;
    private String progress;

    private final int textColor = 16777215;

    public DownloadScreen() {
        super(Component.literal("Download"));
    }

    public void updateProgress(String progressMessage) {
        this.progress = progressMessage;
    }

    @Override
    protected void init() {
        this.progressBar = new ProgressBar(100, 120, this.width - 200, 10);
        this.message = Component.literal("Downloading files...");
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);

        if (progress != null) {
            drawCenteredString(pPoseStack, this.font, Component.empty().append(message).append(" ").append(progress), this.width / 2, 70, textColor);
        }

        progressBar.render(pPoseStack, pMouseX, pMouseY, pPartialTick, 100, 120, this.width - 200, 10);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
