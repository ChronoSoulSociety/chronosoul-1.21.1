package com.chronosoulsociety.chronosoul.client;

import com.chronosoulsociety.chronosoul.core.gui.GuiEventRegistry;
import net.fabricmc.api.ClientModInitializer;

public class ChronoSoulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 注册GUI事件
        GuiEventRegistry.registerGuiEvents();
    }
}
