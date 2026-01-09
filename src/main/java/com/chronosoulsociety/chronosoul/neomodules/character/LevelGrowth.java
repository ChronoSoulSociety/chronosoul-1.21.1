package com.chronosoulsociety.chronosoul.neomodules.character;

import net.minecraft.nbt.NbtCompound;

public class LevelGrowth {
    // 分段成长参数
    private final int slowGrowthCap;
    private final int stableGrowthCap;
    
    // 缓慢成长阶段（1~slowGrowthCap）
    private final int slowHealthGrowth;
    private final int slowDefenseGrowth;
    private final int slowManaGrowth;
    private final float slowDodgeChanceGrowth;
    private final float slowCritChanceGrowth;
    
    // 稳定成长阶段（slowGrowthCap+1~stableGrowthCap）
    private final int stableHealthGrowth;
    private final int stableDefenseGrowth;
    private final int stableManaGrowth;
    private final float stableDodgeChanceGrowth;
    private final float stableCritChanceGrowth;
    
    // 爆发成长阶段（stableGrowthCap+1~maxLevel）
    private final int burstHealthGrowth;
    private final int burstDefenseGrowth;
    private final int burstManaGrowth;
    private final float burstDodgeChanceGrowth;
    private final float burstCritChanceGrowth;
    
    public LevelGrowth(int slowGrowthCap, int stableGrowthCap,
                      int slowHealthGrowth, int slowDefenseGrowth, int slowManaGrowth,
                      int stableHealthGrowth, int stableDefenseGrowth, int stableManaGrowth,
                      int burstHealthGrowth, int burstDefenseGrowth, int burstManaGrowth) {
        this(slowGrowthCap, stableGrowthCap,
            slowHealthGrowth, slowDefenseGrowth, slowManaGrowth, 0.0f, 0.0f,
            stableHealthGrowth, stableDefenseGrowth, stableManaGrowth, 0.0f, 0.0f,
            burstHealthGrowth, burstDefenseGrowth, burstManaGrowth, 0.0f, 0.0f);
    }
    
    public LevelGrowth(int slowGrowthCap, int stableGrowthCap,
                      int slowHealthGrowth, int slowDefenseGrowth, int slowManaGrowth, float slowDodgeChanceGrowth, float slowCritChanceGrowth,
                      int stableHealthGrowth, int stableDefenseGrowth, int stableManaGrowth, float stableDodgeChanceGrowth, float stableCritChanceGrowth,
                      int burstHealthGrowth, int burstDefenseGrowth, int burstManaGrowth, float burstDodgeChanceGrowth, float burstCritChanceGrowth) {
        this.slowGrowthCap = slowGrowthCap;
        this.stableGrowthCap = stableGrowthCap;
        this.slowHealthGrowth = slowHealthGrowth;
        this.slowDefenseGrowth = slowDefenseGrowth;
        this.slowManaGrowth = slowManaGrowth;
        this.slowDodgeChanceGrowth = slowDodgeChanceGrowth;
        this.slowCritChanceGrowth = slowCritChanceGrowth;
        this.stableHealthGrowth = stableHealthGrowth;
        this.stableDefenseGrowth = stableDefenseGrowth;
        this.stableManaGrowth = stableManaGrowth;
        this.stableDodgeChanceGrowth = stableDodgeChanceGrowth;
        this.stableCritChanceGrowth = stableCritChanceGrowth;
        this.burstHealthGrowth = burstHealthGrowth;
        this.burstDefenseGrowth = burstDefenseGrowth;
        this.burstManaGrowth = burstManaGrowth;
        this.burstDodgeChanceGrowth = burstDodgeChanceGrowth;
        this.burstCritChanceGrowth = burstCritChanceGrowth;
    }
    
    // 获取指定等级的生命值增量
    public int getHealthGrowthAtLevel(int level) {
        if (level <= slowGrowthCap) {
            return slowHealthGrowth;
        } else if (level <= stableGrowthCap) {
            return stableHealthGrowth;
        } else {
            return burstHealthGrowth;
        }
    }
    
    // 获取指定等级的防御值增量
    public int getDefenseGrowthAtLevel(int level) {
        if (level <= slowGrowthCap) {
            return slowDefenseGrowth;
        } else if (level <= stableGrowthCap) {
            return stableDefenseGrowth;
        } else {
            return burstDefenseGrowth;
        }
    }
    
