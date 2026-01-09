package com.chronosoulsociety.chronosoul.core.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;

@Environment(EnvType.CLIENT)
public class GuiEventRegistry {
    private static QingLaiSkillBarHud qingLaiSkillBarHud;
    
    public static void registerGuiEvents() {
        MinecraftClient client = MinecraftClient.getInstance();
        qingLaiSkillBarHud = new QingLaiSkillBarHud(client);
        
        // 注册HUD渲染回调
        HudRenderCallback.EVENT.register((drawContext, renderTickCounter) -> {
            if (qingLaiSkillBarHud != null) {
                // 获取tickDelta值，参数true表示是否包含部分tick
                float tickDelta = renderTickCounter.getTickDelta(true);
                qingLaiSkillBarHud.render(drawContext, tickDelta);
            }
        });
    }
}
