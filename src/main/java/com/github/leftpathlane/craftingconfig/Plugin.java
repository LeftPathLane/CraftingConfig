package com.github.leftpathlane.craftingconfig;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private List<NamespacedKey> keys = new ArrayList<>();

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        final ConfigurationSection recipes = this.getConfig().getConfigurationSection("recipes");
        ConfigurationSection section;
        for (String key : recipes.getKeys(false)) {
            NamespacedKey nameKey = new NamespacedKey(this, key);
            keys.add(nameKey);
            section = recipes.getConfigurationSection(key);
            ShapedRecipe recipe = new ShapedRecipe(nameKey,
                    new ItemStack(Material.matchMaterial(section.getString("result"))));
            List<String> shape = section.getStringList("shape");
            recipe.shape(shape.toArray(new String[shape.size()]));
            ConfigurationSection shapekeys = section.getConfigurationSection("shapekeys");
            for (String keyName : shapekeys.getKeys(false)) {
                if (keyName.length() != 1)
                    continue;
                recipe.setIngredient(keyName.charAt(0), Material.matchMaterial(shapekeys.getString(keyName)));
            }
            this.getServer().addRecipe(recipe);
        }
    }

    @Override
    public void onDisable() {
        keys.forEach(key -> this.getServer().removeRecipe(key));
        keys.clear();
    }
}
