package de.threeseconds.collections;

import de.threeseconds.util.ItemBuilder;
import org.bukkit.inventory.ItemStack;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class CollectionItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private ItemBuilder collectionItem;
    private List<Integer> collectionItemMaxXP;
    private List<Integer> collectionItemRewards;

    public CollectionItem(ItemBuilder collectionItem, Integer... collectionItemMaxXP) {
        this.collectionItem = collectionItem;
        this.collectionItemMaxXP = List.of(collectionItemMaxXP);
    }

    public ItemBuilder getCollectionItem() {
        return collectionItem;
    }

    public List<Integer> getCollectionItemMaxXP() {
        return collectionItemMaxXP;
    }
}
