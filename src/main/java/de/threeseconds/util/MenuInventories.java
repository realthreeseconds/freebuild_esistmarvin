package de.threeseconds.util;

import de.threeseconds.FreeBuild;
import de.threeseconds.collections.Collection;
import de.threeseconds.collections.CollectionInventories;
import de.threeseconds.jobs.Job;
import de.threeseconds.jobs.JobInventories;
import de.threeseconds.plot.inventories.PlotBrowser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class MenuInventories {

    public static class DefaultInventory extends InventoryBuilder {
        public DefaultInventory(FreeBuildPlayer freeBuildPlayer) {
            super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>Menü <dark_gray>● <gray>Übersicht"));

            /* BORDER */
            this.setItems(0, 8, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());
            this.setItems(45, 53, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());


            /* Inventory */
            List<Component> lores = new ArrayList<>();
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier findest du Statistiken").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>bezüglich deines Charakters.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gradient:#AB2F41:#FF2B24>❤ Leben</gradient> <dark_gray>» <white>" + (int) Math.round(freeBuildPlayer.getPlayerStats().health())).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gradient:#51E611:#48944D>\uD83D\uDEE1 Verteidigung</gradient> <dark_gray>» <white>" + freeBuildPlayer.getPlayerStats().defense()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gradient:#30CFD0:#926DD1>\uD83D\uDD25 Seelen</gradient> <dark_gray>» <white>" + freeBuildPlayer.getPlayerStats().souls()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <white>\uD83E\uDD7E Speed <dark_gray>» <white>" + freeBuildPlayer.getPlayerStats().speed()*100).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <red>⚔ Stärke <dark_gray>» <white>" + freeBuildPlayer.getPlayerStats().strength()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_red>\uD83D\uDDE1 Krit-Chance <dark_gray>» <white>" + (int)(freeBuildPlayer.getPlayerStats().critChance() * 100) + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_red>\uD83D\uDC80 Krit-Schaden <dark_gray>» <white>" + (int)(freeBuildPlayer.getPlayerStats().critDamage() * 100) + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(freeBuildPlayer.getPlayer().getPlayerProfile().getTextures()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Dein Profil")).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
            lores.clear();

            if(freeBuildPlayer.getJobPlayer().getActiveJob() != null) {
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier siehst du deinen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aktuellen Fortschritt im Beruf.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>" + freeBuildPlayer.getJobPlayer().getLevelByJob(freeBuildPlayer.getJobPlayer().getActiveJob()) + " <gray>von <yellow>" + freeBuildPlayer.getJobPlayer().getActiveJob().getJobLevels().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getJobPlayer().getXPByJob(freeBuildPlayer.getJobPlayer().getActiveJob()), freeBuildPlayer.getJobPlayer().getActiveJob().getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(freeBuildPlayer.getJobPlayer().getActiveJob()) - 1).getMaxXP()) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getJobPlayer().getCompactXPByJob(freeBuildPlayer.getJobPlayer().getActiveJob()) + "/" + freeBuildPlayer.getJobPlayer().getActiveJob().getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(freeBuildPlayer.getJobPlayer().getActiveJob()) - 1).getCompactMaxXP() + " XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                this.setItem(20, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(freeBuildPlayer.getJobPlayer().getActiveJob().getHeadTexture()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + freeBuildPlayer.getJobPlayer().getActiveJob().getJobName())).setLore(lores).getItemStack(), inventoryClickEvent -> {
                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                    new JobInventories.ProgressionInventory(freeBuildPlayer, freeBuildPlayer.getJobPlayer().getActiveJob(), true, 1).open(player);
                });
                lores.clear();
            } else {

                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du hast momentan keinen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aktiven <gold>Beruf<gray>.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Schau am Besten bei <gold>James").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>dem <gold>Schatzmeister <gray>vorbei.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                this.setItem(20, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>Ohne Beruf")).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA2ZDdiZWZjODJmMjAxZjgzZTE5Mzc2N2U2M2Y4MTAzNzIxNWFmZDQ4M2EzOGQzNjk2NTk4MmNhNmQwIn19fQ==").setLore(lores).getItemStack());
                lores.clear();
            }


            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>" + (freeBuildPlayer.getFreeBuildLevel() + 1)).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getCurrentXP(), freeBuildPlayer.getNeededFreeBuildXP(freeBuildPlayer.getFreeBuildLevel() + 1)) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getCurrentXP()) + "/" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getNeededFreeBuildXP(freeBuildPlayer.getFreeBuildLevel() + 1)) + " XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(22, new ItemBuilder(Material.STONE_PICKAXE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Level Fortschritt")).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new ProgressionInventory(freeBuildPlayer, 1).open(player);
            });
            lores.clear();

            int abgeschlossen = Math.round((float) freeBuildPlayer.getCollectionPlayer().getCompletedCollections().size() / Collection.values().length * 100);

            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier siehst du deine").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aktuellen Collections.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Abgeschlossen <dark_gray>» <yellow>" + abgeschlossen + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getCollectionPlayer().getCompletedCollections().size(), Collection.values().length) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getCollectionPlayer().getCompletedCollections().size() + "<dark_gray>/<gray>" + Collection.values().length).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(24, new ItemBuilder(Material.PAPER).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Collections")).setLore(lores).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new CollectionInventories.DefaultInventory(freeBuildPlayer).open(player);
            });
            lores.clear();

            this.setItem(29, new ItemBuilder(Material.WRITABLE_BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Quests")).getItemStack());

            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier siehst du Grundstücke").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>von anderen Spielern.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(31, new ItemBuilder(Material.GRASS_BLOCK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstücke")).setLore(lores).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new PlotBrowser(freeBuildPlayer).open(player);
            });
            lores.clear();

            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier greifst du").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>auf deine <gold>Storage <gray>zu.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Verfügbare Seiten <dark_gray>» <yellow>" + freeBuildPlayer.getMaxStorage()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(33, new ItemBuilder(Material.CHEST).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Storage")).setLore(lores).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new StorageInventory(freeBuildPlayer, 1).open(player);
            });
            lores.clear();

            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Teleportiere dich zu dem").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>letzten gespeichertem Ort.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Letzter Ort <dark_gray>» <yellow>" + freeBuildPlayer.getFastTravelLocation().getWorld().getName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(47, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Fast Travel")).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc5ZTU0Y2JlODc4NjdkMTRiMmZiZGYzZjE4NzA4OTQzNTIwNDhkZmVjZDk2Mjg0NmRlYTg5M2IyMTU0Yzg1In19fQ==").setLore(lores).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                player.closeInventory();
                player.teleport(freeBuildPlayer.getFastTravelLocation());
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            });
            lores.clear();

            this.setItem(51, new ItemBuilder(Material.COMPARATOR).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Einstellungen")).getItemStack());


            this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
        }
    }

    public static class ProgressionInventory extends InventoryBuilder {

        public ProgressionInventory(FreeBuildPlayer freeBuildPlayer, Integer page) {
            super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>Menü <dark_gray>● <gray>Level"));

            /* BORDER */
            this.setItems(0, 8, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());
            this.setItems(45, 53, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());


            /* Inventory */
            List<Component> lores = new ArrayList<>();
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>" + (freeBuildPlayer.getFreeBuildLevel() + 1)).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getCurrentXP(), freeBuildPlayer.getNeededFreeBuildXP(freeBuildPlayer.getFreeBuildLevel() + 1)) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getCurrentXP()) + "<dark_gray>/<gray>" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getNeededFreeBuildXP(freeBuildPlayer.getFreeBuildLevel() + 1)) + " XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(4, new ItemBuilder(Material.STONE_PICKAXE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Dein FastBuild Level")).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
            lores.clear();

            AtomicInteger itemSlot = new AtomicInteger(37);
            AtomicInteger levelPlaced = new AtomicInteger();

            if(page == 1) {
                for(int level = 1; level <= 36; level++) {
                    if(levelPlaced.get() < 18) {

                        int fortschritt = Math.round((float) freeBuildPlayer.getCurrentXP() / freeBuildPlayer.getNeededFreeBuildXP(level + 1) * 100);

                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Fortschritt <dark_gray>» <yellow>" + (Math.min(fortschritt, 100)) + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getCurrentXP(), freeBuildPlayer.getNeededFreeBuildXP(level)) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getCurrentXP()) + "<dark_gray>/<gray>" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getNeededFreeBuildXP(level)) + " XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Belohnung:").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <blue>FÜGE BELOHNUNG EIN").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                        this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>Level " + level + " <dark_gray>(<dark_red>✘<dark_gray>)")).setLore(lores).getItemStack());

                        if(levelPlaced.get() == 0) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>Level " + level + " <dark_gray>(<dark_red>✘<dark_gray>)")).setLore(lores).getItemStack());
                        }

                        if(freeBuildPlayer.getFreeBuildLevel() > level) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Level " + level + " <dark_gray>(<dark_green>✔<dark_gray>)")).setLore(lores).getItemStack());
                            if(levelPlaced.get() == 0) {
                                this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>Level " + level + " <dark_gray>(<dark_green>✔<dark_gray>)")).setLore(lores).getItemStack());

                            }
                        }

                        if(Objects.equals(freeBuildPlayer.getFreeBuildLevel(), level)) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhOTk2NGY1NzJmZDAzYzMyZGZhMjU4NjE1NWZhM2QxMGU2MjdkZjc3OWE0MWYyNjJmZGU4MmJmYjQxYmEwIn19fQ==").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>Level " + level + " <dark_gray>(<gold>/<dark_gray>)")).setLore(lores).getItemStack());
                        }


                        if((itemSlot.get() >= 10 && itemSlot.get() < 12) || (itemSlot.get() >= 39 && itemSlot.get() < 41) || (itemSlot.get() >= 14 && itemSlot.get() < 16)) itemSlot.getAndAdd(1);
                        else if(itemSlot.get() == 41) itemSlot.getAndAdd(-9);
                        else if(itemSlot.get() == 12 || itemSlot.get() == 21 || itemSlot.get() == 30 || itemSlot.get() == 16 || itemSlot.get() == 25) itemSlot.getAndAdd(9);
                        else itemSlot.getAndAdd(-9);

                        lores.clear();
                    }

                    levelPlaced.getAndIncrement();
                }

                this.setItem(43, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkxYWM0MzJhYTQwZDdlN2E2ODdhYTg1MDQxZGU2MzY3MTJkNGYwMjI2MzJkZDUzNTZjODgwNTIxYWYyNzIzYSJ9fX0=").setKey("level-nextpage").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_green>» <green>Weiter")).getItemStack());

            } else {
                AtomicInteger integer = new AtomicInteger();
                for(int level = 18; level <= 35; level++) {
                    if(integer.get() < 18) {

                        int fortschritt = Math.round((float) freeBuildPlayer.getCurrentXP() / freeBuildPlayer.getNeededFreeBuildXP(level + 1) * 100);

                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Fortschritt <dark_gray>» <yellow>" + (Math.min(fortschritt, 100)) + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getCurrentXP(), freeBuildPlayer.getNeededFreeBuildXP(level + 1)) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getCurrentXP()) + "<dark_gray>/<gray>" + freeBuildPlayer.getCompactXP(freeBuildPlayer.getNeededFreeBuildXP(level + 1)) + " XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Belohnung:").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <blue>FÜGE BELOHNUNG EIN").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                        this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>Level " + level + " <dark_gray>(<dark_red>✘<dark_gray>)")).setLore(lores).getItemStack());


                        if(freeBuildPlayer.getFreeBuildLevel() > level) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Level " + level + " <dark_gray>(<dark_green>✔<dark_gray>)")).getItemStack());
                        }

                        if(Objects.equals(freeBuildPlayer.getFreeBuildLevel(), level)) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhOTk2NGY1NzJmZDAzYzMyZGZhMjU4NjE1NWZhM2QxMGU2MjdkZjc3OWE0MWYyNjJmZGU4MmJmYjQxYmEwIn19fQ==").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>Level " + level + " <dark_gray>(<gold>/<dark_gray>)")).getItemStack());
                        }

                        if((itemSlot.get() >= 10 && itemSlot.get() < 12) || (itemSlot.get() >= 39 && itemSlot.get() < 41) || (itemSlot.get() >= 14 && itemSlot.get() < 16)) itemSlot.getAndAdd(1);
                        else if(itemSlot.get() == 41) itemSlot.getAndAdd(-9);
                        else if(itemSlot.get() == 12 || itemSlot.get() == 21 || itemSlot.get() == 30 || itemSlot.get() == 16 || itemSlot.get() == 25) itemSlot.getAndAdd(9);
                        else itemSlot.getAndAdd(-9);

                        lores.clear();
                    }

                }

                this.setItem(37, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EyYzEyY2IyMjkxODM4NGUwYTgxYzgyYTFlZDk5YWViZGNlOTRiMmVjMjc1NDgwMDk3MjMxOWI1NzkwMGFmYiJ9fX0=").setKey("level-prevpage").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>» <red>Zurück")).getItemStack());

            }

            this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setKey("level-back").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack());





            this.addClickHandler(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                inventoryClickEvent.setCancelled(true);

                if(inventoryClickEvent.getCurrentItem() == null) return;

                if(FreeBuild.getInstance().checkPDC("level-back", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>« <red>Zurück")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new DefaultInventory(freeBuildPlayer).open(player);
                    return;
                }

                if(FreeBuild.getInstance().checkPDC("level-nextpage", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_green>» <green>Weiter")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new ProgressionInventory(freeBuildPlayer, 2).open(player);
                    return;
                }

                if(FreeBuild.getInstance().checkPDC("level-prevpage", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>» <red>Zurück")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new ProgressionInventory(freeBuildPlayer, 1).open(player);
                    return;
                }
            });


        }


    }

    public static class StorageInventory extends InventoryBuilder {

        private final String ARROW_UP = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjYmY5ODgzZGQzNTlmZGYyMzg1YzkwYTQ1OWQ3Mzc3NjUzODJlYzQxMTdiMDQ4OTVhYzRkYzRiNjBmYyJ9fX0=";
        private final String ARROW_DOWN = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzI0MzE5MTFmNDE3OGI0ZDJiNDEzYWE3ZjVjNzhhZTQ0NDdmZTkyNDY5NDNjMzFkZjMxMTYzYzBlMDQzZTBkNiJ9fX0=";
        private final String PLUS = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19";

        private final String ARROW_NEXT_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==";
        private final String ARROW_NO_NEXT_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjMyY2E2NjA1NmI3Mjg2M2U5OGY3ZjMyYmQ3ZDk0YzdhMGQ3OTZhZjY5MWM5YWMzYTkxMzYzMzEzNTIyODhmOSJ9fX0=";

        private final String ARROW_PREV_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODU1MGI3Zjc0ZTllZDc2MzNhYTI3NGVhMzBjYzNkMmU4N2FiYjM2ZDRkMWY0Y2E2MDhjZDQ0NTkwY2NlMGIifX19";
        private final String ARROW_NO_PREV_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5NzFkZDg4MWRiYWY0ZmQ2YmNhYTkzNjE0NDkzYzYxMmY4Njk2NDFlZDU5ZDFjOTM2M2EzNjY2YTVmYTYifX19";

        private final String ARROW_FIRST_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGE4MDhhNmM4ZDk3ZGRhMmJkNWQ4NDY0MmUxNzE0Yzc4MTI5NGU5MTcyYjdjMzc1Y2Q5Y2ZjZjNjNTRmMmJhIn19fQ==";
        private final String ARROW_NO_FIRST_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjRiZmVmMTRlODQyMGEyNTZlNDU3YTRhN2M4ODExMmUxNzk0ODVlNTIzNDU3ZTQzODUxNzdiYWQifX19";

        private final String ARROW_LAST_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzdiNzMzZTBiYTk2NGU4MTU3NDc2ZjMzNTM0MjZhODdjZWFiM2RiYzNmYjRiZGRhYTJkNTc4ODZkZjM3YmE2In19fQ==";
        private final String ARROW_NO_LAST_PAGE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGQ5OTNiOGMxMzU4ODkxOWI5ZjhiNDJkYjA2NWQ1YWRmZTc4YWYxODI4MTViNGU2ZjBmOTFiYTY4M2RlYWM5In19fQ==";

        public StorageInventory(FreeBuildPlayer freeBuildPlayer, Integer page) {
            super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#A66107:#D5EB13>Storage</gradient> <dark_gray>● <gray>Seite " + page));

            /* BORDER */
            this.setItems(0, 8, new ItemBuilder(Material.BROWN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gold>")).addItemFlags(ItemFlag.values()).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
            this.setItems(45, 53, new ItemBuilder(Material.BROWN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gold>")).addItemFlags(ItemFlag.values()).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));


            /* Inventory */
            List<Component> lores = new ArrayList<>();
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier hast du Zugriff").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>auf deine Storage.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(4, new ItemBuilder(Material.CHEST).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#A66107:#D5EB13>Storage</gradient>")).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
            lores.clear();


            this.setItem(1, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture((page > 1 ? this.ARROW_FIRST_PAGE : this.ARROW_NO_FIRST_PAGE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize(page > 1 ? "<dark_green>« <green>Erste Seite" : "<dark_red>« <red>Erste Seite")).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(page > 1) {
                    player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                    FreeBuild.getInstance().getStorageManager().saveStorage(freeBuildPlayer, page, inventoryClickEvent.getInventory());
                    new StorageInventory(freeBuildPlayer, 1).open(player);
                }
            });
            this.setItem(2, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture((page > 1 ? this.ARROW_PREV_PAGE : this.ARROW_NO_PREV_PAGE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize(page > 1 ? "<dark_green>« <green>Vorherige Seite" : "<dark_red>« <red>Vorherige Seite")).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(page > 1) {
                    player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                    FreeBuild.getInstance().getStorageManager().saveStorage(freeBuildPlayer, page, inventoryClickEvent.getInventory());
                    new StorageInventory(freeBuildPlayer, page - 1).open(player);
                }
            });
            this.setItem(6, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture((page < freeBuildPlayer.getMaxStorage() ? this.ARROW_NEXT_PAGE : this.ARROW_NO_NEXT_PAGE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize(page < freeBuildPlayer.getMaxStorage() ? "<dark_green>» <green>Nächste Seite" : "<dark_red>» <red>Nächste Seite")).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(page < freeBuildPlayer.getMaxStorage()) {
                    player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                    FreeBuild.getInstance().getStorageManager().saveStorage(freeBuildPlayer, page, inventoryClickEvent.getInventory());
                    new StorageInventory(freeBuildPlayer, page + 1).open(player);
                }
            });
            this.setItem(7, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture((page < freeBuildPlayer.getMaxStorage() ? this.ARROW_LAST_PAGE : this.ARROW_NO_LAST_PAGE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize(page < freeBuildPlayer.getMaxStorage() ? "<dark_green>» <green>Letzte Seite" : "<dark_red>» <red>Letzte Seite")).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(page < freeBuildPlayer.getMaxStorage()) {
                    player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
                    FreeBuild.getInstance().getStorageManager().saveStorage(freeBuildPlayer, page, inventoryClickEvent.getInventory());
                    new StorageInventory(freeBuildPlayer, freeBuildPlayer.getMaxStorage()).open(player);
                }
            });


            for(Integer contents : IntStream.rangeClosed(9, 44).toArray()) {
                if(FreeBuild.getInstance().getStorageManager().hasItems(freeBuildPlayer, page, contents)) this.setItem(contents, FreeBuild.getInstance().getStorageManager().displayItems(freeBuildPlayer, page, contents));
            }


            this.setItem(46, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(this.ARROW_UP).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>Alle Items reinwerfen")).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                ItemStack[] playerItems = IntStream.rangeClosed(9, 35).boxed().map(player.getInventory()::getItem).filter(Objects::nonNull).toArray(ItemStack[]::new);

                if(playerItems.length == 0) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Keine Items zum Verschieben."));
                    return;
                }
                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                inventoryClickEvent.getInventory().addItem(playerItems);
                player.getInventory().removeItem(playerItems);
            });
            this.setItem(47, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(this.ARROW_DOWN).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>Alle Items rausholen")).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                ItemStack[] storageItems = IntStream.rangeClosed(9, 44).boxed().map(inventoryClickEvent.getInventory()::getItem).filter(Objects::nonNull).toArray(ItemStack[]::new);

                if(storageItems.length == 0) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Keine Items zum Verschieben."));
                    return;
                }

                int slot = 9;
                for (ItemStack storageItem : storageItems) {
                    if(slot == 35) {
                        player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Dein Inventar ist voll."));
                        return;
                    }
                    player.getOpenInventory().getBottomInventory().setItem(slot, storageItem);
                    slot++;
                }
                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                inventoryClickEvent.getInventory().removeItem(storageItems);
            });

            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Erweitere dein Storage").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>um <yellow>eine <gray>weitere Seite.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Kosten <dark_gray>» <yellow>5 Döner").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(50, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(this.PLUS).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Storage erweitern")).setLore(lores).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
            });
            lores.clear();


            this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
                inventoryClickEvent.setCancelled(true);
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                FreeBuild.getInstance().getStorageManager().saveStorage(freeBuildPlayer, page, inventoryClickEvent.getInventory());
                new DefaultInventory(freeBuildPlayer).open(player);
            });


            this.addClickHandler(inventoryClickEvent -> {

            });

            this.addCloseHandler(inventoryCloseEvent -> {
                Player player = (Player) inventoryCloseEvent.getPlayer();

                FreeBuild.getInstance().getStorageManager().saveStorage(freeBuildPlayer, page, inventoryCloseEvent.getInventory());

            });
        }
    }

}
