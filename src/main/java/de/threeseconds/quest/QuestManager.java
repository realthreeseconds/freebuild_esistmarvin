package de.threeseconds.quest;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.jobs.Job;
import de.threeseconds.npc.Hologram;
import de.threeseconds.scoreboard.GameScoreboard;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.title.Title;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.JedisPool;

import java.time.Duration;
import java.util.*;

public class QuestManager {
    private final List<Quest> questNPCList;
    private final HashMap<UUID, FreeBuildPlayer> freeBuildPlayers;

    public QuestManager() {
        this.questNPCList = new ArrayList<>();
        this.freeBuildPlayers = new HashMap<>();

        this.questNPCList.addAll(Arrays.asList(Quest.values()));

    }

    public void createFreeBuildPlayer(PlayerJoinEvent playerJoinEvent, Player player) {
        FreeBuildPlayer freeBuildPlayer = new FreeBuildPlayer(player);

        HashMap<Job, Map<Integer, Integer>> hashmap = freeBuildPlayer.getJobLevel();

        for(Job job : Job.values()) {
            hashmap.put(job, Map.ofEntries(Map.entry(16, 22671)));
        }

        freeBuildPlayer.setJobLevel(hashmap);

        GameScoreboard gameScoreboard = new GameScoreboard(player);
        freeBuildPlayer.setGameScoreboard(gameScoreboard);

        freeBuildPlayer.task(Task.Tutorial_0);

        this.freeBuildPlayers.put(player.getUniqueId(), freeBuildPlayer);
    }

    public void showNextQuest(FreeBuildPlayer freeBuildPlayer, Quest oldQuest) {
        Quest nextQuest = this.callQuest(freeBuildPlayer, oldQuest);
        System.out.println(oldQuest.getQuestNPC().getNPC().getName().getString());
        if(!oldQuest.getQuestNPC().getName().equals("Vera")) oldQuest.getQuestNPC().unregister();

        if(nextQuest == null) {

            freeBuildPlayer.setCurrentQuest(null);

            freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            freeBuildPlayer.getPlayer().hideBossBar(freeBuildPlayer.getUserBossBar());
            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>Du hast die Quest <green>'" + oldQuest.getQuestName() + "' <gray>des Kapitel <green>'" + this.getChapterByCurrentQuest(oldQuest).getChapterName().replace(":", "") + "' <gray>abgeschlossen."));

            return;
        }

        JedisPool jedisPool = new JedisPool("localhost", 6379);

        jedisPool.getResource().set(freeBuildPlayer.getPlayer().getUniqueId() + ":freebuild:currentQuest", nextQuest.name());
        freeBuildPlayer.setCurrentQuest(Quest.valueOf(jedisPool.getResource().get(freeBuildPlayer.getPlayer().getUniqueId() + ":freebuild:currentQuest")));
        freeBuildPlayer.getPlayer().hideBossBar(freeBuildPlayer.getUserBossBar());
        freeBuildPlayer.setUserBossBar(BossBar.bossBar(FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:white:red>Tutorial</gradient> " + freeBuildPlayer.getCurrentQuest().getQuestName()), 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_20));

        jedisPool.close();

        FreeBuild.getInstance().getNPCManager().getNPC(oldQuest.getQuestNPC().getName()).remove(freeBuildPlayer.getPlayer()).unregister();
        freeBuildPlayer.getPlayer().getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, oldQuest.getQuestNPC().getLocation(), 50, 0.1, 0.1, 0.1);

        freeBuildPlayer.getCurrentQuest().getQuestNPC().spawn(freeBuildPlayer.getPlayer());
        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        freeBuildPlayer.getPlayer().showBossBar(freeBuildPlayer.getUserBossBar());
        freeBuildPlayer.getPlayer().showTitle(Title.title(FreeBuild.getInstance().getMiniMessage().deserialize("<green>Neue Quest!"), FreeBuild.getInstance().getMiniMessage().deserialize("<white>" + nextQuest.getQuestName()), Title.Times.times(Duration.ofMillis(2000), Duration.ofMillis(2500), Duration.ofMillis(2000))));

        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>Du hast die Quest <green>'" + oldQuest.getQuestName() + "' <gray>des Kapitel <green>'Tutorial' <gray>abgeschlossen."));
    }

    private Quest callQuest(FreeBuildPlayer freeBuildPlayer, Quest oldQuest) {
        return Quest.QUEST_2;
    }

    public FreeBuildPlayer getFreeBuildPlayer(Player player) {
        return this.freeBuildPlayers.get(player.getUniqueId());
    }

    private void sendTitle(Player player) {
        ArrayList<String> animation = getTitleComponent();
        new BukkitRunnable(){
            int step = 0;
            public void run(){

                if(step+1 >= animation.size()) {
                    player.sendTitle(animation.get(step), "§7Willkommen, §f" + PermissionCenterModulePaper.getInstance().getUserManager().getUser(player.getUniqueId()).getDisplay().getColor() + player.getName() + "§7!", 0, 25, 25);
                    player.playSound(player, Sound.BLOCK_ANVIL_LAND, 3f, 3f);
                    cancel();
                    //titleWait.put(player.getUniqueId(), false);
                } else {
                    player.sendTitle(animation.get(step), "", 0, 25, 0);
                    player.playSound(player, Sound.ENTITY_CREEPER_HURT, 3f, 3f);
                }
                step++;
            }

        }.runTaskTimer(FreeBuild.getInstance().getPaperCore(), 10, 2);
    }

    private ArrayList<String> getTitleComponent() {
        ArrayList<String> list = new ArrayList<>();

        list.add("§8» ");
        list.add("§8» §fF");
        list.add("§8» §aF§fr");
        list.add("§8» §aFr§fe");
        list.add("§8» §aFre§fe");
        list.add("§8» §aFree§fB");
        list.add("§8» §aFreeB§fu");
        list.add("§8» §aFreeBuS§fi");
        list.add("§8» §aFreeBui§fl");
        list.add("§8» §aFreeBuil§fd");
        list.add("§8» §aFreeBuild");

        return list;
    }

    public String decodeColorcode(String colorCode) {
        String s = "";
        switch(colorCode) {
            case "§a" -> s = "<green>";
            case "§4" -> s = "<dark_red>";
        }
        return s;
    }

    public List<Quest> getQuestNPCList() {
        return questNPCList;
    }
}
