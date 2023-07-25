package de.threeseconds.quest;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.jobs.Job;
import de.threeseconds.npc.Hologram;
import de.threeseconds.scoreboard.GameScoreboard;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
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
        this.questNPCList.forEach(questNPC -> {
            questNPC.getQuestNPC().create();
            questNPC.getQuestNPC().linkHologram(new Hologram(questNPC.getQuestNPC().getName(), questNPC.getQuestNPC(), new ArrayList<>(Arrays.asList("<white>0/" + questNPC.getQuestDialoge().size(), "<gold>" + questNPC.getQuestNPC().getName(), "<gray>Rechtsklick"))));
        });
    }

    public void createFreeBuildPlayer(PlayerJoinEvent playerJoinEvent, Player player) {
        FreeBuildPlayer freeBuildPlayer;
        if(this.getFreeBuildPlayers().containsKey(player.getUniqueId())) freeBuildPlayer = this.getFreeBuildPlayers().get(player.getUniqueId());
        else freeBuildPlayer = new FreeBuildPlayer(player);

        JedisPool jedisPool = new JedisPool("localhost", 6379);
        if(!jedisPool.getResource().exists(player.getUniqueId() + ":freebuild")) {
            jedisPool.getResource().set(player.getUniqueId() + ":freebuild", "true");

            //playerJoinEvent.joinMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + this.decodeColorcode(PermissionCenterModulePaper.getInstance().getUserManager().getUser(player.getUniqueId()).getDisplay().getColor()) + player.getName() + " <gray>ist neu auf FreeBuild."));

            freeBuildPlayer.setCurrentChapter(Chapter.CHAPTER_TUT);
            freeBuildPlayer.setCurrentQuest(freeBuildPlayer.getCurrentChapter().getChapterQuestList().get(0));

            jedisPool.getResource().set(player.getUniqueId() + ":freebuild:currentChapter", freeBuildPlayer.getCurrentChapter().name());
            jedisPool.getResource().set(player.getUniqueId() + ":freebuild:currentQuest", freeBuildPlayer.getCurrentQuest().name());

            player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            player.showTitle(Title.title(FreeBuild.getInstance().getMiniMessage().deserialize("<green>Neues Kapitel"), FreeBuild.getInstance().getMiniMessage().deserialize("<white>" + freeBuildPlayer.getCurrentChapter().getChapterName()), Title.Times.times(Duration.ofMillis(2000), Duration.ofMillis(2500), Duration.ofMillis(2000))));

            freeBuildPlayer.setUserBossBar(BossBar.bossBar(FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:white:red>" + freeBuildPlayer.getCurrentChapter().getChapterName() + "</gradient> " + freeBuildPlayer.getCurrentQuest().getQuestName()), 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_20));

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.showTitle(Title.title(FreeBuild.getInstance().getMiniMessage().deserialize("<green>Neue Quest"), FreeBuild.getInstance().getMiniMessage().deserialize(freeBuildPlayer.getCurrentQuest().getQuestName()), Title.Times.times(Duration.ofMillis(2000), Duration.ofMillis(2500), Duration.ofMillis(2000))));
                }
            }.runTaskLater((Plugin) FreeBuild.getInstance().getPaperCore(), 50L);
        } else {
            /*
            freeBuildPlayer.setCurrentChapter(Chapter.valueOf(jedisPool.getResource().get(player.getUniqueId() + ":freebuild:currentChapter")));
            freeBuildPlayer.setCurrentQuest(Quest.valueOf(jedisPool.getResource().get(player.getUniqueId() + ":freebuild:currentQuest")));
            freeBuildPlayer.setUserBossBar(BossBar.bossBar(FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:white:red>" + freeBuildPlayer.getCurrentChapter().getChapterName() + "</gradient> " + freeBuildPlayer.getCurrentQuest().getQuestName()), 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_20));

            //freeBuildPlayer.getCurrentQuest().getQuestNPC().spawn(player);

            for(Quest q : Quest.values()) {
                if(q.getQuestNPC() == freeBuildPlayer.getCurrentQuest().getQuestNPC()) q.getQuestNPC().spawn(player);
                else q.getQuestNPC().unregister();
            }



             */

            HashMap<Job, Map<Integer, Integer>> hashmap = freeBuildPlayer.getJobLevel();

            for(Job job : Job.values()) {
                hashmap.put(job, Map.ofEntries(Map.entry(16, 22671)));
            }

            freeBuildPlayer.setJobLevel(hashmap);
        }

        jedisPool.close();

        GameScoreboard gameScoreboard = new GameScoreboard(player);
        freeBuildPlayer.setGameScoreboard(gameScoreboard);



        this.freeBuildPlayers.put(player.getUniqueId(), freeBuildPlayer);
    }

    public void removeFreebuildPlayer(Player player) {
        this.freeBuildPlayers.remove(player.getUniqueId());
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
        freeBuildPlayer.setUserBossBar(BossBar.bossBar(FreeBuild.getInstance().getMiniMessage().deserialize("<gradient:white:red>" + freeBuildPlayer.getCurrentChapter().getChapterName() + "</gradient> " + freeBuildPlayer.getCurrentQuest().getQuestName()), 1, BossBar.Color.GREEN, BossBar.Overlay.NOTCHED_20));

        jedisPool.close();

        FreeBuild.getInstance().getNPCManager().getNPC(oldQuest.getQuestNPC().getName()).remove(freeBuildPlayer.getPlayer()).unregister();
        freeBuildPlayer.getPlayer().getLocation().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, oldQuest.getQuestNPC().getLocation(), 50, 0.1, 0.1, 0.1);

        freeBuildPlayer.getCurrentQuest().getQuestNPC().spawn(freeBuildPlayer.getPlayer());
        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
        freeBuildPlayer.getPlayer().showBossBar(freeBuildPlayer.getUserBossBar());
        freeBuildPlayer.getPlayer().showTitle(Title.title(FreeBuild.getInstance().getMiniMessage().deserialize("<green>Neue Quest!"), FreeBuild.getInstance().getMiniMessage().deserialize("<white>" + nextQuest.getQuestName()), Title.Times.times(Duration.ofMillis(2000), Duration.ofMillis(2500), Duration.ofMillis(2000))));

        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>Du hast die Quest <green>'" + oldQuest.getQuestName() + "' <gray>des Kapitel <green>'" + this.getChapterByCurrentQuest(oldQuest).getChapterName().replace(":", "") + "' <gray>abgeschlossen."));
    }

    private Quest callQuest(FreeBuildPlayer freeBuildPlayer, Quest oldQuest) {
        Iterator<Quest> questIterator = freeBuildPlayer.getCurrentChapter().getChapterQuestList().listIterator();

        while(questIterator.hasNext()) {
            if(questIterator.next() == oldQuest) {
                if(!questIterator.hasNext() || questIterator.next() == null) {
                    return null;
                }
                return questIterator.next();
            }
        }
        return null;
    }

    private Chapter getChapterByCurrentQuest(Quest quest) {
        for (Chapter chapter : Chapter.values()) {
            if(chapter.getChapterQuestList().contains(quest)) return chapter;
        }
        return null;
    }

    public FreeBuildPlayer getFreeBuildPlayer(Player player) {
        return this.freeBuildPlayers.get(player.getUniqueId());
    }

    public void sendTitle(Player player) {
        ArrayList<String> animation = this.getTitleComponent();
        new BukkitRunnable(){
            int step = 0;
            public void run(){

                if(this.step + 1 >= animation.size()) {
                    player.showTitle(Title.title(MiniMessage.miniMessage().deserialize(animation.get(this.step)), MiniMessage.miniMessage().deserialize("<gray>Willkommen, <white>" + PermissionCenterModulePaper.getAPI().getOnlineUser(player).getDisplayString() + player.getName() + "<gray>!"), Title.Times.times(Duration.ofMillis(0L), Duration.ofMillis(1500L), Duration.ofMillis(1500L))));
                    player.playSound(player, Sound.BLOCK_ANVIL_LAND, 3f, 3f);
                    cancel();
                    //titleWait.put(player.getUniqueId(), false);
                } else {
                    player.showTitle(Title.title(MiniMessage.miniMessage().deserialize(animation.get(this.step)), Component.empty(), Title.Times.times(Duration.ofMillis(0L), Duration.ofMillis(1500L), Duration.ofMillis(0L))));
                    player.playSound(player, Sound.ENTITY_CREEPER_HURT, 3f, 3f);
                }
                this.step++;
            }

        }.runTaskTimer(FreeBuild.getInstance().getPaperCore(), 10, 2);
    }

    private ArrayList<String> getTitleComponent() {
        ArrayList<String> list = new ArrayList<>();

        list.add("<dark_gray>» ");
        list.add("<dark_gray>» <white>F");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>F</gradient><white>r");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>Fr</gradient><white>e");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>Fre</gradient><white>e");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>Free</gradient><white>B");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>FreeB</gradient><white>u");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>FreeBu</gradient><white>i");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>FreeBui</gradient><white>l");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>FreeBuil</gradient><white>d");
        list.add("<dark_gray>» <gradient:#33F702:#1B660F>FreeBuild</gradient>");

        return list;
    }

    public String decodeColorcode(String colorCode) {
        String s = "";
        switch(colorCode) {
            case "§a" -> s = "<green>";
            case "§c" -> s = "<red>";
            case "§4" -> s = "<dark_red>";
        }
        return s;
    }

    public HashMap<UUID, FreeBuildPlayer> getFreeBuildPlayers() {
        return freeBuildPlayers;
    }

    public List<Quest> getQuestNPCList() {
        return questNPCList;
    }
}
