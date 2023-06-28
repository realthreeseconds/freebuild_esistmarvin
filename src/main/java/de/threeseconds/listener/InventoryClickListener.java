package de.threeseconds.listener;

import de.threeseconds.FreeBuild;
import de.threeseconds.job.Jobs;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class InventoryClickListener implements Listener {

    private HashMap<UUID, Jobs> clickedJobsHashMap = new HashMap<>();

    public InventoryClickListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onInventoyClick(InventoryClickEvent inventoryClickEvent) {
        if(inventoryClickEvent.getClickedInventory() == null) return;

        if(!(inventoryClickEvent.getWhoClicked() instanceof Player player)) return;

        if(inventoryClickEvent.getCurrentItem() == null) {
            inventoryClickEvent.setCancelled(true);
            return;
        }

        if(inventoryClickEvent.getClick() == ClickType.NUMBER_KEY && inventoryClickEvent.getHotbarButton() == 8) inventoryClickEvent.setCancelled(true);

        if(inventoryClickEvent.getCurrentItem().getItemMeta() == null) return;

        if(FreeBuild.getInstance().checkPDC("menu-item", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")) {
            inventoryClickEvent.setCancelled(true);
            return;
        }
        


        if(inventoryClickEvent.getView().title().equals(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#3434FA:#F6A3FF>Jobs</gradient> <dark_gray>● <gray>Menü"))) {
            inventoryClickEvent.setCancelled(true);
            if(inventoryClickEvent.getCurrentItem() == null) return;

            if(inventoryClickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD && inventoryClickEvent.getSlot() != 4) {
                FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

                if(inventoryClickEvent.isRightClick()) {
                    Jobs clickedJob = FreeBuild.getInstance().getJobManager().getJobBySlot(player, inventoryClickEvent.getSlot());

                    clickedJobsHashMap.put(player.getUniqueId(), clickedJob);

                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                    FreeBuild.getInstance().getJobManager().openJob(inventoryClickEvent.getClickedInventory(), inventoryClickEvent.getView(), clickedJob);

                    return;
                }

                if(inventoryClickEvent.isLeftClick()) {
                    Jobs clickedJob = FreeBuild.getInstance().getJobManager().getJobBySlot(player, inventoryClickEvent.getSlot());

                    clickedJobsHashMap.put(player.getUniqueId(), clickedJob);

                    if(Objects.isNull(clickedJob) || freeBuildPlayer.getJob() == clickedJob) {
                        player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast diesen Beruf bereits ausgewählt."));

                        return;
                    }
                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast den Beruf <green>" + clickedJob.getJobName() + " <gray>ausgewählt."));

                    player.getInventory().addItem(new ItemBuilder(Material.FISHING_ROD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<gold>Fischers Angel")).addEnchantment(Enchantment.DURABILITY, 3, false).addEnchantment(Enchantment.MENDING, 1, false).getItemStack());


                    freeBuildPlayer.setJob(clickedJob);
                    freeBuildPlayer.getGameScoreboard().setScore(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <green>" + clickedJob.getJobName()), 1);

                    FreeBuild.getInstance().getJobManager().updateJobsInventory(inventoryClickEvent.getClickedInventory(), player);

                    return;
                }

            }

            if(inventoryClickEvent.getCurrentItem().getType() == Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE) {
                FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);
                

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                player.openInventory(FreeBuild.getInstance().getJobManager().openJobProgression(player, this.clickedJobsHashMap.get(player.getUniqueId()), 1, true));

                return;
            }

            return;


        }

        if(inventoryClickEvent.getView().title().equals(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#E0167B:#D9D938>Menü</gradient>"))) {
            inventoryClickEvent.setCancelled(true);

            if(inventoryClickEvent.getCurrentItem() == null) return;

            if(inventoryClickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD && inventoryClickEvent.getSlot() == 47) {
                FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

                player.closeInventory();
                player.teleport(freeBuildPlayer.getFastTravelLocation());
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                return;
            }
        }

        if(inventoryClickEvent.getView().title().equals(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gradient:#3434FA:#F6A3FF>Job</gradient> <dark_gray>● <gray>Level"))) {
            inventoryClickEvent.setCancelled(true);

            if(inventoryClickEvent.getCurrentItem() == null) return;

            if(inventoryClickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD && FreeBuild.getInstance().checkPDC("jobs_nextpage", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_green>» <green>Weiter")) {
                Integer page = FreeBuild.getInstance().getJobManager().getLevelPage(player);

                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);

                player.openInventory(FreeBuild.getInstance().getJobManager().openJobProgression(player, this.clickedJobsHashMap.get(player.getUniqueId()), (page == 1 ? 2 : 1), false));
                return;
            }

            if(inventoryClickEvent.getCurrentItem().getType() == Material.PLAYER_HEAD && FreeBuild.getInstance().checkPDC("jobs_prevpage", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_red>» <red>Zurück")) {
                Integer page = FreeBuild.getInstance().getJobManager().getLevelPage(player);

                player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);

                player.openInventory(FreeBuild.getInstance().getJobManager().openJobProgression(player, this.clickedJobsHashMap.get(player.getUniqueId()), (page == 1 ? 2 : 1), false));
                return;
            }
        }
    }

}
