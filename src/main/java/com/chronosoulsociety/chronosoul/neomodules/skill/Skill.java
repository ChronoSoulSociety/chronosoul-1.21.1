package com.chronosoulsociety.chronosoul.neomodules.skill;

import com.chronosoulsociety.chronosoul.gameplay.character.ActiveCharacter;
import net.minecraft.nbt.NbtCompound;

public abstract class Skill {
    protected final String id;
    protected final String name;
    protected final String description;
    protected final SkillType type;
    protected final int manaCost;
    protected final int cooldownTicks;
    protected final int durationTicks;
    
    public Skill(String id, String name, String description, SkillType type, int manaCost, int cooldownTicks) {
        this(id, name, description, type, manaCost, cooldownTicks, 0);
    }
    
    public Skill(String id, String name, String description, SkillType type, int manaCost, int cooldownTicks, int durationTicks) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.manaCost = manaCost;
        this.cooldownTicks = cooldownTicks;
        this.durationTicks = durationTicks;
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public SkillType getType() {
        return type;
    }
    
    public int getManaCost() {
        return manaCost;
    }
    
    public int getCooldownTicks() {
        return cooldownTicks;
    }
    
    public int getDurationTicks() {
        return durationTicks;
    }
    
    /**
     * 执行技能效果
     * @param character 技能使用者
     * @return 是否成功执行技能
     */
    public abstract boolean execute(ActiveCharacter character);
    
    /**
     * 更新技能状态
     * @param character 技能使用者
     */
    public abstract void update(ActiveCharacter character);
    
    /**
     * 结束技能效果
     * @param character 技能使用者
     */
    public abstract void end(ActiveCharacter character);
    
    /**
     * 检查是否可以执行技能
     * @param character 技能使用者
     * @return 是否可以执行技能
     */
    public boolean canExecute(ActiveCharacter character) {
        return character.getCurrentAttributes().getMana() >= manaCost;
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("Id", id);
        nbt.putString("Name", name);
        nbt.putString("Description", description);
        nbt.putString("Type", type.name());
        nbt.putInt("ManaCost", manaCost);
        nbt.putInt("CooldownTicks", cooldownTicks);
        nbt.putInt("DurationTicks", durationTicks);
        return nbt;
    }
    
    public abstract boolean isActive();
    
    public abstract void reset();
}