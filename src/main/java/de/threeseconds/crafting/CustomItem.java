package de.threeseconds.crafting;

import de.threeseconds.FreeBuild;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.function.Consumer;


public class CustomItem {

    private ItemStack itemStack;
    private NamespacedKey namespacedKey;
    private ShapedRecipe shapedRecipe;
    private ShapelessRecipe shapelessRecipe;
    private Integer durability;
    private boolean singleStack;

    public CustomItem(Consumer<CustomItem> customItemConsumer) {
        customItemConsumer.accept(this);
    }

    public CustomItem itemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }

    public CustomItem durability(Integer durability) {
        this.durability = durability;
        return this;
    }

    public CustomItem namespacedKey(String key) {
        this.namespacedKey = new NamespacedKey(FreeBuild.getInstance().getPaperCore(), key);
        return this;
    }

    public CustomItem shapedRecipe() {
        this.shapedRecipe = new ShapedRecipe(this.namespacedKey, this.itemStack);
        return this;
    }

    public CustomItem shapelessRecipe() {
        this.shapelessRecipe = new ShapelessRecipe(this.namespacedKey, this.itemStack);
        return this;
    }

    public CustomItem shape(String... shape) {
        if(this.shapelessRecipe == null && this.shapedRecipe != null) {
            this.shapedRecipe.shape(shape);
        }
        return this;
    }

    public CustomItem ingredient(Character character, ItemStack itemNeeded) {
        this.shapedRecipe.setIngredient(character, itemNeeded);
        return this;
    }

    public void add() {
        Bukkit.removeRecipe(this.namespacedKey);
        Bukkit.addRecipe(this.shapedRecipe);
    }


    public ItemStack getItemStack() {
        return itemStack;
    }

    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public ShapedRecipe getShapedRecipe() {
        return shapedRecipe;
    }

    public ShapelessRecipe getShapelessRecipe() {
        return shapelessRecipe;
    }
}
