package de.threeseconds.collections;

import de.threeseconds.FreeBuild;
import de.threeseconds.jobs.Job;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Item;

import java.util.Arrays;
import java.util.List;

public enum Collection {

    MINING(
            new ItemBuilder(Material.IRON_PICKAXE),

            new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE),

            "<gray>",

            Job.MINER,

            new CollectionItem(new ItemBuilder(Material.NETHERITE_SCRAP), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.GOLD_INGOT), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.QUARTZ), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.COAL), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.IRON_INGOT), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.OBSIDIAN), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.NETHERRACK), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.COPPER_INGOT), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.COBBLESTONE), 50, 100, 250, 1000),
            new CollectionItem(new ItemBuilder(Material.LAPIS_LAZULI), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.REDSTONE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.DIAMOND), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.BASALT), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.AMETHYST_SHARD), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.MAGMA_BLOCK), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.DIORITE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.GRANITE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.ANDESITE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.DEEPSLATE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.END_STONE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.ICE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.SANDSTONE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.GLOWSTONE_DUST), 50, 100, 250, 1000, 2500)
    ),
    FORAGING(
            new ItemBuilder(Material.GOLDEN_AXE),

            new ItemBuilder(Material.BROWN_STAINED_GLASS_PANE),

            "<color:#8B4513>",

            Job.HOLZFÄLLER,

            new CollectionItem(new ItemBuilder(Material.OAK_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.SPRUCE_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.BIRCH_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.JUNGLE_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.ACACIA_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.DARK_OAK_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.MANGROVE_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.CHERRY_LOG), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.WARPED_STEM), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.CRIMSON_STEM), 50, 100, 250, 1000, 2500)
    ),
    FISHING(
            new ItemBuilder(Material.FISHING_ROD),

            new ItemBuilder(Material.BLUE_STAINED_GLASS_PANE),

            "<blue>",

            Job.FISCHER,

            new CollectionItem(new ItemBuilder(Material.CLAY_BALL), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.TROPICAL_FISH), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.INK_SAC), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.SALMON), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.COD), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.PRISMARINE_SHARD), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.PRISMARINE_CRYSTALS), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.PUFFERFISH), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.SPONGE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.LILY_PAD), 50, 100, 250, 1000, 2500)
    ),
    COMBAT(
            new ItemBuilder(Material.DIAMOND_SWORD),

            new ItemBuilder(Material.MAGENTA_STAINED_GLASS_PANE),

            "<dark_purple>",

            Job.JÄGER,

            new CollectionItem(new ItemBuilder(Material.BONE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.SPIDER_EYE), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.ROTTEN_FLESH), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.ENDER_PEARL), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.GHAST_TEAR), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.BLAZE_ROD), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.GUNPOWDER), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.STRING), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.MAGMA_CREAM), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.SHULKER_SHELL), 50, 100, 250, 1000, 2500),
            new CollectionItem(new ItemBuilder(Material.PHANTOM_MEMBRANE), 50, 100, 250, 1000, 2500)
    );

    private ItemBuilder mainItem;
    private ItemBuilder borderPane;
    private String colorCode;
    private Job relatedJob;
    private List<CollectionItem> collectionItems;

    Collection(ItemBuilder mainItem, ItemBuilder borderPane, String colorCode, Job relatedJob, CollectionItem... collectionItems) {
        this.mainItem = mainItem;
        this.borderPane = borderPane;
        this.colorCode = colorCode;
        this.relatedJob = relatedJob;
        this.collectionItems = List.of(collectionItems);
    }

    public Job getRelatedJob() {
        return relatedJob;
    }

    public ItemBuilder getMainItem() {
        return mainItem;
    }

    public ItemBuilder getBorderPane() {
        return borderPane;
    }

    public void setBorderPane(ItemBuilder borderPane) {
        this.borderPane = borderPane;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public List<CollectionItem> getCollectionItems() {
        return collectionItems;
    }

    public void setCollectionItems(List<CollectionItem> collectionItems) {
        this.collectionItems = collectionItems;
    }
}
