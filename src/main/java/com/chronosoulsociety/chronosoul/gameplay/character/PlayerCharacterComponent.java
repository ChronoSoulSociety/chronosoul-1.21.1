package com.chronosoulsociety.chronosoul.gameplay.character;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.entity.player.PlayerEntity;

public class PlayerCharacterComponent {
    private final PlayerEntity player;
    private ActiveCharacter activeCharacter;
    private boolean hasActiveCharacter = false;
    
    public PlayerCharacterComponent(PlayerEntity player) {
        this.player = player;
    }
    
    public boolean hasActiveCharacter() {
        return hasActiveCharacter;
    }
    
    public ActiveCharacter getActiveCharacter() {
        return activeCharacter;
    }
    
    public void setActiveCharacter(ActiveCharacter activeCharacter) {
        this.activeCharacter = activeCharacter;
        this.hasActiveCharacter = activeCharacter != null;
    }
    
    public void removeActiveCharacter() {
        this.activeCharacter = null;
        this.hasActiveCharacter = false;
    }
    
    public void update() {
        if (activeCharacter != null) {
            activeCharacter.update();
        }
    }
    
    public void readFromNbt(NbtCompound nbt) {
        if (nbt.contains("HasActiveCharacter")) {
            hasActiveCharacter = nbt.getBoolean("HasActiveCharacter");
            if (hasActiveCharacter && nbt.contains("ActiveCharacter")) {
                activeCharacter = ActiveCharacter.fromNbt(nbt.getCompound("ActiveCharacter"));
            }
        }
    }
    
    public void writeToNbt(NbtCompound nbt) {
        nbt.putBoolean("HasActiveCharacter", hasActiveCharacter);
        if (hasActiveCharacter && activeCharacter != null) {
            nbt.put("ActiveCharacter", activeCharacter.toNbt());
        }
    }
    
    // 为了简化测试，添加一个静态方法来获取组件
    private static final ThreadLocal<PlayerCharacterComponent> TEST_COMPONENT = new ThreadLocal<>();
    
    public static void setTestComponent(PlayerCharacterComponent component) {
        TEST_COMPONENT.set(component);
    }
    
    public static PlayerCharacterComponent get(PlayerEntity player) {
        // 实际项目中应该使用Fabric的Component API
        // 这里暂时使用测试组件，后续替换为真实的组件获取
        return TEST_COMPONENT.get();
    }
}