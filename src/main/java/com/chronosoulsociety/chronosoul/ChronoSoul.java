package com.chronosoulsociety.chronosoul;

import com.chronosoulsociety.chronosoul.data.PlayerCharacterData;
import com.chronosoulsociety.chronosoul.gameplay.registration.GameplayRegistration;
import com.chronosoulsociety.chronosoul.neomodules.registration.NeoModulesRegistration;
import com.chronosoulsociety.chronosoul.networking.CharacterSelectPayload;
import com.chronosoulsociety.chronosoul.networking.CharacterStatusSyncPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChronoSoul implements ModInitializer {
	public static final String MOD_ID = "chrono_soul";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Hello Fabric world!");
        
        // 1. 注册静态数据（服务端+客户端都需要）
        NeoModulesRegistration.register();
        
        // 2. 注册游戏玩法模块
        GameplayRegistration.register();
        
        // 3. 注册网络包Payload
        registerNetworkPayloads();
        
        // 4. 注册网络包处理
        registerNetworkPackets();
        
        // 5. 注册玩家加入事件
        registerPlayerJoinEvent();
    }
    
    /**
     * 注册网络包Payload类型
     */
    private void registerNetworkPayloads() {
        CharacterSelectPayload.register();
        CharacterStatusSyncPayload.register();
    }
    
    /**
     * 注册网络包处理
     */
    private void registerNetworkPackets() {
        // 处理客户端发送的角色选择请求
        ServerPlayNetworking.registerGlobalReceiver(
            CharacterSelectPayload.ID,
            (payload, context) -> {
                String characterId = payload.characterId();
                
                // 在主线程执行
                context.server().execute(() -> {
                    ServerPlayerEntity player = context.player();
                    PlayerCharacterData data = PlayerCharacterData.get(player);
                    
                    // 检查是否已经选择了角色
                    if (data.hasSelectedCharacter()) {
                        player.sendMessage(Text.literal("你已经选择了角色！"), true);
                        return;
                    }
                    
                    // 保存角色选择
                    data.setCharacter(characterId);
                    player.sendMessage(Text.literal("成功选择角色：" + characterId), true);
                    
                    // 发送角色状态同步包给客户端
                    syncCharacterStatus(player);
                });
            }
        );
    }
    
    /**
     * 注册玩家加入事件
     */
    private void registerPlayerJoinEvent() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            
            // 发送角色状态同步包给客户端
            syncCharacterStatus(player);
        });
    }
    
    /**
     * 同步角色状态给客户端
     * @param player 服务器玩家实体
     */
    private void syncCharacterStatus(ServerPlayerEntity player) {
        PlayerCharacterData data = PlayerCharacterData.get(player);
        
        // 构造响应包
        boolean hasSelected = data.hasSelectedCharacter();
        String characterId = hasSelected ? data.getCharacterId() : "";
        
        // 发送给客户端
        com.chronosoulsociety.chronosoul.networking.CharacterStatusSyncPayload payload = 
            new com.chronosoulsociety.chronosoul.networking.CharacterStatusSyncPayload(hasSelected, characterId);
        
        ServerPlayNetworking.send(player, payload);
    }
}