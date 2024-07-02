package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import net.minecraftforge.common.ForgeHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DownloadScreen extends Screen {
    private ProgressBar progressBar;
    private ErrorPanel errorPanel;

    private String details;
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

        this.state = State.PREPARING;
    }

    public void updateProgress(int readBytes, int totalBytes) {
        float progress = (float) readBytes / totalBytes;

        this.state = State.DOWNLOADING;
        this.details = "(" + readBytes + " / " + totalBytes + ")";
        this.progressBar.progress = progress;
    }

    public void displayError(String errorMessage) {
        this.state = State.ERROR;
        this.details = errorMessage;

        this.errorPanel.setContent(errorMessage);
    }

    @Override
    protected void init() {
        this.progressBar = new ProgressBar(100, 120, this.width - 200, 10);
        this.errorPanel = new ErrorPanel(this.width - 100, this.height - 140, 90, 50);

        if(state == State.ERROR) {
            this.errorPanel.setContent(details);
        }
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);

        GuiComponent.drawCenteredString(pPoseStack, this.font, getMessage(), this.width / 2, 70, infoColor);

        if (state == State.ERROR) {
            this.errorPanel.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        } else {
            this.progressBar.render(pPoseStack);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private Component getMessage() {
        return switch (state) {
            case ERROR -> downloadFailedMessage;
            case PREPARING -> preparingMessage;
            case DOWNLOADING -> Component.empty().append(downloadingMessage).append(" ").append(details);
        };
    }

    private class ErrorPanel extends ScrollPanel {
        private List<FormattedCharSequence> lines = new ArrayList<>();
        private String content;

        public ErrorPanel(int width, int height, int top, int left) {
            super(minecraft, width, height, top, left);
        }

        public void setContent(String content) {
            this.content = content;
            updateLines();
        }

        private void updateLines() {
            List<FormattedCharSequence> newLines = new ArrayList<>();

            if(content == null) {
                return;
            }

            String[] contentLines = content.split("\n");

            for (String line : contentLines) {
                Component chat = ForgeHooks.newChatWithLinks(line, false);

                int maxTextLength = this.width - 12;

                if (maxTextLength >= 0) {
                    newLines.addAll(Language.getInstance().getVisualOrder(font.getSplitter().splitLines(chat, maxTextLength, Style.EMPTY)));
                }
            }

            this.lines = newLines;
        }

        @Override
        protected void drawPanel(PoseStack poseStack, int entryRight, int relativeY, Tesselator tess, int mouseX, int mouseY) {
            int y = relativeY;

            for (FormattedCharSequence line : lines) {
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
        ERROR
    }
}
