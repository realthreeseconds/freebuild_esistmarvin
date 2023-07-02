package de.threeseconds.jobs;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class JobManager {

    public JobManager() {

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
