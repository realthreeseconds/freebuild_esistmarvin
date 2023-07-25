package de.threeseconds.plot.inventories;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotGroupPermission;
import de.threeseconds.plot.PlotSetting;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.concurrent.atomic.AtomicInteger;

public class PlotUser extends InventoryBuilder {

    public PlotUser(FreeBuildPlayer freeBuildPlayer, FreeBuildPlayer targetFreeBuildPlayer, Plot plot) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstück <dark_gray>● " + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getPlayer().getName()));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        this.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(targetFreeBuildPlayer.getPlayer().getPlayerProfile().getTextures()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getUserName() + " <dark_gray>(" + plot.getPlotMembers().get(targetFreeBuildPlayer).getGroupName() + "<dark_gray>)")).getItemStack());


        this.setItem(19, new ItemBuilder(Material.PISTON).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Kicken")).getItemStack());
        this.setItem(28, new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.KICK) ? (freeBuildPlayer.getPlayer().getChunk() == plot.getPlotChunks().get(0).toChunk() ? Material.LIME_CONCRETE : Material.RED_CONCRETE) : Material.BARRIER)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Kicken")).getItemStack());

        this.setItem(22, new ItemBuilder(Material.NAME_TAG).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Gruppe geben")).getItemStack());
        this.setItem(31, new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.PLAYER_GROUP) ? Material.PLAYER_HEAD : Material.BARRIER)).setSkullTexture((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.PLAYER_GROUP) ? plot.getPlotMembers().get(targetFreeBuildPlayer).getSkullTexture() : null)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Gruppe geben")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();


            if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.PLAYER_GROUP)) {
                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast keine Rechte dafür."));
                return;
            }

            //threeseconds Modetaor(2) < twoseconds Admin(1)
            if(plot.getPlotMembers().get(freeBuildPlayer).getGroupId() > plot.getPlotMembers().get(targetFreeBuildPlayer).getGroupId()) {
                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst diesem Spieler keine Gruppe geben."));

                return;
            }

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotUserGroups(freeBuildPlayer, targetFreeBuildPlayer, plot).open(player);
        });

        this.setItem(25, new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Bannen")).getItemStack());
        this.setItem(34, new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.KICK) ? (freeBuildPlayer.getPlayer().getChunk() == plot.getPlotChunks().get(0).toChunk() ? Material.LIME_CONCRETE : Material.RED_CONCRETE) : Material.BARRIER)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Bannen")).getItemStack());


        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotOverview(freeBuildPlayer, plot).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }
}
