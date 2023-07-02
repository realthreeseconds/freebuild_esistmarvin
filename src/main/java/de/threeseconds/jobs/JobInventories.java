package de.threeseconds.jobs;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JobInventories {

    public static class DefaultInventory extends InventoryBuilder {

        public DefaultInventory(FreeBuildPlayer freeBuildPlayer) {
            super(9*5, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#3434FA:#F6A3FF>Jobs</gradient> <dark_gray>● <gray>Menü"));

            /* BORDER */
            this.setItems(0, 8, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());
            this.setItems(36, 44, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());


            /* Inventory */
            List<Component> lores = new ArrayList<>();
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier findest du eine Aufzählung").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aller momentan verfügbaren <gold>Jobs<gray>.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <red>(!) <gray>Du kannst deinen Job nur alle <green>2 Tage <gray>wechseln.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(4, new ItemBuilder(Material.OAK_HANGING_SIGN).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#1BFAF3:#283E99>Informationen")).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
            lores.clear();

            HashMap<Integer, Job> jobData = new HashMap<>();

            int slot = 19;
            for(Job jobs : Job.values()) {
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                if(jobs == Job.MINER) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Minen-Arbeiter werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>und alle Höhlen erforschen?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

                } else if(jobs == Job.HOLZFÄLLER) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Holzfäller werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>und den Wald roden?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

                } else if(jobs == Job.FISCHER) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Fischer werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>und in die Ichthyologie einsteigen?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

                } else if(jobs == Job.JÄGER) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Jäger werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>bis das Blut tropft?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                }

                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>" + freeBuildPlayer.getJobPlayer().getLevelByJob(jobs) + " <gray>von <yellow>" + jobs.getJobLevels().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getJobPlayer().getXPByJob(jobs), jobs.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(jobs)).getMaxXP()) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getJobPlayer().getXPByJob(jobs) + "/" + jobs.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(jobs)).getMaxXP() + "XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» " + (freeBuildPlayer.getJobPlayer().getActiveJob() == jobs ? "<green>Ausgewählt ✔" : "<blue>Auswählen")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Rechtsklick <dark_gray>» <blue>Informationen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                jobData.put(slot, jobs);

                this.setItem(slot, new ItemBuilder(Material.PLAYER_HEAD).setJobData(jobs).setSkullTexture(jobs.getHeadTexture()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>" + jobs.getJobName())).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
                lores.clear();

                slot += 2;
            }

            this.addClickHandler(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                inventoryClickEvent.setCancelled(true);

                if(inventoryClickEvent.getCurrentItem() == null) return;

                Job clickedJob = jobData.get(inventoryClickEvent.getSlot());

                if(inventoryClickEvent.isLeftClick()) {
                    if(inventoryClickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                        if(freeBuildPlayer.getJobPlayer().getActiveJob() == clickedJob) {
                            player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                            player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast diesen Beruf bereits ausgewählt."));

                            return;
                        }
                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast <green>" + clickedJob.getJobName() + " <gray>als <gold>Beruf <gray>ausgewählt."));


                        freeBuildPlayer.getJobPlayer().setActiveJob(clickedJob);
                        freeBuildPlayer.getGameScoreboard().setScore(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <green>" + clickedJob.getJobName()), 1);
                        new DefaultInventory(freeBuildPlayer).open(player);
                    }
                    return;
                }

                if(inventoryClickEvent.isRightClick()) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new ProgressionInventory(freeBuildPlayer, clickedJob, 1).open(player);
                }
            });
        }
    }

    public static class ProgressionInventory extends InventoryBuilder {

        public ProgressionInventory(FreeBuildPlayer freeBuildPlayer, Job job, Integer page) {
            super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + job.getJobName() + " <dark_gray>● <gray>Level"));

            /* BORDER */
            this.setItems(0, 8, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());
            this.setItems(45, 53, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).addItemFlags(ItemFlag.values()).getItemStack());


            /* Inventory */
            List<Component> lores = new ArrayList<>();
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>" + freeBuildPlayer.getJobPlayer().getLevelByJob(job) + " <gray>von <yellow>" + job.getJobLevels().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getJobPlayer().getXPByJob(job), job.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(job)).getMaxXP()) + "</st><reset> <dark_gray>» <gray>" + freeBuildPlayer.getJobPlayer().getXPByJob(job) + "/" + job.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(job)).getMaxXP() + "XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(job.getHeadTexture()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + job.getJobName())).setLore(lores).addItemFlags(ItemFlag.values()).getItemStack());
            lores.clear();

            AtomicInteger itemSlot = new AtomicInteger(37);
            AtomicInteger levelPlaced = new AtomicInteger();

            if(page == 1) {
                job.getJobLevels().forEach(jobLevel -> {
                    if(levelPlaced.get() < 18) {

                        int fortschritt = Math.round((float) freeBuildPlayer.getJobPlayer().getXPByJob(job) / jobLevel.getMaxXP() * 100);

                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Fortschritt <dark_gray>» <yellow>" + (Math.min(fortschritt, 100)) + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getJobPlayer().getXPByJob(job), jobLevel.getMaxXP()) + "</st><reset> <dark_gray>» <gray>" + Math.min(freeBuildPlayer.getJobPlayer().getXPByJob(job), jobLevel.getMaxXP()) + "<dark_gray>/<gray>" + jobLevel.getMaxXP()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Belohnung:").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <blue>FÜGE BELOHNUNG EIN").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                        this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>Level " + levelPlaced.get() + " <dark_gray>(<dark_red>✘<dark_gray>)")).setLore(lores).getItemStack());

                        if(levelPlaced.get() == 0) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>Level " + levelPlaced.get() + " <dark_gray>(<dark_red>✘<dark_gray>)")).setLore(lores).getItemStack());
                        }

                        if(freeBuildPlayer.getJobPlayer().getLevelByJob(job) > job.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(job)).getLevel()) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Level " + levelPlaced.get() + " <dark_gray>(<dark_green>✔<dark_gray>)")).setLore(lores).getItemStack());
                            if(levelPlaced.get() == 0) {
                                this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>Level " + levelPlaced.get() + " <dark_gray>(<dark_green>✔<dark_gray>)")).setLore(lores).getItemStack());

                            }
                        }

                        if(Objects.equals(freeBuildPlayer.getJobPlayer().getLevelByJob(job), job.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(job)).getLevel())) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhOTk2NGY1NzJmZDAzYzMyZGZhMjU4NjE1NWZhM2QxMGU2MjdkZjc3OWE0MWYyNjJmZGU4MmJmYjQxYmEwIn19fQ==").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>Level" + levelPlaced.get() + " <dark_gray>(<gold>/<dark_gray>)")).setLore(lores).getItemStack());
                        }


                        if((itemSlot.get() >= 10 && itemSlot.get() < 12) || (itemSlot.get() >= 39 && itemSlot.get() < 41) || (itemSlot.get() >= 14 && itemSlot.get() < 16)) itemSlot.getAndAdd(1);
                        else if(itemSlot.get() == 41) itemSlot.getAndAdd(-9);
                        else if(itemSlot.get() == 12 || itemSlot.get() == 21 || itemSlot.get() == 30 || itemSlot.get() == 16 || itemSlot.get() == 25) itemSlot.getAndAdd(9);
                        else itemSlot.getAndAdd(-9);

                        lores.clear();
                    }

                    levelPlaced.getAndIncrement();
                });

                this.setItem(43, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkxYWM0MzJhYTQwZDdlN2E2ODdhYTg1MDQxZGU2MzY3MTJkNGYwMjI2MzJkZDUzNTZjODgwNTIxYWYyNzIzYSJ9fX0=").setKey("jobs-nextpage").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_green>» <green>Weiter")).getItemStack());

            } else {
                AtomicInteger integer = new AtomicInteger();
                job.getJobLevels().forEach(jobLevel -> {
                    integer.getAndIncrement();
                    if(integer.get() > 17) {
                        this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>" + jobLevel.getLevel() + ". Level <dark_gray>(<dark_red>✘<dark_gray>)")).getItemStack());


                        if(freeBuildPlayer.getJobPlayer().getLevelByJob(job) > job.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(job)).getLevel()) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Level" + levelPlaced.get() + " <dark_gray>(<dark_green>✔<dark_gray>)")).getItemStack());
                        }

                        if(Objects.equals(freeBuildPlayer.getJobPlayer().getLevelByJob(job), job.getJobLevels().get(freeBuildPlayer.getJobPlayer().getLevelByJob(job)).getLevel())) {
                            this.setItem(itemSlot.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhOTk2NGY1NzJmZDAzYzMyZGZhMjU4NjE1NWZhM2QxMGU2MjdkZjc3OWE0MWYyNjJmZGU4MmJmYjQxYmEwIn19fQ==").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>Level" + levelPlaced.get() + " <dark_gray>(<gold>/<dark_gray>)")).getItemStack());
                        }

                        if((itemSlot.get() >= 10 && itemSlot.get() < 12) || (itemSlot.get() >= 39 && itemSlot.get() < 41) || (itemSlot.get() >= 14 && itemSlot.get() < 16)) itemSlot.getAndAdd(1);
                        else if(itemSlot.get() == 41) itemSlot.getAndAdd(-9);
                        else if(itemSlot.get() == 12 || itemSlot.get() == 21 || itemSlot.get() == 30 || itemSlot.get() == 16 || itemSlot.get() == 25) itemSlot.getAndAdd(9);
                        else itemSlot.getAndAdd(-9);

                    }



                });

                this.setItem(37, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EyYzEyY2IyMjkxODM4NGUwYTgxYzgyYTFlZDk5YWViZGNlOTRiMmVjMjc1NDgwMDk3MjMxOWI1NzkwMGFmYiJ9fX0=").setKey("jobs-prevpage").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>» <red>Zurück")).getItemStack());

            }

            this.setItem(49, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUwZjY4NWQ3ZjBhNTY3YTZhZDNkNzI0ZDk2YjEyMDNmM2E5MzVlNzYzZDZmNTgwMzQ0OTdkZDJhMjk5NGZiNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <light_purple>Lohn auszahlen")).getItemStack());

            this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setKey("jobs-back").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack());



            this.addClickHandler(inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                inventoryClickEvent.setCancelled(true);

                if(FreeBuild.getInstance().checkPDC("jobs-back", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>« <red>Zurück")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new DefaultInventory(freeBuildPlayer).open(player);
                    return;
                }

                if(FreeBuild.getInstance().checkPDC("jobs-nextpage", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_green>» <green>Weiter")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new ProgressionInventory(freeBuildPlayer, job, 2).open(player);
                    return;
                }

                if(FreeBuild.getInstance().checkPDC("jobs-prevpage", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>» <red>Zurück")) {
                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    new ProgressionInventory(freeBuildPlayer, job, 1).open(player);
                    return;
                }
            });
        }
    }

}
