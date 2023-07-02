package de.threeseconds.lib;

import de.threeseconds.FreeBuild;
import de.threeseconds.npc.NPC;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public class PlayerCam {

    private Location startLocation, endLocation;

    private int durationInTicks;

    private int tick;

    private BukkitTask bukkitTask;

    private List<Location> path;

    private Location playerLocation;
    private GameMode playerGamemode;
    private boolean playerFlying, playerAllowFlight;
    private Player player;

    public PlayerCam(Player player, Location startLocation, Location endLocation, int durationInTicks) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.durationInTicks = durationInTicks;

        this.path = new ArrayList<>();

        generatePath();

        this.player = player;
    }

    public void run() {

        this.tick = 1;

        this.playerFlying = player.isFlying();
        this.playerAllowFlight = player.getAllowFlight();
        this.playerGamemode = player.getGameMode();
        this.playerLocation = player.getLocation().clone();

        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(startLocation);
        player.setVelocity(path.get(1).toVector().subtract(startLocation.toVector()));

        Bukkit.getOnlinePlayers().forEach(target -> {
            if(target != player) target.hidePlayer(FreeBuild.getInstance().getPaperCore(), player);
        });

        bukkitTask = new BukkitRunnable() {

            @Override
            public void run() {
                if (!(tick >= durationInTicks) && !(tick == path.size() - 1)) {

                   player.teleport(path.get(tick));

                   Location current = path.get(tick);
                   Location next = path.get(tick+1);

                   player.setVelocity(new Vector(next.getX() - current.getX(), next.getY() - current.getY(), next.getZ() - current.getZ()));

                } else if(tick > durationInTicks) stop();

                tick++;
            }

        }.runTaskTimer(FreeBuild.getInstance().getPaperCore(), 1L, 1L);

    }

    public void stop() {
        bukkitTask.cancel();

        player.teleport(playerLocation);
        player.setGameMode(playerGamemode);
        player.setFlying(playerFlying);
        player.setAllowFlight(playerAllowFlight);

        Bukkit.getOnlinePlayers().forEach(target -> {
            if(target != player) target.showPlayer(FreeBuild.getInstance().getPaperCore(), player);
        });

    }

    public void generatePath(Location startLocation, Location endLocation, int durationInTicks) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.durationInTicks = durationInTicks;

        generatePath();
    }

    private void generatePath() {

        if(startLocation.getWorld() != endLocation.getWorld()) return;

        float stepYaw = (endLocation.getYaw() - startLocation.getYaw()) / durationInTicks;
        float stepPitch = (endLocation.getPitch() - startLocation.getPitch()) / durationInTicks;

        Location step = new Location(startLocation.getWorld(), (endLocation.getX() - startLocation.getX()) / durationInTicks, (endLocation.getY() - startLocation.getY()) / durationInTicks, (endLocation.getZ() - startLocation.getZ()) / durationInTicks, stepYaw, stepPitch);
        path.add(startLocation);

        for(int durationStep = 1; durationStep <= durationInTicks; durationStep++) {
            Location nextlocation = path.get(durationStep-1).clone().add(step);

            nextlocation.setYaw(nextlocation.getYaw() + stepYaw);
            nextlocation.setPitch(nextlocation.getPitch() + stepPitch);

            path.add(nextlocation);
        }

    }

}
