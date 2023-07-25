package de.threeseconds.plot.inventories;

import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotSetting;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.checkerframework.checker.units.qual.A;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PlotIconBlocks extends InventoryBuilder {

    public PlotIconBlocks(FreeBuildPlayer freeBuildPlayer, Plot plot) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstück <dark_gray>● <gray>Icon"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        this.setItem(4, new ItemBuilder((plot.getPlotBlock() != null ? plot.getPlotBlock() : Material.GRASS_BLOCK)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Icon")).getItemStack());

        List<Material> iconList = Arrays.asList(Material.GRASS_BLOCK, Material.CHISELED_STONE_BRICKS, Material.SEA_LANTERN, Material.DIAMOND_BLOCK, Material.NETHERITE_BLOCK, Material.AMETHYST_BLOCK, Material.BAMBOO_BLOCK, Material.PURPUR_BLOCK, Material.COPPER_BLOCK, Material.END_STONE, Material.WARPED_HYPHAE, Material.CRIMSON_HYPHAE, Material.RAW_GOLD_BLOCK, Material.SCULK_CATALYST);

        AtomicInteger atomicInteger = new AtomicInteger(19);
        iconList.forEach(icons -> {

            this.setItem(atomicInteger.get(), new ItemBuilder(icons).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(plot.getPlotBlock() == inventoryClickEvent.getCurrentItem().getType()) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast diesen Block bereits als Icon."));
                    return;
                }

                player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);

                plot.setPlotBlock(icons);

                plot.getPlotMembers().forEach((plotMembers, plotGroup) -> plot.showBorder(plotMembers.getPlayer(), true));

                new PlotSettings(freeBuildPlayer, plot).open(player);
            });

            if(atomicInteger.get() == 25) atomicInteger.getAndAdd(3);
            else atomicInteger.getAndIncrement();

        });


        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotSettings(freeBuildPlayer, plot).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }
}
