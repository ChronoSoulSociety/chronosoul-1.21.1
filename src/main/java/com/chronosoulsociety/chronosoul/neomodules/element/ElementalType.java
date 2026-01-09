package com.chronosoulsociety.chronosoul.neomodules.element;

import net.minecraft.util.StringIdentifiable;

public enum ElementalType implements StringIdentifiable {
    PYRO("Pyro", "火"),
    HYDRO("Hydro", "水"),
    ELECTRO("Electro", "雷"),
    CRYO("Cryo", "冰"),
    ANEMO("Anemo", "风"),
    GEO("Geo", "岩"),
    TIME("Time", "时"),
    SOUL("Soul", "魂"),
    VOID("Void", "虚无");
    
    private final String englishName;
    private final String chineseName;
    
    ElementalType(String englishName, String chineseName) {
        this.englishName = englishName;
        this.chineseName = chineseName;
    }
    
    public String getEnglishName() {
        return englishName;
    }
    
    public String getChineseName() {
        return chineseName;
    }
    
    public static ElementalType fromName(String name) {
        for (ElementalType type : values()) {
            if (type.englishName.equalsIgnoreCase(name) || type.chineseName.equals(name) || type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String asString() { return englishName.toLowerCase(); }
}