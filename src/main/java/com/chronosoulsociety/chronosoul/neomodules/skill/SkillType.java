package com.chronosoulsociety.chronosoul.neomodules.skill;

public enum SkillType {
    NORMAL_ATTACK("Normal Attack", "普通攻击"),
    ELEMENTAL_SKILL("Elemental Skill", "元素战技"),
    ELEMENTAL_BURST("Elemental Burst", "元素爆发"),
    ULTIMATE_SKILL("Ultimate Skill", "终极技能"),
    PASSIVE_SKILL("Passive Skill", "被动技能"),
    DEFENSIVE_SKILL("Defensive Skill", "防御技能"),
    MOVEMENT_SKILL("Movement Skill", "移动技能"),
    BUFF_SKILL("Buff Skill", "增益技能"),
    DEBUFF_SKILL("Debuff Skill", "减益技能");
    
    private final String englishName;
    private final String chineseName;
    
    SkillType(String englishName, String chineseName) {
        this.englishName = englishName;
        this.chineseName = chineseName;
    }
    
    public String getEnglishName() {
        return englishName;
    }
    
    public String getChineseName() {
        return chineseName;
    }
}