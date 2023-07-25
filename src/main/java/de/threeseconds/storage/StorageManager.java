package de.threeseconds.storage;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.stream.IntStream;

public class StorageManager {

    //HashMap<FreeBuildPlayer, HashMap<Page, HashMap<Slot, Item>>>
    private HashMap<FreeBuildPlayer, HashMap<Integer, HashMap<Integer, ItemStack>>> storageHash;

    public StorageManager() {
        this.storageHash = new HashMap<>();
    }

    public void saveStorage(FreeBuildPlayer freeBuildPlayer, Integer page, Inventory storageContent) {
        HashMap<Integer, HashMap<Integer, ItemStack>> firstHash;
        if(this.storageHash.get(freeBuildPlayer) == null) {
            firstHash = new HashMap<>();
        } else firstHash = this.storageHash.get(freeBuildPlayer);

        HashMap<Integer, ItemStack> innerHash;
        if(firstHash.get(page) == null) {
            innerHash = new HashMap<>();
        } else innerHash = firstHash.get(page);

        for(Integer contents : IntStream.rangeClosed(9, 44).toArray()) {
            innerHash.put(contents, storageContent.getItem(contents));
        }
        firstHash.put(page, innerHash);

        this.storageHash.put(freeBuildPlayer, firstHash);
    }

    public boolean hasItems(FreeBuildPlayer freeBuildPlayer, Integer page, Integer slot) {
        if(this.storageHash.containsKey(freeBuildPlayer)) {
            return this.storageHash.get(freeBuildPlayer).containsKey(page);
        }
        return false;
    }

    public ItemStack displayItems(FreeBuildPlayer freeBuildPlayer, Integer page, Integer slot) {
        return this.storageHash.get(freeBuildPlayer).get(page).get(slot);
    }

}
