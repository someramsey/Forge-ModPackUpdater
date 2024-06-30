package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class PopupScreen extends Screen {
    private static final Component updateAvailableMessage = Component.translatable("gui.updater.update_available");

    private State state = State.Confirm;
    private MultiLineLabel statusLabel;
    private Button continueButton;

    public PopupScreen() {
        super(Component.translatable("gui.updater.title"));
    }

    @Override
    protected void init() {
        this.statusLabel = MultiLineLabel.create(this.font, updateAvailableMessage, this.width - 50);
    }

    private void renderDownloadButton() {
        this.continueButton = this.addRenderableWidget(new Button(this.width / 2 - 100, this.height - 25, 200, 20, Component.translatable("gui.updater.download"), (button) -> {

        }));
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderDirtBackground(0);

        this.statusLabel.renderCentered(pPoseStack, this.width / 2, 90);

        switch (this.state) {
            case Confirm:
                this.renderDownloadButton();
                break;
                case Downloading:
                renderLoadingBar(pPoseStack,0.5f);
        }

        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private void renderLoadingBar(PoseStack pPoseStack, float pProgress) {
        int x = (this.width - 150) / 2;
        int y = 120;

        int width = 150;
        int height = 10;

        int margin = 1;
        int innerMargin = 2;

        int fillColor = -1;
        int backgroundColor = -16777216;

        GuiComponent.fill(pPoseStack, x, y, x + width, y + height, fillColor);
        GuiComponent.fill(pPoseStack, x + margin, y + margin, x + width - margin, y + height - margin, backgroundColor);
        GuiComponent.fill(pPoseStack, x + innerMargin, y + innerMargin, (int) (x + (width * pProgress) - innerMargin), y + height - innerMargin, fillColor);
    }

    private enum State {
        Confirm,
        Downloading
    }
}
