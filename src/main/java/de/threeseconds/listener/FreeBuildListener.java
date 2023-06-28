package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class FreeBuildListener implements Listener {

    public FreeBuildListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        blockPlaceEvent.setCancelled(blockPlaceEvent.getBlock().getWorld().getName().equals("world"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        blockBreakEvent.setCancelled(blockBreakEvent.getBlock().getWorld().getName().equals("world"));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if(entityDamageEvent.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            entityDamageEvent.setCancelled(true);
        }

    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent playerDropItemEvent) {

        if(FreeBuild.getInstance().checkPDC("menu-item", playerDropItemEvent.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer(), "<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")) {
            playerDropItemEvent.setCancelled(true);
        }

    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
        if(FreeBuild.getInstance().checkPDC("menu-item", playerSwapHandItemsEvent.getOffHandItem().getItemMeta().getPersistentDataContainer(), "<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")) {
            playerSwapHandItemsEvent.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {

    }

    @EventHandler
    public void onPlayerHealth(EntityRegainHealthEvent entityRegainHealthEvent) {
        if(entityRegainHealthEvent.getEntity() instanceof Player) {
            Player player = (Player) entityRegainHealthEvent.getEntity();

        }
    }

}
