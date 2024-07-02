package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import org.jetbrains.annotations.NotNull;

public class ProgressBar extends GuiComponent implements Widget {
    public float progress;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private static final int outlineThickness = 1;
    private static final int innerMargin = 2;

    private static final int outlineColor = -1;
    private static final int fillColor = -1;

    public ProgressBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(@NotNull PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        GuiComponent.fill(pPoseStack, x, y, x + width, y + outlineThickness, outlineColor);
        GuiComponent.fill(pPoseStack, x, y + height - outlineThickness, x + width, y + height, outlineColor);
        GuiComponent.fill(pPoseStack, x, y + outlineThickness, x + outlineThickness, y + height - outlineThickness, outlineColor);
        GuiComponent.fill(pPoseStack, x + width - outlineThickness, y + outlineThickness, x + width, y + height - outlineThickness, outlineColor);

        GuiComponent.fill(pPoseStack, x + innerMargin, y + innerMargin, x + (int) ((width - innerMargin) * progress), y + height - innerMargin, fillColor);
    }
}
