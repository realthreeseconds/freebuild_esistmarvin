package de.threeseconds.collections;

import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CollectionPlayer {

    private FreeBuildPlayer freeBuildPlayer;

    private List<Collection> completedCollections;
    private HashMap<Collection, List<CollectionItem>> totalCollectionItems;
    private HashMap<Collection, List<CollectionItem>> completedCollectionItem;
    private HashMap<Collection, HashMap<CollectionItem, Integer>> completedCollectionItemLevel;
    private HashMap<Collection, HashMap<CollectionItem, Integer>> collectionItemAmount;

    public CollectionPlayer(FreeBuildPlayer freeBuildPlayer) {
        this.freeBuildPlayer = freeBuildPlayer;
        this.completedCollections = new ArrayList<>();
        this.completedCollectionItem = new HashMap<>();
        this.collectionItemAmount = new HashMap<>();
        this.totalCollectionItems = new HashMap<>();
        this.completedCollectionItemLevel = new HashMap<>();

        for(Collection collections : Collection.values()) {
            HashMap<CollectionItem, Integer> itemIntegerHashMap = new HashMap<>();
            HashMap<CollectionItem, Integer> itemLevelHashMap = new HashMap<>();
            collections.getCollectionItems().forEach(collectionItem -> {
                itemIntegerHashMap.put(collectionItem, 0);
                itemLevelHashMap.put(collectionItem, 0);

            });
            this.totalCollectionItems.put(collections, collections.getCollectionItems());

            this.collectionItemAmount.put(collections, itemIntegerHashMap);
            this.completedCollectionItemLevel.put(collections, itemLevelHashMap);

            this.completedCollectionItem.put(collections, new ArrayList<>());
        }

    }

    public Collection getCollectionByItem(Material material) {
        AtomicReference<Collection> collection = new AtomicReference<>();
        for(Collection collections : Collection.values()) {
            collections.getCollectionItems().forEach(items -> {
                if(items.getCollectionItem().getItemStack().getType() == material) collection.set(collections);
            });
        }
        return collection.get();
    }

    public CollectionItem getCollectionItemFromItemStack(Collection collection, Material material) {
        AtomicReference<CollectionItem> collectionItem = new AtomicReference<>();
        this.totalCollectionItems.get(collection).forEach(items -> {
            if(items.getCollectionItem().getItemStack().getType() == material) {
                collectionItem.set(items);
            }
        });
        return collectionItem.get();
    }

    public List<CollectionItem> getCompletedItemsFromCollection(Collection collection) {
        return this.completedCollectionItem.get(collection);
    }

    public Integer getCollectionItemLevel(Collection collection, CollectionItem collectionItem) {
        return this.completedCollectionItemLevel.get(collection).get(collectionItem);
    }

    public void addItemAmountFromCollectionItem(Collection collection, CollectionItem collectionItem, Integer amountToAdd) {
        this.collectionItemAmount.get(collection).put(collectionItem, this.getItemAmountFromCollectionItem(collection, collectionItem) + amountToAdd);
    }

    public void addItemLevelToCollectionItem(Collection collection, CollectionItem collectionItem) {
        this.completedCollectionItemLevel.get(collection).put(collectionItem, this.getCollectionItemLevel(collection, collectionItem) + 1);
    }

    public void addCompletedCollectionItem(Collection collection, CollectionItem collectionItem) {
        this.completedCollectionItem.get(collection).add(collectionItem);
    }

    public boolean hasItemLevelMaxed(Collection collection, CollectionItem collectionItem) {
        return (this.getCollectionItemLevel(collection, collectionItem) > collectionItem.getCollectionItemMaxXP().size() - 1);
    }

    public Integer getMaxXPByItemLevel(Collection collection, CollectionItem collectionItem) {
        if(this.getCollectionItemLevel(collection, collectionItem) > collectionItem.getCollectionItemMaxXP().size() - 1) {
            return collectionItem.getCollectionItemMaxXP().get(collectionItem.getCollectionItemMaxXP().size() - 1);
        }
        return collectionItem.getCollectionItemMaxXP().get(this.getCollectionItemLevel(collection, collectionItem));
    }

    public Integer getItemAmountFromCollectionItem(Collection collection, CollectionItem collectionItem) {
        return this.collectionItemAmount.get(collection).get(collectionItem);
    }


    public FreeBuildPlayer getFreeBuildPlayer() {
        return freeBuildPlayer;
    }

    public List<Collection> getCompletedCollections() {
        return completedCollections;
    }
}
