package com.chronosoulsociety.chronosoul.client;

import com.chronosoulsociety.chronosoul.client.gui.CharacterSelectScreen;
import com.chronosoulsociety.chronosoul.core.gui.GuiEventRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ChronoSoulClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // 注册GUI事件
        GuiEventRegistry.registerGuiEvents();
        
        // 注册网络包接收处理
        registerClientNetworkPackets();
    }
    
    /**
     * 注册客户端网络包接收处理
     */
    private void registerClientNetworkPackets() {
        // 处理服务器发送的角色状态同步包
        ClientPlayNetworking.registerGlobalReceiver(
            com.chronosoulsociety.chronosoul.networking.CharacterStatusSyncPayload.ID,
            (payload, context) -> {
                boolean hasSelected = payload.hasSelected();
                String characterId = payload.characterId();
                
                // 在主线程执行GUI相关操作
                context.client().execute(() -> {
                    if (!hasSelected) {
                        // 弹出角色选择界面
                        context.client().setScreen(new CharacterSelectScreen());
                    } else {
                        // 缓存角色ID供技能栏使用
                        ClientCharacterManager.setActiveCharacter(characterId);
                    }
                });
            }
        );
    }
}
