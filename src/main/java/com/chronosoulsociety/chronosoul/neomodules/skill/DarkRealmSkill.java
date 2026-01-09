package com.chronosoulsociety.chronosoul.neomodules.skill;

import com.chronosoulsociety.chronosoul.gameplay.character.ActiveCharacter;

public class DarkRealmSkill extends Skill {
    private static final int MANA_COST = 150;
    private static final int COOLDOWN_TICKS = 60 * 20; // 60秒
    private static final int DURATION_TICKS = 8 * 20; // 8秒
    
    private long lastUseTick = -COOLDOWN_TICKS;
    private boolean isActive = false;
    
    public DarkRealmSkill() {
        super(
            "qing_lai_dark_realm",
            "暗域展开",
            "消耗150点法力，进入暗域状态8秒，期间获得20%全属性提升，解锁三段跳和突进能力。",
            SkillType.BUFF_SKILL,
            MANA_COST,
            COOLDOWN_TICKS,
            DURATION_TICKS
        );
    }
    
    @Override
    public boolean execute(ActiveCharacter character) {
        // 检查是否可以执行技能
        if (!canExecute(character)) {
            return false;
        }
        
        // 扣除法力
        character.getCurrentAttributes().setMana(character.getCurrentAttributes().getMana() - manaCost);
        
        // 进入暗域状态
        character.enterDarkRealm();
        
        // 记录使用时间
        this.lastUseTick = System.currentTimeMillis() / 50;
        this.isActive = true;
        
        return true;
    }
    
    @Override
    public void update(ActiveCharacter character) {
        if (!isActive) {
            return;
        }
        
        // 更新暗域持续时间
        if (character.getDarkRealmDuration() > 0) {
            character.setDarkRealmDuration(character.getDarkRealmDuration() - 1);
        } else {
            // 暗域时间结束
            end(character);
        }
        
        // 处理三段跳重置逻辑
        long currentTick = System.currentTimeMillis() / 50;
        if (character.getLastGroundTime() > 0 && currentTick - character.getLastGroundTime() >= 10) { // 0.5秒
            character.resetJumpCount();
        }
    }
    
    @Override
    public void end(ActiveCharacter character) {
        // 退出暗域状态
        character.exitDarkRealm();
        
        // 重置技能状态
        this.isActive = false;
    }
    
    @Override
    public boolean canExecute(ActiveCharacter character) {
        // 检查法力和冷却
        long currentTick = System.currentTimeMillis() / 50;
        return super.canExecute(character) && 
               (currentTick - lastUseTick) >= cooldownTicks;
    }
    
    @Override
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public void reset() {
        this.isActive = false;
        this.lastUseTick = -COOLDOWN_TICKS;
    }
}