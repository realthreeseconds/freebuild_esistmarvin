package de.threeseconds.listener;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.commands.ClaimCommand;
import de.threeseconds.plot.*;
import de.threeseconds.plot.listener.PlayerEnterPlotEvent;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.apache.commons.lang.WordUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.*;

public class FreeBuildListener implements Listener {

    public FreeBuildListener() {
        FreeBuild.getInstance().getPaperCore().getServer().getPluginManager().registerEvents(this, FreeBuild.getInstance().getPaperCore());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(blockPlaceEvent.getPlayer());
        if(freeBuildPlayer.canBuildInHub()) return;

        blockPlaceEvent.setCancelled(blockPlaceEvent.getBlock().getWorld().getName().equals("world"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(blockBreakEvent.getPlayer());
        if(freeBuildPlayer.canBuildInHub()) return;

        blockBreakEvent.setCancelled(blockBreakEvent.getBlock().getWorld().getName().equals("world"));
    }

    @EventHandler
    public void onDamage(EntityDamageEvent entityDamageEvent) {
        if(entityDamageEvent.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
            entityDamageEvent.setCancelled(true);
        }

    }

    private void createHologram(EntityDamageByEntityEvent entityDamageByEntityEvent, @NotNull Entity entity, String[] finalDamage) {

        var formattedDamage = new DecimalFormat("#").format((int)Math.round(Double.parseDouble(finalDamage[0])) + entityDamageByEntityEvent.getDamage());
        var location = entity.getLocation();

        var x = rand(0.0, 1.0);
        var y = rand(0.6, 1.2);
        var z = rand(0.0, 1.0);

        location.getWorld().spawn(location.add(x, y, z), ArmorStand.class, armorStand -> {
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setInvulnerable(true);

            Component damageComponent = MiniMessage.miniMessage().deserialize("<gray>" + "%damage%".replace("%damage%", formattedDamage));
            if(Boolean.parseBoolean(finalDamage[1])) damageComponent = MiniMessage.miniMessage().deserialize("<red>* <gradient:gold:red>" + "%damage%".replace("%damage%", formattedDamage) + "</gradient> <red>*");

            armorStand.customName(damageComponent);
            armorStand.setCustomNameVisible(true);

            FreeBuild.getInstance().getIndicatorManager().activeArmorStands().put(armorStand, System.currentTimeMillis());
        });




        if(entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.LivingEntity) {

            ((LivingEntity) entityDamageByEntityEvent.getEntity()).damage((int)Math.round(Double.parseDouble(finalDamage[0])) + entityDamageByEntityEvent.getDamage());

            double health = ((org.bukkit.entity.LivingEntity) entityDamageByEntityEvent.getEntity()).getHealth();
            double maxHealth = ((LivingEntity) entityDamageByEntityEvent.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

            entityDamageByEntityEvent.getEntity().customName(MiniMessage.miniMessage().deserialize("<dark_gray>(<gray>Lv15<dark_gray>) <red>" + WordUtils.capitalize(entityDamageByEntityEvent.getEntityType().name().replace("_", " ").toLowerCase()) + " <dark_gray>● <green>" + (int)health + "/" + (int)maxHealth + " <red>❤"));

        }
    }

    private String[] calculateDamage(FreeBuildPlayer freeBuildPlayer) {
        StringJoiner stringJoiner = new StringJoiner(":");


        SplittableRandom splittableRandom = new SplittableRandom();
        boolean isCrit = false;

        int finalDamage = (int) (5 * (1 + (freeBuildPlayer.getPlayerStats().strength()/100)));


        if(splittableRandom.nextInt(0, 100) <= freeBuildPlayer.getPlayerStats().critChance()*100) {
            finalDamage = (int) (finalDamage * (1 + (freeBuildPlayer.getPlayerStats().critDamage())));
            isCrit = true;
        }
        stringJoiner.add(String.valueOf(finalDamage));
        stringJoiner.add(String.valueOf(isCrit));

        return stringJoiner.toString().split(":");
    }

    private double rand(double min, double max) {
        return min + Math.random() * (max - min);
    }

    @EventHandler
    public void onMobSpawn(CreatureSpawnEvent creatureSpawnEvent) {
        if(creatureSpawnEvent.getEntity() instanceof ArmorStand) return;

        creatureSpawnEvent.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(250d);
        creatureSpawnEvent.getEntity().setHealth(250d);
        creatureSpawnEvent.getEntity().customName(MiniMessage.miniMessage().deserialize("<dark_gray>(<gray>Lv15<dark_gray>) <red>" + WordUtils.capitalize(creatureSpawnEvent.getEntityType().name().replace("_", " ").toLowerCase()) + " <dark_gray>● <green>" + (int)creatureSpawnEvent.getEntity().getHealth() + "/" + (int)creatureSpawnEvent.getEntity().getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() + " <red>❤"));
        creatureSpawnEvent.getEntity().setCustomNameVisible(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent playerDropItemEvent) {

        if(FreeBuild.getInstance().checkPDC("menu-item", playerDropItemEvent.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer(), "<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")) {
            playerDropItemEvent.setCancelled(true);
        }

    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent playerSwapHandItemsEvent) {
        if(FreeBuild.getInstance().checkPDC("menu-item", playerSwapHandItemsEvent.getOffHandItem().getItemMeta().getPersistentDataContainer(), "<green><b>Menü</b> <dark_gray>» <gray>Rechtsklick")) {
            playerSwapHandItemsEvent.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent entityDamageByEntityEvent) {


        if(entityDamageByEntityEvent.getDamager() instanceof Player player) {
            FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

            if (player.getWorld().getName().equals("freebuildWorld")) {

                Plot currentPlot = FreeBuild.getInstance().getPlotManager().getPlotByChunk(player.getChunk());
                if(currentPlot == null) {
                    return;
                }

                if(!currentPlot.getSettingState(PlotSetting.PVP)) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Auf diesem <yellow>Grundstück <red>ist PvP nicht erlaubt."));
                }

                entityDamageByEntityEvent.setCancelled(!currentPlot.getSettingState(PlotSetting.PVP));

            } //else entityDamageByEntityEvent.setCancelled(true);

            this.createHologram(entityDamageByEntityEvent, entityDamageByEntityEvent.getEntity(), this.calculateDamage(freeBuildPlayer));

        }

        //this.createHologram((Player) entityDamageByEntityEvent.getDamager(), entityDamageByEntityEvent.getEntity(), entityDamageByEntityEvent.getFinalDamage());

    }

    @EventHandler
    public void onPlayerHealth(EntityRegainHealthEvent entityRegainHealthEvent) {
        if(entityRegainHealthEvent.getEntity() instanceof Player) {
            Player player = (Player) entityRegainHealthEvent.getEntity();

            return;
        }

        if(entityRegainHealthEvent.getEntity() instanceof LivingEntity) {
            double health = ((org.bukkit.entity.LivingEntity) entityRegainHealthEvent.getEntity()).getHealth();
            double maxHealth = ((LivingEntity) entityRegainHealthEvent.getEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();

            entityRegainHealthEvent.getEntity().customName(MiniMessage.miniMessage().deserialize("<dark_gray>(<gray>Lv15<dark_gray>) <red>" + WordUtils.capitalize(entityRegainHealthEvent.getEntityType().name().replace("_", " ").toLowerCase()) + " <dark_gray>● <green>" + (int)health + "/" + (int)maxHealth + " <red>❤"));

        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent inventoryDragEvent) {
        if(inventoryDragEvent.getInventory().getHolder() instanceof InventoryBuilder inventoryBuilder) {

            inventoryBuilder.handleDrag(inventoryDragEvent);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent playerTeleportEvent) {
        Player player = playerTeleportEvent.getPlayer();

        if(playerTeleportEvent.getTo().getWorld().getName().equals("freebuildWorld")) {
            FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player).getGameScoreboard().setScore(MiniMessage.miniMessage().deserialize(" <dark_gray>» <dark_green>Farmwelt"), 7);

            //player.showTitle(Title.title(MiniMessage.miniMessage().deserialize("<dark_red>❤ <red>Neulingsschutz <dark_red>❤"), MiniMessage.miniMessage().deserialize("<gray>Regeneration I für 2 Stunden!")));
            //player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 144000, 0, false, true, true));
        }
    }


    @EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(player.getWorld().getName().equals("freebuildWorld")) {

            Location fromLocation = playerMoveEvent.getFrom();
            Location toLocation = playerMoveEvent.getTo();

            if((fromLocation.getBlockX() >> 4) != (toLocation.getBlockX() >> 4) || (fromLocation.getBlockZ() >> 4) != (toLocation.getBlockZ() >> 4)) {
                PlayerEnterPlotEvent playerEnterPlotEvent = new PlayerEnterPlotEvent(player, freeBuildPlayer, FreeBuild.getInstance().getPlotManager().getPlotByChunk(fromLocation.getChunk()), FreeBuild.getInstance().getPlotManager().getPlotByChunk(toLocation.getChunk()));
                playerEnterPlotEvent.callEvent();
            }
        }
    }






    /*@EventHandler
    public void onMove(PlayerMoveEvent playerMoveEvent) {
        Player player = playerMoveEvent.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if (player.getWorld().getName().equals("freebuildWorld")) {
            if (playerMoveEvent.hasExplicitlyChangedBlock()) {
                Plot currentPlot = FreeBuild.getInstance().getPlotManager().getPlotByChunk(playerMoveEvent.getFrom().getChunk());
                if (currentPlot == null) {
                    return;
                }

                Plot nextPlot = FreeBuild.getInstance().getPlotManager().getPlotByChunk(playerMoveEvent.getTo().getChunk());
                if (nextPlot == null) {
                    if (currentPlot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        FreeBuild.getInstance().getPlotManager().playerBukkitTask.remove(freeBuildPlayer);
                        return;
                    }
                    currentPlot.leavePlot(freeBuildPlayer);
                    return;
                }

                if (!nextPlot.getSettingState(PlotSetting.JOINABLE)) {
                    if (!nextPlot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dieses <yellow>Grundstück <red>nicht betreten."));
                        playerMoveEvent.setCancelled(true);
                        return;
                    }

                }

                if (nextPlot != currentPlot) {
                    if (nextPlot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        currentPlot.leavePlot(freeBuildPlayer);
                        return;
                    }
                }

                if (currentPlot.getPlotMembers().containsKey(freeBuildPlayer)) {
                    if (!FreeBuild.getInstance().getPlotManager().playerBukkitTask.containsKey(freeBuildPlayer)) {
                        FreeBuild.getInstance().getPlotManager().playerBukkitTask.put(freeBuildPlayer, true);
                        currentPlot.showBorder(player);
                        return;
                    }
                    return;
                }

                nextPlot.enterPlot(currentPlot, freeBuildPlayer);
            }
        }


    }

     */

    @EventHandler
    public void onClaim(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        FreeBuildPlayer freeBuildPlayer = FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player);

        if(!player.getWorld().getName().equals("world")) {
            if(playerInteractEvent.getAction().isLeftClick()) {
                if(ClaimCommand.claimMap.containsKey(freeBuildPlayer)) {
                    if(playerInteractEvent.getClickedBlock() == null) {
                        Chunk clickedChunk = player.getTargetBlockExact(30).getLocation().getChunk();
                        Location clickedLocation = player.getTargetBlockExact(30).getLocation();
                        Location spawnLocation = new Location(Bukkit.getWorld("freebuildWorld"), 0, 64, 0);


                        List<Chunk> chunks;
                        if(ClaimCommand.claimMap.get(freeBuildPlayer) == null) {
                            chunks = new ArrayList<>();
                        } else {
                            chunks = ClaimCommand.claimMap.get(freeBuildPlayer);
                        }

                        if(ClaimCommand.claimMap.get(freeBuildPlayer) != null && ClaimCommand.claimMap.get(freeBuildPlayer).contains(clickedChunk)) {

                            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                            player.sendMessage(MiniMessage.miniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast das Grundstück <red>nicht mehr <gray>markiert."));
                            chunks.remove(clickedChunk);
                            this.markedChunkBorder(player, clickedChunk, null, false);

                            return;
                        }

                        if(ClaimCommand.claimMap.get(freeBuildPlayer) != null && ClaimCommand.claimMap.get(freeBuildPlayer).size() == 3) {
                            player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                            player.sendMessage(MiniMessage.miniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst keine Grundstücke mehr claimen."));

                            return;
                        }

                        if(clickedLocation.distance(spawnLocation) < 35) {
                            player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                            player.sendMessage(MiniMessage.miniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Dieses Grundstück ist zu nah am Spawn, um es zu claimen."));
                            return;
                        }

                        player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                        player.sendMessage(MiniMessage.miniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast das Grundstück <green>erfolgreich <gray>markiert."));
                        player.sendMessage(MiniMessage.miniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Um das Grundstück zu claimen, musst du nochmal <yellow>/claim <gray>eingeben."));

                        chunks.add(clickedChunk);

                        ClaimCommand.claimMap.put(freeBuildPlayer, chunks);

                        markedChunkBorder(player, clickedChunk, Material.BLUE_TERRACOTTA, true);

                    }

                }
            }
        }
    }

    @EventHandler
    public void onChat(AsyncChatEvent asyncChatEvent) {
        Player player = asyncChatEvent.getPlayer();
        asyncChatEvent.setCancelled(true);

        Bukkit.getOnlinePlayers().forEach(players -> {
            players.sendMessage(MiniMessage.miniMessage().deserialize(PermissionCenterModulePaper.getAPI().getOnlineUser(player).getHighstGroup().getChatPrefix() + player.getName() + " <dark_gray>» <gray>" + PlainTextComponentSerializer.plainText().serialize(asyncChatEvent.message())));
        });
    }

    public static void markedChunkBorder(Player player, Chunk chunk, @Nullable Material material, boolean marked) {
        World world = chunk.getWorld();
        int chunkX = chunk.getX();
        int chunkZ = chunk.getZ();

        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int maxX = minX + 14;
        int maxZ = minZ + 14;


        for (int x = minX; x <= maxX; x++) {
            Location first = new Location(world, x, world.getHighestBlockYAt(x, minZ), minZ);
            Location second = new Location(world, x, world.getHighestBlockYAt(x, maxZ), maxZ + 1);

            player.sendBlockChange(first, (marked ? material.createBlockData() : world.getBlockAt(first).getBlockData()));
            player.sendBlockChange(second, (marked ? material.createBlockData() : world.getBlockAt(second).getBlockData()));

        }

        for (int z = minZ; z <= maxZ; z++) {
            Location first = new Location(world, minX, world.getHighestBlockYAt(minX, z), z);
            Location second = new Location(world, maxX + 1, world.getHighestBlockYAt(maxX, z), z);

            player.sendBlockChange(first, (marked ? material.createBlockData() : world.getBlockAt(first).getBlockData()));
            player.sendBlockChange(second, (marked ? material.createBlockData() : world.getBlockAt(second).getBlockData()));
        }

    }

}
