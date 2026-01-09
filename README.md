# 🕰️ 《Chrono Soul: Beyond Life and Death》  

## —— 跨世界永恒成长的 Fabric RPG 模组

> **作者**：ChronoSoulSociety（时魂社）  
> **Mod ID**：`chronosoul`  
> **许可证**：MIT  
> **目标版本**：Minecraft 1.20.1 + Fabric Loader  
> **仓库地址**：`https://github.com/ChronoSoulSociety/ChronoSoul`  
> **最后更新**：2026 年 1 月  

---

## 🌌 一、模组愿景：死亡不是终点，而是新生的序章

在 vanilla Minecraft 的规则中，死亡意味着经验清零、物品散落、进度中断。玩家被迫从零开始，每一次失误都可能摧毁数小时的心血。这种“脆弱性”虽是原版魅力的一部分，却也限制了深度角色扮演的可能性。

而 **《Chrono Soul: Beyond Life and Death》** 彻底颠覆这一逻辑——**你的角色将拥有“不朽之魂”**。

无论你因末影龙突袭而陨落，因下界岩浆溺亡，或因自定义维度中的远古 Boss 粉碎身躯，你的以下核心数据都将被“**时魂系统**”（Chrono Soul System）永久记录，并在你重生于**任何世界**（主世界、下界、末地、自定义维度）时自动恢复：

- ✅ **角色等级与经验值**
- ✅ **职业路径与分支选择**
- ✅ **已解锁技能列表及其等级**
- ✅ **元素亲和力与附着状态**
- ✅ **核心装备绑定状态**（如“灵魂绑定的传说武器”）
- ✅ **成就与挑战进度**

这不是简单的 NBT 数据备份，而是一套完整的 **RPG 角色生命周期管理系统**（Character Lifecycle Management System, CLMS）。它将 Minecraft 从“沙盒建造游戏”升维为“跨存档叙事平台”。

你不再是“玩家”，而是一个**穿越多重宇宙的永恒旅者**。每一次死亡，都是对灵魂的淬炼；每一次重生，都是更强形态的归来。世界可毁，魂火长燃。

---

## ⚔️ 二、核心玩法机制详解

### 2.1 永久角色成长系统（Persistent Character Progression）

- 所有角色数据存储于全局目录：  
  `saves/global_playerdata/chronosoul/<player_uuid>.dat`
- 此文件独立于任何单个存档（world），确保：
  - 新建世界 → 自动加载旧角色
  - 删除世界 → 角色数据不受影响
  - 多人服务器 → 统一角色进度（需服务端启用）
- 数据格式采用 Minecraft 原生 NBT 结构，兼容 `PlayerData` 接口，但通过 `GlobalPlayerStorage.java` 扩展。

#### 2.1.2 职业体系设计

初始提供三大基础职业，每种职业由 `modules/character/CharacterModule.java` 注册，并通过 JSON 配置：

| 职业 | 核心特性 | 成长曲线 | 初始技能 |

|------|--------|--------|--------|
| **战士（Warrior）** | 高生命、近战爆发、嘲讽控制 | 线性（HP↑↑，MP→） | `slash`, `taunt` |
| **法师（Mage）** | 元素掌控、远程 AoE、法力依赖 | 指数（MP↑↑↑，HP→） | `fireball`, `frost_nova` |
| **游侠（Archer）** | 敏捷闪避、精准暴击、陷阱协同 | 阶梯（Dex↑↑，Crit%↑） | `piercing_shot`, `trap_mine` |

- **属性成长曲线**：由 `modules/character/data/AttributeGrowthCurve.java` 定义，支持三种模式：
  
  ```java
  public enum GrowthType {
      LINEAR,    // y = a*x + b
      EXPONENTIAL, // y = a * e^(b*x)
      STEPPED   // y = base + floor(x/5)*step
  }

  ```

- **被动天赋树**：每升 5 级解锁一个天赋槽，从预设池中选择（如“火焰抗性+10%”、“暴击伤害+25%”）。

#### 2.1.3 经验与升级

- 经验来源：
  - 击杀生物（按难度加权）
  - 完成挑战（如“首次击败末影龙”）
  - 探索结构（发现“时之祭坛”）
- 经验单位为“**魂晶**”（Soul Crystal），不可交易、不可丢弃。
- 升级后触发 `LevelingSystemMec.java` 中的 `onLevelUp(PlayerEntity player)` 方法，自动应用属性增益。

---

### 2.2 动态技能系统（Dynamic Skill System）

#### 2.2.1 技能本质

技能不是物品，而是**绑定于角色的数据实体**，由 `modules/skill/SkillModule.java` 注册，并通过 `SkillDefinition.java` 描述：

