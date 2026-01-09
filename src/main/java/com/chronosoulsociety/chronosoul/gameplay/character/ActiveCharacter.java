package com.chronosoulsociety.chronosoul.gameplay.character;

import com.chronosoulsociety.chronosoul.neomodules.character.CharacterAttributes;
import com.chronosoulsociety.chronosoul.neomodules.character.CharacterManager;
import com.chronosoulsociety.chronosoul.neomodules.character.CharacterTemplate;
import com.chronosoulsociety.chronosoul.neomodules.effect.StatusEffect;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;
import java.util.List;

public class ActiveCharacter {
    private String templateId;
    private CharacterAttributes currentAttributes;
    private int level = 1;
    private long experience = 0;
    private float elementalEnergy = 0;
    private int ascensionLevel = 0; // 突破层数，预留突破系统接口
    private final List<StatusEffect> statusEffects;
    
    // 回魂保护技能相关字段
    private int temporaryShield = 0; // 当前护盾值
    
    // 暗域展开技能相关字段
    private boolean inDarkRealm = false; // 是否处于暗域状态
    private int darkRealmDuration = 0; // 暗域剩余持续时间
    private CharacterAttributes originalAttributes; // 暗域开启前的原始属性
    
    // 三段跳状态
    private int jumpCount = 0;
    private long lastGroundTime = 0;
    
    // 突进状态
    private boolean canDash = true;
    private long lastDashUseTime = 0;
    
    public ActiveCharacter(String templateId) {
        CharacterTemplate template = CharacterManager.getCharacterTemplate(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Invalid character template ID: " + templateId);
        }
        
        this.templateId = templateId;
        this.currentAttributes = template.createAttributesCopy();
        this.statusEffects = new ArrayList<>();
    }
    
    public String getTemplateId() {
        return templateId;
    }
    
    public CharacterAttributes getCurrentAttributes() {
        return currentAttributes;
    }
    
    public int getLevel() {
        return level;
    }
    
    public long getExperience() {
        return experience;
    }
    
    public void addExperience(long amount) {
        this.experience += amount;
        checkLevelUp();
    }
    
    private void checkLevelUp() {
        // 使用while循环支持多级升级
        while (true) {
            long requiredExp = calculateRequiredExperience(level + 1);
            if (experience >= requiredExp) {
                levelUp();
            } else {
                break;
            }
        }
    }
    
    private long calculateRequiredExperience(int targetLevel) {
        // 简单的经验值计算公式：100 * (level^2)
        return 100L * targetLevel * targetLevel;
    }
    
    public void levelUp() {
        // 获取角色模板
        CharacterTemplate template = CharacterManager.getCharacterTemplate(templateId);
        if (template == null) {
            return;
        }
        
        // 检查是否已达最大等级
        if (level >= template.getMaxLevel()) {
            return;
        }
        
        int newLevel = level + 1;
        long expToReachNewLevel = calculateRequiredExperience(newLevel);
        
        // 获取成长配置
        var growth = template.getLevelGrowth();
        
        // 升级
        level = newLevel;
        // 使用非线性成长，根据当前等级获取实际增量
        currentAttributes.addHealth(growth.getHealthGrowthAtLevel(level));
        currentAttributes.addMana(growth.getManaGrowthAtLevel(level));
        currentAttributes.addDefense(growth.getDefenseGrowthAtLevel(level));
        currentAttributes.setDodgeChance(currentAttributes.getDodgeChance() + growth.getDodgeChanceGrowthAtLevel(level));
        currentAttributes.setCritChance(currentAttributes.getCritChance() + growth.getCritChanceGrowthAtLevel(level));
        
        // 扣除经验
        experience -= expToReachNewLevel;
    }
    
    public float getElementalEnergy() {
        return elementalEnergy;
    }
    
    /**
     * 添加元素能量
     * @param amount 要添加的能量值
     */
    public void addElementalEnergy(float amount) {
        this.elementalEnergy += amount;
        // 从角色模板获取最大能量值，默认为100
        CharacterTemplate template = CharacterManager.getCharacterTemplate(templateId);
        float maxEnergy = template != null ? 100 : 100; // 后续可扩展为模板配置
        if (this.elementalEnergy > maxEnergy) {
            this.elementalEnergy = maxEnergy;
        }
    }
    
    /**
     * 消耗元素能量，用于释放主动技能
     * @param amount 要消耗的能量值
     * @return 是否成功消耗（能量足够）
     */
    public boolean consumeElementalEnergy(float amount) {
        if (this.elementalEnergy >= amount) {
            this.elementalEnergy -= amount;
            return true;
        }
        return false;
    }
    
