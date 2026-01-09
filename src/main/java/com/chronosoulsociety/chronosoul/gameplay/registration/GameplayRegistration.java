package com.chronosoulsociety.chronosoul.gameplay.registration;

import com.chronosoulsociety.chronosoul.gameplay.character.PlayerCharacterComponent;

/**
 * 游戏玩法模块注册总线
 */
public class GameplayRegistration {

    /**
     * 由主类调用，完成所有gameplay内容的初始化
     */
    public static void register() {
        // 未来可扩展：
        // Effects.register();
        // Mechanics.register();
        // CombatSystem.register();
        
        // 注册玩家角色组件
        PlayerCharacterComponent.register();
    }
}