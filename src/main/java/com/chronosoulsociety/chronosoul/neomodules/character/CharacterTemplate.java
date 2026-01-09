package com.chronosoulsociety.chronosoul.neomodules.character;

import com.chronosoulsociety.chronosoul.neomodules.element.ElementalType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CharacterTemplate {
    private final String name;
    private final ElementalType elementalType;
    private final CharacterAttributes baseAttributes;
    private final LevelGrowth levelGrowth;
    private final int maxLevel;
    private final List<CharacterTrait> traits;
    private final String id;
    
    public CharacterTemplate(String id, String name, ElementalType elementalType, CharacterAttributes baseAttributes,
                           LevelGrowth levelGrowth, int maxLevel) {
        this(id, name, elementalType, baseAttributes, levelGrowth, maxLevel, new ArrayList<>());
    }
    
    public CharacterTemplate(String id, String name, ElementalType elementalType, CharacterAttributes baseAttributes,
                           LevelGrowth levelGrowth, int maxLevel, List<CharacterTrait> traits) {
        this.id = id;
        this.name = name;
        this.elementalType = elementalType;
        this.baseAttributes = baseAttributes;
        this.levelGrowth = levelGrowth;
        this.maxLevel = maxLevel;
        this.traits = new ArrayList<>(traits);
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public ElementalType getElementalType() {
        return elementalType;
    }
    
    public CharacterAttributes getBaseAttributes() {
        return baseAttributes;
    }
    
    public LevelGrowth getLevelGrowth() {
        return levelGrowth;
    }
    
    public int getMaxLevel() {
        return maxLevel;
    }
    
    public List<CharacterTrait> getTraits() {
        return Collections.unmodifiableList(traits);
    }
    
    public void addTrait(CharacterTrait trait) {
        traits.add(trait);
    }
    
    public CharacterAttributes createAttributesCopy() {
        return new CharacterAttributes(
            baseAttributes.getHealth(),
            baseAttributes.getDefense(),
            baseAttributes.getMana(),
            baseAttributes.getDodgeChance(),
            baseAttributes.getCritChance(),
            baseAttributes.getCritDamage()
        );
    }
    
    @Override
    public String toString() {
        return String.format("CharacterTemplate{id=%s, name=%s, elementalType=%s, baseAttributes=%s, levelGrowth=%s, maxLevel=%d, traits=%s}",
            id, name, elementalType, baseAttributes, levelGrowth, maxLevel, traits);
    }
}