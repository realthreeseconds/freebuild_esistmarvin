package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.collections.Collection;
import de.threeseconds.collections.CollectionItem;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class CollectionListener implements Listener {

    public CollectionListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onPick(BlockDropItemEvent blockDropItemEvent) {
        Player player = blockDropItemEvent.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(blockDropItemEvent.getBlock().getWorld().getName().equals("freebuildWorld")) {

            blockDropItemEvent.setCancelled(true);

            AtomicReference<Item> droppedItem = new AtomicReference<>();

            for(int i = 0; i < blockDropItemEvent.getItems().size(); i++) {
                droppedItem.set(blockDropItemEvent.getItems().get(i));
            }

            Collection collection;
            CollectionItem collectionItem;

            if(droppedItem.get() != null) {
                player.getWorld().dropItem(droppedItem.get().getLocation(), droppedItem.get().getItemStack());
                if(freeBuildPlayer.getCollectionPlayer().getCollectionByItem(droppedItem.get().getItemStack().getType()) != null) {
                    collection = freeBuildPlayer.getCollectionPlayer().getCollectionByItem(droppedItem.get().getItemStack().getType());
                    if(freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, droppedItem.get().getItemStack().getType()) != null) {
                        collectionItem = freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, droppedItem.get().getItemStack().getType());

                        if(!freeBuildPlayer.getCollectionPlayer().hasItemLevelMaxed(collection, collectionItem)) {
                            if(droppedItem.get().getItemStack().getType() == collectionItem.getCollectionItem().getItemStack().getType()) {
                                if(player.getInventory().firstEmpty() == -1) {
                                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Dein Inventar ist voll!"));
                                } else {
                                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1);
                                }

                                FreeBuild.getInstance().getCollectionManager().addItemsToCollection(freeBuildPlayer, collection, collectionItem, droppedItem.get().getItemStack().getAmount());
                            }
                        }

                    }

                }
            }


        }
    }

    @EventHandler
    public void onKillEntity(EntityDeathEvent entityDeathEvent) {
        LivingEntity livingEntity = entityDeathEvent.getEntity();

        if(livingEntity.getType() == EntityType.PLAYER) return;

        if(entityDeathEvent.getEntity().getKiller() != null) {
            Player player = entityDeathEvent.getEntity().getKiller();
            FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

            AtomicReference<ItemStack> droppedItem = new AtomicReference<>();

            for(int i = 0; i < entityDeathEvent.getDrops().size(); i++) {
                droppedItem.set(entityDeathEvent.getDrops().get(i));
            }

            Collection collection;
            CollectionItem collectionItem;
            if(droppedItem.get() != null) {
                if(freeBuildPlayer.getCollectionPlayer().getCollectionByItem(droppedItem.get().getType()) != null) {
                    collection = freeBuildPlayer.getCollectionPlayer().getCollectionByItem(droppedItem.get().getType());
                    if(freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, droppedItem.get().getType()) != null) {
                        collectionItem = freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, droppedItem.get().getType());

                        if(droppedItem.get().getType() == collectionItem.getCollectionItem().getItemStack().getType()) {
                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1);

                            FreeBuild.getInstance().getCollectionManager().addItemsToCollection(freeBuildPlayer, collection, collectionItem, droppedItem.get().getAmount());
                        }
                    }

                }
            }
        }
    }

    @EventHandler
    public void onFishing(PlayerFishEvent playerFishEvent) {
        Player player = playerFishEvent.getPlayer();
        Entity fish = playerFishEvent.getCaught();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(fish instanceof Item item) {
            Collection collection;
            CollectionItem collectionItem;
            if(freeBuildPlayer.getCollectionPlayer().getCollectionByItem(item.getItemStack().getType()) != null) {
                collection = freeBuildPlayer.getCollectionPlayer().getCollectionByItem(item.getItemStack().getType());
                if(freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, item.getItemStack().getType()) != null) {
                    collectionItem = freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, item.getItemStack().getType());

                    if(item.getItemStack().getType() == collectionItem.getCollectionItem().getItemStack().getType()) {
                        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1);

                        FreeBuild.getInstance().getCollectionManager().addItemsToCollection(freeBuildPlayer, collection, collectionItem, item.getItemStack().getAmount());
                    }
                }

            }

        }

    }

    @EventHandler
    public void onSmelting(InventoryClickEvent inventoryClickEvent) {
        Player player = (Player)inventoryClickEvent.getWhoClicked();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(inventoryClickEvent.getView().getType() == InventoryType.FURNACE || inventoryClickEvent.getView().getType() == InventoryType.BLAST_FURNACE) {

            if(inventoryClickEvent.getCurrentItem() == null) return;

            Collection collection;
            CollectionItem collectionItem;
            if(freeBuildPlayer.getCollectionPlayer().getCollectionByItem(inventoryClickEvent.getCurrentItem().getType()) != null) {
                collection = freeBuildPlayer.getCollectionPlayer().getCollectionByItem(inventoryClickEvent.getCurrentItem().getType());
                if(freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, inventoryClickEvent.getCurrentItem().getType()) != null) {
                    collectionItem = freeBuildPlayer.getCollectionPlayer().getCollectionItemFromItemStack(collection, inventoryClickEvent.getCurrentItem().getType());

                    if(inventoryClickEvent.getCurrentItem().getType() == collectionItem.getCollectionItem().getItemStack().getType()) {
                        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1);

                        FreeBuild.getInstance().getCollectionManager().addItemsToCollection(freeBuildPlayer, collection, collectionItem, inventoryClickEvent.getCurrentItem().getAmount());
                    }
                }

            }

        }

    }
}
