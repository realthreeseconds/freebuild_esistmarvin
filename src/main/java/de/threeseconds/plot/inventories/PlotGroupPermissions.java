package de.threeseconds.plot.inventories;

import de.threeseconds.FreeBuild;
import de.threeseconds.jobs.Job;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotGroup;
import de.threeseconds.plot.PlotGroupPermission;
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

public class PlotGroupPermissions extends InventoryBuilder {

    public PlotGroupPermissions(FreeBuildPlayer freeBuildPlayer, Plot plot, PlotGroup plotGroup) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plotGroup.getGroupName() + " <dark_gray>● <gray>Permissions"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        this.setItem(4, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(plotGroup.getSkullTexture()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Permissions <gray>für " + plotGroup.getGroupName())).getItemStack());

        AtomicInteger atomicInteger = new AtomicInteger(19);

        List<PlotGroupPermission> tempPermissions = new ArrayList<>(plotGroup.getGroupPermissions());

        Arrays.stream(PlotGroupPermission.values()).forEach(plotGroupPermission -> {

            this.setItem(atomicInteger.get(), new ItemBuilder(plotGroupPermission.getPermissionMaterial()).setKey("plotGroupPermissionItem").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>" + plotGroupPermission.getPermissionName() + " <dark_gray>(" + (tempPermissions.contains(plotGroupPermission) ? "<green>✔" : "<red>✘") + "<dark_gray>)")).getItemStack(), inventoryClickEvent -> {

            });

            if(atomicInteger.get() == 24) {
                atomicInteger.getAndAdd(4);
            } else atomicInteger.getAndIncrement();

        });


        this.setItem(45, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_green>« <green>Speichern <dark_green>✔")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            plotGroup.getGroupPermissions().clear();
            tempPermissions.forEach(plotGroup.getGroupPermissions()::add);

            new PlotGroups(freeBuildPlayer, plot).open(player);
        });

        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Abbrechen & Zurück <dark_red>✘")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotGroups(freeBuildPlayer, plot).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);

            if(inventoryClickEvent.getCurrentItem() == null) return;

            Arrays.stream(PlotGroupPermission.values()).forEach(plotGroupPermission -> {
                if(inventoryClickEvent.getCurrentItem().getType() == plotGroupPermission.getPermissionMaterial() && FreeBuild.getInstance().checkPDC("plotGroupPermissionItem", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_gray>» <yellow>" + plotGroupPermission.getPermissionName() + " <dark_gray>(" + (tempPermissions.contains(plotGroupPermission) ? "<green>✔" : "<red>✘") + "<dark_gray>)")) {
                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                    player.playSound(player, Sound.BLOCK_LEVER_CLICK, 1, 1);

                    if(tempPermissions.contains(plotGroupPermission)) tempPermissions.remove(plotGroupPermission);
                    else tempPermissions.add(plotGroupPermission);

                    this.setItem(inventoryClickEvent.getSlot(), new ItemBuilder(plotGroupPermission.getPermissionMaterial()).setKey("plotGroupPermissionItem").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>" + plotGroupPermission.getPermissionName() + " <dark_gray>(" + (tempPermissions.contains(plotGroupPermission) ? "<green>✔" : "<red>✘") + "<dark_gray>)")).getItemStack());

                }
            });
        });
    }
}