```json
{
  "skill_id": "chronosoul:fireball",
  "display_name": "Fireball",
  "type": "ACTIVE",
  "mana_cost": 15,
  "cooldown_seconds": 3.0,
  "cast_time_ticks": 20,
  "effects": [
    { "type": "PROJECTILE", "damage": 8, "element": "pyro" }
  ]
}
```

#### 2.2.2 技能类型

| 类型 | 说明 | 示例 |

|------|------|------|
| **主动技能** | 需玩家手动触发 | 火球术、治疗波 |
| **被动技能** | 持续生效 | 暴击率提升、元素抗性 |
| **组合技** | 多技能连续施放触发 | 冰霜新星 + 雷击 → 超导 |

#### 2.2.3 技能执行流程

1. 玩家按下技能键（绑定至快捷栏）
2. `SkillCastHandlerMec.java` 检查：
   - 是否拥有该技能
   - 法力是否足够
   - 是否在冷却中
3. 若通过，播放施法动画（客户端）
4. 服务端验证后，执行 `SkillExecutor.execute(SkillDefinition, PlayerEntity)`
5. 触发元素附着、伤害计算、粒子效果

#### 2.2.4 冷却与资源管理

- 冷却时间由 `SkillCooldownManagerMec.java` 统一管理，使用 `Map<UUID, Map<String, Long>>` 存储。
- 法力（Mana）作为独立资源，随时间自然恢复，也可通过药水补充。

---

### 2.3 元素反应引擎（Elemental Reaction Engine）

#### 2.3.1 设计理念

受《原神》启发，但完全重构为 Minecraft 原生事件驱动模型。**元素不是状态效果，而是可叠加、可衰减的“附着层”**。

#### 2.3.2 元素类型

| 元素 | ID | 特性 |

|------|----|------|
| 火（Pyro） | `pyro` | 高伤害、持续燃烧 |
| 水（Hydro） | `hydro` | 治疗、减速 |
| 雷（Electro） | `electro` | 高频打击、麻痹 |
| 冰（Cryo） | `cryo` | 冻结、护甲降低 |
| 风（Anemo） | `anemo` | 扩散、聚怪 |
| 岩（Geo） | `geo` | 护盾、地形生成 |

#### 2.3.3 反应规则表

| 元素 A | 元素 B | 反应效果 |

|--------|--------|--------|
| Pyro | Hydro | 蒸汽爆炸（范围击退 + 盲目） |
| Electro | Cryo | 超导（降低护甲 + 冻结） |
| Anemo | 任意 | 扩散（扩大 AoE 范围） |
| Geo | Pyro/Hydro/Electro/Cryo | 结晶（生成对应元素晶盾） |

#### 2.3.4 技术实现

- **元素附着**：由 `ElementAttachmentManagerMec.java` 管理，每个实体拥有 `ElementStack`：
  
  ```java
  public class ElementStack {
      private Map<String, ElementLayer> layers; // key: element_id
      public void apply(String elementId, int durationTicks, float intensity);
      public void decay(); // 每 tick 衰减
  }

  ```

- **反应判定**：由 `ElementalReactionEngineMec.java` 在 `LivingEntityDamageEvent` 或 `ProjectileHitEvent` 中触发，遍历所有附着层，匹配反应规则。

---

### 2.4 史诗级 Boss 挑战

#### 2.4.1 Boss 模板系统

每个 Boss 是一个 JSON 配置文件，例如：

```json
{
  "boss_id": "chronosoul:re8_dimi",
  "display_name": "Dimi the Time Warden",
  "health": 500,
  "phases": [
    {
      "threshold": 1.0,
      "behaviors": ["summon_minions"]
    },
    {
      "threshold": 0.7,
      "behaviors": ["time_stop", "aoe_pulse"]
    },
    {
      "threshold": 0.3,
      "behaviors": ["berserk", "heal_self"]
    }
  ],
  "drops": [
    { "item": "chronosoul:soulbound_sword", "chance": 0.1 }
  ]
}
```

#### 2.4.2 行为控制

- 由 `BossAiControllerMec.java` 实现状态机：
  
  ```java
  public void tick(BossEntity boss) {
      float hpRatio = boss.getHealth() / boss.getMaxHealth();
      Phase current = getPhase(hpRatio);
      if (current != lastPhase) {
          switchPhase(current);
          lastPhase = current;
      }
      executeBehaviors(current.behaviors);
  }
  ```

#### 2.4.3 刷怪逻辑

- 仅在特定结构（如“时之祭坛”）或维度生成。
- 由 `modmechanic/worldmechanic/entity/ChronoBossSpawner.java` 控制，防止滥刷。

---

### 2.5 世界融合机制

#### 2.5.1 战利品注入

