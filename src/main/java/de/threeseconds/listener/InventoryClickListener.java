package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.collections.CollectionInventories;
import de.threeseconds.jobs.Job;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class InventoryClickListener implements Listener {

    public InventoryClickListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onInventoyClick(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getClickedInventory() == null) return;

        if(!(inventoryClickEvent.getWhoClicked() instanceof Player player)) return;

        if(player.getOpenInventory().getSlotType(8) == InventoryType.SlotType.QUICKBAR) {
            inventoryClickEvent.setCancelled(true);
            return;
        }

        if(inventoryClickEvent.getSlotType() == InventoryType.SlotType.QUICKBAR && inventoryClickEvent.getSlot() == 8) {
            inventoryClickEvent.setCancelled(true);

        }

        if((inventoryClickEvent.getClick() == ClickType.NUMBER_KEY && inventoryClickEvent.getHotbarButton() == 8)) {
            inventoryClickEvent.setCancelled(true);

        }

        if(inventoryClickEvent.getInventory().getHolder() instanceof InventoryBuilder inventoryBuilder) {
            inventoryBuilder.handleClick(inventoryClickEvent);

            return;
        }

    }

}
