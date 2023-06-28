package de.threeseconds.npc;

import de.threeseconds.FreeBuild;
import de.threeseconds.quest.Chapter;
import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class MainLobbyRunnable extends BukkitRunnable {

    public MainLobbyRunnable() {
        
    }

    @Override
    public void run() {

        /*  NPCs JUST FOR THE QUESTPLAYER VISIBLE
        Bukkit.getOnlinePlayers().forEach(player -> {
            FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

            NPC currentQuestNPC = freeBuildPlayer.getCurrentQuest().getQuestNPC();

            int health = (int) Math.round(freeBuildPlayer.getHealth());
            Integer souls = freeBuildPlayer.getSouls();
            Integer defense = freeBuildPlayer.getDefense();

            player.sendActionBar(FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:#30CFD0:#926DD1>\uD83D\uDD25 " + souls + "/50 Seelen</gradient>     <gradient:#AB2F41:#FF2B24>❤ " + health + "/100 Leben</gradient>     <gradient:#51E611:#48944D>\uD83D\uDEE1 " + defense + "/10 Verteidigung</gradient>"));


            if (player.getWorld().getName().equals("world")) {
                double distance = player.getLocation().distance(currentQuestNPC.getLocation());
                boolean isVisible = currentQuestNPC.getIsVisibleForPlayer().getOrDefault(player.getUniqueId(), false);

                if (distance > currentQuestNPC.getVisibiltyDistance() && isVisible) {

                    currentQuestNPC.remove(player);
                }
                else if (distance < currentQuestNPC.getVisibiltyDistance() && !isVisible) currentQuestNPC.spawn(player);

                if (isVisible && currentQuestNPC.isTurnToPlayer() && distance < currentQuestNPC.getTurnToDistance()) {
                    Location lookToLocation = player.getLocation().clone();
                    lookToLocation.setDirection(lookToLocation.subtract(currentQuestNPC.getLocation()).toVector());
                    currentQuestNPC.lookAt(player, lookToLocation);

                    if(currentQuestNPC.getNPC().getName().equals("Vera")) {
                        FreeBuild.getInstance().getQuestManager().showNextQuest(freeBuildPlayer, freeBuildPlayer.getCurrentQuest());
                        freeBuildPlayer.getGameScoreboard().setScore(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <red>Jobcenter"), 7);
                    }


                } else if (isVisible && currentQuestNPC.isTurnToPlayer() && distance > currentQuestNPC.getTurnToDistance()) {
                    currentQuestNPC.lookAt(player, currentQuestNPC.getLocation());
                    freeBuildPlayer.getGameScoreboard().setScore(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <blue>Hub"), 7);
                }



            }
        });

         */



        FreeBuild.getInstance().getNPCManager().getAllNPCs().forEach( npc -> Bukkit.getOnlinePlayers().forEach(player -> {

            FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);
            int health = (int) Math.round(freeBuildPlayer.getHealth());
            Integer souls = freeBuildPlayer.getSouls();
            Integer defense = freeBuildPlayer.getDefense();

            player.sendActionBar(FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:#30CFD0:#926DD1>\uD83D\uDD25 " + souls + "/50 Seelen</gradient>     <gradient:#AB2F41:#FF2B24>❤ " + health + "/100 Leben</gradient>     <gradient:#51E611:#48944D>\uD83D\uDEE1 " + defense + "/10 Verteidigung</gradient>"));



            if(player.getWorld().getName().equals("world")) {
                double distance = player.getLocation().distance(npc.getLocation());
                boolean isVisible = npc.getIsVisibleForPlayer().getOrDefault(player.getUniqueId(), false);

                if (distance > npc.getVisibiltyDistance() && isVisible) npc.remove(player);
                else if (distance < npc.getVisibiltyDistance() && !isVisible) npc.spawn(player);

                if (isVisible && npc.isTurnToPlayer() && distance < npc.getTurnToDistance()) {
                    Location lookToLocation = player.getLocation().clone();
                    lookToLocation.setDirection(lookToLocation.subtract(npc.getLocation()).toVector());
                    npc.lookAt(player, lookToLocation);
                } else if (isVisible && npc.isTurnToPlayer() && distance > npc.getTurnToDistance()) {

                    if(player.getLocation().getY() <= 15) {
                        if (freeBuildPlayer.getCurrentChapter() == Chapter.CHAPTER_TUT) {
                            player.teleport(FreeBuild.getInstance().getHubLocation());
                            player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                            player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du darfst die <blue>Hub <red>erst verlassen, nachdem du das Kapitel <green>" + Chapter.CHAPTER_TUT.getChapterName().replace(":", "") + " <red>abgeschlossen hast!"));

                            return;
                        }

                    }
                    npc.lookAt(player, npc.getLocation());
                }

            }

        }));


    }

}
