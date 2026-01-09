package com.chronosoulsociety.chronosoul.neomodules.character;

import net.minecraft.nbt.NbtCompound;

public class CharacterTrait {
    private final String id;
    private final String name;
    private final String description;
    private final TraitType type;
    private final TraitTrigger trigger;
    private final float value;
    
    public enum TraitType {
        DAMAGE_BOOST,
        REACTION_BOOST,
        ENERGY_REGEN,
        DEFENSE_BOOST,
        HEALTH_REGEN,
        CRIT_BOOST,
        DODGE_BOOST,
        OTHER
    }
    
    public enum TraitTrigger {
        ALWAYS,
        ON_ATTACK,
        ON_HIT,
        ON_CRIT,
        ON_DODGE,
        ON_REACTION,
        ON_SKILL_USE,
        ON_ENERGY_FULL
    }
    
    public CharacterTrait(String id, String name, String description, TraitType type, TraitTrigger trigger, float value) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.trigger = trigger;
        this.value = value;
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
    
    public TraitType getType() {
        return type;
    }
    
    public TraitTrigger getTrigger() {
        return trigger;
    }
    
    public float getValue() {
        return value;
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("Id", id);
        nbt.putString("Name", name);
        nbt.putString("Description", description);
        nbt.putString("Type", type.name());
        nbt.putString("Trigger", trigger.name());
        nbt.putFloat("Value", value);
        return nbt;
    }
    
    public static CharacterTrait fromNbt(NbtCompound nbt) {
        return new CharacterTrait(
            nbt.getString("Id"),
            nbt.getString("Name"),
            nbt.getString("Description"),
            TraitType.valueOf(nbt.getString("Type")),
            TraitTrigger.valueOf(nbt.getString("Trigger")),
            nbt.getFloat("Value")
        );
    }
    
    @Override
    public String toString() {
        return String.format("CharacterTrait{id='%s', name='%s', type=%s, trigger=%s, value=%.2f}",
                id, name, type, trigger, value);
    }
}