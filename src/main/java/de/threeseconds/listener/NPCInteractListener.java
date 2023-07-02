package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.npc.PlayerHologram;
import de.threeseconds.npc.PlayerNPC;
import de.threeseconds.npc.event.NonPlayerCharacterInteractEvent;
import de.threeseconds.npc.event.PlayerNonPlayerCharacterInteractEvent;
import de.threeseconds.quest.Task;
import de.threeseconds.quest.event.DialogCallback;
import de.threeseconds.quest.event.PlayerDialogEvent;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class NPCInteractListener implements Listener {

    public NPCInteractListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onDialog(PlayerDialogEvent event) {
        Player player = event.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(event.getTask() == Task.Tutorial_1 && event.getMessageId() == 5) {
            FreeBuild.getInstance().getNPCManager().getPlayerNPC(player, Task.Tutorial_1.getNPCName()).getLinkedPlayerHologram().setLines("<yellow>Caleb<br><grey>Einwohner").refreshEntityData();
        }

    }

    @EventHandler
    public void onInteract(NonPlayerCharacterInteractEvent event) {
        Player player = event.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

    }

    @EventHandler
    public void onPlayerInteract(PlayerNonPlayerCharacterInteractEvent event) {
        Player player = event.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(event.getPlayerNPC().getName().equals("npcTutorial2")) {
            if(freeBuildPlayer.isInDialog()) return;

            freeBuildPlayer.setCanMove(false);
            Task.Tutorial_2.playDialog(player, () -> {
                freeBuildPlayer.setCanMove(true);
            });
        }

        if(event.getPlayerNPC().getName().equals("npcTutorial")) {

            if(freeBuildPlayer.isInDialog()) return;

            if(freeBuildPlayer.getTask() == Task.Tutorial_1) {
                freeBuildPlayer.setCanMove(false);
                Task.Tutorial_1.playDialog(player, () -> {
                    freeBuildPlayer.setCanMove(true);
                    freeBuildPlayer.task(Task.Tutorial_2);

                    PlayerNPC playerNPC = new PlayerNPC(player, "npcTutorial2", new Location(Bukkit.getWorld("world"), 20.5, 70, 48.5, -25.5f, 0f)).register();
                    playerNPC.setSkin("ewogICJ0aW1lc3RhbXAiIDogMTY1MjM0Nzg3OTQ2MSwKICAicHJvZmlsZUlkIiA6ICJmMjc0YzRkNjI1MDQ0ZTQxOGVmYmYwNmM3NWIyMDIxMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJIeXBpZ3NlbCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84ODE4NjVhYjY1ODg5ODAxMzUxODM3MGE1Mjk4Y2VhY2M3N2U1OWRhNmJjNjA4YTA4OTgwNzg4ZjFhZTg4ZjBhIgogICAgfQogIH0KfQ==", "mt94F/IL37wLi4gOgIeBRDWseZ7uCkC8nE3lqc45arVGhUMh17L79TS0q/JHzlR72EXPQW3mTcmfpp2nMPTkOmwvX6wQ/6PDKqozKU6UCzb56BBS4fftTZf0xHzd78nmJP1lTCjffA1mOj+TqNzd5iTOEH1mdRexu8tKSpZKeV0bos4T/BtOcDLG+ufU9IayobnpkLsrW9JGGy169ZaqlYyJ6cExnSFWrVjrC6T+0QEiQPYH2+Wu4BNaUNQh+SfHBTQTX21VKUPB5eeX78upCCZlLpccJWC+Kkhe6kc3Qo0m+6MgNysZmL0tWrbF6LRvIlOpR9PzS+XgMaSzNbeIQIv6ONeOdWWX3tB8ylK9fGKCGTznI7v9nuoQqsnNeo4F4nQ1t98qm26vpPMwNZnBCGBKGGyBSQ0NMsRTWF2yvhr2eREczvV6ePF31R/vv5X/2MhOt7yTN6KHzuCmHHMRj2BzyHLS5WogapUNRXY9bIkpUruqWO2BiRGOk+pR9R+wACBv2tz62PgX1ffbnFDbG0+YXOA2dq6Fo+bi7YtvXhqBSW8jXAc6f0ss4MmZfQcdSRlGyfUHkdjbrjBs1w9ZV8ux8G2ks+zNKVhisy1EHIVvoDGSOiXNL8xs+lhjhvzCG+HnOvBvj5BHqnrK8jTDGkOGYXFLclB9NYCZhQeo1E4=").create();

                    playerNPC.setTurnToPlayer(true);
                    playerNPC.linkPlayerHologram(new PlayerHologram(player.getPlayer(), "holoTutorial2", playerNPC, "<gold>James<reset><br><grey>Schatzmeister").register().create());

                });
            }

        }

    }

}
