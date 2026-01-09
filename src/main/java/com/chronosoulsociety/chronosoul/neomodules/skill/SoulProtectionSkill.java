package com.chronosoulsociety.chronosoul.neomodules.skill;

import com.chronosoulsociety.chronosoul.gameplay.character.ActiveCharacter;

public class SoulProtectionSkill extends Skill {
    private static final int SHIELD_VALUE = 150;
    private static final int COOLDOWN_TICKS = 40 * 20; // 40秒
    private static final int MANA_COST = 100;
    
    private long lastUseTick = -COOLDOWN_TICKS;
    private boolean isActive = false;
    
    public SoulProtectionSkill() {
        super(
            "qing_lai_soul_protection",
            "回魂保护",
            "消耗100点法力，生成一个可以吸收150点伤害的护盾，持续至护盾耗尽或角色死亡。",
            SkillType.DEFENSIVE_SKILL,
            MANA_COST,
            COOLDOWN_TICKS
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
        
        // 设置护盾值
        character.setTemporaryShield(SHIELD_VALUE);
        
        // 记录使用时间
        this.lastUseTick = System.currentTimeMillis() / 50; // Minecraft tick = 50ms
        this.isActive = true;
        
        return true;
    }
    
    @Override
    public void update(ActiveCharacter character) {
        // 检查护盾是否耗尽
        if (character.getTemporaryShield() <= 0 && isActive) {
            this.isActive = false;
        }
    }
    
    @Override
    public void end(ActiveCharacter character) {
        // 护盾只能通过耗尽或死亡消失，技能本身没有结束机制
        // 但可以在这里处理一些清理工作
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