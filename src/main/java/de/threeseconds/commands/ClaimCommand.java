package de.threeseconds.commands;

import de.threeseconds.FreeBuild;
import de.threeseconds.listener.FreeBuildListener;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.map.MapMarker;
import de.threeseconds.plot.map.MapService;
import de.threeseconds.util.ChunkReference;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ClaimCommand extends Command {

    public static HashMap<FreeBuildPlayer, List<Chunk>> claimMap = new HashMap<>();
    private Map<FreeBuildPlayer, Chunk> chunkLocations = new HashMap<>();
    private Map<FreeBuildPlayer, BukkitTask> bukkitTaskMap = new HashMap<>();

    public ClaimCommand() {
        super("claim");
        FreeBuild.getInstance().getPaperCore().getServer().getCommandMap().register(FreeBuild.getInstance().getPaperCore().getName(), this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;


        if(!player.getWorld().getName().equals("world")) {
            Location spawnLocation = new Location(Bukkit.getWorld("freebuildWorld"), 0, 64, 0);
            FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);
            if(player.getLocation().distance(spawnLocation) >= 35) {

                if(claimMap.get(freeBuildPlayer) != null) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.setFlying(false);
                    player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                    Plot plot = new Plot(freeBuildPlayer, claimMap.get(freeBuildPlayer));
                    FreeBuild.getInstance().getPlotManager().addPlot(freeBuildPlayer, plot);


                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, player.getLocation().getY(), plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                    player.teleport(location);

                    player.sendMessage(MiniMessage.miniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast <green>erfolgreich <yellow>" + claimMap.get(freeBuildPlayer).size() + " Chunks <gray>auf dein Grundstück hinzugefügt."));

                    claimMap.values().forEach(chunks -> chunks.forEach(chunk -> FreeBuildListener.markedChunkBorder(player, chunk, null, false)));

                    claimMap.remove(freeBuildPlayer);


                    FreeBuild.getInstance().getPlotManager().getPlotByChunk(player.getChunk()).showBorder(player, true);


                    return false;
                }

                if(chunkLocations != null) chunkLocations = new HashMap<>();

                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
                player.teleport(player.getLocation().add(0, 25, 0));

                claimMap.put(freeBuildPlayer, null);

            } else {

                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du bist noch zu nah am Spawn, um den Chunk zu claimen."));
            }
            return false;
        }
        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst in der Hub keine Grundstücke claimen."));
        player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);

        return false;
    }



    private List<Block> chunkBorderBlocks(Chunk chunk) {

        List<Block> blocks = new ArrayList<>();

        World world = chunk.getWorld();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int maxX = minX + 14;
        int maxZ = minZ + 14;


        for (int x = minX; x <= maxX; x++) {
            Location first = new Location(world, x, world.getHighestBlockYAt(x, minZ), minZ);
            Location second = new Location(world, x, world.getHighestBlockYAt(x, maxZ), maxZ + 1);

            blocks.add(first.getBlock());
            blocks.add(second.getBlock());
        }

        for (int z = minZ; z <= maxZ; z++) {
            Location first = new Location(world, minX, world.getHighestBlockYAt(minX, z), z);
            Location second = new Location(world, maxX + 1, world.getHighestBlockYAt(maxX, z), z);

            blocks.add(first.getBlock());
            blocks.add(second.getBlock());

        }

        return blocks;
    }

}
