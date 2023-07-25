package de.threeseconds.util;

import de.threeseconds.FreeBuild;
import de.threeseconds.collections.CollectionPlayer;
import de.threeseconds.jobs.Job;
import de.threeseconds.jobs.JobPlayer;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotFilter;
import de.threeseconds.quest.Chapter;
import de.threeseconds.quest.Quest;
import de.threeseconds.scoreboard.GameScoreboard;
import de.threeseconds.stats.data.PlayerStats;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.text.NumberFormat;
import java.util.*;

public class FreeBuildPlayer {

    private Player player;
    private String uuid;
    private String userName;
    private Integer currentXP;
    private Integer freeBuildXP;
    private PlayerStats playerStats;
    private Integer maxStorage;
    private Component actionBar;
    private JobPlayer jobPlayer;
    private GameScoreboard gameScoreboard;
    private BossBar userBossBar;
    private Chapter currentChapter;
    private Quest currentQuest;
    private Location fastTravelLocation;
    private HashMap<Quest, Integer> currentQuestDialogeCount;
    private HashMap<Job, Map<Integer, Integer>> jobLevel;

    private Boolean canBuildInHub;

    private CollectionPlayer collectionPlayer;

    private HashMap<PlotFilter, Boolean> plotFilter;


    private Chunk chunk;

    private Plot lastPlot;


    public FreeBuildPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId().toString();
        this.userName = player.getName();
        this.currentXP = 0;
        this.freeBuildXP = 10000;

        this.playerStats = new PlayerStats(player, stats -> {

            stats.health(100D);
            stats.souls(50);
            stats.defense(0);
            stats.strength(0);
            stats.speed(1);
            stats.critChance(0.3);
            stats.critDamage(0.5);
        });

        this.maxStorage = 3;
        this.jobPlayer = new JobPlayer(this);
        this.actionBar = FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:#30CFD0:#926DD1>\uD83D\uDD25 " + this.getPlayerStats().souls() + "/50 Seelen</gradient>     <gradient:#AB2F41:#FF2B24>❤ " + (int)Math.round(this.getPlayerStats().health()) + "/100 Leben</gradient>     <gradient:#51E611:#48944D>\uD83D\uDEE1 " + this.getPlayerStats().defense() + "/10 Verteidigung</gradient>");
        this.gameScoreboard = null;
        this.userBossBar = null;
        this.fastTravelLocation = FreeBuild.getInstance().getHubLocation();
        this.currentQuestDialogeCount = new HashMap<>();
        this.jobLevel = new HashMap<>();

        this.canBuildInHub = false;

        this.collectionPlayer = new CollectionPlayer(this);

        this.chunk = null;

        this.lastPlot = null;

        this.plotFilter = new HashMap<>();
        for(PlotFilter plotFilters : PlotFilter.values()) {
            this.plotFilter.put(plotFilters, plotFilters.getDefaultFilter());
        }



        /*final int fixScaleAt = (int)Math.rint(this.getPlayerStats().health());
        final int fixScaleTo = (int)Math.rint(40d);
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (maxHealth >= fixScaleAt) {
            player.setHealthScale(fixScaleTo);
        }
        else {
            player.setHealthScale(maxHealth);
        }*/

        this.setHealth(player, (int)Math.rint(this.getPlayerStats().health()));
    }

    private void setHealth(Player player, int maxHealth) {
        if (maxHealth < 125) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);

        } else if (maxHealth < 165) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(22);

        } else if (maxHealth < 230) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(24);

        } else if (maxHealth < 300) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(26);

        } else if (maxHealth < 400) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(28);

        } else if (maxHealth < 500) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30);

        } else if (maxHealth < 650) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(32);

        } else if (maxHealth < 800) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(34);

        } else if (maxHealth < 1000) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(36);

        } else if (maxHealth < 1250) {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(38);

        } else {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(40);
        }

        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
    }

    public Integer getFreeBuildXP(Integer freeBuildLevel) {
        return (int) (1250 * Math.pow(freeBuildLevel + 1, 2) + 8750 * freeBuildLevel + 1);
    }

    public Integer getNeededFreeBuildXP(Integer freeBuildLevel) {
        return 10000 + 2500 * (freeBuildLevel + 1 - 2);
    }

    public Integer getFreeBuildLevel() {
        return (int) ((int) Math.sqrt(this.freeBuildXP / 1250 + 12.25) - 3.5);
    }

    public Integer getCurrentXP() {
        return currentXP;
    }

    public String getCompactXP(Integer freeBuildXP) {
        NumberFormat numberFormat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        numberFormat.setMinimumFractionDigits(1);

        return numberFormat.format(freeBuildXP).toLowerCase();
    }

    public CollectionPlayer getCollectionPlayer() {
        return collectionPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getMaxStorage() {
        return maxStorage;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PlayerStats getPlayerStats() {
        return playerStats;
    }

    public Component getActionBar() {
        return actionBar;
    }

    public void setActionBar(Component actionBar) {
        this.actionBar = actionBar;
    }

    public JobPlayer getJobPlayer() {
        return jobPlayer;
    }

    public GameScoreboard getGameScoreboard() {
        return gameScoreboard;
    }

    public void setGameScoreboard(GameScoreboard gameScoreboard) {
        this.gameScoreboard = gameScoreboard;
    }

    public BossBar getUserBossBar() {
        return userBossBar;
    }

    public void setUserBossBar(BossBar userBossBar) {
        this.userBossBar = userBossBar;
    }

    public Quest getCurrentQuest() {
        return currentQuest;
    }

    public void setCurrentQuest(Quest currentQuest) {
        this.currentQuest = currentQuest;
    }

    public Chapter getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(Chapter currentChapter) {
        this.currentChapter = currentChapter;
    }

    public Location getFastTravelLocation() {
        return fastTravelLocation;
    }

    public void setFastTravelLocation(Location fastTravelLocation) {
        this.fastTravelLocation = fastTravelLocation;
    }

    public HashMap<Quest, Integer> getCurrentQuestDialogeCount() {
        return currentQuestDialogeCount;
    }

    public void setCurrentQuestDialogeCount(HashMap<Quest, Integer> currentQuestDialogeCount) {
        this.currentQuestDialogeCount = currentQuestDialogeCount;
    }

    public Boolean canBuildInHub() {
        return canBuildInHub;
    }

    public void setCanBuildInHub(Boolean canBuildInHub) {
        this.canBuildInHub = canBuildInHub;
    }

    public HashMap<Job, Map<Integer, Integer>> getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(HashMap<Job, Map<Integer, Integer>> jobLevel) {
        this.jobLevel = jobLevel;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Plot getLastPlot() {
        return lastPlot;
    }

    public void setLastPlot(Plot lastPlot) {
        this.lastPlot = lastPlot;
    }

    public Boolean getPlotFilter(PlotFilter plotFilter) {
        return this.plotFilter.get(plotFilter);
    }

    public void changePlotFilter(PlotFilter plotFilter) {
        this.plotFilter.put(plotFilter, !this.plotFilter.get(plotFilter));
    }
}
