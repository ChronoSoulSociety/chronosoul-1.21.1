package com.chronosoulsociety.chronosoul.neomodules.character;

import net.minecraft.nbt.NbtCompound;

public class CharacterAttributes {
    private int health;
    private int defense;
    private int mana;
    private float dodgeChance;
    private float critChance;
    private String critDamage;
    
    public CharacterAttributes(int health, int defense, int mana, float dodgeChance, float critChance, String critDamage) {
        this.health = health;
        this.defense = defense;
        this.mana = mana;
        this.dodgeChance = dodgeChance;
        this.critChance = critChance;
        this.critDamage = critDamage;
    }
    
    public int getHealth() {
        return health;
    }
    
    public void setHealth(int health) {
        this.health = health;
    }
    
    public void addHealth(int amount) {
        this.health += amount;
    }
    
    public int getDefense() {
        return defense;
    }
    
    public void setDefense(int defense) {
        this.defense = defense;
    }
    
    public void addDefense(int amount) {
        this.defense += amount;
    }
    
    public int getMana() {
        return mana;
    }
    
    public void setMana(int mana) {
        this.mana = mana;
    }
    
    public void addMana(int amount) {
        this.mana += amount;
    }
    
    public float getDodgeChance() {
        return dodgeChance;
    }
    
    public void setDodgeChance(float dodgeChance) {
        this.dodgeChance = dodgeChance;
    }
    
    public void addDodgeChance(float amount) {
        this.dodgeChance += amount;
    }
    
    public float getCritChance() {
        return critChance;
    }
    
    public void setCritChance(float critChance) {
        this.critChance = critChance;
    }
    
    public void addCritChance(float amount) {
        this.critChance += amount;
    }
    
    public String getCritDamage() {
        return critDamage;
    }
    
    public void setCritDamage(String critDamage) {
        this.critDamage = critDamage;
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("Health", health);
        nbt.putInt("Defense", defense);
        nbt.putInt("Mana", mana);
        nbt.putFloat("DodgeChance", dodgeChance);
        nbt.putFloat("CritChance", critChance);
        nbt.putString("CritDamage", critDamage);
        return nbt;
    }
    
    public static CharacterAttributes fromNbt(NbtCompound nbt) {
        return new CharacterAttributes(
            nbt.getInt("Health"),
            nbt.getInt("Defense"),
            nbt.getInt("Mana"),
            nbt.getFloat("DodgeChance"),
            nbt.getFloat("CritChance"),
            nbt.getString("CritDamage")
        );
    }
    
    @Override
    public String toString() {
        return String.format("CharacterAttributes{health=%d, defense=%d, mana=%d, dodgeChance=%.1f%%, critChance=%.1f%%, critDamage=%s}",
            health, defense, mana, dodgeChance * 100, critChance * 100, critDamage);
    }
}