package de.threeseconds.plot.inventories;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.concurrent.atomic.AtomicInteger;

public class PlotPlayerList extends InventoryBuilder {

    public PlotPlayerList(FreeBuildPlayer freeBuildPlayer, Plot plot) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstück <dark_gray>● <gray>Spieler einladen"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        this.setItem(4, new ItemBuilder(Material.WRITABLE_BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Einladen")).setGlowing().getItemStack());


        AtomicInteger atomicInteger = new AtomicInteger(9);
        Bukkit.getOnlinePlayers().forEach(targetPlayer -> {
            if(targetPlayer != freeBuildPlayer.getPlayer()) {
                if(plot.getPlotMembers().containsKey(FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer))) {
                    return;
                }
                if(FreeBuild.getInstance().getPlotManager().getRequests(FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer)) != null && FreeBuild.getInstance().getPlotManager().getRequests(FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer)).contains(plot)) {
                    return;
                }
                this.setItem(atomicInteger.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(targetPlayer.getPlayerProfile().getTextures()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + PermissionCenterModulePaper.getAPI().getOnlineUser(targetPlayer).getDisplayString() + targetPlayer.getName() )).getItemStack(), inventoryClickEvent -> {

                    if(FreeBuild.getInstance().getPlotManager().getRequests(FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer)) != null && FreeBuild.getInstance().getPlotManager().getRequests(FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer)).contains(plot)) {
                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast " + PermissionCenterModulePaper.getAPI().getOnlineUser(targetPlayer).getDisplayString() + targetPlayer.getName() + " <red>bereits eingeladen."));

                        return;
                    }

                    if(FreeBuild.getInstance().getPlotManager().getRequests(plot) != null && FreeBuild.getInstance().getPlotManager().getRequests(plot).contains(FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer))) {
                        plot.addMember(freeBuildPlayer, FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer));
                        return;
                    }

                    freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast " + PermissionCenterModulePaper.getAPI().getOnlineUser(targetPlayer).getDisplayString() + targetPlayer.getName() + " <gray>als <yellow>Mitglied <gray>auf deinem Grundstück eingeladen."));
                    targetPlayer.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du wurdest von " + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>als <yellow>Mitglied <gray>auf seinem Grundstück eingeladen."));
                    targetPlayer.playSound(targetPlayer, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

                    FreeBuild.getInstance().getPlotManager().invitePlayerToPlot(plot, FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer));
                });

                atomicInteger.getAndIncrement();
            }


        });


        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotOverview(freeBuildPlayer, plot).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }
}
