package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.npc.manager.PacketReader;
import de.threeseconds.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinListener implements Listener {

    public JoinListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        new PacketReader(player).inject();

        playerJoinEvent.joinMessage(null);

        player.teleport(FreeBuild.getInstance().getHubLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).setKey("menu-item").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")).getItemStack());

        FreeBuild.getInstance().getQuestManager().createFreeBuildPlayer(playerJoinEvent, player);

        //showArrow(player);

    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        event.quitMessage(null);
    }

    /*
    public static void showArrow(Player player) {
        new BukkitRunnable() {
            @Override
            public void run() {

                if(player.getWorld().getName().equals("world")) {
                    if(!titleWait.get(player.getUniqueId())) {
                        //if(player.getLocation().distance(plugin.currentNPCLocation.get(player.getUniqueId())) > 5D) {

                        //}
                        directionArrow(player, plugin.currentNPCLocation.get(player.getUniqueId()));
                    }
                } else this.cancel();


            }
        }.runTaskTimerAsynchronously(plugin.getPaperCore(), 0L, 1L);
    }



    private static void directionArrow(Player player, Location npcLocation) {
        Location playerLocation = player.getLocation();

        Vector locVector = npcLocation.toVector().subtract(playerLocation.toVector());

        String direction = null;

        double locAngle = Math.atan2(locVector.getZ(), locVector.getX());
        double playerAngle = Math.atan2(playerLocation.getDirection().getZ(), playerLocation.getDirection().getX());

        double angle = playerAngle - locAngle;

        while (angle > Math.PI) {
            angle = angle - 2 * Math.PI;
        }

        while (angle < -Math.PI) {
            angle = angle + 2 * Math.PI;
        }

        if (angle < -2.749 || angle >= 2.749) { // -7/8 pi
            direction = "⬇";
        } else if (angle < -1.963) { // -5/8 pi
            direction = "⬊";
        } else if (angle < -1.178) { // -3/8 pi
            direction = "➡";
        } else if (angle < -0.393) { // -1/8 pi
            direction = "⬈";
        } else if (angle < 0.393) { // 1/8 pi
            direction = "⬆";
        } else if (angle < 1.178) { // 3/8 pi
            direction = "⬉";
        } else if (angle < 1.963) { // 5/8 p
            direction = "⬅";
        } else if (angle < 2.749) { // 7/8 pi
            direction = "⬋";
        }

        MiniMessage miniMessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.newline())
                        .build()
                )
                .build();

        Component component = miniMessage.deserialize("<aqua>" + direction);

        //this.bossBar.name(component);

        player.showTitle(Title.title(Component.text(""), component, Title.Times.times(Duration.ofMillis(0), Duration.ofMillis(500), Duration.ofMillis(0))));
    }
    */




}
