package de.threeseconds.stats.indicator;

import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IndicatorManager {

    private final Map<ArmorStand, Long> activeArmorStands;

    public IndicatorManager() {
        this.activeArmorStands = new HashMap<>();

        Bukkit.getScheduler().runTaskTimer(FreeBuild.getInstance().getPaperCore(), new ArmorStandTask(this), 0L, 1L);
    }


    @NotNull
    public Map<ArmorStand, Long> activeArmorStands() {
        return this.activeArmorStands;
    }
}
