package de.threeseconds.quest.event;

import de.threeseconds.quest.Dialog;
import de.threeseconds.quest.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerDialogEvent extends Event {

    private final Dialog dialog;

    private final Player player;

    private final Task task;

    private final int messageId;

    private final static HandlerList handlerList = new HandlerList();

    public PlayerDialogEvent(Dialog dialog, Player player, Task task, int messageId) {
        this.dialog = dialog;
        this.player = player;
        this.task = task;
        this.messageId = messageId;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public Player getPlayer() {
        return player;
    }

    public Task getTask() {
        return task;
    }

    public int getMessageId() {
        return messageId;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

}
