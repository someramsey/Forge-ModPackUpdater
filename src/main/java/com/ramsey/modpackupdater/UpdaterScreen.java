package com.ramsey.modpackupdater;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class UpdaterScreen extends Screen {
    protected UpdaterScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 2 - 50, 200, 20, Component.literal("dw"), button -> {


            System.out.println("dw");
        }));
    }

}
