package de.threeseconds.npc.event;

import de.threeseconds.npc.PlayerNPC;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerNonPlayerCharacterInteractEvent extends Event {

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

    private final PlayerNPC playerNPC;

    private final Player player;

    private final ActionType actionType;

    private final boolean isSneaking;

    public PlayerNonPlayerCharacterInteractEvent(PlayerNPC playerNPC, Player player, ActionType actionType, boolean isSneaking) {
        this.playerNPC = playerNPC;
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

    public PlayerNPC getPlayerNPC() {
        return playerNPC;
    }

    public ActionType getActionType() {
        return actionType;
    }

}
