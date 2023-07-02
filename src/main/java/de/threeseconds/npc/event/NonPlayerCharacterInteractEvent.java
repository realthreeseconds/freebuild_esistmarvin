package de.threeseconds.npc.event;

import de.threeseconds.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class NonPlayerCharacterInteractEvent extends Event {

    public enum ActionType {
        INTERACT, ATTACK;
    }

    private final static HandlerList handlerList = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private final NPC npc;

    private final Player player;

    private final ActionType actionType;

    private final boolean isSneaking;

    public NonPlayerCharacterInteractEvent(NPC npc, Player player, ActionType actionType, boolean isSneaking) {
        this.npc = npc;
        this.player = player;
        this.actionType = actionType;
        this.isSneaking = isSneaking;
    }

    public boolean isSneaking() {
        return isSneaking;
    }

    public Player getPlayer() {
        return player;
    }

    public NPC getNPC() {
        return npc;
    }

    public ActionType getActionType() {
        return actionType;
    }

}
