package de.threeseconds.util;

import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MenuManager {

    private HashMap<UUID, Boolean> hasInventoryOpen;

    public MenuManager() {
        this.hasInventoryOpen = new HashMap<>();
    }

    public Inventory openInventory(Player player, Boolean firstTime) {
        Inventory inventory = Bukkit.createInventory(player, 9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#E0167B:#D9D938>Menü</gradient>"));
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(firstTime)  {
            this.animateFrameInventory(inventory);
        } else {
            int slot;
            for (slot = 0; slot < 9;) {
                inventory.setItem(slot, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
                slot++;
            }
            for (slot = 45; slot < 54;) {
                inventory.setItem(slot, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
                slot++;
            }
        }

        List<Component> lores = new ArrayList<>();
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier findest du Statistiken").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>bezüglich deines Charakters.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gradient:#AB2F41:#FF2B24>❤ Leben</gradient> <dark_gray>» <white>" + (int) Math.round(freeBuildPlayer.getHealth())).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gradient:#51E611:#48944D>\uD83D\uDEE1 Verteidigung</gradient> <dark_gray>» <white>" + freeBuildPlayer.getDefense()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gradient:#30CFD0:#926DD1>\uD83D\uDD25 Seelen</gradient> <dark_gray>» <white>" + freeBuildPlayer.getSouls()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <white>\uD83E\uDD7E Speed <dark_gray>» <white>" + freeBuildPlayer.getSpeed()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <red>⚔ Stärke <dark_gray>» <white>" + freeBuildPlayer.getStrength()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_red>\uD83D\uDDE1 Krit-Chance <dark_gray>» <white>" + freeBuildPlayer.getCritChance() * 100 + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_red>\uD83D\uDC80 Krit-Schaden <dark_gray>» <white>" + freeBuildPlayer.getCritDamage() * 100 + "%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        inventory.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Dein Profil")).setSkullTexture(player.getPlayerProfile().getTextures()).setLore(lores).getItemStack());
        lores.clear();


        if(freeBuildPlayer.getJobPlayer().getActiveJob() != null) {
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier siehst du deinen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>aktuellen Fortschritt im Beruf.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>" + freeBuildPlayer.getJobPlayer().getLevelByJob(freeBuildPlayer.getJobPlayer().getActiveJob())).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>" + FreeBuild.getInstance().getJobManager().calculateProgress(freeBuildPlayer.getJobPlayer().getXPByJob(freeBuildPlayer.getJobPlayer().getActiveJob()), freeBuildPlayer.getJobPlayer().getMaxXPByLevel(freeBuildPlayer.getJobPlayer().getLevelByJob(freeBuildPlayer.getJobPlayer().getActiveJob()))) + "</st><reset> <dark_gray>» <gray>" + Math.min(freeBuildPlayer.getJobPlayer().getXPByJob(freeBuildPlayer.getJobPlayer().getActiveJob()), freeBuildPlayer.getJobPlayer().getMaxXPByLevel(freeBuildPlayer.getJobPlayer().getLevelByJob(freeBuildPlayer.getJobPlayer().getActiveJob()))) + "<dark_gray>/<gray>" + freeBuildPlayer.getJobPlayer().getMaxXPByLevel(freeBuildPlayer.getJobPlayer().getLevelByJob(freeBuildPlayer.getJobPlayer().getActiveJob()))).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            inventory.setItem(20, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(freeBuildPlayer.getJobPlayer().getActiveJob().getHeadTexture()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>" + freeBuildPlayer.getJobPlayer().getActiveJob().getJobName())).setLore(lores).getItemStack());
            lores.clear();
        }
        else inventory.setItem(20, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>Ohne Beruf")).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA2ZDdiZWZjODJmMjAxZjgzZTE5Mzc2N2U2M2Y4MTAzNzIxNWFmZDQ4M2EzOGQzNjk2NTk4MmNhNmQwIn19fQ==").getItemStack());


        inventory.setItem(22, new ItemBuilder(Material.STONE_PICKAXE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Level Fortschritt")).getItemStack());

        inventory.setItem(24, new ItemBuilder(Material.PAPER).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Collections")).getItemStack());

        inventory.setItem(30, new ItemBuilder(Material.WRITABLE_BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Quests")).getItemStack());

        inventory.setItem(32, new ItemBuilder(Material.CHEST).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Storage")).getItemStack());

        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Teleportiere dich zu dem").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>letzten gespeichertem Ort.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Letzter Ort <dark_gray>» <yellow>" + freeBuildPlayer.getFastTravelLocation().getWorld().getName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        inventory.setItem(47, new ItemBuilder(Material.PLAYER_HEAD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Fast Travel")).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODc5ZTU0Y2JlODc4NjdkMTRiMmZiZGYzZjE4NzA4OTQzNTIwNDhkZmVjZDk2Mjg0NmRlYTg5M2IyMTU0Yzg1In19fQ==").setLore(lores).getItemStack());
        lores.clear();

        inventory.setItem(51, new ItemBuilder(Material.COMPARATOR).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Einstellungen")).getItemStack());

        this.hasInventoryOpen.put(player.getUniqueId(), Boolean.TRUE);

        return inventory;
    }



    private void animateFrameInventory(Inventory inventory) {
        int slot;
        for (slot = 0; slot < 6;) {
            inventory.setItem(slot, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
            slot++;
        }
        for (slot = 48; slot < 54;) {
            inventory.setItem(slot, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
            slot++;
        }

        (new BukkitRunnable() {
            public void run() {
                inventory.setItem(6, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
                //inventory.setItem(47, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
            }
        }).runTaskLaterAsynchronously(FreeBuild.getInstance().getPaperCore(), 3L);
        (new BukkitRunnable() {
            public void run() {
                inventory.setItem(7, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
                inventory.setItem(46, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
            }
        }).runTaskLaterAsynchronously(FreeBuild.getInstance().getPaperCore(), 6L);
        (new BukkitRunnable() {
            public void run() {
                inventory.setItem(8, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
                inventory.setItem(45, (new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<white>")).getItemStack());
            }
        }).runTaskLaterAsynchronously(FreeBuild.getInstance().getPaperCore(), 9L);

    }

}
