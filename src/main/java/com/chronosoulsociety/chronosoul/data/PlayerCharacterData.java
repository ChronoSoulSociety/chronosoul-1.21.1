package com.chronosoulsociety.chronosoul.data;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.nbt.NbtCompound;

/**
 * 玩家角色数据管理类，用于存储和检测玩家的角色选择状态
 */
public class PlayerCharacterData {
    private static final String NBT_KEY = "ChronoSoul_Character";
    
    private final ServerPlayerEntity player;
    private String characterId = null;

    public static PlayerCharacterData get(ServerPlayerEntity player) {
        return new PlayerCharacterData(player);
    }

    private PlayerCharacterData(ServerPlayerEntity player) {
        this.player = player;
        load();
    }

    /**
     * 检查玩家是否已经选择了角色
     * @return 是否已选择角色
     */
    public boolean hasSelectedCharacter() {
        return characterId != null && !characterId.isEmpty();
    }

    /**
     * 获取玩家选择的角色ID
     * @return 角色ID
     */
    public String getCharacterId() {
        return characterId;
    }

    /**
     * 设置玩家的角色ID
     * @param id 角色ID
     */
    public void setCharacter(String id) {
        this.characterId = id;
        save();
    }

    /**
     * 从玩家的持久化数据中加载角色信息
     */
    private void load() {
        // 直接从玩家的NBT数据中读取
        NbtCompound playerData = player.writeNbt(new NbtCompound());
        if (playerData.contains(NBT_KEY)) {
            this.characterId = playerData.getString(NBT_KEY);
        }
    }

    /**
     * 将角色信息保存到玩家的持久化数据中
     */
    private void save() {
        // 直接写入玩家的NBT数据
        NbtCompound playerData = player.writeNbt(new NbtCompound());
        if (characterId != null) {
            playerData.putString(NBT_KEY, characterId);
        } else {
            playerData.remove(NBT_KEY);
        }
        // 保存回玩家对象
        player.readNbt(playerData);
    }
}