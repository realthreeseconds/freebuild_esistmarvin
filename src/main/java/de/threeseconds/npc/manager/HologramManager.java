package de.threeseconds.npc.manager;

import de.threeseconds.npc.Hologram;
import de.threeseconds.npc.PlayerHologram;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class HologramManager {

    private final HashMap<String, Hologram> serverHolograms;
    private final HashMap<Player, HashMap<String, PlayerHologram>> playerHolograms;

    public HologramManager() {
        this.playerHolograms = new HashMap<>();
        this.serverHolograms = new HashMap<>();
    }

    public void addPlayerHologram(PlayerHologram playerHologram) {
        HashMap<String, PlayerHologram> playerNPCs = this.playerHolograms.getOrDefault(playerHologram.getPlayer(), new HashMap<>());
        playerNPCs.put(playerHologram.getName(), playerHologram);
        this.playerHolograms.put(playerHologram.getPlayer(), playerNPCs);
    }

    public void removePlayerHologram(PlayerHologram playerHologram) {
        if(!playerHolograms.containsKey(playerHologram.getPlayer())) return;
        playerHolograms.get(playerHologram.getPlayer()).remove(playerHologram.getName());
    }

    public Collection<PlayerHologram> getAllPlayerHolograms(Player player) {
        return getPlayerHolograms(player).values();
    }

    public HashMap<String, PlayerHologram> getPlayerHolograms(Player player) {
        return new HashMap<>(this.playerHolograms.getOrDefault(player, new HashMap<>()));
    }

    public void removeHologram(Hologram hologram) {
        serverHolograms.remove(hologram.getName());
    }

    public void addHologram(Hologram hologram) {
        serverHolograms.put(hologram.getName(), hologram);
    }

    public Hologram getHologram(String name) {
        return serverHolograms.getOrDefault(name, null);
    }

    public Collection<Hologram> getAllHolograms() {
        return new ArrayList<>(serverHolograms.values());
    }

    public HashMap<String, Hologram> getHologramHashMap() {
        return new HashMap<>(serverHolograms);
    }

}
