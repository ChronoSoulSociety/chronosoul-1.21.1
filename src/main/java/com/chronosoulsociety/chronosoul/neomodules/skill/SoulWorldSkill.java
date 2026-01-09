package com.chronosoulsociety.chronosoul.neomodules.skill;

import com.chronosoulsociety.chronosoul.gameplay.character.ActiveCharacter;

public class SoulWorldSkill extends Skill {
    // 基础参数
    private static final int COOLDOWN_TICKS = 35 * 20; // 35秒
    private static final int SHORT_PRESS_THRESHOLD = 300; // 0.3秒（毫秒）
    
    // 三段连击参数
    private static final int[] COMBO_MANA_COST = {25, 35, 45}; // 每段耗蓝
    private static final int COMBO_DAMAGE_PER_HIT = 20; // 每段伤害
    private static final int MAX_FLOAT_SEGMENTS = 2; // 最多两次浮空
    
    // 蓄力攻击参数
    private static final int CHARGE_MANA_PER_SECOND = 45; // 每秒耗蓝
    private static final int MAX_CHARGE_TIME = 5 * 1000; // 最大蓄力时间（毫秒）
    private static final float BASE_DAMAGE = 40f; // 基础伤害
    private static final float MAX_EXTRA_DAMAGE = 160f; // 最大额外伤害
    
    // 技能状态
    private enum SkillState {
        IDLE,
        SHORT_PRESS_WAITING, // 等待短按/长按判定
        COMBO_MODE, // 三段连击模式
        CHARGE_MODE, // 蓄力攻击模式
        RECOVERY // 恢复状态
    }
    
    private SkillState state = SkillState.IDLE;
    private long pressStartTime = 0; // 按键按下时间
    private int comboStage = 0; // 当前连击阶段
    private int floatSegmentsUsed = 0; // 已使用的浮空次数
    private long chargeStartTime = 0; // 蓄力开始时间
    private long lastUseTick = -COOLDOWN_TICKS; // 上次使用时间
    private boolean isActive = false;
    
    public SoulWorldSkill() {
        super(
            "qing_lai_soul_world",
            "魂世",
            "青籁的核心输出技能，兼具高频连段与高爆发蓄力两种玩法。短按进入三段连击，长按进入蓄力攻击。",
            SkillType.ELEMENTAL_SKILL,
            25, // 基础耗蓝（实际根据模式变化）
            COOLDOWN_TICKS
        );
    }
    
    @Override
    public boolean execute(ActiveCharacter character) {
        // 检查冷却
        long currentTick = System.currentTimeMillis() / 50;
        if ((currentTick - lastUseTick) < cooldownTicks) {
            return false;
        }
        
        // 重置技能状态
        resetState();
        
        // 记录按下时间，进入等待状态
        this.pressStartTime = System.currentTimeMillis();
        this.state = SkillState.SHORT_PRESS_WAITING;
        this.isActive = true;
        this.lastUseTick = currentTick;
        
        return true;
    }
    
    /**
     * 处理技能释放（用于短按/长按判定）
     */
    public void handleRelease(ActiveCharacter character) {
        if (state != SkillState.SHORT_PRESS_WAITING) {
            return;
        }
        
        long pressDuration = System.currentTimeMillis() - pressStartTime;
        
        if (pressDuration < SHORT_PRESS_THRESHOLD) {
            // 短按：进入三段连击模式
            enterComboMode(character);
        } else {
            // 长按：进入蓄力攻击模式
            enterChargeMode(character);
        }
    }
    
    /**
     * 进入三段连击模式
     */
    private void enterComboMode(ActiveCharacter character) {
        this.state = SkillState.COMBO_MODE;
        this.comboStage = 1;
        
        // 消耗第一段法力
        if (character.getCurrentAttributes().getMana() < COMBO_MANA_COST[0]) {
            // 法力不足，中断
            end(character);
            return;
        }
        
        character.getCurrentAttributes().setMana(
            character.getCurrentAttributes().getMana() - COMBO_MANA_COST[0]
        );
        
        // 执行第一段连击
        executeComboStage(character, comboStage);
    }
    
    /**
     * 执行连击阶段
     */
    @SuppressWarnings("unused")
    private void executeComboStage(ActiveCharacter character, int stage) {
        // 处理当前阶段的伤害和效果
        // 计算连击伤害：基础伤害 + 阶段加成
        float comboDamage = COMBO_DAMAGE_PER_HIT * (1 + (stage - 1) * 0.3f); // 每段伤害递增30%
        
        // 最后一段有额外加成
        if (stage == 3) {
            comboDamage *= 1.5f; // 最后一段伤害提高50%
            this.state = SkillState.RECOVERY;
        }
        
        // 触发伤害结算
        // 这里简化实现，实际需要触发攻击动画和伤害结算
        // 伤害值将在后续的伤害系统中使用
        // ChronoSoul.LOGGER.info("Soul World combo stage {} damage: {}", stage, comboDamage);
    }
    
