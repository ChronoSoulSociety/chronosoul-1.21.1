package com.chronosoulsociety.chronosoul.client.gui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * 角色选择界面，用于玩家选择角色
 */
public class CharacterSelectScreen extends Screen {
    
    public CharacterSelectScreen() {
        super(Text.translatable("gui.chronosoul.character_select.title"));
    }

    @Override
    protected void init() {
        super.init();
        
        // 计算屏幕中心位置
        int centerX = this.width / 2;
        int centerY = this.height / 2;
        
        // 添加角色选择按钮 - 青籁
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.chronosoul.character.qinglai"),
                button -> this.selectCharacter("qing_lai")
            )
            .position(centerX - 100, centerY - 30)
            .size(200, 20)
            .build()
        );
        
        // 添加角色选择按钮 - 素琳
        this.addDrawableChild(ButtonWidget.builder(
                Text.translatable("gui.chronosoul.character.sulin"),
                button -> this.selectCharacter("su_lin")
            )
            .position(centerX - 100, centerY + 10)
            .size(200, 20)
            .build()
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // 绘制背景
        super.renderBackground(context, mouseX, mouseY, delta);
        
        // 绘制标题
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                40,
                0xFFFFFF
        );
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    /**
     * 处理角色选择逻辑
     * @param characterId 选择的角色ID
     */
    private void selectCharacter(String characterId) {
        // 创建并发送选择角色请求包
        com.chronosoulsociety.chronosoul.networking.CharacterSelectPayload payload = 
            new com.chronosoulsociety.chronosoul.networking.CharacterSelectPayload(characterId);
        ClientPlayNetworking.send(payload);
        
        // 关闭GUI
        this.close();
    }
    
    @Override
    public boolean shouldPause() {
        // 角色选择界面不需要暂停游戏
        return false;
    }
}