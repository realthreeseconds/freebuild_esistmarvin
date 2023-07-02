package de.threeseconds.lib;

import de.threeseconds.FreeBuild;
import de.threeseconds.quest.Chapter;
import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class MainLobbyRunnable extends BukkitRunnable {

    public MainLobbyRunnable() {
        
    }

    private HashMap<Player, Location> locations = new HashMap<>();

    @Override
    public void run() {

        Bukkit.getOnlinePlayers().forEach(player -> {
            FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);
            int health = (int) Math.round(freeBuildPlayer.getHealth());
            Integer souls = freeBuildPlayer.getSouls();
            Integer defense = freeBuildPlayer.getDefense();

            player.sendActionBar(FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:#30CFD0:#926DD1>\uD83D\uDD25 " + souls + "/50 Seelen</gradient>     <gradient:#AB2F41:#FF2B24>❤ " + health + "/100 Leben</gradient>     <gradient:#51E611:#48944D>\uD83D\uDEE1 " + defense + "/10 Verteidigung</gradient>"));

            if (player.getLocation().getY() <= 15) {
                player.teleport(FreeBuild.getInstance().getHubLocation());
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du darfst die <blue>Hub <red>erst verlassen, nachdem du das Kapitel <green>Tutorial <red>abgeschlossen hast!"));
            }

            if (freeBuildPlayer.getMission() == null && player.getLocation().distance(FreeBuild.getInstance().getHubLocation()) > 15) {
                FreeBuild.getInstance().getMissionManager().startTutorial(freeBuildPlayer);
            }

            if (!freeBuildPlayer.canMove()) {
                if (!locations.containsKey(player)) locations.put(player, player.getLocation().clone());

                Location from = locations.get(player);
                Location to = player.getLocation();

                if (from.getX() != to.getX() && from.getZ() != to.getZ())
                    player.teleport(new Location(from.getWorld(), from.getX(), to.getY(), from.getZ(), to.getYaw(), to.getPitch()));
            } else locations.remove(player);

            if (freeBuildPlayer.getNavigateLocation() != null) {
                FreeBuild.getInstance().getMissionManager().showDirectionArrow(player, freeBuildPlayer.getNavigateLocation());
                if (freeBuildPlayer.getNavigateLocation().distance(player.getLocation()) <= 10)
                    freeBuildPlayer.setNavigateLocation(null);
            }

            FreeBuild.getInstance().getNPCManager().getAllPlayerNPCs(player).forEach(npc -> {

                if(player.getWorld().getName().equals("world")) {
                    double distance = player.getLocation().distance(npc.getLocation());
                    if(distance > npc.getVisibiltyDistance() && npc.isVisible()) npc.remove();
                    else if(distance < npc.getVisibiltyDistance() && !npc.isVisible()) npc.spawn();

                    if (npc.isVisible() && npc.isTurnToPlayer() && distance < npc.getTurnToDistance()) {
                        Location lookToLocation = player.getLocation().clone();
                        lookToLocation.setDirection(lookToLocation.subtract(npc.getLocation()).toVector());
                        npc.lookAt(lookToLocation);
                    } else if (npc.isVisible() && npc.isTurnToPlayer() && distance > npc.getTurnToDistance())
                        npc.lookAt(npc.getLocation());
                }

            });

            FreeBuild.getInstance().getNPCManager().getAllNPCs().forEach(npc -> {

                if(player.getWorld().getName().equals("world")) {
                    double distance = player.getLocation().distance(npc.getLocation());
                    boolean isVisible = npc.getIsVisibleForPlayer().getOrDefault(player.getUniqueId(), false);

                    if (distance > npc.getVisibiltyDistance() && isVisible) npc.remove(player);
                    else if (distance < npc.getVisibiltyDistance() && !isVisible) npc.spawn(player);

                    if (isVisible && npc.isTurnToPlayer() && distance < npc.getTurnToDistance()) {
                        Location lookToLocation = player.getLocation().clone();
                        lookToLocation.setDirection(lookToLocation.subtract(npc.getLocation()).toVector());
                        npc.lookAt(player, lookToLocation);
                    } else if (isVisible && npc.isTurnToPlayer() && distance > npc.getTurnToDistance())
                        npc.lookAt(player, npc.getLocation());
                }

            });

        });

    }

}
