package de.threeseconds.crafting;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryCrafting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.inventory.*;

import java.util.*;
import java.util.List;

public class CraftingHandle implements Listener {

    private List<CustomItem> customItems;

    public CraftingHandle() {
        this.customItems = new ArrayList<>();
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent pluginEnableEvent) {

        CustomItem i = new CustomItem(customItem -> {

            List<Component> lore = new ArrayList<>();
            lore.add(MiniMessage.miniMessage().deserialize(" "));
            lore.add(MiniMessage.miniMessage().deserialize(" <dark_gray>● <gray>Geschwindigkeit <dark_gray>» <white>Iron").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lore.add(MiniMessage.miniMessage().deserialize(" <dark_gray>● <gray>Haltbarkeit <dark_gray>» <green>220<dark_gray>/<green>220").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lore.add(MiniMessage.miniMessage().deserialize(" "));
            lore.add(MiniMessage.miniMessage().deserialize("<b>" + ItemRarity.COMMON.getColorCode() + WordUtils.capitalize(ItemRarity.COMMON.name().toLowerCase())).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

            customItem
                    .itemStack(
                            new ItemBuilder(Material.STONE_PICKAXE, (short) 220)
                                    .setKey("hardend_pickaxe")
                                    .setDisplayName(MiniMessage.miniMessage().deserialize("<gray><b>Hardend Pickaxe</b>"))
                                    .setLore(lore)
                                    .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                                    .getItemStack())
                    .durability(220)
                    .namespacedKey("hardned_pickaxe")
                    .shapedRecipe()
                    .shape("SSS", " T ", " T ")
                    .ingredient('S', new ItemStack(Material.STONE))
                    .ingredient('T', new ItemStack(Material.STICK));
        });


        i.add();

        new CustomItem(customItem -> customItem
                .itemStack(new ItemBuilder(Material.COBBLESTONE).setKey("copper_infused_cobblestone").setDisplayName(MiniMessage.miniMessage().deserialize("<gold><b>Copper Infused Cobblestone</b>")).getItemStack())
                .namespacedKey("copper_infused_cobblestone")
                .shapedRecipe()
                .shape("CSC", "SSS", "CSC")
                .ingredient('C', new ItemStack(Material.RAW_COPPER_BLOCK))
                .ingredient('S', new ItemStack(Material.COBBLESTONE))).add();



    }

/*
    @EventHandler
    public void onEveryItem(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getCursor() == null || inventoryClickEvent.getCursor().getType() == Material.AIR || inventoryClickEvent.getCurrentItem() == null) return;

        ItemStack currentItem = inventoryClickEvent.getCursor();
        ItemMeta meta = currentItem.getItemMeta();
        List<Component> lore = new ArrayList<>();
        lore.add(MiniMessage.miniMessage().deserialize(" "));
        lore.add(MiniMessage.miniMessage().deserialize("<b>" + ItemRarity.COMMON.getColorCode() + ItemRarity.COMMON.name()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        meta.lore(lore);
        currentItem.setItemMeta(meta);
        inventoryClickEvent.setCursor(currentItem);
    }

 */

    @EventHandler
    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {
        if(inventoryOpenEvent.getView().getTopInventory() instanceof CraftInventoryCrafting) {
            inventoryOpenEvent.setCancelled(true);

            new CraftingInventory().open((Player) inventoryOpenEvent.getPlayer());
        }
    }


}
