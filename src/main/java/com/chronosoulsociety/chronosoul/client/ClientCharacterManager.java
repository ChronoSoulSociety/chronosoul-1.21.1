package com.chronosoulsociety.chronosoul.client;

/**
 * 客户端角色管理器，用于缓存当前激活的角色ID
 */
public class ClientCharacterManager {
    private static String activeCharacter = null;

    /**
     * 设置当前激活的角色ID
     * @param id 角色ID
     */
    public static void setActiveCharacter(String id) {
        activeCharacter = id;
    }

    /**
     * 获取当前激活的角色ID
     * @return 角色ID
     */
    public static String getActiveCharacter() {
        return activeCharacter;
    }

    /**
     * 清除当前激活的角色ID
     */
    public static void clearActiveCharacter() {
        activeCharacter = null;
    }
}