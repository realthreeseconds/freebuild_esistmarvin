package de.threeseconds.scoreboard;

import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Objects;

public abstract class ScoreboardBuilder {

    protected final Scoreboard scoreboard;
    protected final Objective objective;

    protected final Player player;

    public ScoreboardBuilder(Player player, Component displayName) {
        this.player = player;

        if(player.getScoreboard().equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
            player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        this.scoreboard = player.getScoreboard();

        if(this.scoreboard.getObjective("display") != null) {
            Objects.requireNonNull(this.scoreboard.getObjective("display")).unregister();
        }

        this.objective = this.scoreboard.registerNewObjective("display", Criteria.DUMMY, displayName);
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        Objective healthObj = this.scoreboard.registerNewObjective("health", Criteria.HEALTH, FreeBuild.getInstance().getMiniMessage().deserialize("<red>❤"), RenderType.HEARTS);
        healthObj.setDisplaySlot(DisplaySlot.BELOW_NAME);

        createScoreboard();
    }

    public abstract void createScoreboard();

    public abstract void update();

    public void setScore(Component content, int score) {
        Team team = getTeamByScore(score);

        if(team == null) {
            return;
        }

        team.prefix(content);
        showScore(score);
    }

    public void removeScore(int score) {
        hideScore(score);
    }

    private EntryName getEntryNameByScore(int score) {
        for(EntryName name : EntryName.values()) {
            if(score == name.getEntry()) {
                return name;
            }
        }

        return null;
    }

    private Team getTeamByScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return null;
        }

        Team team = scoreboard.getEntryTeam(name.getEntryName());

        if(team != null) {
            return team;
        }

        team = scoreboard.registerNewTeam(name.name());
        team.addEntry(name.getEntryName());
        return team;
    }

    private void showScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return;
        }

        if(objective.getScore(name.getEntryName()).isScoreSet()) {
            return;
        }

        objective.getScore(name.getEntryName()).setScore(score);
    }

    private void hideScore(int score) {
        EntryName name = getEntryNameByScore(score);

        if(name == null) {
            return;
        }

        if(!objective.getScore(name.getEntryName()).isScoreSet()) {
            return;
        }

        scoreboard.resetScores(name.getEntryName());
    }
}
