package com.ramsey.updater;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;

public class ProgressBar extends GuiComponent implements Widget {
    public float progress;

    private final int x;
    private final int y;
    private final int width;
    private final int height;

    private static final int outerMargin = 1;
    private static final int innerMargin = 2;

    private static final int outlineColor = -1;
    private static final int fillColor = -1;
    private static final int backgroundColor = -16777216;

    public ProgressBar(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        GuiComponent.fill(pPoseStack, x, y, x + width, y + height, outlineColor);
        GuiComponent.fill(pPoseStack, x + outerMargin, y + outerMargin, x + width - outerMargin, y + height - outerMargin, backgroundColor);
        GuiComponent.fill(pPoseStack, x + innerMargin, y + innerMargin, (int) (x + ((width - innerMargin) * progress)), y + height - innerMargin, fillColor);
    }
}
