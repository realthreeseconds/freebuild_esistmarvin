package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.jobs.JobInventories;
import de.threeseconds.npc.NonPlayerCharacterInteractEvent;
import de.threeseconds.quest.Quest;
import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.*;

public class NPCInteractListener implements Listener {

    public NPCInteractListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onInteract(NonPlayerCharacterInteractEvent nonPlayerCharacterInteractEvent) {
        Player player = nonPlayerCharacterInteractEvent.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(freeBuildPlayer.getCurrentQuest() != null) {
            if(nonPlayerCharacterInteractEvent.getNPC() != freeBuildPlayer.getCurrentQuest().getQuestNPC()) {
                player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast diese Quest bereits abgeschlossen!"));
                return;
            }

            if(nonPlayerCharacterInteractEvent.getNPC().getName().equals(freeBuildPlayer.getCurrentQuest().getQuestNPC().getName()) && nonPlayerCharacterInteractEvent.getActionType().equals(NonPlayerCharacterInteractEvent.ActionType.INTERACT)) {

                Quest currentQuest = freeBuildPlayer.getCurrentQuest();

                if (currentQuest.getQuestDialoge() != null) {
                    HashMap<Quest, Integer> currentQuestDialogeCount = freeBuildPlayer.getCurrentQuestDialogeCount();

                    currentQuestDialogeCount.put(currentQuest, (currentQuestDialogeCount.get(currentQuest) != null ? currentQuestDialogeCount.get(currentQuest) + 1 : 0));

                    if (currentQuestDialogeCount.get(currentQuest) == currentQuest.getQuestDialoge().size()) {

                        FreeBuild.getInstance().getQuestManager().showNextQuest(freeBuildPlayer, currentQuest);

                    } else {

                        player.playSound(player, Sound.ITEM_TRIDENT_RETURN, 1, 1);

                        player.sendMessage("");
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(currentQuest.getQuestDialoge().get(currentQuestDialogeCount.get(currentQuest))));
                        player.sendMessage("");
                    }
                } else {
                    FreeBuild.getInstance().getQuestManager().showNextQuest(freeBuildPlayer, currentQuest);
                    if(currentQuest.getQuestNPC().getNPC().getName().equals("Vera")) {
                        player.playSound(player, Sound.BLOCK_BARREL_OPEN, 1, 1);
                        new JobInventories.DefaultInventory(freeBuildPlayer).open(player);
                        //player.openInventory(FreeBuild.getInstance().getJobManager().openJobsInventory(player));
                    }
                }
            }
            return;
        }

        if(nonPlayerCharacterInteractEvent.getNPC().getName().equals("Vera") && nonPlayerCharacterInteractEvent.getActionType().equals(NonPlayerCharacterInteractEvent.ActionType.INTERACT)) {

            player.playSound(player, Sound.BLOCK_BARREL_OPEN, 1, 1);
            new JobInventories.DefaultInventory(freeBuildPlayer).open(player);
            //player.openInventory(FreeBuild.getInstance().getJobManager().openJobsInventory(player));

            return;
        }

        if(nonPlayerCharacterInteractEvent.getNPC().getName().equals("Vera") && nonPlayerCharacterInteractEvent.getActionType().equals(NonPlayerCharacterInteractEvent.ActionType.ATTACK)) {

            if (player.getItemInHand().getType() == Material.NETHER_STAR) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast den <green>Fast Travel <gray>zu <gold>" + nonPlayerCharacterInteractEvent.getNPC().getName() + " <gray>gesetzt."));
                freeBuildPlayer.setFastTravelLocation(player.getLocation());
                return;
            }

            return;
        }

    }
}
