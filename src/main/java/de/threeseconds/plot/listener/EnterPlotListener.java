package de.threeseconds.plot.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotGroup;
import de.threeseconds.plot.PlotSetting;
import de.threeseconds.util.ChunkReference;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.net.http.WebSocket;

public class EnterPlotListener implements Listener {

    public EnterPlotListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onEnter(PlayerEnterPlotEvent playerEnterPlotEvent) {
        Player player = playerEnterPlotEvent.getPlayer();
        FreeBuildPlayer freeBuildPlayer = playerEnterPlotEvent.getFreeBuildPlayer();

        Plot enteredPlot = playerEnterPlotEvent.getEnteredPlot();
        Plot lastPlot = playerEnterPlotEvent.getLastPlot();
        if(enteredPlot == null) {
            if(lastPlot == null) {
                return;
            }

            lastPlot.showBorder(freeBuildPlayer.getPlayer(), false);
            lastPlot.leave(freeBuildPlayer);
            return;
        }

        if(!enteredPlot.getSettingState(PlotSetting.JOINABLE)) {
            if(!enteredPlot.getPlotMembers().containsKey(freeBuildPlayer)) {
                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                player.sendMessage(playerEnterPlotEvent.getCancelMessage());
                playerEnterPlotEvent.setCancelled(true);
                return;
            }
        }

        enteredPlot.enter(lastPlot, freeBuildPlayer);

    }

}