    /**
     * 检查是否可以释放技能
     * @param skillCost 技能消耗的能量值
     * @return 是否可以释放技能
     */
    public boolean canUseSkill(float skillCost) {
        return this.elementalEnergy >= skillCost;
    }
    
    /**
     * 获取元素能量百分比
     * @return 能量百分比（0.0-1.0）
     */
    public float getElementalEnergyPercentage() {
        CharacterTemplate template = CharacterManager.getCharacterTemplate(templateId);
        float maxEnergy = template != null ? 100 : 100;
        return this.elementalEnergy / maxEnergy;
    }
    
    /**
     * 重置元素能量
     */
    public void resetElementalEnergy() {
        this.elementalEnergy = 0;
    }
    
    /**
     * 获取突破层数
     * @return 突破层数
     */
    public int getAscensionLevel() {
        return ascensionLevel;
    }
    
    /**
     * 设置突破层数
     * @param ascensionLevel 突破层数
     */
    public void setAscensionLevel(int ascensionLevel) {
        this.ascensionLevel = ascensionLevel;
    }
    
    // 回魂保护技能相关方法
    public int getTemporaryShield() {
        return temporaryShield;
    }
    
    public void setTemporaryShield(int temporaryShield) {
        this.temporaryShield = Math.max(0, temporaryShield);
    }
    
    public void addTemporaryShield(int amount) {
        this.temporaryShield = Math.max(0, this.temporaryShield + amount);
    }
    
    public void clearTemporaryShield() {
        this.temporaryShield = 0;
    }
    
    // 暗域展开技能相关方法
    public boolean isInDarkRealm() {
        return inDarkRealm;
    }
    
    public int getDarkRealmDuration() {
        return darkRealmDuration;
    }
    
    public void setInDarkRealm(boolean inDarkRealm) {
        this.inDarkRealm = inDarkRealm;
    }
    
    public void setDarkRealmDuration(int darkRealmDuration) {
        this.darkRealmDuration = darkRealmDuration;
    }
    
    public void enterDarkRealm() {
        // 记录原始属性
        this.originalAttributes = new CharacterAttributes(
            currentAttributes.getHealth(),
            currentAttributes.getDefense(),
            currentAttributes.getMana(),
            currentAttributes.getDodgeChance(),
            currentAttributes.getCritChance(),
            currentAttributes.getCritDamage()
        );
        
        // 应用20%属性提升
        currentAttributes.setHealth(Math.round(currentAttributes.getHealth() * 1.2f));
        currentAttributes.setDefense(Math.round(currentAttributes.getDefense() * 1.2f));
        currentAttributes.setMana(Math.round(currentAttributes.getMana() * 1.2f));
        currentAttributes.setDodgeChance(currentAttributes.getDodgeChance() * 1.2f);
        currentAttributes.setCritChance(currentAttributes.getCritChance() * 1.2f);
        
        // 设置暗域状态
        this.inDarkRealm = true;
        this.darkRealmDuration = 8 * 20; // 8秒，20ticks=1秒
        
        // 重置跳数和突进状态
        resetJumpCount();
        setCanDash(true);
    }
    
    public void exitDarkRealm() {
        if (!inDarkRealm) return;
        
        // 还原原始属性
        if (originalAttributes != null) {
            this.currentAttributes = originalAttributes;
        }
        
        // 清除暗域状态
        this.inDarkRealm = false;
        this.darkRealmDuration = 0;
        this.originalAttributes = null;
        
        // 重置跳数和突进状态
        resetJumpCount();
        setCanDash(true);
    }
    
    // 三段跳相关方法
    public int getJumpCount() {
        return jumpCount;
    }
    
    public void setJumpCount(int jumpCount) {
        this.jumpCount = jumpCount;
    }
    
    public long getLastGroundTime() {
        return lastGroundTime;
    }
    
    public void setLastGroundTime(long lastGroundTime) {
        this.lastGroundTime = lastGroundTime;
    }
    
    public void resetJumpCount() {
        this.jumpCount = 0;
    }
    
