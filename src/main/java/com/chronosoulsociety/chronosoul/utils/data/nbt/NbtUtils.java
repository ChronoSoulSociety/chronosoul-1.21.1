package com.chronosoulsociety.chronosoul.utils.data.nbt;

import com.chronosoulsociety.chronosoul.neomodules.element.ElementalStack;
import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NbtUtils {
    // Write UUID to NBT
    public static void writeUuid(NbtCompound nbt, String key, UUID uuid) {
        nbt.putUuid(key, uuid);
    }

    // Read UUID from NBT
    public static UUID readUuid(NbtCompound nbt, String key) {
        return nbt.getUuid(key);
    }

    // Write ElementalStack to NBT
    public static void writeElementalStack(NbtCompound nbt, String key, ElementalStack stack) {
        if (stack != null && !stack.isEmpty()) {
            NbtCompound stackNbt = new NbtCompound();
            stack.writeNbt(stackNbt);
            nbt.put(key, stackNbt);
        }
    }

    // Read ElementalStack from NBT
    public static ElementalStack readElementalStack(NbtCompound nbt, String key) {
        if (nbt.contains(key, NbtElement.COMPOUND_TYPE)) {
            return ElementalStack.fromNbt(nbt.getCompound(key));
        }
        return null;
    }

    // Write list of ElementalStacks to NBT
    public static void writeElementalStackList(NbtCompound nbt, String key, List<ElementalStack> stacks) {
        NbtList list = new NbtList();
        for (ElementalStack stack : stacks) {
            if (stack != null && !stack.isEmpty()) {
                NbtCompound stackNbt = new NbtCompound();
                stack.writeNbt(stackNbt);
                list.add(stackNbt);
            }
        }
        nbt.put(key, list);
    }

    // Read list of ElementalStacks from NBT
    public static List<ElementalStack> readElementalStackList(NbtCompound nbt, String key) {
        List<ElementalStack> stacks = new ArrayList<>();
        if (nbt.contains(key, NbtElement.LIST_TYPE)) {
            NbtList list = nbt.getList(key, NbtElement.COMPOUND_TYPE);
            for (int i = 0; i < list.size(); i++) {
                NbtCompound stackNbt = list.getCompound(i);
                ElementalStack stack = ElementalStack.fromNbt(stackNbt);
                if (stack != null && !stack.isEmpty()) {
                    stacks.add(stack);
                }
            }
        }
        return stacks;
    }

    // Write float array to NBT
    public static void writeFloatArray(NbtCompound nbt, String key, float[] array) {
        NbtList list = new NbtList();
        for (float f : array) {
            list.add(NbtFloat.of(f));
        }
        nbt.put(key, list);
    }

    // Read float array from NBT
    public static float[] readFloatArray(NbtCompound nbtCompound, String key) {
        if (!nbtCompound.contains(key, NbtElement.LIST_TYPE)) return new float[0];
        NbtList list = nbtCompound.getList(key, NbtElement.FLOAT_TYPE);
        float[] array = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.getFloat(i);
        }
        return array;
    }

    // Write int array to NBT
    public static void writeIntArray(NbtCompound nbt, String key, int[] array) {
        nbt.putIntArray(key, array);
    }

    // Read int array from NBT
    public static int[] readIntArray(NbtCompound nbt, String key) {
        return nbt.getIntArray(key);
    }

    // Write boolean to NBT (as byte)
    public static void writeBoolean(NbtCompound nbt, String key, boolean value) {
        nbt.putBoolean(key, value);
    }

    // Read boolean from NBT (as byte)
    public static boolean readBoolean(NbtCompound nbt, String key) {
        return nbt.getBoolean(key);
    }

    // Write string list to NBT
    public static void writeStringList(NbtCompound nbt, String key, List<String> list) {
        NbtList stringList = new NbtList();
        for (String s : list) {
            stringList.add(NbtString.of(s));
        }
        nbt.put(key, stringList);
    }

    // Read string list from NBT
    public static List<String> readStringList(NbtCompound nbt, String key) {
        List<String> list = new ArrayList<>();
        if (nbt.contains(key, NbtElement.LIST_TYPE)) {
            NbtList stringList = nbt.getList(key, NbtElement.STRING_TYPE);
            for (int i = 0; i < stringList.size(); i++) {
                list.add(stringList.getString(i));
            }
        }
        return list;
    }

    // Clone NBT compound
    public static NbtCompound cloneNbt(NbtCompound nbt) {
        return nbt.copy();
    }

    // Merge two NBT compounds
    public static NbtCompound mergeNbt(NbtCompound target, NbtCompound source) {
        NbtCompound result = target.copy();
        for (String key : source.getKeys()) {
            NbtElement element = source.get(key);
            result.put(key, element.copy());
        }
        return result;
    }
}