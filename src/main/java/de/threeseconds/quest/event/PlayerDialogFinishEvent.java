package de.threeseconds.quest.event;

import de.threeseconds.quest.Dialog;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerDialogFinishEvent extends Event {

    private final Dialog dialog;

    private final Player player;

    private final static HandlerList handlerList = new HandlerList();

    public PlayerDialogFinishEvent(Dialog dialog, Player player) {
        this.dialog = dialog;
        this.player = player;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