    // 突进相关方法
    public boolean canDash() {
        return canDash && inDarkRealm;
    }
    
    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }
    
    public long getLastDashUseTime() {
        return lastDashUseTime;
    }
    
    public void setLastDashUseTime(long lastDashUseTime) {
        this.lastDashUseTime = lastDashUseTime;
    }
    
    // 重置所有技能状态
    public void resetAllSkillStates() {
        clearTemporaryShield();
        exitDarkRealm();
        resetJumpCount();
        setCanDash(true);
    }
    
    public List<StatusEffect> getStatusEffects() {
        return new ArrayList<>(statusEffects);
    }
    
    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
        applyStatusEffect(effect);
    }
    
    public void removeStatusEffect(StatusEffect effect) {
        if (statusEffects.remove(effect)) {
            revertStatusEffect(effect);
        }
    }
    
    private void applyStatusEffect(StatusEffect effect) {
        // 根据状态效果类型应用不同的属性变化
        switch (effect.getType()) {
            case BUFF:
                currentAttributes.addHealth(effect.getHealthModifier());
                currentAttributes.addDefense(effect.getDefenseModifier());
                currentAttributes.addMana(effect.getManaModifier());
                currentAttributes.setDodgeChance(currentAttributes.getDodgeChance() + effect.getDodgeChanceModifier());
                currentAttributes.setCritChance(currentAttributes.getCritChance() + effect.getCritChanceModifier());
                break;
            case DEBUFF:
                currentAttributes.addHealth(-effect.getHealthModifier());
                currentAttributes.addDefense(-effect.getDefenseModifier());
                currentAttributes.addMana(-effect.getManaModifier());
                currentAttributes.setDodgeChance(currentAttributes.getDodgeChance() - effect.getDodgeChanceModifier());
                currentAttributes.setCritChance(currentAttributes.getCritChance() - effect.getCritChanceModifier());
                break;
        }
    }
    
    private void revertStatusEffect(StatusEffect effect) {
        // 移除状态效果的属性变化
        switch (effect.getType()) {
            case BUFF:
                currentAttributes.addHealth(-effect.getHealthModifier());
                currentAttributes.addDefense(-effect.getDefenseModifier());
                currentAttributes.addMana(-effect.getManaModifier());
                currentAttributes.setDodgeChance(currentAttributes.getDodgeChance() - effect.getDodgeChanceModifier());
                currentAttributes.setCritChance(currentAttributes.getCritChance() - effect.getCritChanceModifier());
                break;
            case DEBUFF:
                currentAttributes.addHealth(effect.getHealthModifier());
                currentAttributes.addDefense(effect.getDefenseModifier());
                currentAttributes.addMana(effect.getManaModifier());
                currentAttributes.setDodgeChance(currentAttributes.getDodgeChance() + effect.getDodgeChanceModifier());
                currentAttributes.setCritChance(currentAttributes.getCritChance() + effect.getCritChanceModifier());
                break;
        }
    }
    
    public void update() {
        // 更新状态效果持续时间
        List<StatusEffect> toRemove = new ArrayList<>();
        for (StatusEffect effect : statusEffects) {
            effect.decrementDuration();
            if (effect.getDuration() <= 0) {
                toRemove.add(effect);
            }
        }
        
        // 移除过期的状态效果
        for (StatusEffect effect : toRemove) {
            removeStatusEffect(effect);
        }
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        
        nbt.putString("TemplateId", templateId);
        nbt.putInt("Level", level);
        nbt.putLong("Experience", experience);
        nbt.putFloat("ElementalEnergy", elementalEnergy);
        nbt.putInt("AscensionLevel", ascensionLevel);
        nbt.put("CurrentAttributes", currentAttributes.toNbt());
        
        // 序列化状态效果
        NbtList effectsList = new NbtList();
        for (StatusEffect effect : statusEffects) {
            effectsList.add(effect.toNbt());
        }
        nbt.put("StatusEffects", effectsList);
        
        return nbt;
    }
    
    public static ActiveCharacter fromNbt(NbtCompound nbt) {
        String templateId = nbt.getString("TemplateId");
        ActiveCharacter character = new ActiveCharacter(templateId);
        
        character.level = nbt.getInt("Level");
        character.experience = nbt.getLong("Experience");
        character.elementalEnergy = nbt.getFloat("ElementalEnergy");
        character.ascensionLevel = nbt.contains("AscensionLevel") ? nbt.getInt("AscensionLevel") : 0;
        character.currentAttributes = CharacterAttributes.fromNbt(nbt.getCompound("CurrentAttributes"));
        
        // 反序列化状态效果
        NbtList effectsList = nbt.getList("StatusEffects", 10);
        for (int i = 0; i < effectsList.size(); i++) {
            NbtCompound effectNbt = effectsList.getCompound(i);
            StatusEffect effect = StatusEffect.fromNbt(effectNbt);
            character.addStatusEffect(effect);
        }
        
        return character;
    }
    
    @Override
    public String toString() {
        return String.format("ActiveCharacter{templateId='%s', level=%d, ascensionLevel=%d, experience=%d, elementalEnergy=%.1f, attributes=%s}",
                templateId, level, ascensionLevel, experience, elementalEnergy, currentAttributes);
    }
}