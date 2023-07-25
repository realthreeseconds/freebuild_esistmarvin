package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.MenuInventories;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ItemInteractListener implements Listener {

    public ItemInteractListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent playerInteractEvent) {
        if(playerInteractEvent.getItem() == null || playerInteractEvent.getMaterial() == Material.AIR) return;

        Player player = playerInteractEvent.getPlayer();
        if(playerInteractEvent.getAction().isRightClick()) {
            if(playerInteractEvent.getMaterial() == Material.NETHER_STAR) {

                if(FreeBuild.getInstance().checkPDC("menu-item", playerInteractEvent.getItem().getItemMeta().getPersistentDataContainer(), "<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")) {
                    new MenuInventories.DefaultInventory(FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player)).open(player);
                    player.playSound(player, Sound.BLOCK_BARREL_OPEN, 1, 1);

                    return;
                }
            }
        }
    }

}
