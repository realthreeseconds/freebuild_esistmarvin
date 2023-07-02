package de.threeseconds.collections;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.stream.Collectors;

public class CollectionManager {

    public CollectionManager() {}


    public void addItemsToCollection(FreeBuildPlayer freeBuildPlayer, Collection collection, CollectionItem collectionItem, Integer amountToAdd) {
        freeBuildPlayer.getCollectionPlayer().addItemAmountFromCollectionItem(collection, collectionItem, amountToAdd);

        if(freeBuildPlayer.getCollectionPlayer().getItemAmountFromCollectionItem(collection, collectionItem) >= collectionItem.getCollectionItemMaxXP().get(freeBuildPlayer.getCollectionPlayer().getCollectionItemLevel(collection, collectionItem))) {
            freeBuildPlayer.getCollectionPlayer().addItemLevelToCollectionItem(collection, collectionItem);

            if(freeBuildPlayer.getCollectionPlayer().hasItemLevelMaxed(collection, collectionItem)) {
                freeBuildPlayer.getCollectionPlayer().addCompletedCollectionItem(collection, collectionItem);
            }

            freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast <green>Level " + freeBuildPlayer.getCollectionPlayer().getCollectionItemLevel(collection, collectionItem) + " <gray>in Collection " + collection.getColorCode() + "<lang:" + collectionItem.getCollectionItem().getItemStack().translationKey() + "> <blue>(" + WordUtils.capitalize(collection.name().toLowerCase()) + ") <gray>abgeschlossen."));

        }
    }

}
