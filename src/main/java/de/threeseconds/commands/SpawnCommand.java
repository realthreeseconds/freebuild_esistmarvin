package de.threeseconds.commands;

import de.threeseconds.FreeBuild;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand extends Command {

    public SpawnCommand() {
        super("spawn");
        FreeBuild.getInstance().getPaperCore().getServer().getCommandMap().register(FreeBuild.getInstance().getPaperCore().getName(), this);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if(!(sender instanceof Player player)) return false;


        player.teleport(new Location(Bukkit.getWorld("freebuildWorld"), 0, 64, 0));
        player.playSound(FreeBuild.getInstance().getHubLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        return false;
    }
}
