package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DownloadScreen extends Screen {
    private ProgressBar progressBar;
    private ErrorPanel errorPanel;

    private String progress;
    private State state;

    private final Component downloadingMessage;
    private final Component preparingMessage;
    private final Component downloadFailedMessage;

    private final int infoColor = 16777215;
    private final int errorColor = -3014656;

    public DownloadScreen() {
        super(Component.literal("Download"));

        this.downloadingMessage = Component.literal("Downloading...");
        this.preparingMessage = Component.literal("Preparing the download...");
        this.downloadFailedMessage = Component.literal("Download failed");

        this.state = State.DOWNLOADING;
    }

    public void updateProgress(String progressMessage) {
        this.progress = progressMessage;
    }

    public void displayError(String errorMessage) {
        this.state = State.ERROR;
        this.errorPanel.setContent(errorMessage);
    }

    @Override
    protected void init() {
        this.progressBar = new ProgressBar(100, 120, this.width - 200, 10);
        this.errorPanel = new ErrorPanel(this.width - 100, this.height - 140, 90, 50);
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);

        drawCenteredString(pPoseStack, this.font, getMessage(), this.width / 2, 70, infoColor);

        this.progressBar.render(pPoseStack);
        this.errorPanel.render(pPoseStack, pMouseX, pMouseY, pPartialTick);

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }





    private Component getMessage() {
        if (progress == null) {
            return this.preparingMessage;
        }

        return Component.empty().append(downloadingMessage).append(" ").append(progress);
    }

    private class ErrorPanel extends ScrollPanel {
        private final List<String> lines = new ArrayList<>();

        public ErrorPanel(int width, int height, int top, int left) {
            super(minecraft, width, height, top, left);
        }

        public void setContent(String content) {
            lines.clear();

            if (content != null) {
                Collections.addAll(lines, content.split("\n"));
            }
        }

        @Override
        protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            int y = relativeY;

            for (String line : lines) {
                drawString(poseStack, font, line, left + 10, y + 10, errorColor);
                y += font.lineHeight;
            }
        }

        @Override
        protected int getContentHeight() {
            return font.lineHeight * lines.size();
        }

        @Override
        public @NotNull NarrationPriority narrationPriority() {
            return NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(@NotNull NarrationElementOutput pNarrationElementOutput) {
        }
    }

    private enum State {
        PREPARING,
        DOWNLOADING,
        ERROR,
        DONE
    }
}
