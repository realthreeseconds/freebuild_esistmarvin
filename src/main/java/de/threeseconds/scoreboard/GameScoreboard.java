package de.threeseconds.scoreboard;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.privateseconds.permissioncentermodulepaper.lib.Color;
import de.privateseconds.permissioncentermodulepaper.lib.PermissionGroup;
import de.privateseconds.permissioncentermodulepaper.lib.PermissionUser;
import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GameScoreboard extends ScoreboardBuilder {

    private int socialId;

    public GameScoreboard(Player player) {
        super(player, FreeBuild.getInstance().getMiniMessage().deserialize("<yellow>EsIstServer <dark_gray>● <gradient:#33F702:#1B660F>FreeBuild</gradient>"));

        this.updatePlayerPrefix();
        //run();
    }

    @Override
    public void createScoreboard() {
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>"), 9);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>Bereich:"), 8);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <blue>Hub"), 7);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize("<red>"), 6);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>Dein Profil:"), 5);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» " + FreeBuild.getInstance().getQuestManager().decodeColorcode(PermissionCenterModulePaper.getInstance().getUserManager().getUser(this.player.getUniqueId()).getDisplay().getColor()) + this.player.getName()), 4);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize("<yellow>"), 3);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize("<gray>Job:"), 2);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <green>Ohne Beruf"), 1);
        setScore(FreeBuild.getInstance().getMiniMessage().deserialize("<green>"), 0);
    }

    public void updatePlayerPrefix() {
        for (PermissionUser permissionUser : PermissionCenterModulePaper.getInstance().getUserManager().getChachedUsers()) {
            Player player = permissionUser.getPlayer();
            Scoreboard scoreboard = player.getScoreboard();
            for (PermissionGroup group : PermissionCenterModulePaper.getInstance().getGroupManager().getGroups()) {
                Team teamGroup = (scoreboard.getTeam(group.getSortId().toString() + group.getSortId().toString()) == null) ? scoreboard.registerNewTeam(group.getSortId().toString() + group.getSortId().toString()) : scoreboard.getTeam(group.getSortId().toString() + group.getSortId().toString());
                assert teamGroup != null;
                teamGroup.prefix(Component.text(group.getPrefix()));
                teamGroup.color(this.translateColor(group.getDisplay()));
            }
            for (PermissionUser targetUser : PermissionCenterModulePaper.getInstance().getUserManager().getChachedUsers()) {
                if (targetUser.getPlayer() != null)
                    scoreboard.getTeam(targetUser.getHighstGroup().getSortId().toString() + targetUser.getHighstGroup().getSortId().toString()).addPlayer(targetUser.getPlayer());
            }
        }
    }

    private NamedTextColor translateColor(Color color) {
        if (color == Color.BLACK)
            return NamedTextColor.BLACK;
        if (color == Color.AQUA)
            return NamedTextColor.AQUA;
        if (color == Color.BLUE)
            return NamedTextColor.BLUE;
        if (color == Color.DARK_AQUA)
            return NamedTextColor.DARK_AQUA;
        if (color == Color.DARK_BLUE)
            return NamedTextColor.DARK_BLUE;
        if (color == Color.DARK_GRAY)
            return NamedTextColor.DARK_GRAY;
        if (color == Color.DARK_GREEN)
            return NamedTextColor.DARK_GREEN;
        if (color == Color.DARK_PURPLE)
            return NamedTextColor.DARK_PURPLE;
        if (color == Color.DARK_RED)
            return NamedTextColor.DARK_RED;
        if (color == Color.GOLD)
            return NamedTextColor.GOLD;
        if (color == Color.GRAY)
            return NamedTextColor.GRAY;
        if (color == Color.GREEN)
            return NamedTextColor.GREEN;
        if (color == Color.LIGHT_PURPEL)
            return NamedTextColor.LIGHT_PURPLE;
        if (color == Color.RED)
            return NamedTextColor.RED;
        if (color == Color.YELLOW)
            return NamedTextColor.YELLOW;
        return NamedTextColor.WHITE;
    }
    @Override
    public void update() {

    }
}
