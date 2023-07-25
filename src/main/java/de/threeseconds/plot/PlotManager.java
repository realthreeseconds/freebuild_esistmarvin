package de.threeseconds.plot;

import de.threeseconds.plot.listener.PlayerEnterPlotEvent;
import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class PlotManager {

    public List<Plot> plots;
    private HashMap<FreeBuildPlayer, Plot> plotList;
    private HashMap<Plot, List<FreeBuildPlayer>> playerInvites;
    private HashMap<FreeBuildPlayer, List<Plot>> plotInvites;

    public HashMap<FreeBuildPlayer, Long> lastChunkKey;
    public HashMap<FreeBuildPlayer, Plot> lastPlot;

    public HashMap<FreeBuildPlayer, Boolean> playerBukkitTask;

    public PlotManager() {
        this.plots = new ArrayList<>();
        this.plotList = new HashMap<>();
        this.playerInvites = new HashMap<>();
        this.plotInvites = new HashMap<>();
        this.lastChunkKey = new HashMap<>();
        this.lastPlot = new HashMap<>();

        this.playerBukkitTask = new HashMap<>();
    }

    public void addPlot(FreeBuildPlayer freeBuildPlayer, Plot plot) {
        this.plotList.put(freeBuildPlayer, plot);
    }

    public void invitePlayerToPlot(Plot plot, FreeBuildPlayer freeBuildPlayer) {
        List<Plot> plots;
        if(this.plotInvites.get(freeBuildPlayer) != null) {
            plots = this.plotInvites.get(freeBuildPlayer);
        } else {
            plots = new ArrayList<>();
        }
        plots.add(plot);
        this.plotInvites.put(freeBuildPlayer, plots);
    }

    public void requestToPlot(Plot plot, FreeBuildPlayer freeBuildPlayer) {
        List<FreeBuildPlayer> invitedPlayer;
        if(this.playerInvites.get(plot) != null) {
            invitedPlayer = this.playerInvites.get(plot);
        } else {
            invitedPlayer = new ArrayList<>();
        }
        invitedPlayer.add(freeBuildPlayer);
        this.playerInvites.put(plot, invitedPlayer);
    }

    public Plot getPlotByChunk(Chunk playerChunk) {
        AtomicReference<Plot> c = new AtomicReference<>();
        this.getPlots().forEach(plots -> {
            //Bukkit.broadcast(Component.text("PlotChunk: " + plots.getPlotChunk().getChunkKey() + " | PlayerChunk: " + playerChunk.getChunkKey()));
            plots.getPlotChunks().forEach(chunks -> {
                if(chunks.toChunk().getChunkKey() == playerChunk.getChunkKey()) c.set(plots);
            });

        });

        return c.get();
    }

    public HashMap<FreeBuildPlayer, Plot> getPlotList() {
        return plotList;
    }

    public Collection<FreeBuildPlayer> getRequests(Plot plot) {
        return this.playerInvites.get(plot);
    }

    public Collection<Plot> getRequests(FreeBuildPlayer freeBuildPlayer) {
        return this.plotInvites.get(freeBuildPlayer);
    }

    public HashMap<FreeBuildPlayer, Boolean> getPlayerBukkitTask() {
        return playerBukkitTask;
    }

    public List<Plot> getPlots() {
        return plots;
    }
}
