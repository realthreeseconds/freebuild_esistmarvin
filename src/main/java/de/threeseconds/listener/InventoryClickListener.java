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

        if(inventoryClickEvent.getCurrentItem() == null) {
            inventoryClickEvent.setCancelled(true);
            return;
        }

        if(inventoryClickEvent.getClick() == ClickType.NUMBER_KEY && inventoryClickEvent.getHotbarButton() == 8) inventoryClickEvent.setCancelled(true);

        if(inventoryClickEvent.getCurrentItem().getItemMeta() == null) return;

        if(FreeBuild.getInstance().checkPDC("menu-item", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")) {
            inventoryClickEvent.setCancelled(true);
            return;
        }


        if(inventoryClickEvent.getInventory().getHolder() instanceof InventoryBuilder inventoryBuilder) {
            inventoryBuilder.handleClick(inventoryClickEvent);

            return;
        }

        if(inventoryClickEvent.getView().title().equals(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#E0167B:#D9D938>Menü</gradient>"))) {
            inventoryClickEvent.setCancelled(true);

            if(inventoryClickEvent.getCurrentItem() == null) return;

            if(inventoryClickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD && inventoryClickEvent.getSlot() == 47) {
                FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

                player.closeInventory();
                player.teleport(freeBuildPlayer.getFastTravelLocation());
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                return;
            }

            if(inventoryClickEvent.getCurrentItem().getType() == Material.PAPER && inventoryClickEvent.getSlot() == 24) {
                FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

                new CollectionInventories.DefaultInventory(freeBuildPlayer).open(player);
                return;
            }
        }

    }

}
