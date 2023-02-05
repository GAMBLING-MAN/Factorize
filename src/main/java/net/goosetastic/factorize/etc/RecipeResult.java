package net.goosetastic.factorize.etc;

import net.minecraft.world.item.ItemStack;

public class RecipeResult {
    public final boolean hasRecipe;
    public final ItemStack result;

    public RecipeResult(boolean hasRecipe, ItemStack result) {
        this.hasRecipe = hasRecipe;
        this.result = result;
    }
}
