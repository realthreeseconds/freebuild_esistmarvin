package de.threeseconds.plot.inventories;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotGroup;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlotUserGroups extends InventoryBuilder {

    public PlotUserGroups(FreeBuildPlayer freeBuildPlayer, FreeBuildPlayer targetFreeBuildPlayer, Plot plot) {
        super(9*5, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Gruppe <dark_gray>● " + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getPlayer().getName()));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(36, 44, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        this.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(targetFreeBuildPlayer.getPlayer().getPlayerProfile().getTextures()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " +  PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getUserName() + " <dark_gray>(" + plot.getPlotMembers().get(targetFreeBuildPlayer).getGroupName() + "<dark_gray>)")).getItemStack());


        AtomicInteger atomicInteger = new AtomicInteger(19);

        Arrays.stream(PlotGroup.values()).forEach(plotGroup -> {
            if(plotGroup == PlotGroup.OWNER) return;

            this.setItem(atomicInteger.get(), new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupId() < plotGroup.getGroupId() ? Material.PLAYER_HEAD : Material.BARRIER)).setSkullTexture((plot.getPlotMembers().get(freeBuildPlayer).getGroupId() < plotGroup.getGroupId() ? plotGroup.getSkullTexture() : null)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plotGroup.getGroupName())).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(inventoryClickEvent.getCurrentItem().getType() == Material.BARRIER) return;

                plot.setGroup(freeBuildPlayer, targetFreeBuildPlayer, plotGroup);
            });

            atomicInteger.getAndAdd(2);
        });


        this.setItem(44, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotUser(freeBuildPlayer, targetFreeBuildPlayer, plot).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }
}