- 使用 `LootTableEvents.MODIFY` 事件，在以下战利品表中注入：
  - `chests/stronghold_library`
  - `chests/end_city_treasure`
  - `entities/ender_dragon`
- 注入物品包括：
  - `Soul Crystal`（用于升级）
  - 起始装备（如 `Iron Sword of Warrior`）

#### 2.5.2 结构生成

- 新增“时之祭坛”结构，使用 `StructureMechanicModule.java` 注册。
- 功能：
  - 职业转职（战士 → 骑士）
  - 技能重置（消耗魂晶）
  - 元素亲和测试

#### 2.5.3 维度扩展（未来）

- 计划加入“**时之回廊**”维度：
  - 无昼夜、无天气
  - 高密度精英怪
  - 专属 Boss 与稀有资源
  - 传送门由 9 个“时之碎片”激活

---

## 🧱 三、代码架构设计哲学

### 3.1 三层核心分层

| 层级 | 包路径 | 职责 | 关键原则 |

|------|--------|------|--------|
| **注册声明层** | `com.chronosoulsociety.chronosoul.modules.*` | 定义“有什么” | 每个游戏内容 = 一个注册模块（如 `WarriorModule.java`） |
| **玩法逻辑层** | `com.chronosoulsociety.chronosoul.modmechanic.modulesmechanic.*` | 实现“怎么玩” | 所有类以 `Mec.java` 结尾，无状态、纯逻辑 |
| **世界集成层** | `com.chronosoulsociety.chronosoul.modmechanic.worldmechanic.*` | 决定“在哪出现” | 使用 Fabric API 钩子，不侵入核心逻辑 |

**命名规范**：

- `*Module.java` → 注册声明
- `*Mec.java` → 玩法机制  
- `*Mechanic.java` → 世界机制（部分遗留）

### 3.2 数据驱动设计

所有内容均可通过 JSON 配置，无需编译 Java：

#### 角色模版

```json
{
  "class_id": "chronosoul:warrior",
  "display_name": "Warrior",
  "base_health": 24,
  "base_mana": 10,
  "growth_curve": "linear",
  "unlocked_skills": ["slash", "taunt"],
  "passive_talents": ["iron_skin", "battle_fury"]
}
```

#### 技能定义

```json
{
  "skill_id": "chronosoul:heal",
  "type": "ACTIVE",
  "mana_cost": 20,
  "cooldown_seconds": 10.0,
  "effects": [
    { "type": "HEAL", "amount": 6 }
  ]
}
```

#### 元素反应

```json
[
  {
    "input": ["pyro", "hydro"],
    "output": "vaporize",
    "effect": { "type": "EXPLOSION", "power": 2.0 }
  }
]
```

### 3.3 持久化与兼容性

- **存储路径**：  
  `saves/global_playerdata/chronosoul/<uuid>.dat`
- **数据结构**：

  ```nbt
  {
    Level: 42,
    Class: "chronosoul:warrior",
    Skills: ["fireball", "heal"],
    Elements: { pyro: 0.8, hydro: 0.0 },
    InventoryBindings: { "sword": true }
  }
  ```

- **版本迁移**：`DataMigrator.java` 支持从 v1 → v2 自动转换。
- **多人安全**：所有关键操作（升级、技能释放）必须经服务端验证。

---

## 📂 四、完整项目结构（开发者视角）

### 4.1 Java 源码目录

