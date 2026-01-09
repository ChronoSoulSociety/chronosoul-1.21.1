package com.chronosoulsociety.chronosoul.modulesmechanic.elementmechanic;


import com.chronosoulsociety.chronosoul.neomodules.element.ElementalType;

public class ElementalEvaporationMechanic {
    /**
     * 计算带元素反应的最终伤害
     *
     * @param baseDamage 基础伤害值
     * @param attacker   攻击方元素类型
     * @param defender   防御方元素类型
     * @return 最终伤害（已应用蒸发等反应加成）
     */
    public static double applyReactionDamage(double baseDamage, ElementalType attacker, ElementalType defender) {
        if (attacker == ElementalType.PYRO && defender == ElementalType.HYDRO) {
            return baseDamage * 2.0;
        }
        return baseDamage;
    }
}
