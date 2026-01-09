package com.chronosoulsociety.chronosoul.neomodules.element;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import java.util.ArrayList;
import java.util.List;

public class ElementalUtils {
    // Merge two elemental stacks of the same type
    public static ElementalStack mergeStacks(ElementalStack stack1, ElementalStack stack2) {
        if (stack1 == null || stack1.isEmpty()) {
            return stack2;
        }
        if (stack2 == null || stack2.isEmpty()) {
            return stack1;
        }
        if (stack1.getType() != stack2.getType()) {
            return stack1; // Cannot merge different element types
        }
        
        int newAmount = stack1.getAmount() + stack2.getAmount();
        float newIntensity = (stack1.getIntensity() + stack2.getIntensity()) / 2.0f;
        boolean isInfinite = stack1.isInfinite() || stack2.isInfinite();
        int newDuration = isInfinite ? 0 : Math.max(stack1.getDurationTicks(), stack2.getDurationTicks());
        
        return new ElementalStack(stack1.getType(), newAmount, newDuration, newIntensity);
    }
    
    // Transfer element from one container to another
    public static int transferElement(ElementalContainer from, ElementalContainer to, ElementalType type, int amount, float intensity) {
        if (!from.canExtractElement(type, amount, intensity)) {
            return 0;
        }
        
        ElementalStack extracted = from.extractElement(type, amount, intensity);
        if (extracted == null || extracted.isEmpty()) {
            return 0;
        }
        
        if (to.canInsertElement(extracted)) {
            if (to.insertElement(extracted)) {
                return extracted.getAmount();
            } else {
                // Failed to insert, return to original container
                from.insertElement(extracted);
                return 0;
            }
        } else {
            // Cannot insert, return to original container
            from.insertElement(extracted);
            return 0;
        }
    }
    
    // Write a list of elemental stacks to NBT
    public static NbtCompound writeElementalStacksToNbt(List<ElementalStack> stacks) {
        NbtCompound nbt = new NbtCompound();
        for (int i = 0; i < stacks.size(); i++) {
            ElementalStack stack = stacks.get(i);
            if (stack != null && !stack.isEmpty()) {
                NbtCompound stackNbt = new NbtCompound();
                stack.writeNbt(stackNbt);
                nbt.put("Stack_" + i, stackNbt);
            }
        }
        return nbt;
    }
    
    // Read a list of elemental stacks from NBT
    public static List<ElementalStack> readElementalStacksFromNbt(NbtCompound nbt) {
        List<ElementalStack> stacks = new ArrayList<>();
        for (String key : nbt.getKeys()) {
            if (key.startsWith("Stack_")) {
                NbtElement element = nbt.get(key);
                if (element instanceof NbtCompound stackNbt) {
                    ElementalStack stack = ElementalStack.fromNbt(stackNbt);
                    if (stack != null && !stack.isEmpty()) {
                        stacks.add(stack);
                    }
                }
            }
        }
        return stacks;
    }
    
    // Calculate the total element value based on amount and intensity
    public static float calculateElementValue(ElementalStack stack) {
        if (stack == null || stack.isEmpty()) {
            return 0.0f;
        }
        return stack.getAmount() * stack.getIntensity();
    }
    
    // Check if two elemental stacks are compatible (can be merged)
    public static boolean areStacksCompatible(ElementalStack stack1, ElementalStack stack2) {
        if (stack1 == null || stack2 == null) {
            return true; // Null stacks are compatible with anything
        }
        return stack1.isEmpty() || stack2.isEmpty() || stack1.getType() == stack2.getType();
    }
    
    // Get the element display name in the current language
    public static String getElementDisplayName(ElementalType type, boolean useEnglish) {
        return useEnglish ? type.getEnglishName() : type.getChineseName();
    }
}