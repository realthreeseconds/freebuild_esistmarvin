package de.threeseconds.job;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class JobManager {

    private HashMap<UUID, HashMap<Integer, Jobs>> cachedJob;
    private HashMap<UUID, Integer> currentJobLevelPage;

    public JobManager() {
        this.cachedJob = new HashMap<>();
        this.currentJobLevelPage = new HashMap<>();
    }

    public Inventory openJobsInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9*5, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#3434FA:#F6A3FF>Jobs</gradient> <dark_gray>● <gray>Menü"));

        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        this.animateFrameInventory(inventory, false);

        int i = 19;
        HashMap<Integer, Jobs> jobOnSlot = new HashMap<>();

        for(Jobs jobs : Jobs.values()) {

            List<Component> jobLore = jobs.getLore();

            if(freeBuildPlayer.getJob() == jobs) {
                jobLore.set(4, jobLore.get(4).replaceText(TextReplacementConfig.builder().match("%LEVEL%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[0])).build()));
                jobLore.set(5, jobLore.get(5)
                        .replaceText(TextReplacementConfig.builder()
                                .match("%PROGRESS%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.calculateProgress(Integer.valueOf(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[1]), freeBuildPlayer.getJob().getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[0]) - 1).getXpNeeded())))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%XP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[1]))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%MAXXP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(String.valueOf(freeBuildPlayer.getJob().getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[0]) - 1).getXpNeeded())))
                                .build()));
                jobLore.set(7, FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <green>Ausgewählt ✔").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

            }
            else {
                jobLore.set(4, jobLore.get(4).replaceText(TextReplacementConfig.builder().match("%LEVEL%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, jobs)[0])).build()));
                jobLore.set(5, jobLore.get(5)
                        .replaceText(TextReplacementConfig.builder()
                                .match("%PROGRESS%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.calculateProgress(Integer.valueOf(this.getLevelByJob(freeBuildPlayer, jobs)[1]), jobs.getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, jobs)[0]) - 1).getXpNeeded())))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%XP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, jobs)[1]))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%MAXXP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(String.valueOf(jobs.getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, jobs)[0]) - 1).getXpNeeded())))
                                .build()));
                jobLore.set(7, FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <blue>Auswählen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            }
            inventory.setItem(i, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + jobs.getJobName())).setSkullTexture(jobs.getHeadTexture()).setLore(jobLore).getItemStack());
            jobOnSlot.put(i, jobs);
            i += 2;
        }


        List<Component> lores = new ArrayList<>();
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier findest du eine Aufzählung").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aller momentan verfügbaren <gold>Jobs<gray>.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <red>(!) <gray>Du kannst deinen Job nur alle <green>2 Tage <gray>wechseln.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        inventory.setItem(4, new ItemBuilder(Material.OAK_HANGING_SIGN).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#1BFAF3:#283E99>Informationen")).setLore(lores).getItemStack());
        lores.clear();

        this.cachedJob.put(player.getUniqueId(), jobOnSlot);

        return inventory;
    }

    public void updateJobsInventory(Inventory inventory, Player player) {
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        int slot;
        for (slot = 0; slot < 9;) {
            inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            slot++;
        }
        for (slot = 36; slot < 45;) {
            inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            slot++;
        }

        int i = 19;
        HashMap<Integer, Jobs> jobOnSlot = new HashMap<>();

        for(Jobs jobs : Jobs.values()) {
            List<Component> jobLore = jobs.getLore();

            if(freeBuildPlayer.getJob() == jobs) {
                jobLore.set(4, jobLore.get(4).replaceText(TextReplacementConfig.builder().match("%LEVEL%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[0])).build()));
                jobLore.set(5, jobLore.get(5)
                        .replaceText(TextReplacementConfig.builder()
                                .match("%PROGRESS%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.calculateProgress(Integer.valueOf(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[1]), freeBuildPlayer.getJob().getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[0]) - 1).getXpNeeded())))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%XP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[1]))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%MAXXP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(String.valueOf(freeBuildPlayer.getJob().getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, freeBuildPlayer.getJob())[0]) - 1).getXpNeeded())))
                                .build()));
                jobLore.set(7, FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <green>Ausgewählt ✔").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

            }
            else {
                jobLore.set(4, jobLore.get(4).replaceText(TextReplacementConfig.builder().match("%LEVEL%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, jobs)[0])).build()));
                jobLore.set(5, jobLore.get(5)
                        .replaceText(TextReplacementConfig.builder()
                                .match("%PROGRESS%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.calculateProgress(Integer.valueOf(this.getLevelByJob(freeBuildPlayer, jobs)[1]), jobs.getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, jobs)[0]) - 1).getXpNeeded())))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%XP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(this.getLevelByJob(freeBuildPlayer, jobs)[1]))
                                .build())
                        .replaceText(TextReplacementConfig.builder()
                                .match("%MAXXP%").replacement(FreeBuild.getInstance().getMiniMessage().deserialize(String.valueOf(jobs.getJobsProgress().getJobLevel().get(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, jobs)[0]) - 1).getXpNeeded())))
                                .build()));
                jobLore.set(7, FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <blue>Auswählen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            }
            inventory.setItem(i, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + jobs.getJobName())).setSkullTexture(jobs.getHeadTexture()).setLore(jobLore).getItemStack());
            jobOnSlot.put(i, jobs);
            i += 2;
        }

        List<Component> lores = new ArrayList<>();
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier findest du eine Aufzählung").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aller momentan verfügbaren <gold>Jobs<gray>.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <red>(!) <gray>Du kannst deinen Job nur alle <green>2 Tage <gray>wechseln.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        inventory.setItem(4, new ItemBuilder(Material.OAK_HANGING_SIGN).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#1BFAF3:#283E99>Informationen")).setLore(lores).getItemStack());
        lores.clear();

        this.cachedJob.put(player.getUniqueId(), jobOnSlot);
    }

    public void openJob(Inventory inventory, InventoryView inventoryView, Jobs jobs) {
        inventoryView.title().replaceText(TextReplacementConfig.builder().match(inventoryView.getTitle()).replacement(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#3434FA:#F6A3FF>Jobs</gradient> <dark_gray>● <gray>" + jobs.getJobName())).build());
        inventory.clear();

        int slot;
        for (slot = 0; slot < 9;) {
            inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            slot++;
        }
        for (slot = 36; slot < 45;) {
            inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            slot++;
        }

        inventory.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(jobs.getHeadTexture()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#1BFAF3:#283E99>" + jobs.getJobName())).getItemStack());

        inventory.setItem(20, new ItemBuilder(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Level Fortschritt")).addItemFlag(ItemFlag.HIDE_ARMOR_TRIM).addItemFlag(ItemFlag.HIDE_ITEM_SPECIFICS).getItemStack());
        inventory.setItem(24, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUwZjY4NWQ3ZjBhNTY3YTZhZDNkNzI0ZDk2YjEyMDNmM2E5MzVlNzYzZDZmNTgwMzQ0OTdkZDJhMjk5NGZiNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <light_purple>Lohn auszahlen")).getItemStack());

        inventory.setItem(44, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<red>Zurück")).getItemStack());
    }

    public String calculateProgress(Integer currentXP, Integer maxXP) {
        String emptyString = "                         ";
        double calculation = (double)currentXP / maxXP * (emptyString.length());
        String greenString = emptyString.substring(0, (int)calculation);
        return greenString + "<gray>" + StringUtils.repeat(" ", emptyString.length()-(int)calculation);
    }

    public Inventory openJobProgression(Player player, Jobs job, Integer page, Boolean firstOpen) {
        Inventory inventory = Bukkit.createInventory(player, 9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#3434FA:#F6A3FF>Job</gradient> <dark_gray>● <gray>Level"));

        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        int slot;
        for (slot = 0; slot < 9;) {
            inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            slot++;
        }
        for (slot = 45; slot < 54;) {
            inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            slot++;
        }


        inventory.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#1BFAF3:#283E99>" + job.getJobName())).setSkullTexture(job.getHeadTexture()).getItemStack());

        final int[] i = {37};
        AtomicInteger levelPlaced = new AtomicInteger();

        List<JobLevel> jobsArrayList = job.getJobsProgress().getJobLevel();

        if(page == 1) {
            jobsArrayList.forEach(jobLevel -> {
                if(levelPlaced.get() < 18) {
                    inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>" + jobLevel.getLevel() + ". Level <dark_gray>(<dark_red>✘<dark_gray>)")).getItemStack());


                    if(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, job)[0]) > jobLevel.getLevel()) {
                        inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + jobLevel.getLevel() + ". Level <dark_gray>(<dark_green>✔<dark_gray>)")).getItemStack());
                        if(levelPlaced.get() == 0) {
                            inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>" + jobLevel.getLevel() + ". Level <dark_gray>(<dark_green>✔<dark_gray>)")).getItemStack());

                        }
                    }

                    if(this.getLevelByJob(freeBuildPlayer, job)[0].equals(String.valueOf(jobLevel.getLevel()))) {
                        inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhOTk2NGY1NzJmZDAzYzMyZGZhMjU4NjE1NWZhM2QxMGU2MjdkZjc3OWE0MWYyNjJmZGU4MmJmYjQxYmEwIn19fQ==").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>" + jobLevel.getLevel() + ". Level <dark_gray>(<gold>/<dark_gray>)")).getItemStack());
                    }



                    if((i[0] >= 10 && i[0] < 12) || (i[0] >= 39 && i[0] < 41) || (i[0] >= 14 && i[0] < 16)) i[0] += 1;
                    else if(i[0] == 41) i[0] -= 9;
                    else if(i[0] == 12 || i[0] == 21 || i[0] == 30 || i[0] == 16 || i[0] == 25) i[0] += 9;
                    else i[0] -= 9;
                }

                levelPlaced.getAndIncrement();

            });

            inventory.setItem(43, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjkxYWM0MzJhYTQwZDdlN2E2ODdhYTg1MDQxZGU2MzY3MTJkNGYwMjI2MzJkZDUzNTZjODgwNTIxYWYyNzIzYSJ9fX0=").setKey("jobs_nextpage").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_green>» <green>Weiter")).getItemStack());

        } else {
            AtomicInteger integer = new AtomicInteger();
            jobsArrayList.forEach(jobLevel -> {
                integer.getAndIncrement();
                if(integer.get() > 17) {
                    inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjlhNTFmMjdkMmQ5Mzg4OTdiYzQyYTNmZTJjMzEzNWRhMjY3MTY4NmY1NzgyNDExNWY4ZjhkYTc4YSJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>" + jobLevel.getLevel() + ". Level <dark_gray>(<dark_red>✘<dark_gray>)")).getItemStack());


                    if(Integer.parseInt(this.getLevelByJob(freeBuildPlayer, job)[0]) > jobLevel.getLevel()) {
                        inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWNiODFhMzVkMmI0OGQ1ZmQ4MTI0OTM2OTQzM2MwNzhiN2M4YmY0MmRmNWFhOWMzNzVjMWFjODVmNDUxNCJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + jobLevel.getLevel() + ". Level <dark_gray>(<dark_green>✔<dark_gray>)")).getItemStack());
                        if(levelPlaced.get() == 0) {
                            inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWYzZWFmNGMxNWFkNjdjNTFkZmY5MDk3YmQ3YWJkNGE4MmJhYjdiZWQ4M2FiNzdhNjE3N2YyZTU3YiJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <white>" + jobLevel.getLevel() + ". Level <dark_gray>(<dark_green>✔<dark_gray>)")).getItemStack());

                        }
                    }

                    if(this.getLevelByJob(freeBuildPlayer, job)[0].equals(String.valueOf(jobLevel.getLevel()))) {
                        inventory.setItem(i[0], new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdhOTk2NGY1NzJmZDAzYzMyZGZhMjU4NjE1NWZhM2QxMGU2MjdkZjc3OWE0MWYyNjJmZGU4MmJmYjQxYmEwIn19fQ==").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>" + jobLevel.getLevel() + ". Level <dark_gray>(<gold>/<dark_gray>)")).getItemStack());
                    }

                    if((i[0] >= 10 && i[0] < 12) || (i[0] >= 39 && i[0] < 41) || (i[0] >= 14 && i[0] < 16)) i[0] += 1;
                    else if(i[0] == 41) i[0] -= 9;
                    else if(i[0] == 12 || i[0] == 21 || i[0] == 30 || i[0] == 16 || i[0] == 25) i[0] += 9;
                    else i[0] -= 9;

                }



            });

            inventory.setItem(37, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2EyYzEyY2IyMjkxODM4NGUwYTgxYzgyYTFlZDk5YWViZGNlOTRiMmVjMjc1NDgwMDk3MjMxOWI1NzkwMGFmYiJ9fX0=").setKey("jobs_prevpage").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>» <red>Zurück")).getItemStack());

        }

        inventory.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>» <red>Zurück")).getItemStack());

        currentJobLevelPage.put(player.getUniqueId(), page);

        return inventory;
    }

    private void animateFrameInventory(Inventory inventory, boolean bigInv) {
        int slot;
        for (slot = 0; slot < 8;) {
            inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            slot++;
        }
        if(bigInv) {
            for (slot = 48; slot < 54;) {
                inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
                slot++;
            }
        } else {
            for (slot = 39; slot < 45;) {
                inventory.setItem(slot, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
                slot++;
            }
        }


        (new BukkitRunnable() {
            public void run() {
                inventory.setItem(6, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
                inventory.setItem((bigInv ? 47 : 38), (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            }
        }).runTaskLaterAsynchronously(FreeBuild.getInstance().getPaperCore(), 3L);
        (new BukkitRunnable() {
            public void run() {
                inventory.setItem(7, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
                inventory.setItem((bigInv ? 46 : 37), (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            }
        }).runTaskLaterAsynchronously(FreeBuild.getInstance().getPaperCore(), 6L);
        (new BukkitRunnable() {
            public void run() {
                inventory.setItem(8, (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
                inventory.setItem((bigInv ? 45 : 36), (new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>")).getItemStack());
            }
        }).runTaskLaterAsynchronously(FreeBuild.getInstance().getPaperCore(), 9L);

    }

    public Jobs getJobBySlot(Player player, int slot) {
        return this.cachedJob.get(player.getUniqueId()).get(slot);
    }

    public Integer getLevelPage(Player player) {
        return this.currentJobLevelPage.get(player.getUniqueId());
    }

    public String[] getLevelByJob(FreeBuildPlayer freeBuildPlayer, Jobs job) {
        String s = "";
        for(Map.Entry<Integer, Integer> level : freeBuildPlayer.getJobLevel().get(job).entrySet()) {
            s = level.getKey() + ":" + level.getValue();
        }
        return s.split(":");
    }
}
