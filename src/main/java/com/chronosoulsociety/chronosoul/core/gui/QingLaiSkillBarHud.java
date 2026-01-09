package com.chronosoulsociety.chronosoul.core.gui;

import com.chronosoulsociety.chronosoul.client.ClientCharacterManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.RotationAxis;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.List;

public class QingLaiSkillBarHud {
    private static final int SLOT_WIDTH = 22;
    private static final int SLOT_HEIGHT = 22;
    private static final int SLOT_SPACING = 4;
    private static final int RIGHT_MARGIN = 10;
    private static final int BOTTOM_MARGIN = 35;
    
    // 技能图标资源位置注册
    private static final Identifier ICON_PROTECT = 
        Identifier.of("chrono_soul:textures/icons/skill_soul_protection.png");
    private static final Identifier ICON_SOUL_REALM = 
        Identifier.of("chrono_soul:textures/icons/skill_soul_realm.png");
    private static final Identifier ICON_DARK_DOMAIN = 
        Identifier.of("chrono_soul:textures/icons/skill_dark_domain.png");
    
    private static final Identifier[] ICONS = {
        ICON_PROTECT,
        ICON_SOUL_REALM,
        ICON_DARK_DOMAIN
    };
    
    private final MinecraftClient client;
    private final TextRenderer textRenderer;
    
    public QingLaiSkillBarHud(MinecraftClient client) {
        this.client = client;
        this.textRenderer = client.textRenderer;
    }
    
    public void render(DrawContext context, float tickDelta) {
        PlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        
        // 获取当前激活的角色ID
        String activeChar = ClientCharacterManager.getActiveCharacter();
        
        // 只有当角色是青籁时才渲染技能栏
        if (activeChar != null && activeChar.equals("qing_lai")) {
            renderSkillBar(context, tickDelta);
            renderTooltip(context, tickDelta);
        }
    }
    
    private void renderSkillBar(DrawContext context, float tickDelta) {
        Window window = client.getWindow();
        int screenWidth = window.getWidth();
        int screenHeight = window.getHeight();
        
        // 计算起始位置
        int totalWidth = SLOT_WIDTH * 3 + SLOT_SPACING * 2;
        int startX = screenWidth - (totalWidth + RIGHT_MARGIN);
        int y = screenHeight - BOTTOM_MARGIN;
        
        // 绘制三个技能槽（暂时跳过角色检查，直接渲染用于测试）
        for (int i = 0; i < 3; i++) {
            int x = startX + i * (SLOT_WIDTH + SLOT_SPACING);
            
            // 1. 绘制背景槽
            renderSlotBackground(context, x, y);
            
            // 2. 获取图标
            Identifier icon = ICONS[i];
            
            // 3. 模拟技能状态（实际项目中需要对接技能系统）
            // 这里使用固定值，后续替换为实际技能系统数据
            boolean onCooldown = false; // 冷却状态
            float cooldownProgress = 0.0f; // 冷却进度
            boolean canUse = true; // 法力是否足够
            
            // 4. 绘制技能图标（带状态效果）
            renderSkillIcon(context, x, y, icon, onCooldown, cooldownProgress, canUse);
            
            // 5. 绘制技能名称（保留文字显示，后续可以替换为只显示图标）
            String[] skillKeys = {
                "skill.qing_lai.protect",
                "skill.qing_lai.soul_world",
                "skill.qing_lai.dark_realm"
            };
            renderSkillName(context, x, y, skillKeys[i]);
        }
    }
    
    private void renderSkillIcon(DrawContext context, int x, int y, Identifier texture, 
                                boolean onCooldown, float cooldownProgress, boolean canUse) {
        // 图标绘制区域（居中于 22x22 槽）
        int iconSize = 16;
        int iconX = x + (SLOT_WIDTH - iconSize) / 2;
        int iconY = y + (SLOT_HEIGHT - iconSize) / 2;
        
        // 保存颜色状态
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        
        // 法力不足：变灰
        if (!canUse) {
            RenderSystem.setShaderColor(0.5f, 0.5f, 0.5f, 0.7f);
        } else {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
        
        // 绘制图标
        context.drawTexture(texture, iconX, iconY, 0, 0, iconSize, iconSize, iconSize, iconSize);
        
        // 恢复颜色
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        
        // 绘制冷却遮罩（扇形）
        if (onCooldown && cooldownProgress > 0) {
            renderCooldownOverlay(context, x, y, SLOT_WIDTH, SLOT_HEIGHT, cooldownProgress);
        }
        
        RenderSystem.disableBlend();
    }
    
    private void renderCooldownOverlay(DrawContext context, int x, int y, int width, int height, float progress) {
        // 保存矩阵
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        
        // 移动到槽中心
        float centerX = x + width / 2f;
        float centerY = y + height / 2f;
        matrices.translate(centerX, centerY, 0);
        
        // 旋转（注意：Minecraft Y轴向下，需反向）
        float angle = 360f * (1f - progress); // 顺时针减少
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-angle));
        
