package de.threeseconds.collections;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ScopedComponent;
import net.kyori.adventure.text.TranslatableComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CollectionInventories {

    public static class DefaultInventory extends InventoryBuilder {

        public DefaultInventory(FreeBuildPlayer freeBuildPlayer) {
            super(9*5, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <dark_green>Collections <dark_gray>● <gray>Menü"));

            /* BORDER */
            this.setItems(0, 8, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());
            this.setItems(36, 44, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());


            /* Inventory */



            List<Component> lores = new ArrayList<>();
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier findest du eine Aufzählung").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aller momentan verfügbaren <dark_green>Collections<gray>.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <gray>Klicke auf eine der Items, um mehr zu erfahren.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(4, new ItemBuilder(Material.OAK_HANGING_SIGN).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#42D624:#138A13>Collections")).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
            lores.clear();

            int slot = 19;
            for(Collection collections : Collection.values()) {
                int abgeschlossen = Math.round((float) freeBuildPlayer.getCollectionPlayer().getCompletedItemsFromCollection(collections).size() / collections.getCollectionItems().size() * 100);

                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier siehst du deine " + WordUtils.capitalize(collections.name().toLowerCase()) + " Collection.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Abgeschlossen <dark_gray>» <yellow>" + abgeschlossen + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getCollectionPlayer().getCompletedItemsFromCollection(collections).size(), collections.getCollectionItems().size()) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getCollectionPlayer().getCompletedItemsFromCollection(collections).size() + "<dark_gray>/<gray>" + collections.getCollectionItems().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_aqua>» <gray>Klicke hier für mehr Informationen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                this.setItem(slot, collections.getMainItem().setCollection(collections).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● " + collections.getColorCode() + WordUtils.capitalize(collections.name().toLowerCase()))).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
                lores.clear();

                slot += 2;
            }

            this.setItem(44, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setKey("collections-back").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).addItemFlags(ItemFlag.values()).getItemStack());

            this.addClickHandler(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                inventoryClickEvent.setCancelled(true);

                if(FreeBuild.getInstance().checkPDC("collections-back", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>« <red>Zurück")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    player.openInventory(FreeBuild.getInstance().getMenuManager().openInventory(player, false));
                    return;
                }

                if(inventoryClickEvent.getView().title().equals(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <dark_green>Collections <dark_gray>● <gray>Menü"))) {
                    if(
                            FreeBuild.getInstance().checkPDC("collection-item", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), Collection.MINING.name()) ||
                                    FreeBuild.getInstance().checkPDC("collection-item", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), Collection.FORAGING.name()) ||
                                    FreeBuild.getInstance().checkPDC("collection-item", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), Collection.FISHING.name()) ||
                                    FreeBuild.getInstance().checkPDC("collection-item", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), Collection.COMBAT.name())) {
                        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                        new CollectionInventory(freeBuildPlayer, Collection.valueOf((String) FreeBuild.getInstance().getPDCValue("collection-item", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), PersistentDataType.STRING))).open(player);
                        return;
                    }

                }
            });
        }
    }

    public static class CollectionInventory extends InventoryBuilder {

        public CollectionInventory(FreeBuildPlayer freeBuildPlayer, Collection collection) {
            super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <dark_green>Collections <dark_gray>● <gray>Menü"));

            /* BORDER */
            this.setItems(0, 8, collection.getBorderPane().setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());
            this.setItems(45, 53, collection.getBorderPane().setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());


            /* Inventory */

            this.setItem(4, collection.getMainItem().setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + collection.getColorCode() + WordUtils.capitalize(collection.name().toLowerCase(), null))).getItemStack());

            HashMap<Integer, CollectionItem> collectionSlotItem = new HashMap<>();

            AtomicInteger atomicInteger = new AtomicInteger((collection == Collection.MINING ? 10 : 19));
            collection.getCollectionItems().forEach(items -> {

                int currentAmount = freeBuildPlayer.getCollectionPlayer().getItemAmountFromCollectionItem(collection, items);
                int currentMaxXP = freeBuildPlayer.getCollectionPlayer().getMaxXPByItemLevel(collection, items);

                List<Component> lores = new ArrayList<>();

                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier siehst du deinen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Fortschritt und Belohnungen bei " + collection.getColorCode() + "<lang:" + items.getCollectionItem().getItemStack().translationKey() + ">" + "<gray>.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>" + freeBuildPlayer.getCollectionPlayer().getCollectionItemLevel(collection, items) + " <gray>von <yellow>" + (items.getCollectionItemMaxXP().size())).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(Math.min(currentAmount, currentMaxXP), currentMaxXP) + "</st><reset> <dark_gray>» <gray>" + Math.min(currentAmount, currentMaxXP) + "<dark_gray>/<gray>" + currentMaxXP).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                if(!freeBuildPlayer.getCollectionPlayer().hasItemLevelMaxed(collection, items)) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray><lang:" + items.getCollectionItem().getItemStack().translationKey() + "> " + (freeBuildPlayer.getCollectionPlayer().getCollectionItemLevel(collection, items) + 1) + " Belohnung:").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("   <dark_gray>» <blue>" + "<lang:" + items.getCollectionItem().getItemStack().translationKey() + "> " + "Minion <gray>Rezept").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                }

                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_aqua>» <gray>Klicke hier für mehr Informationen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                this.setItem(atomicInteger.get(), items.getCollectionItem().setCollectionItem(items, atomicInteger.get()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + collection.getColorCode() + "<lang:" + items.getCollectionItem().getItemStack().translationKey() + ">")).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
                lores.clear();

                collectionSlotItem.put(atomicInteger.get(), items);

                if(atomicInteger.get() == 16 || atomicInteger.get() == 25 || atomicInteger.get() == 34 || atomicInteger.get() == 43) atomicInteger.getAndAdd(2);

                atomicInteger.getAndIncrement();


            });

            this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setKey("collections-back").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).addItemFlags(ItemFlag.values()).getItemStack());

            this.addClickHandler(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                inventoryClickEvent.setCancelled(true);

                if(FreeBuild.getInstance().checkPDC("collections-back", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>« <red>Zurück")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new DefaultInventory(freeBuildPlayer).open(player);
                    return;
                }

                if(FreeBuild.getInstance().checkPDC("collectionItem-itemSlot", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), inventoryClickEvent.getSlot())) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new CollectionItemProgressInventory(freeBuildPlayer, collection, collectionSlotItem.get(inventoryClickEvent.getSlot())).open(player);
                    return;
                }
            });
        }
    }


    public static class CollectionItemProgressInventory extends InventoryBuilder {
        public CollectionItemProgressInventory(FreeBuildPlayer freeBuildPlayer, Collection collection, CollectionItem collectionItem) {
            super(9*5, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <dark_green>Collections <dark_gray>● <gray>Menü"));

            /* BORDER */
            this.setItems(0, 8, collection.getBorderPane().setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());
            this.setItems(36, 44, collection.getBorderPane().setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());


            /* Inventory */

            this.setItem(4, collectionItem.getCollectionItem().setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + collection.getColorCode() + "<lang:" + collectionItem.getCollectionItem().getItemStack().translationKey() + ">")).addItemFlags(ItemFlag.values()).getItemStack());

            AtomicInteger atomicInteger = new AtomicInteger(20);
            AtomicInteger levelInteger = new AtomicInteger(1);

            collectionItem.getCollectionItemMaxXP().forEach(itemLevel -> {

                int fortschritt = Math.round((float) freeBuildPlayer.getCollectionPlayer().getItemAmountFromCollectionItem(collection, collectionItem) / itemLevel * 100);
                int currentAmount = freeBuildPlayer.getCollectionPlayer().getItemAmountFromCollectionItem(collection, collectionItem);

                List<Component> lores = new ArrayList<>();
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Fortschritt <dark_gray>» <yellow>" + (Math.min(fortschritt, 100)) + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getCollectionPlayer().getItemAmountFromCollectionItem(collection, collectionItem), itemLevel) + "</st><reset> <dark_gray>» <gray>" + Math.min(currentAmount, itemLevel) + "<dark_gray>/<gray>" + itemLevel).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Belohnung:").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <blue>FÜGE BELOHNUNG EIN").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                this.setItem(atomicInteger.get(), new ItemBuilder(Material.PLAYER_HEAD)
                        .setSkullTexture(
                                (atomicInteger.get() == 20 ? "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=" :
                                        (freeBuildPlayer.getCollectionPlayer().getItemAmountFromCollectionItem(collection, collectionItem) >= itemLevel ?
                                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=" :
                                                "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=")))
                        .setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + (atomicInteger.get() == 20 ? "<white>" : (freeBuildPlayer.getCollectionPlayer().getItemAmountFromCollectionItem(collection, collectionItem) > itemLevel ? "<green>" : "<red>")) + "Level " + levelInteger.get()))
                        .setLore(lores)
                        .addItemFlags(ItemFlag.values())
                        .getItemStack());

                lores.clear();

                atomicInteger.getAndIncrement();
                levelInteger.getAndIncrement();
            });


            this.setItem(44, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setKey("collections-back").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack());

            this.addClickHandler(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();
                inventoryClickEvent.setCancelled(true);

                if(FreeBuild.getInstance().checkPDC("collections-back", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>« <red>Zurück")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new CollectionInventory(freeBuildPlayer, collection).open(player);
                    return;
                }
            });
        }
    }

}