    // 获取指定等级的法力值增量
    public int getManaGrowthAtLevel(int level) {
        if (level <= slowGrowthCap) {
            return slowManaGrowth;
        } else if (level <= stableGrowthCap) {
            return stableManaGrowth;
        } else {
            return burstManaGrowth;
        }
    }
    
    // 获取指定等级的闪避几率增量
    public float getDodgeChanceGrowthAtLevel(int level) {
        if (level <= slowGrowthCap) {
            return slowDodgeChanceGrowth;
        } else if (level <= stableGrowthCap) {
            return stableDodgeChanceGrowth;
        } else {
            return burstDodgeChanceGrowth;
        }
    }
    
    // 获取指定等级的暴击几率增量
    public float getCritChanceGrowthAtLevel(int level) {
        if (level <= slowGrowthCap) {
            return slowCritChanceGrowth;
        } else if (level <= stableGrowthCap) {
            return stableCritChanceGrowth;
        } else {
            return burstCritChanceGrowth;
        }
    }
    
    public NbtCompound toNbt() {
        NbtCompound nbt = new NbtCompound();
        
        // 分段参数
        nbt.putInt("SlowGrowthCap", slowGrowthCap);
        nbt.putInt("StableGrowthCap", stableGrowthCap);
        
        // 缓慢成长阶段
        nbt.putInt("SlowHealthGrowth", slowHealthGrowth);
        nbt.putInt("SlowDefenseGrowth", slowDefenseGrowth);
        nbt.putInt("SlowManaGrowth", slowManaGrowth);
        nbt.putFloat("SlowDodgeChanceGrowth", slowDodgeChanceGrowth);
        nbt.putFloat("SlowCritChanceGrowth", slowCritChanceGrowth);
        
        // 稳定成长阶段
        nbt.putInt("StableHealthGrowth", stableHealthGrowth);
        nbt.putInt("StableDefenseGrowth", stableDefenseGrowth);
        nbt.putInt("StableManaGrowth", stableManaGrowth);
        nbt.putFloat("StableDodgeChanceGrowth", stableDodgeChanceGrowth);
        nbt.putFloat("StableCritChanceGrowth", stableCritChanceGrowth);
        
        // 爆发成长阶段
        nbt.putInt("BurstHealthGrowth", burstHealthGrowth);
        nbt.putInt("BurstDefenseGrowth", burstDefenseGrowth);
        nbt.putInt("BurstManaGrowth", burstManaGrowth);
        nbt.putFloat("BurstDodgeChanceGrowth", burstDodgeChanceGrowth);
        nbt.putFloat("BurstCritChanceGrowth", burstCritChanceGrowth);
        
        return nbt;
    }
    
    public static LevelGrowth fromNbt(NbtCompound nbt) {
        return new LevelGrowth(
            nbt.getInt("SlowGrowthCap"),
            nbt.getInt("StableGrowthCap"),
            
            nbt.getInt("SlowHealthGrowth"),
            nbt.getInt("SlowDefenseGrowth"),
            nbt.getInt("SlowManaGrowth"),
            nbt.getFloat("SlowDodgeChanceGrowth"),
            nbt.getFloat("SlowCritChanceGrowth"),
            
            nbt.getInt("StableHealthGrowth"),
            nbt.getInt("StableDefenseGrowth"),
            nbt.getInt("StableManaGrowth"),
            nbt.getFloat("StableDodgeChanceGrowth"),
            nbt.getFloat("StableCritChanceGrowth"),
            
            nbt.getInt("BurstHealthGrowth"),
            nbt.getInt("BurstDefenseGrowth"),
            nbt.getInt("BurstManaGrowth"),
            nbt.getFloat("BurstDodgeChanceGrowth"),
            nbt.getFloat("BurstCritChanceGrowth")
        );
    }
    
    @Override
    public String toString() {
        return String.format("LevelGrowth{slowGrowthCap=%d, stableGrowthCap=%d, slowHealthGrowth=%d, stableHealthGrowth=%d, burstHealthGrowth=%d}",
                slowGrowthCap, stableGrowthCap, slowHealthGrowth, stableHealthGrowth, burstHealthGrowth);
    }
}