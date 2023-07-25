package de.threeseconds.plot;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.inventories.PlotOverview;
import de.threeseconds.plot.map.MapMarker;
import de.threeseconds.plot.map.MapService;
import de.threeseconds.util.BoundingBox;
import de.threeseconds.util.ChunkReference;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Plot {

    private UUID plotId;
    private FreeBuildPlayer plotOwner;
    private String plotName;
    private Material plotBlock;
    private HashMap<FreeBuildPlayer, PlotGroup> plotMembers;
    private HashMap<PlotSetting, Boolean> plotSettings;
    private List<ChunkReference> plotChunks;

    private BoundingBox outerBounds;
    //SETTINGS

    public Plot(FreeBuildPlayer plotOwner, List<Chunk> plotChunks) {
        this.plotChunks = new ArrayList<>();
        this.plotId = UUID.randomUUID();
        this.plotOwner = plotOwner;
        plotChunks.forEach(chunks -> this.plotChunks.add(ChunkReference.ofChunk(chunks)));
        this.plotName = "<yellow>Grundstück <gray>von " + PermissionCenterModulePaper.getAPI().getOnlineUser(plotOwner.getPlayer()).getDisplayString() + plotOwner.getUserName();
        this.plotBlock = Material.GRASS_BLOCK;

        this.plotMembers = new HashMap<>();
        this.plotMembers.put(plotOwner, PlotGroup.OWNER);

        this.plotSettings = new HashMap<>();
        this.plotSettings.put(PlotSetting.JOINABLE, Boolean.TRUE);
        this.plotSettings.put(PlotSetting.BROWSER_VISIBILITY, Boolean.TRUE);
        this.plotSettings.put(PlotSetting.PVP, Boolean.TRUE);
        this.plotSettings.put(PlotSetting.RECEIVE_REQUESTS, Boolean.TRUE);

        FreeBuild.getInstance().getPlotManager().plots.add(this);

        this.generateBounds();
    }

    public Plot(FreeBuildPlayer plotOwner, Chunk plotChunk) {
        this.plotChunks = new ArrayList<>();

        this.plotId = UUID.randomUUID();
        this.plotOwner = plotOwner;
        this.plotChunks.add(ChunkReference.ofChunk(plotChunk));
        this.plotName = "<yellow>Grundstück <gray>von " + PermissionCenterModulePaper.getAPI().getOnlineUser(plotOwner.getPlayer()).getDisplayString() + plotOwner.getUserName();
        this.plotBlock = Material.GRASS_BLOCK;

        this.plotMembers = new HashMap<>();
        this.plotMembers.put(plotOwner, PlotGroup.OWNER);

        this.plotSettings = new HashMap<>();
        this.plotSettings.put(PlotSetting.JOINABLE, Boolean.TRUE);
        this.plotSettings.put(PlotSetting.BROWSER_VISIBILITY, Boolean.TRUE);
        this.plotSettings.put(PlotSetting.PVP, Boolean.TRUE);
        this.plotSettings.put(PlotSetting.RECEIVE_REQUESTS, Boolean.TRUE);

        FreeBuild.getInstance().getPlotManager().plots.add(this);



    }

    public void addMember(FreeBuildPlayer freeBuildPlayer, FreeBuildPlayer targetFreeBuildPlayer) {
        if(this.plotMembers.containsKey(targetFreeBuildPlayer)) {
            freeBuildPlayer.getPlayer().playSound(targetFreeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(targetFreeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getUserName() + " <red>ist bereits Mitglied auf deinem Grundstück."));
        } else {

            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(targetFreeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getUserName() + " <gray>ist <green>nun <gray>Mitglied deines Grundstücks."));
            freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

            targetFreeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du bist <green>nun <gray>Mitglied dieses Grundstücks."));

            this.plotMembers.put(targetFreeBuildPlayer, PlotGroup.MEMBER);
            FreeBuild.getInstance().getPlotManager().getRequests(this).remove(targetFreeBuildPlayer);

            new PlotOverview(freeBuildPlayer, this).open(freeBuildPlayer.getPlayer());
        }
    }

    public void addMember(FreeBuildPlayer targetFreeBuildPlayer) {
        if(this.plotMembers.containsKey(targetFreeBuildPlayer)) {
            targetFreeBuildPlayer.getPlayer().playSound(targetFreeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
            targetFreeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du bist bereits Mitglied auf diesem Grundstück."));
        } else {


            this.getPlotMembers().forEach((plotMembers, plotGroup) -> {
                if(plotMembers.getPlayer().isOnline()) {
                    plotMembers.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(targetFreeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getUserName() + " <gray>ist <green>nun <gray>Mitglied dieses Grundstücks."));

                }
            });

            targetFreeBuildPlayer.getPlayer().playSound(targetFreeBuildPlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            targetFreeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du bist <green>nun <gray>Mitglied dieses Grundstücks."));

            this.plotMembers.put(targetFreeBuildPlayer, PlotGroup.MEMBER);
            FreeBuild.getInstance().getPlotManager().getRequests(targetFreeBuildPlayer).remove(this);

            new PlotOverview(targetFreeBuildPlayer, this).open(targetFreeBuildPlayer.getPlayer());
        }
    }

    public void setGroup(FreeBuildPlayer freeBuildPlayer, FreeBuildPlayer targetFreeBuildPlayer, PlotGroup plotGroup) {
        if(this.getPlotMembers().get(targetFreeBuildPlayer) == plotGroup) {
            freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(targetFreeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getUserName() + " <red>ist bereits " + plotGroup.getGroupName() + "<red>."));

            return;
        }

        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(targetFreeBuildPlayer.getPlayer()).getDisplayString() + targetFreeBuildPlayer.getUserName() + " <gray>ist <green>nun " + plotGroup.getGroupName() + "<gray>."));
        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);

        targetFreeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du bist <green>nun <gray>auf " + this.getPlotName() + " " + plotGroup.getGroupName() + "<gray>."));

        this.plotMembers.put(targetFreeBuildPlayer, plotGroup);

    }

    private void validateMarkers() {
        if (MapService.isAvailable()) {
            MapService ms = MapService.getNonNull();
            ms.getMarkerAsync(this, (MapMarker marker) -> {
                if (marker != null) marker.update(this);
            });
        }
    }

    private void generateBounds() {
        BoundingBox bb = null;
        boolean set = false;
        for (ChunkReference c : this.plotChunks) {
            BoundingBox bounds = c.getBounds();
            if (!set) {
                bb = bounds;
                set = true;
            } else {
                bb.union(bounds);
            }
        }
        this.outerBounds = (set ? bb : new BoundingBox());
        validateMarkers();
    }


    public BoundingBox getOuterBounds() {
        return outerBounds;
    }

    public void enter(Plot lastPlot, FreeBuildPlayer freeBuildPlayer) {
        this.showBorder(freeBuildPlayer.getPlayer(), true);
        if(!this.getPlotMembers().containsKey(freeBuildPlayer)) {

            this.getPlotMembers().forEach((plotMembers, plotGroup) -> {
                if(plotMembers.getPlayer().isOnline()) {
                    if(plotGroup != PlotGroup.GUEST) {
                        plotMembers.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>hat dein <yellow>Grundstück <gray>betreten."));
                    }
                }
            });

            this.plotMembers.put(freeBuildPlayer, PlotGroup.GUEST);

            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast " + this.getPlotName() + " <gray>betreten."));

            if(lastPlot != null) {
                lastPlot.leave(freeBuildPlayer);
            }
        } else {
            if(this.getPlotMembers().get(freeBuildPlayer) != PlotGroup.GUEST) {
                if(lastPlot != null) {
                    if(!lastPlot.getPlotChunks().contains(ChunkReference.ofChunk(freeBuildPlayer.getPlayer().getChunk()))) lastPlot.showBorder(freeBuildPlayer.getPlayer(), false);
                    lastPlot.leave(freeBuildPlayer);
                }
            }
        }


    }

    public void leave(FreeBuildPlayer freeBuildPlayer) {
        if(this.getPlotMembers().containsKey(freeBuildPlayer) && this.getPlotMembers().get(freeBuildPlayer) == PlotGroup.GUEST) {
            this.plotMembers.remove(freeBuildPlayer);

            this.getPlotMembers().forEach((plotMembers, plotGroup) -> {
                if (plotMembers.getPlayer().isOnline()) {
                    if (plotGroup != PlotGroup.GUEST) {
                        plotMembers.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>hat dein <yellow>Grundstück <gray>verlassen."));
                    }
                }
            });

            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast " + this.getPlotName() + " <gray>verlassen."));

            this.showBorder(freeBuildPlayer.getPlayer(), false);
        }

    }

    public void showBorder(Player player, boolean show) {
        plotChunks.forEach(chunkReference -> {
            World world = chunkReference.toChunk().getWorld();
            int chunkX = chunkReference.toChunk().getX();
            int chunkZ = chunkReference.toChunk().getZ();

            int minX = chunkX * 16;
            int minZ = chunkZ * 16;

            Chunk north = world.getChunkAt(chunkX, chunkZ - 1);
            if(FreeBuild.getInstance().getPlotManager().getPlotByChunk(north) == null) {
                for (int x = minX; x < minX + 16; x++) {
                    player.sendBlockChange(new Location(world, x, world.getHighestBlockYAt(x, minZ, HeightMap.MOTION_BLOCKING_NO_LEAVES), minZ), (show ? plotBlock.createBlockData() : world.getBlockAt(x, world.getHighestBlockYAt(x, minZ, HeightMap.MOTION_BLOCKING_NO_LEAVES), minZ).getBlockData()));
                }
            }

            Chunk south = world.getChunkAt(chunkX, chunkZ + 1);
            if(FreeBuild.getInstance().getPlotManager().getPlotByChunk(south) == null) {
                for (int x = minX; x < minX + 16; x++) {
                    player.sendBlockChange(new Location(world, x, world.getHighestBlockYAt(x, minZ + 15, HeightMap.MOTION_BLOCKING_NO_LEAVES), minZ + 15), (show ? plotBlock.createBlockData() : world.getBlockAt(x, world.getHighestBlockYAt(x, minZ + 15, HeightMap.MOTION_BLOCKING_NO_LEAVES), minZ + 15).getBlockData()));
                }
            }

            Chunk west = world.getChunkAt(chunkX - 1, chunkZ);
            if(FreeBuild.getInstance().getPlotManager().getPlotByChunk(west) == null) {
                for (int z = minZ; z < minZ + 16; z++) {
                    player.sendBlockChange(new Location(world, minX, world.getHighestBlockYAt(minX, z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z), (show ? plotBlock.createBlockData() : world.getBlockAt(minX, world.getHighestBlockYAt(minX, z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z).getBlockData()));
                }
            }

            Chunk east = world.getChunkAt(chunkX + 1, chunkZ);
            if(FreeBuild.getInstance().getPlotManager().getPlotByChunk(east) == null) {
                for (int z = minZ; z < minZ + 16; z++) {
                    player.sendBlockChange(new Location(world, minX + 15, world.getHighestBlockYAt(minX + 15, z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z), (show ? plotBlock.createBlockData() : world.getBlockAt(minX + 15, world.getHighestBlockYAt(minX + 15, z, HeightMap.MOTION_BLOCKING_NO_LEAVES), z).getBlockData()));
                }
            }


        });

    }

    public void removePlayer(FreeBuildPlayer freeBuildPlayer) {
        this.plotMembers.remove(freeBuildPlayer);
    }

    public UUID getPlotId() {
        return plotId;
    }

    public HashMap<PlotSetting, Boolean> getPlotSettings() {
        return plotSettings;
    }

    public Boolean getSettingState(PlotSetting plotSetting) {
        return this.plotSettings.get(plotSetting);
    }

    public void setSettingState(PlotSetting plotSetting) {
        this.plotSettings.put(plotSetting, !this.plotSettings.get(plotSetting));
    }

    public Material getPlotBlock() {
        return plotBlock;
    }

    public void setPlotBlock(Material plotBlock) {
        this.plotBlock = plotBlock;
    }

    public String getPlotName() {
        return plotName;
    }

    public void setPlotName(String plotName) {
        this.plotName = plotName;
    }

    public FreeBuildPlayer getPlotOwner() {
        return plotOwner;
    }

    public List<ChunkReference> getPlotChunks() {
        return plotChunks;
    }

    public HashMap<FreeBuildPlayer, PlotGroup> getPlotMembers() {
        return plotMembers;
    }
}
