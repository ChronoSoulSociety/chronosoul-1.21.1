package com.chronosoulsociety.chronosoul.neomodules.element;

import net.minecraft.nbt.NbtCompound;

public class ElementalStack {
    private ElementalType type;
    private int amount;
    private int durationTicks;
    private float intensity;
    private boolean isInfinite;
    
    public ElementalStack(ElementalType type, int amount, int durationTicks, float intensity) {
        this.type = type;
        this.amount = amount;
        this.durationTicks = durationTicks;
        this.intensity = intensity;
        this.isInfinite = false;
    }
    
    public ElementalStack(ElementalType type, int amount, float intensity) {
        this.type = type;
        this.amount = amount;
        this.durationTicks = 0;
        this.intensity = intensity;
        this.isInfinite = true;
    }
    
    // Getters and setters
    public ElementalType getType() {
        return type;
    }
    
    public void setType(ElementalType type) {
        this.type = type;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(int amount) {
        this.amount = Math.max(0, amount);
    }
    
    public int getDurationTicks() {
        return durationTicks;
    }
    
    public void setDurationTicks(int durationTicks) {
        this.durationTicks = durationTicks;
    }
    
    public float getIntensity() {
        return intensity;
    }
    
    public void setIntensity(float intensity) {
        this.intensity = Math.max(0.0f, intensity);
    }
    
    public boolean isInfinite() {
        return isInfinite;
    }
    
    public void setInfinite(boolean isInfinite) {
        this.isInfinite = isInfinite;
    }
    
    public boolean isEmpty() {
        return type == null || amount <= 0;
    }
    
    public void decreaseAmount(int amount) {
        this.amount = Math.max(0, this.amount - amount);
    }
    
    public void decreaseDuration(int ticks) {
        if (!isInfinite) {
            this.durationTicks = Math.max(0, this.durationTicks - ticks);
        }
    }
    
    public boolean hasExpired() {
        return !isInfinite && durationTicks <= 0;
    }
    
    // NBT serialization
    public NbtCompound writeNbt(NbtCompound nbt) {
        if (type != null) {
            nbt.putString("Type", type.name());
            nbt.putInt("Amount", amount);
            nbt.putInt("DurationTicks", durationTicks);
            nbt.putFloat("Intensity", intensity);
            nbt.putBoolean("IsInfinite", isInfinite);
        }
        return nbt;
    }
    
    public static ElementalStack fromNbt(NbtCompound nbt) {
        if (!nbt.contains("Type")) {
            return null;
        }
        
        ElementalType type = ElementalType.valueOf(nbt.getString("Type"));
        int amount = nbt.getInt("Amount");
        int durationTicks = nbt.getInt("DurationTicks");
        float intensity = nbt.getFloat("Intensity");
        boolean isInfinite = nbt.getBoolean("IsInfinite");
        
        ElementalStack stack = new ElementalStack(type, amount, durationTicks, intensity);
        stack.setInfinite(isInfinite);
        return stack;
    }

    public ElementalStack copy() {
        ElementalStack copy = new ElementalStack(this.type, this.amount, this.durationTicks, this.intensity);
        copy.setInfinite(this.isInfinite);
        return copy;
    }
}