package de.threeseconds.npc;

import de.threeseconds.FreeBuild;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class NonPlayerCharacterManager {

    private final HashMap<String, NPC> serverNPC;

    public NonPlayerCharacterManager() {
        this.serverNPC = new HashMap<String, NPC>();
    }

    public void removeNPC(NPC npc) {
        serverNPC.remove(npc.getName());
    }

    public void addNPC(NPC npc) {
        serverNPC.put(npc.getName(), npc);
    }

    public NPC getNPC(String name) {
        return serverNPC.getOrDefault(name, null);
    }

    public Collection<NPC> getAllNPCs() {
        return new ArrayList<>(serverNPC.values());
    }

    public NPC getNPC(int entityId) {
        for(NPC npc : serverNPC.values()) if(npc.getNPC() != null && npc.getNPC().getId() == entityId) return npc;
        return null;
    }

    public HashMap<String, NPC> getNPCHashMap() {
        return new HashMap<>(serverNPC);
    }
}