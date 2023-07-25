package de.threeseconds.plot.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerEnterPlotEvent extends PlayerEvent implements Cancellable {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private boolean isCancelled = false;
    private final FreeBuildPlayer freeBuildPlayer;
    private final Plot lastPlot;
    private final Plot enteredPlot;

    public PlayerEnterPlotEvent(@NotNull final Player player, @NotNull final FreeBuildPlayer freeBuildPlayer, @Nullable final Plot lastPlot, @Nullable final Plot enteredPlot) {
        super(player);
        this.freeBuildPlayer = freeBuildPlayer;
        this.lastPlot = lastPlot;
        this.enteredPlot = enteredPlot;
    }

    public FreeBuildPlayer getFreeBuildPlayer() {
        return freeBuildPlayer;
    }

    public Plot getLastPlot() {
        return this.lastPlot;
    }

    public Plot getEnteredPlot() {
        return this.enteredPlot;
    }


    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    public Component getCancelMessage() {
        return FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dieses <yellow>Grundstück <red>nicht betreten.");
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
