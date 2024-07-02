package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.Util;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.widget.ScrollPanel;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.loading.FMLPaths;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UpdateScreen extends Screen {
    private ProgressBar progressBar;
    private ErrorPanel errorPanel;
    private Button restartButton;
    private Button openFolderButton;

    private String details;
    private State state;

    private final Component preparingMessage;
    private final Component downloadFailedMessage;
    private final Component downloadCompletedMessage;

    private final int infoColor = 16777215;
    private final int errorColor = -3014656;

    public UpdateScreen() {
        super(Component.translatable("gui.updater.active.title"));

        this.preparingMessage = Component.translatable("gui.updater.active.preparing");
        this.downloadFailedMessage = Component.translatable("gui.updater.active.failed");
        this.downloadCompletedMessage = Component.translatable("gui.updater.active.done");

        this.state = State.PREPARING;
    }

    public void displayProgress(String details, float progress) {
        this.state = State.WORKING;
        this.details = details;
        this.progressBar.progress = progress;
    }

    public void displayError(String errorMessage) {
        this.state = State.ERROR;
        this.details = errorMessage;

        this.errorPanel.setContent(errorMessage);
    }

    public void updateComplete() {
        this.state = State.DONE;
    }

    private Component getMessage() {
        return switch (state) {
            case ERROR -> downloadFailedMessage;
            case PREPARING -> preparingMessage;
            case DONE -> downloadCompletedMessage;
            case WORKING -> Component.literal(details);
        };
    }

    @Override
    public @NotNull List<? extends GuiEventListener> children() {
        return switch (state) {
            case ERROR -> List.of(errorPanel);
            case DONE -> List.of(restartButton, openFolderButton);
            default -> Collections.emptyList();
        };
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected void init() {
        this.progressBar = new ProgressBar(100, 120, this.width - 200, 10);
        this.errorPanel = new ErrorPanel(this.width - 100, this.height - 100, 90, 50);

        this.restartButton = new Button(this.width / 2 - 135, 170, 110, 20, Component.translatable("gui.updater.restart"), button -> Objects.requireNonNull(this.minecraft).stop());
        this.openFolderButton = new Button(this.width / 2 + 15, 170, 110, 20, Component.translatable("gui.updater.openModsFolder"), button -> Util.getPlatform().openFile(FMLPaths.MODSDIR.get().toFile()));

        if (state == State.ERROR) {
            this.errorPanel.setContent(details);
        }
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pPoseStack);

        GuiComponent.drawCenteredString(pPoseStack, this.font, getMessage(), this.width / 2, 70, infoColor);

        if (state == State.ERROR) {
            this.errorPanel.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            return;
        }


        if (state == State.DONE) {
            this.restartButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
            this.openFolderButton.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        }

        this.progressBar.render(pPoseStack);
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

            if (content == null) {
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
        protected int getScrollAmount() {
            return font.lineHeight * 3;
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
        WORKING,
        DONE,
        ERROR
    }
}
