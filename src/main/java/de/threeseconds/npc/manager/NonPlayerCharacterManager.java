package de.threeseconds.npc.manager;

import de.threeseconds.FreeBuild;
import de.threeseconds.npc.NPC;
import de.threeseconds.npc.PlayerNPC;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class NonPlayerCharacterManager {

    private final HashMap<String, NPC> serverNPCs;
    private final HashMap<Player, HashMap<String, PlayerNPC>> playerNPCs;

    public NonPlayerCharacterManager() {
        this.playerNPCs = new HashMap<>();
        this.serverNPCs = new HashMap<String, NPC>();
    }

    public void addPlayerNPC(PlayerNPC playerNPC) {
        HashMap<String, PlayerNPC> playerNPCs = this.playerNPCs.getOrDefault(playerNPC.getPlayer(), new HashMap<>());
        playerNPCs.put(playerNPC.getName(), playerNPC);
        this.playerNPCs.put(playerNPC.getPlayer(), playerNPCs);
    }

    public void removePlayerNPC(PlayerNPC playerNPC) {
        if(!playerNPCs.containsKey(playerNPC.getPlayer())) return;
        playerNPCs.get(playerNPC.getPlayer()).remove(playerNPC.getName());
    }

    public HashMap<String, PlayerNPC> getPlayerNPCs(Player player) {
        return new HashMap<>(playerNPCs.getOrDefault(player, new HashMap<>()));
    }

    public Collection<PlayerNPC> getAllPlayerNPCs(Player player) {
        return getPlayerNPCs(player).values();
    }

    public PlayerNPC getPlayerNPC(Player player, String name) {
        return getPlayerNPCs(player).getOrDefault(name, null);
    }

    public PlayerNPC getPlayerNPC(Player player, int entityId) {
        for(PlayerNPC npc : getPlayerNPCs(player).values()) if(npc.getNPC() != null && npc.getNPC().getId() == entityId) return npc;
        return null;
    }

    public void removeNPC(NPC npc) {
        serverNPCs.remove(npc.getName());
    }

    public void addNPC(NPC npc) {
        serverNPCs.put(npc.getName(), npc);
    }

    public NPC getNPC(String name) {
        return serverNPCs.getOrDefault(name, null);
    }

    public Collection<NPC> getAllNPCs() {
        return new ArrayList<>(serverNPCs.values());
    }

    public NPC getNPC(int entityId) {
        for(NPC npc : serverNPCs.values()) if(npc.getNPC() != null && npc.getNPC().getId() == entityId) return npc;
        return null;
    }

    public HashMap<String, NPC> getNPCHashMap() {
        return new HashMap<>(serverNPCs);
    }
}