```text
src/main/java/com/chronosoulsociety/chronosoul/
├── ChronoSoul.java                          ← 主类（通用初始化）
├── client/ChronoSoulClient.java             ← 客户端入口（渲染/音效/屏幕）
│
├── core/
│   ├── registration/                        ← 注册物品、方块、状态效果等
│   │   ├── ModItems.java
│   │   ├── ModBlocks.java
│   │   └── ModEntities.java
│   ├── network/PacketHandler.java           ← 数据包通信
│   ├── config/ChronoSoulConfig.java         ← 配置（Cloth Config 集成）
│   ├── lifecycle/PlayerLifecycleHandler.java← 玩家加入/死亡/切换世界事件
│   └── events/GlobalEventListeners.java     ← 全局事件（伤害、掉落、经验等）
│
├── api/                                     ← 接口层（供其他模组扩展）
│   ├── ICharacterDataProvider.java
│   ├── ISkillDataProvider.java
│   ├── IElementReactionProvider.java
│   └── IBossTemplateProvider.java
│
├── registry/                                ← 自定义注册表
│   └── ChronoSoulRegistries.java            ← 如 CLASS, ELEMENT, SKILL 等
│
├── modules/                                 ← ★ 注册声明层 ★
│   ├── character/
│   │   ├── CharacterModule.java
│   │   └── data/
│   │       ├── PlayerClassTemplate.java
│   │       └── AttributeGrowthCurve.java
│   ├── skill/
│   │   ├── SkillModule.java
│   │   └── data/SkillDefinition.java
│   ├── element/
│   │   ├── ElementModule.java
│   │   └── data/
│   │       ├── ElementType.java
│   │       └── ElementReactionRule.java
│   ├── boss/
│   │   ├── BossModule.java
│   │   └── data/BossTemplate.java
│   └── world/                               ← 世界相关数据模板
│       └── data/BiomeMechanicProfile.java
│
├── modmechanic/
│   ├── modulesmechanic/                     ← ★ 玩法逻辑层（*Mec.java）★
│   │   ├── charactermechanic/
│   │   │   ├── CharacterMechanicModule.java
│   │   │   ├── LevelingSystemMec.java
│   │   │   └── AttributeCalculatorMec.java
│   │   ├── skillmechanic/
│   │   │   ├── SkillMechanicModule.java
│   │   │   ├── SkillCastHandlerMec.java
│   │   │   └── SkillCooldownManagerMec.java
│   │   ├── elementmechanic/
│   │   │   ├── ElementMechanicModule.java
│   │   │   ├── ElementalReactionEngineMec.java
│   │   │   └── ElementAttachmentManagerMec.java
│   │   └── bossmechanic/
│   │       ├── BossMechanicModule.java
│   │       └── BossAiControllerMec.java
│   │
│   └── worldmechanic/                       ← ★ 世界集成层 ★
│       ├── biomemechanic/
│       │   └── BiomeWorldMechanic.java
│       ├── structuremechanic/
│       │   └── StructureWorldMechanic.java
│       ├── lootmechanic/
│       │   └── LootTableInjector.java
│       └── dimensionmechanic/
│           └── SoulRealmDimension.java
│
├── persistence/                             ← 永久数据存储（跨世界）
│   ├── storage/GlobalPlayerStorage.java     ← 核心：读写 player-data/*.dat
│   ├── cache/PlayerDataCache.java           ← 内存缓存（防频繁IO）
│   ├── migration/DataMigrator.java          ← 版本迁移
│   └── data/PlayerCharacterData.java        ← 序列化数据载体
│
└── utils/
    ├── nbt/NbtSerializer.java               ← NBT ↔ Java 对象转换
    ├── validation/DataValidator.java        ← 数据校验（防崩溃）
    └── algorithm/RngUtils.java              ← 随机算法工具
```

### 4.2 资源文件目录

```text
src/main/resources/
├── fabric.mod.json
├── chronosoul.mixins.json
├── assets/chronosoul/
│   ├── icon.png
│   └── lang/
│       ├── en_us.json
│       └── zh_cn.json
└── data/chronosoul/
    ├── characters/
    │   ├── warrior.json
    │   ├── mage.json
    │   └── archer.json
    ├── skills/
    │   ├── fireball.json
    │   ├── heal.json
    │   └── dash.json
    ├── elements/
    │   └── reactions.json
    ├── bosses/
    │   └── re8_dimi.json
    └── progression/
        └── level_curve.json
```

---

## 🛠 五、开发路线图（Roadmap）

### 第一阶段（Alpha）✅

- [x] 基础角色注册与升级
- [x] 跨世界数据存取
- [x] 简易技能系统（火球术）
- [x] 战利品表注入

### 第二阶段（Beta）⏳

- [ ] 职业分支系统（战士 → 骑士 / 狂战士）
- [ ] 元素反应可视化粒子
- [ ] 时之祭坛结构生成
- [ ] 多人魂火共享（公会系统雏形）

### 第三阶段（正式版）🔮

- [ ] 自定义维度“时之回廊”
- [ ] 装备词条与强化系统
- [ ] 社区 JSON 编辑器支持
- [ ] Mod Compatibility Layer（兼容主流 RPG 模组）

---

## 🌍 六、社区与贡献

《Chrono Soul》由 **ChronoSoulSociety（时魂社）** 开发，欢迎社区参与：

- **翻译**：完善 `zh_cn.json`、`ja_jp.json` 等语言文件
- **平衡**：调整 `level_curve.json` 或技能参数
- **创作**：设计新 Boss 模板或职业
- **代码**：提交 PR 修复 Bug 或优化性能

我们相信：**最好的 RPG 模组，是由玩家共同书写的传奇**。

---

## 🔚 结语

在方块世界的无限可能中，《Chrono Soul》为你点燃一盏不灭的魂灯。  
无论你穿越多少次世界，经历多少次死亡，你的故事——**永不重置**。

> **Worlds may fall. Souls endure.**  
> —— ChronoSoulSociety

