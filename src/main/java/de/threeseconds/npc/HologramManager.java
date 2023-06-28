package de.threeseconds.npc;

import de.threeseconds.FreeBuild;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class HologramManager {

    private final HashMap<String, Hologram> hologramMap;

    public HologramManager() {
        this.hologramMap = new HashMap<>();
    }

    public void removeHologram(Hologram hologram) {
        hologramMap.remove(hologram.getName());
    }

    public void addHologram(Hologram hologram) {
        hologramMap.put(hologram.getName(), hologram);
    }

    public Hologram getHologram(String name) {
        return hologramMap.getOrDefault(name, null);
    }

    public Collection<Hologram> getAllHolograms() {
        return new ArrayList<>(hologramMap.values());
    }

    public HashMap<String, Hologram> getHologramHashMap() {
        return new HashMap<>(hologramMap);
    }

}
