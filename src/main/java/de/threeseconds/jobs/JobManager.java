package de.threeseconds.jobs;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.units.qual.C;

import java.text.CompactNumberFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JobManager {

    private HashMap<FreeBuildPlayer, BukkitTask> task;

    private final String[] compactPatterns
            = {"", "", "", "0k", "00k", "000k", "0m", "00m", "000m"};

    private final DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.GERMANY);

    private final CompactNumberFormat compactNumberFormat = new CompactNumberFormat(this.decimalFormat.toPattern(), this.decimalFormat.getDecimalFormatSymbols(), this.compactPatterns);

    public JobManager() {
        this.task = new HashMap<>();
    }

    public void displayXP(FreeBuildPlayer freeBuildPlayer, Job job, Integer xp) {
        this.grantXP(freeBuildPlayer, job, xp);

        new BukkitRunnable() {
            @Override
            public void run() {
                Component component = FreeBuild.getInstance().getMiniMessage().deserialize("<gray>+" + xp + " XP (" + freeBuildPlayer.getJobPlayer().getCompactXPByJob(job) + "/" + freeBuildPlayer.getJobPlayer().getCompactMaxXPByLevel(freeBuildPlayer.getJobPlayer().getLevelByJob(job)) + ") (" + job.getJobName() + ")");

                freeBuildPlayer.setActionBar(component);

                if(task.get(freeBuildPlayer) == null) {
                    task.put(freeBuildPlayer, new BukkitRunnable() {
                        @Override
                        public void run() {

                            Component component = FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:#30CFD0:#926DD1>\uD83D\uDD25 " + freeBuildPlayer.getPlayerStats().souls() + "/50 Seelen</gradient>     <gradient:#AB2F41:#FF2B24>❤ " + (int)Math.round(freeBuildPlayer.getPlayerStats().health()) + "/100 Leben</gradient>     <gradient:#51E611:#48944D>\uD83D\uDEE1 " + freeBuildPlayer.getPlayerStats().defense() + "/10 Verteidigung</gradient>");

                            freeBuildPlayer.setActionBar(component);
                            task.remove(freeBuildPlayer);
                        }
                    }.runTaskLater(FreeBuild.getInstance().getPaperCore(), 40L));
                }


            }
        }.runTask(FreeBuild.getInstance().getPaperCore());



    }

    private void grantXP(FreeBuildPlayer freeBuildPlayer, Job job, Integer xp) {
        freeBuildPlayer.getJobPlayer().addXPToJob(job, xp);
        if(freeBuildPlayer.getJobPlayer().getXPByJob(job) >= freeBuildPlayer.getJobPlayer().getMaxXPByLevel(freeBuildPlayer.getJobPlayer().getLevelByJob(job))) {
            freeBuildPlayer.getJobPlayer().addLevelByJob(job);
            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast <green>Level " + (freeBuildPlayer.getJobPlayer().getLevelByJob(job) - 1) + " <gray>in <yellow>" + job.getJobName() + " <gray>abgeschlossen."));
        }
    }

    public String calculateProgress(Integer currentXP, Integer maxXP) {
        String emptyString = "                         ";
        if(currentXP >= maxXP) {
            return "<green>" + emptyString;
        }

        double calculation = (double)currentXP / maxXP * (emptyString.length());
        String greenString = emptyString.substring(0, (int)calculation);
        return greenString + "<gray>" + StringUtils.repeat(" ", emptyString.length()-(int)calculation);
    }

}