        // 绘制半透明黑色矩形（覆盖右半边）
        context.fill(
            0, -height,          // 左上
            width, height,       // 右下（实际只显示旋转后的部分）
            0x99000000           // 半透明黑色
        );
        
        // 恢复矩阵
        matrices.pop();
    }
    
    private void renderSlotBackground(DrawContext context, int x, int y) {
        // 绘制22x22的矩形背景，模仿原版快捷栏风格
        context.fill(x, y, x + SLOT_WIDTH, y + SLOT_HEIGHT, 0x80404040); // 半透明灰色背景
        context.drawBorder(x, y, SLOT_WIDTH, SLOT_HEIGHT, 0xFFFFFFFF); // 白色边框
    }
    
    private void renderSkillName(DrawContext context, int x, int y, String skillKey) {
        // 绘制技能名称，居中显示，使用更小的字体
        Text text = Text.translatable(skillKey);
        int textWidth = textRenderer.getWidth(text);
        int textHeight = textRenderer.fontHeight;
        
        // 计算居中位置，微调以适应22x22的槽位
        int textX = x + (SLOT_WIDTH - textWidth) / 2;
        int textY = y + (SLOT_HEIGHT - textHeight) / 2 + 2;
        
        // 绘制文本，白色
        context.drawText(textRenderer, text, textX, textY, 0xFFFFFFFF, false);
    }
    
    private void renderTooltip(DrawContext context, float tickDelta) {
        Window window = client.getWindow();
        int screenWidth = window.getWidth();
        int screenHeight = window.getHeight();
        
        // 计算起始位置
        int totalWidth = SLOT_WIDTH * 3 + SLOT_SPACING * 2;
        int startX = screenWidth - (totalWidth + RIGHT_MARGIN);
        int startY = screenHeight - BOTTOM_MARGIN;
        
        // 获取鼠标位置
        double mouseX = client.mouse.getX() / (double)window.getScaleFactor();
        double mouseY = client.mouse.getY() / (double)window.getScaleFactor();
        
        // 检测鼠标是否在技能槽上
        if (isMouseInSlot((int)mouseX, (int)mouseY, startX, startY)) {
            renderSkillTooltip(context, (int)mouseX, (int)mouseY, "skill.qing_lai.protect", "Skill 1: Creates a shield that absorbs 150 damage.");
        } else if (isMouseInSlot((int)mouseX, (int)mouseY, startX + SLOT_WIDTH + SLOT_SPACING, startY)) {
            renderSkillTooltip(context, (int)mouseX, (int)mouseY, "skill.qing_lai.soul_world", "Skill 2: Dual-mode attack - combo strikes or charged attack.");
        } else if (isMouseInSlot((int)mouseX, (int)mouseY, startX + (SLOT_WIDTH + SLOT_SPACING) * 2, startY)) {
            renderSkillTooltip(context, (int)mouseX, (int)mouseY, "skill.qing_lai.dark_realm", "Skill 3: Grants 20% buff, triple jump, and dash ability.");
        }
    }
    
    private boolean isMouseInSlot(int mouseX, int mouseY, int slotX, int slotY) {
        return mouseX >= slotX && mouseX <= slotX + SLOT_WIDTH && 
               mouseY >= slotY && mouseY <= slotY + SLOT_HEIGHT;
    }
    
    private void renderSkillTooltip(DrawContext context, int mouseX, int mouseY, String skillKey, String skillDescription) {
        // 绘制技能提示框
        List<Text> tooltipLines = new ArrayList<>();
        tooltipLines.add(Text.translatable(skillKey).formatted(Formatting.YELLOW, Formatting.BOLD));
        tooltipLines.add(Text.literal(skillDescription).formatted(Formatting.GRAY));
        
        // 计算提示框位置，确保不会超出屏幕
        int tooltipX = mouseX + 10;
        int tooltipY = mouseY - 10;
        
        // 绘制提示框
        context.drawTooltip(textRenderer, tooltipLines, tooltipX, tooltipY);
    }
}
