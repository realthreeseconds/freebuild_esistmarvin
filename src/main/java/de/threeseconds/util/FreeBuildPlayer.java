package de.threeseconds.util;

import de.threeseconds.FreeBuild;
import de.threeseconds.job.Jobs;
import de.threeseconds.listener.JoinListener;
import de.threeseconds.quest.Chapter;
import de.threeseconds.quest.Quest;
import de.threeseconds.scoreboard.GameScoreboard;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class FreeBuildPlayer {

    private Player player;
    private String uuid;
    private String userName;
    private Integer souls;
    private Double health;
    private Integer defense;
    private double strength;
    private Integer speed;
    private double critChance;
    private double critDamage;
    private Component actionBar;
    private Jobs job;
    private GameScoreboard gameScoreboard;
    private BossBar userBossBar;
    private Chapter currentChapter;
    private Quest currentQuest;
    private Location fastTravelLocation;
    private HashMap<Quest, Integer> currentQuestDialogeCount;
    private HashMap<Jobs, Map<Integer, Integer>> jobLevel;


    public FreeBuildPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId().toString();
        this.userName = player.getName();
        this.souls = 50;
        this.health = 100D;
        this.defense = 10;
        this.strength = 10;
        this.speed = 1;
        this.critChance = 0.5;
        this.critDamage = 1.0;
        this.actionBar = FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:#30CFD0:#926DD1>\uD83D\uDD25 " + this.souls + "/50 Seelen</gradient>     <gradient:#AB2F41:#FF2B24>❤ " + (int)Math.round(this.health) + "/100 Leben</gradient>     <gradient:#51E611:#48944D>\uD83D\uDEE1 " + this.defense + "/10 Verteidigung</gradient>");
        this.job = null;
        this.gameScoreboard = null;
        this.userBossBar = null;
        this.fastTravelLocation = FreeBuild.getInstance().getHubLocation();
        this.currentQuestDialogeCount = new HashMap<>();
        this.jobLevel = new HashMap<>();


        final int fixScaleAt = (int)Math.rint(100d);
        final int fixScaleTo = (int)Math.rint(40d);
        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        if (maxHealth >= fixScaleAt) {
            player.setHealthScale(fixScaleTo);
        }
        else {
            player.setHealthScale(maxHealth);
        }
    }

    public Player getPlayer() {
        return player;
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

    public Integer getSouls() {
        return souls;
    }

    public void setSouls(Integer souls) {
        this.souls = souls;
    }

    public Double getHealth() {
        return health;
    }

    public void setHealth(Double health) {
        this.health = health;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Double getStrength() {
        return strength;
    }

    public void setStrength(double strength) {
        this.strength = strength;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public double getCritChance() {
        return critChance;
    }

    public void setCritChance(double critChance) {
        this.critChance = critChance;
    }

    public double getCritDamage() {
        return critDamage;
    }

    public void setCritDamage(double critDamage) {
        this.critDamage = critDamage;
    }

    public Component getActionBar() {
        return actionBar;
    }

    public void setActionBar(Component actionBar) {
        this.actionBar = actionBar;
    }

    public Jobs getJob() {
        return job;
    }

    public Jobs getJob(Jobs jobs) {
        return jobs;
    }

    public void setJob(Jobs job) {
        this.job = job;
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

    public HashMap<Jobs, Map<Integer, Integer>> getJobLevel() {
        return jobLevel;
    }

    public void setJobLevel(HashMap<Jobs, Map<Integer, Integer>> jobLevel) {
        this.jobLevel = jobLevel;
    }
}
