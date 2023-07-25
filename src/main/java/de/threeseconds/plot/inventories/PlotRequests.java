package de.threeseconds.plot.inventories;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlotRequests extends InventoryBuilder {

    public PlotRequests(FreeBuildPlayer freeBuildPlayer, @Nullable Plot plot, Boolean plotRequests) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstück <dark_gray>● <gray>Anfragen"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        List<Component> lores = new ArrayList<>();
        this.setItem(4, new ItemBuilder(Material.BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Anfragen")).getItemStack());

        AtomicInteger atomicInteger = new AtomicInteger(9);

        if(plotRequests) {
            FreeBuild.getInstance().getPlotManager().getRequests(plot).forEach(requests -> {
                this.setItem(atomicInteger.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(requests.getPlayer().getPlayerProfile().getTextures()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● " + PermissionCenterModulePaper.getAPI().getOnlineUser(requests.getPlayer()).getDisplayString() + requests.getPlayer().getName())).getItemStack(), inventoryClickEvent -> {
                    assert plot != null;
                    plot.addMember(freeBuildPlayer, requests);
                });

                atomicInteger.getAndIncrement();
            });

        } else {
            FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer).forEach(requests -> {
                this.setItem(atomicInteger.get(), new ItemBuilder(requests.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● " + requests.getPlotName())).getItemStack(), inventoryClickEvent -> {
                    requests.addMember(freeBuildPlayer);
                });

                atomicInteger.getAndIncrement();
            });
        }




        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            if(!plotRequests) new PlotBrowser(freeBuildPlayer).open(player);
            else {
                assert plot != null;
                new PlotOverview(freeBuildPlayer, plot).open(player);
            }
        });

        this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }
}