    /**
     * 进入蓄力攻击模式
     */
    private void enterChargeMode(ActiveCharacter character) {
        this.state = SkillState.CHARGE_MODE;
        this.chargeStartTime = System.currentTimeMillis();
    }
    
    /**
     * 结束蓄力攻击
     */
    public void endCharge(ActiveCharacter character) {
        if (state != SkillState.CHARGE_MODE) {
            return;
        }
        
        // 计算蓄力时间
        long chargeTime = System.currentTimeMillis() - chargeStartTime;
        float normalizedChargeTime = Math.min(1.0f, (float) chargeTime / MAX_CHARGE_TIME);
        
        // 计算消耗的法力
        int manaCost = (int) (CHARGE_MANA_PER_SECOND * (chargeTime / 1000.0f));
        manaCost = Math.max(0, Math.min(manaCost, character.getCurrentAttributes().getMana()));
        
        // 扣除法力
        character.getCurrentAttributes().setMana(
            character.getCurrentAttributes().getMana() - manaCost
        );
        
        // 计算伤害
        @SuppressWarnings("unused")
        float damage = BASE_DAMAGE + MAX_EXTRA_DAMAGE * normalizedChargeTime;
        
        // 触发伤害结算
        // 这里简化实现，实际需要触发攻击动画和伤害结算
        // 伤害值将在后续的伤害系统中使用
        // ChronoSoul.LOGGER.info("Soul World attack damage: {}", damage);
        
        // 进入恢复状态
        this.state = SkillState.RECOVERY;
    }
    
    /**
     * 处理跳跃动作，用于连击浮空
     */
    public boolean handleJump(ActiveCharacter character) {
        if (state != SkillState.COMBO_MODE || floatSegmentsUsed >= MAX_FLOAT_SEGMENTS) {
            return false;
        }
        
        // 触发浮空
        floatSegmentsUsed++;
        
        // 进入下一阶段
        comboStage++;
        
        // 检查法力是否足够
        if (comboStage > 3 || character.getCurrentAttributes().getMana() < COMBO_MANA_COST[comboStage - 1]) {
            // 连击结束或法力不足
            end(character);
            return false;
        }
        
        // 消耗当前阶段法力
        character.getCurrentAttributes().setMana(
            character.getCurrentAttributes().getMana() - COMBO_MANA_COST[comboStage - 1]
        );
        
        // 执行下一阶段连击
        executeComboStage(character, comboStage);
        
        return true;
    }
    
    @Override
    public void update(ActiveCharacter character) {
        if (!isActive) {
            return;
        }
        
        switch (state) {
            case IDLE:
                // 空闲状态，无需处理
                break;
                
            case SHORT_PRESS_WAITING:
                // 检查是否超过长按阈值
                if (System.currentTimeMillis() - pressStartTime >= SHORT_PRESS_THRESHOLD) {
                    enterChargeMode(character);
                }
                break;
                
            case COMBO_MODE:
                // 连击模式处理
                break;
                
            case CHARGE_MODE:
                // 持续消耗法力
                long chargeTime = System.currentTimeMillis() - chargeStartTime;
                // 已使用计算出的法力消耗
                
                // 检查法力是否耗尽
                if (character.getCurrentAttributes().getMana() <= 0) {
                    endCharge(character);
                }
                
                // 检查是否达到最大蓄力时间
                if (chargeTime >= MAX_CHARGE_TIME) {
                    endCharge(character);
                }
                break;
                
            case RECOVERY:
                // 恢复状态，一段时间后回到 idle
                end(character);
                break;
        }
    }
    
    @Override
    public void end(ActiveCharacter character) {
        // 重置技能状态
        resetState();
        this.isActive = false;
    }
    
    @Override
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public void reset() {
        resetState();
        this.isActive = false;
        this.lastUseTick = -COOLDOWN_TICKS;
    }
    
    /**
     * 重置技能状态
     */
    private void resetState() {
        this.state = SkillState.IDLE;
        this.comboStage = 0;
        this.floatSegmentsUsed = 0;
        this.chargeStartTime = 0;
        this.pressStartTime = 0;
    }
    
    // 状态查询方法
    public SkillState getState() {
        return state;
    }
    
    public int getComboStage() {
        return comboStage;
    }
    
    public int getFloatSegmentsUsed() {
        return floatSegmentsUsed;
    }
    
    public long getPressStartTime() {
        return pressStartTime;
    }
    
    public long getChargeStartTime() {
        return chargeStartTime;
    }
}
