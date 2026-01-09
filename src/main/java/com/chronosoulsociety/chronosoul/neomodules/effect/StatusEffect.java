package com.chronosoulsociety.chronosoul.neomodules.effect;

import net.minecraft.nbt.NbtCompound;

public class StatusEffect {
    public enum EffectType {
        BUFF,
        DEBUFF
    }
    
    private final String id;
    private final EffectType type;
    private int duration;
    private final int healthModifier;
    private final int defenseModifier;
    private final int manaModifier;
    private final float dodgeChanceModifier;
    private final float critChanceModifier;
    
    public StatusEffect(String id, EffectType type, int duration, int healthModifier, int defenseModifier, 
                       int manaModifier, float dodgeChanceModifier, float critChanceModifier) {
        this.id = id;
        this.type = type;
        this.duration = duration;
        this.healthModifier = healthModifier;
        this.defenseModifier = defenseModifier;
        this.manaModifier = manaModifier;
        this.dodgeChanceModifier = dodgeChanceModifier;
        this.critChanceModifier = critChanceModifier;
    }
    
    public String getId() {
        return id;
    }
    
    public EffectType getType() {
        return type;
    }
    
    public int getDuration() {
        return duration;
    }
    
    public void setDuration(int duration) {
        this.duration = duration;
    }
    
    public void decrementDuration() {
        this.duration--;
    }
    
    public int getHealthModifier() {
        return healthModifier;
    }
    
    public int getDefenseModifier() {
        return defenseModifier;
    }
    
    public int getManaModifier() {
        return manaModifier;
    }
    
    public float getDodgeChanceModifier() {
        return dodgeChanceModifier;
    }
    
    public float getCritChanceModifier() {
        return critChanceModifier;
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        
        nbt.putString("Id", id);
        nbt.putString("Type", type.name());
        nbt.putInt("Duration", duration);
        nbt.putInt("HealthModifier", healthModifier);
        nbt.putInt("DefenseModifier", defenseModifier);
        nbt.putInt("ManaModifier", manaModifier);
        nbt.putFloat("DodgeChanceModifier", dodgeChanceModifier);
        nbt.putFloat("CritChanceModifier", critChanceModifier);
        
        return nbt;
    }
    
    public static StatusEffect fromNbt(NbtCompound nbt) {
        String id = nbt.getString("Id");
        EffectType type = EffectType.valueOf(nbt.getString("Type"));
        int duration = nbt.getInt("Duration");
        int healthModifier = nbt.getInt("HealthModifier");
        int defenseModifier = nbt.getInt("DefenseModifier");
        int manaModifier = nbt.getInt("ManaModifier");
        float dodgeChanceModifier = nbt.getFloat("DodgeChanceModifier");
        float critChanceModifier = nbt.getFloat("CritChanceModifier");
        
        return new StatusEffect(id, type, duration, healthModifier, defenseModifier, manaModifier, 
                               dodgeChanceModifier, critChanceModifier);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatusEffect that = (StatusEffect) o;
        return duration == that.duration &&
                healthModifier == that.healthModifier &&
                defenseModifier == that.defenseModifier &&
                manaModifier == that.manaModifier &&
                Float.compare(that.dodgeChanceModifier, dodgeChanceModifier) == 0 &&
                Float.compare(that.critChanceModifier, critChanceModifier) == 0 &&
                id.equals(that.id) &&
                type == that.type;
    }
    
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + type.hashCode();
        result = 31 * result + duration;
        result = 31 * result + healthModifier;
        result = 31 * result + defenseModifier;
        result = 31 * result + manaModifier;
        result = 31 * result + Float.floatToIntBits(dodgeChanceModifier);
        result = 31 * result + Float.floatToIntBits(critChanceModifier);
        return result;
    }
    
    @Override
    public String toString() {
        return String.format("StatusEffect{id='%s', type=%s, duration=%d, healthModifier=%d, defenseModifier=%d, manaModifier=%d}",
                id, type, duration, healthModifier, defenseModifier, manaModifier);
    }
}