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

public class PlotGroups extends InventoryBuilder {

    public PlotGroups(FreeBuildPlayer freeBuildPlayer, Plot plot) {
        super(9*5, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstück <dark_gray>● <gray>Gruppen"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(36, 44, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        this.setItem(2, new ItemBuilder(Material.ARMOR_STAND).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Mitglieder")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
        });
        this.setItem(4, new ItemBuilder(Material.NAME_TAG).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Gruppen")).setGlowing().getItemStack());
        this.setItem(6, new ItemBuilder(Material.COMPARATOR).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Einstellungen")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotSettings(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
        });


        AtomicInteger atomicInteger = new AtomicInteger(19);
        List<Component> lores = new ArrayList<>();

        Arrays.stream(PlotGroup.values()).forEach(plotGroup -> {
            if(plotGroup == PlotGroup.OWNER) return;

            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            if(plotGroup == PlotGroup.GUEST) {
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_red>(!) <red>Du kannst diese Rechte").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_red>(!) <red>nicht verändern.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            } else {
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier kannst du die").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Rechte der Gruppe anpassen.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Permissions <dark_gray>» <yellow>" + plotGroup.getGroupPermissionSize()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            }
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(atomicInteger.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(plotGroup.getSkullTexture()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plotGroup.getGroupName())).setLore(lores).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(plotGroup == PlotGroup.GUEST) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Die Rechte von " + plotGroup.getGroupName() + " <red>können nicht verändert werden!"));
                    return;
                }

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new PlotGroupPermissions(freeBuildPlayer, plot, plotGroup).open(player);
            });
            lores.clear();

            atomicInteger.getAndAdd(2);
        });


        this.setItem(44, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotBrowser(freeBuildPlayer).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }

}
