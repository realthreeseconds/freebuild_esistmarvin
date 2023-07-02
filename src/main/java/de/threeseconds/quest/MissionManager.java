package de.threeseconds.quest;

import de.threeseconds.FreeBuild;
import de.threeseconds.lib.PlayerCam;
import de.threeseconds.npc.Hologram;
import de.threeseconds.npc.NPC;
import de.threeseconds.npc.PlayerHologram;
import de.threeseconds.npc.PlayerNPC;
import de.threeseconds.quest.event.DialogCallback;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.time.Duration;
import java.util.ArrayList;

public class MissionManager {

    private final FreeBuild module;

    public MissionManager(final FreeBuild module) {
        this.module = module;
    }

    public void startTutorial(FreeBuildPlayer player) {

        player.setMission(Mission.Tutorial);

        PlayerNPC npc = new PlayerNPC(player.getPlayer(), "npcTutorial", new Location(Bukkit.getWorld("world"), 74.5, 59, -25.5, 60.5f, 0f)).register();
        npc.setSkin("ewogICJ0aW1lc3RhbXAiIDogMTYxNTI5MzUyODQwNSwKICAicHJvZmlsZUlkIiA6ICJjZGM5MzQ0NDAzODM0ZDdkYmRmOWUyMmVjZmM5MzBiZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJSYXdMb2JzdGVycyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS83YmQwZWM2ZDQwZmMxZDM2OWMwODZhMTA3MWQ3ZmRjMTMwZGZjMDU2ZjA5YmExMTIxMmJiNWFjYzUxMDQ0ZmFlIgogICAgfQogIH0KfQ==", "QmsNpfF1yFPoFoO+QzNr/AfZdMAK7jR0T07VbU1h+GzalPuVY5WiIXLwr/fhM7jjiQ/i8pTXrZCLxh1QkAS6Ka2jLTO6DJFh0m7vAJf52elAmvqUXuxihrZLq+Xqh9OSeV1gnKQrBdtPfr+m+VQX7OoZf5HNUeO9uvaHMh8zTI8rSc6gc4aK5KhsVcaDS36SvSREyIDBRtFKrKZClbs9KDkoSGpLWv5INE/IpPRUPKNPllPzN8T38bix5b9Uw8prWVTsej7RvaXdiXU5qjw6JAPKuID3H9CgF+6iHq/BWdJUfuJtiPLS1UcyjOEjV5j/SqIAUEA0kqqiGztjyunVsJrnMEJbaIMGAmOVtcJkomGWpclOGGla2gWbbS9b0HKVDK9ImDElesP8Xxr2NeDTPvnx3i6SzOsWSGY/AaAwGYYYFdXMQdzeYs1U3IkMgXL61iy616PJb+SbNzUlEhpwf1pwGeVMIwCsCu6ciyjblJD/crPbJk8jmcNt/3edK8bP92l+G1WNPqLAHSZpZVo0hC1aduMeouzL0let7zEeanqg7teerRaWSFfcL96+T3ML7nYhOTSbzPSUy3OkJJhGINtQQtzpe76qfkznremGZnApD5cIuz0qK0yIN7aZ2GGVJFGHO5SFtZjyfEcdacKqoMxIPacMhyr/E1IcaAyz8h4=").create();

        Task.Tutorial_0.playDialog(player.getPlayer(), () -> {
            player.task(Task.Tutorial_1);
            player.setNavigateLocation(npc.getLocation());
            npc.setTurnToPlayer(true);
        });

        npc.setTurnToPlayer(false);
        npc.linkPlayerHologram(new PlayerHologram(player.getPlayer(), "holoTutorial", npc, "<i><yellow>Unbekannt<reset>").register().create());
        npc.spawn();

        PlayerCam cam = new PlayerCam(player.getPlayer(), new Location(Bukkit.getWorld("world"), 65, 63, -29, -66.5f, 16.5f), new Location(Bukkit.getWorld("world"), 71, 62, -18, -156f, 19f), 125);
        cam.run();

    }

    public void showDirectionArrow(Player player, Location location) {

        Vector locVector = location.toVector().subtract(player.getLocation().toVector());
        double locAngle = Math.atan2(locVector.getZ(), locVector.getX());
        double playerAngle = Math.atan2(player.getLocation().getDirection().getZ(), player.getLocation().getDirection().getX());
        double angle = playerAngle - locAngle;

        while (angle > Math.PI) {
            angle = angle - 2 * Math.PI;
        }

        while (angle < -Math.PI) {
            angle = angle + 2 * Math.PI;
        }

        String direction = null;
        String color = null;

        if (angle < -2.749 || angle >= 2.749) // -7/8 pi
            direction = "⬇";
        else if (angle < -1.963) // -5/8 pi
            direction = "⬊";
        else if (angle < -1.178) // -3/8 pi
            direction = "➡";
        else if (angle < -0.393) // -1/8 pi
            direction = "⬈";
        else if (angle < 0.393) // 1/8 pi
            direction = "⬆";
        else if (angle < 1.178) // 3/8 pi
            direction = "⬉";
        else if (angle < 1.963) // 5/8 p
            direction = "⬅";
        else if (angle < 2.749) // 7/8 pi
            direction = "⬋";

        double distance = player.getLocation().distance(location);

        if(distance > 85) color = "<dark_red>";
        else if(distance > 55) color = "<red>";
        else if(distance > 30) color = "<yellow>";
        else color = "<green>";

        player.showTitle(Title.title(Component.empty(), FreeBuild.getInstance().getMiniMessage().deserialize(color + direction), Title.Times.times(Duration.ofMillis(0), Duration.ofMillis(500), Duration.ofMillis(0))));
    }

}
