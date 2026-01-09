package com.chronosoulsociety.chronosoul.neomodules.element;

public interface ElementalContainer {
    // Check if the container can accept the given elemental stack
    boolean canInsertElement(ElementalStack stack);
    
    // Try to insert the elemental stack into the container
    boolean insertElement(ElementalStack stack);
    
    // Check if the container can extract the specified amount and intensity of the given element type
    boolean canExtractElement(ElementalType type, int amount, float intensity);
    
    // Try to extract the specified amount and intensity of the given element type
    ElementalStack extractElement(ElementalType type, int amount, float intensity);
    
    // Get the current elemental stack of the given type in the container
    ElementalStack getElement(ElementalType type);
    
    // Check if the container contains any amount of the given element type
    boolean hasElement(ElementalType type);
    
    // Get the maximum amount of the given element type the container can hold
    int getMaxAmount(ElementalType type);
    
    // Get the maximum intensity of the given element type the container can hold
    float getMaxIntensity(ElementalType type);
    
    // Tick update for the container (e.g., for duration-based elements)
    default void tick() {
        // Default implementation does nothing
    }
}