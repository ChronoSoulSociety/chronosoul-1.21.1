package com.chronosoulsociety.chronosoul.neomodules.character;

import com.chronosoulsociety.chronosoul.neomodules.element.ElementalType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CharacterManager {
    private static final Map<String, CharacterTemplate> CHARACTER_TEMPLATES = new HashMap<>();
    
    public static void initialize() {
        registerDefaultCharacters();
    }
    
    private static void registerDefaultCharacters() {
        // 青籁 - 魂元素（偏法力成长）
        CharacterAttributes qingLaiAttributes = new CharacterAttributes(
            40, // 生命: 20 * 2 = 40
            100, // 防御
            150, // 法力
            0.10f, // 闪避几率: 10%
            0.05f, // 暴击几率: 5%
            "S" // 暴击伤害等级: S (X*2)
        );
        
        // 青籁的成长配置：偏法力，非线性成长
        // 1~30级：缓慢成长，31~70级：稳定成长，71~100级：爆发成长
        LevelGrowth qingLaiGrowth = new LevelGrowth(
            30, // 缓慢成长上限
            70, // 稳定成长上限
            
            // 缓慢成长阶段（1~30级）
            2, // 每级生命+2
            1, // 每级防御+1
            8, // 每级法力+8
            0.003f, // 每级闪避+0.3%
            0.005f, // 每级暴击+0.5%
            
            // 稳定成长阶段（31~70级）
            4, // 每级生命+4
            3, // 每级防御+3
            15, // 每级法力+15
            0.005f, // 每级闪避+0.5%
            0.01f, // 每级暴击+1%
            
            // 爆发成长阶段（71~100级）
            8, // 每级生命+8
            6, // 每级防御+6
            25, // 每级法力+25
            0.01f, // 每级闪避+1%
            0.02f // 每级暴击+2%
        );
        
        // 青籁的特质：能量驱动型
        List<CharacterTrait> qingLaiTraits = new ArrayList<>();
        qingLaiTraits.add(new CharacterTrait(
            "qing_lai_soul_energy_boost",
            "魂能涌动",
            "当能量充满时，技能伤害提升20%",
            CharacterTrait.TraitType.DAMAGE_BOOST,
            CharacterTrait.TraitTrigger.ON_ENERGY_FULL,
            0.2f
        ));
        qingLaiTraits.add(new CharacterTrait(
            "qing_lai_dodge_energy_regen",
            "闪避回能",
            "闪避成功时，回复2点魂能",
            CharacterTrait.TraitType.ENERGY_REGEN,
            CharacterTrait.TraitTrigger.ON_DODGE,
            2.0f
        ));
        qingLaiTraits.add(new CharacterTrait(
            "qing_lai_energy_crit_boost",
            "魂能增幅",
            "每10点能量，增加1%暴击率",
            CharacterTrait.TraitType.CRIT_BOOST,
            CharacterTrait.TraitTrigger.ALWAYS,
            0.01f
        ));
        
        CharacterTemplate qingLai = new CharacterTemplate(
            "qing_lai",
            "青籁",
            ElementalType.SOUL,
            qingLaiAttributes,
            qingLaiGrowth,
            100, // 最大等级100
            qingLaiTraits
        );
        registerCharacterTemplate(qingLai);
        
        // 苏林 - 火元素（偏生命/防御成长）
        CharacterAttributes suLinAttributes = new CharacterAttributes(
            35, // 生命: 20 + 15 = 35
            110, // 防御
            200, // 法力
            0.10f, // 闪避几率: 10%
            0.05f, // 暴击几率: 5%
            "S" // 暴击伤害等级: S (X*2)
        );
        
        // 苏林的成长配置：偏生命/防御，非线性成长
        LevelGrowth suLinGrowth = new LevelGrowth(
            30, // 缓慢成长上限
            70, // 稳定成长上限
            
            // 缓慢成长阶段（1~30级）
            4, // 每级生命+4
            3, // 每级防御+3
            5, // 每级法力+5
            0.002f, // 每级闪避+0.2%
            0.004f, // 每级暴击+0.4%
            
            // 稳定成长阶段（31~70级）
            7, // 每级生命+7
            5, // 每级防御+5
            10, // 每级法力+10
            0.004f, // 每级闪避+0.4%
            0.008f, // 每级暴击+0.8%
            
            // 爆发成长阶段（71~100级）
            12, // 每级生命+12
            9, // 每级防御+9
            18, // 每级法力+18
            0.008f, // 每级闪避+0.8%
            0.015f // 每级暴击+1.5%
        );
        
        // 苏林的特质：反应增益型
        List<CharacterTrait> suLinTraits = new ArrayList<>();
        suLinTraits.add(new CharacterTrait(
            "su_lin_fire_damage_boost",
            "火焰精通",
            "火元素伤害提升10%",
            CharacterTrait.TraitType.DAMAGE_BOOST,
            CharacterTrait.TraitTrigger.ALWAYS,
            0.1f
        ));
        suLinTraits.add(new CharacterTrait(
            "su_lin_evaporation_boost",
            "蒸发增幅",
            "触发蒸发反应时，反应伤害倍率提升0.5",
            CharacterTrait.TraitType.REACTION_BOOST,
            CharacterTrait.TraitTrigger.ON_REACTION,
            0.5f
        ));
        suLinTraits.add(new CharacterTrait(
            "su_lin_crit_on_reaction",
            "反应暴击",
            "触发元素反应时，暴击率提升5%，持续3秒",
            CharacterTrait.TraitType.CRIT_BOOST,
            CharacterTrait.TraitTrigger.ON_REACTION,
            0.05f
        ));
        suLinTraits.add(new CharacterTrait(
            "su_lin_crit_energy_regen",
            "暴击回火",
            "暴击成功时，回复1点火能",
            CharacterTrait.TraitType.ENERGY_REGEN,
            CharacterTrait.TraitTrigger.ON_CRIT,
            1.0f
        ));
        
        CharacterTemplate suLin = new CharacterTemplate(
            "su_lin",
            "苏林",
            ElementalType.PYRO,
            suLinAttributes,
            suLinGrowth,
            100, // 最大等级100
            suLinTraits
        );
        registerCharacterTemplate(suLin);
    }
    
    public static void registerCharacterTemplate(CharacterTemplate template) {
        CHARACTER_TEMPLATES.put(template.getId(), template);
    }
    
    public static CharacterTemplate getCharacterTemplate(String id) {
        return CHARACTER_TEMPLATES.get(id);
    }
    
    public static CharacterTemplate getCharacterTemplateByName(String name) {
        for (CharacterTemplate template : CHARACTER_TEMPLATES.values()) {
            if (template.getName().equals(name)) {
                return template;
            }
        }
        return null;
    }
    
    public static Map<String, CharacterTemplate> getAllCharacterTemplates() {
        return new HashMap<>(CHARACTER_TEMPLATES);
    }
    
    public static boolean hasCharacterTemplate(String id) {
        return CHARACTER_TEMPLATES.containsKey(id);
    }
}