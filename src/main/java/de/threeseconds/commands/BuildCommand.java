package de.threeseconds.commands;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.ItemBuilder;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BuildCommand extends Command {

    public BuildCommand() {
        super("build");
        FreeBuild.getInstance().getPaperCore().getServer().getCommandMap().register(FreeBuild.getInstance().getPaperCore().getName(), this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if(!(sender instanceof Player player)) return false;
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if (!player.hasPermission("freebuild.command.build")) {
            player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast keine Berechtigung dafür."));
            return false;
        }
        if (args.length > 1) {
            if (player.hasPermission("freebuild.command.build.other")) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<green>/build [Player] <dark_gray>- <gray>Baurechte für Spieler."));
            } else {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<green>/build <dark_gray>- <gray>Baurechte für Spieler."));
            }
            return false;
        }
        if (args.length == 1) {
            if (!player.hasPermission("freebuild.command.build.other")) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<green>/build <dark_gray>- <gray>Baurechte für Spieler."));
                return false;
            }
            Player targetPlayer = Bukkit.getPlayer(args[0]);

            if (targetPlayer == null) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<yellow>" + args[0] + " <red>ist nicht online."));

                return false;
            }
            FreeBuildPlayer targetFreeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(targetPlayer);

            if (targetPlayer == player) {
                if (freeBuildPlayer.canBuildInHub()) {
                    freeBuildPlayer.setCanBuildInHub(Boolean.FALSE);
                    player.setGameMode(GameMode.SURVIVAL);
                    player.getInventory().clear();
                    player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).setKey("menu-item").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")).getItemStack());
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>wurde <red>deaktiviert<gray>."));

                    return false;
                }
                freeBuildPlayer.setCanBuildInHub(Boolean.TRUE);
                player.setGameMode(GameMode.CREATIVE);
                player.getInventory().clear();
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>wurde <green>aktiviert<gray>."));

                return false;
            }
            if (targetFreeBuildPlayer.canBuildInHub()) {
                targetFreeBuildPlayer.setCanBuildInHub(Boolean.FALSE);
                targetPlayer.setGameMode(GameMode.SURVIVAL);
                targetPlayer.getInventory().clear();
                targetPlayer.setGameMode(GameMode.SURVIVAL);
                targetPlayer.getInventory().clear();
                targetPlayer.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).setKey("menu-item").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")).getItemStack());
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>für " +  PermissionCenterModulePaper.getAPI().getOnlineUser(targetPlayer).getDisplayString() + targetPlayer.getName() + " <gray>wurde <red>deaktiviert<gray>."));
                targetPlayer.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>wurde <red>deaktiviert<gray>."));

                return false;
            }
            targetFreeBuildPlayer.setCanBuildInHub(Boolean.TRUE);
            player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>für " +  PermissionCenterModulePaper.getAPI().getOnlineUser(targetPlayer).getDisplayString() + targetPlayer.getName() + " <gray>wurde <green>aktiviert<gray>."));
            targetPlayer.setGameMode(GameMode.CREATIVE);
            targetPlayer.getInventory().clear();
            targetPlayer.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>wurde <green>aktiviert<gray>."));

            return false;
        }
        if (freeBuildPlayer.canBuildInHub()) {
            freeBuildPlayer.setCanBuildInHub(Boolean.FALSE);
            player.setGameMode(GameMode.SURVIVAL);
            player.getInventory().clear();
            player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).setKey("menu-item").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")).getItemStack());
            player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>wurde <red>deaktiviert<gray>."));

            return false;
        }
        freeBuildPlayer.setCanBuildInHub(Boolean.TRUE);
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Der <green>Baumodus <gray>wurde <green>aktiviert<gray>."));

        return false;

    }
}
