package com.chronosoulsociety.chronosoul.neomodules.registration;

import com.chronosoulsociety.chronosoul.neomodules.character.CharacterManager;
import com.chronosoulsociety.chronosoul.neomodules.element.ElementModule;

/**
 * 静态数据注册总线（仅用于注册模板类，非运行时实例）
 */
public class NeoModulesRegistration {

    /**
     * 由主类调用，完成所有 neomodules 内容的初始化
     */
    public static void register() {
        // 注册角色模板
        CharacterManager.initialize();
        
        // 注册元素模块
        ElementModule.register();

        // 未来可扩展：
    }
}