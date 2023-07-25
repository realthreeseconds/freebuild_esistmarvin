package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.npc.PacketReader;
import de.threeseconds.sign.PacketInjector;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.*;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class JoinListener implements Listener {

    public JoinListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        //new PacketReader(player).inject();
        PacketInjector.addPacketInjector(player);

        playerJoinEvent.joinMessage(null);

        FreeBuild.getInstance().getQuestManager().sendTitle(player);

        player.teleport(FreeBuild.getInstance().getHubLocation());
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().setItem(8, new ItemBuilder(Material.NETHER_STAR).setKey("menu-item").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")).getItemStack());

        FreeBuild.getInstance().getQuestManager().createFreeBuildPlayer(playerJoinEvent, player);

        /*player.getDiscoveredRecipes().forEach(player::undiscoverRecipe);

        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof ShapedRecipe shapedRecipe) {

                Map<Character, RecipeChoice> itemStackMap = shapedRecipe.getChoiceMap();

                itemStackMap.forEach(((character, recipeChoice) -> {
                    if(recipeChoice != null) Bukkit.broadcast(Component.text(character + " | " + recipeChoice.getItemStack().getType() + " = " + recipe.getResult().getType()));
                }));

            }
        });

         */

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        playerQuitEvent.quitMessage(null);

        PacketInjector.removePacketInjector(player);

        //FreeBuild.getInstance().getQuestManager().removeFreebuildPlayer(player);
    }

}
