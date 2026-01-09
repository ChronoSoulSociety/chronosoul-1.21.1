package com.chronosoulsociety.chronosoul.networking;

import net.minecraft.util.Identifier;

/**
 * 网络包定义类，用于管理所有Mod的网络包标识符
 */
public class ModPackets {
    // 同步角色状态包
    public static final Identifier SYNC_CHARACTER_STATUS = Identifier.of("chronosoul", "sync_char_status");
    // 选择角色请求包
    public static final Identifier SELECT_CHARACTER = Identifier.of("chronosoul", "select_character");
}