package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PopupScreen extends Screen {
    private static final Component updateAvailableMessage = Component.translatable("gui.updater.update_available");

    private MultiLineLabel message;
    private Button button;

    public PopupScreen() {
        super(Component.literal("dw"));
    }

    @Override
    protected void init() {
        this.message = MultiLineLabel.create(this.font, updateAvailableMessage, this.width - 50);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderDirtBackground(0);

        this.message.renderCentered(pPoseStack, this.width / 2, 90);

//
//        int width = 150;
//
//        this.addRenderableWidget(new Button((this.width - width) / 2, 120, width, 20, Component.literal("text"), (button) -> {
//
//        }));
        renderLoadingBar(pPoseStack, (this.width - 150) / 2, 120, 150, 10, 0.5f);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }

    private void renderLoadingBar(PoseStack pPoseStack, int pX, int pY, int pWidth, int pHeight, float pProgress) {
        int margin = 1;
        int innerMargin = 2;

        int fillColor = -1;
        int backgroundColor = -16777216;

        GuiComponent.fill(pPoseStack, pX, pY, pX + pWidth, pY + pHeight, fillColor);
        GuiComponent.fill(pPoseStack, pX + margin, pY + margin, pX + pWidth - margin, pY + pHeight - margin, backgroundColor);
        GuiComponent.fill(pPoseStack, pX + innerMargin, pY + innerMargin, (int) (pX + (pWidth * pProgress) - innerMargin), pY + pHeight - innerMargin, fillColor);
    }
}
