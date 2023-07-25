package de.threeseconds.plot.inventories;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotGroup;
import de.threeseconds.plot.PlotGroupPermission;
import de.threeseconds.sign.PacketHandler;
import de.threeseconds.sign.PacketInjector;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PlotOverview extends InventoryBuilder {

    public PlotOverview(FreeBuildPlayer freeBuildPlayer, Plot plot) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstück <dark_gray>● <gray>Übersicht"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        List<Component> lores = new ArrayList<>();

        this.setItem(2, new ItemBuilder(Material.ARMOR_STAND).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Mitglieder")).setGlowing().getItemStack());
        this.setItem(4, new ItemBuilder(Material.NAME_TAG).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Gruppen")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.GROUP_PERMS)) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                return;
            }
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
            new PlotGroups(freeBuildPlayer, plot).open(player);
        });
        this.setItem(6, new ItemBuilder(Material.COMPARATOR).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Einstellungen")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotSettings(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
        });


        // TODO: 08.07.2023  ->  display all plots
        AtomicInteger atomicInteger = new AtomicInteger(9);

        plot.getPlotMembers().entrySet().stream().sorted(Map.Entry.comparingByValue()).forEach(plotMember -> {
            this.setItem(atomicInteger.get(), new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(plotMember.getKey().getPlayer().getPlayerProfile().getTextures()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + PermissionCenterModulePaper.getAPI().getOnlineUser(plotMember.getKey().getPlayer()).getDisplayString() + plotMember.getKey().getUserName() + " <dark_gray>(" + plotMember.getValue().getGroupName() + "<dark_gray>)")).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(freeBuildPlayer == plotMember.getKey()) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst keine Einstellungen an dir vornehmen."));
                    return;
                }

                if(plotMember.getValue() == PlotGroup.OWNER) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst keine Einstellungen an " + plotMember.getValue().getGroupName() + " <red>vornehmen."));
                    return;
                }

                if(plotMember.getValue() == PlotGroup.GUEST) {
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst keine Einstellungen an " + plotMember.getValue().getGroupName() + " <red>vornehmen."));
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Lade zunächst " + PermissionCenterModulePaper.getAPI().getOnlineUser(plotMember.getKey().getPlayer()).getDisplayString() + plotMember.getKey().getUserName() + " <red>du diesem Grundstück ein."));
                    return;
                }

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new PlotUser(freeBuildPlayer, plotMember.getKey(), plot).open(freeBuildPlayer.getPlayer());
            });

            atomicInteger.getAndIncrement();
        });


        if(plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.HANDLE_REQUESTS)) {
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Hier siehst du alle Anfragen,").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>die dein Grundstück erhalten hat.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Anfragen <dark_gray>» <yellow>" + (FreeBuild.getInstance().getPlotManager().getRequests(plot) == null ? 0 : FreeBuild.getInstance().getPlotManager().getRequests(plot).size())).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            this.setItem(46, new ItemBuilder(Material.BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Anfragen")).setLore(lores).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                if(FreeBuild.getInstance().getPlotManager().getRequests(plot) == null ){

                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Dein Grundstück hast keine offenen Anfragen."));
                    return;
                }

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new PlotRequests(freeBuildPlayer, plot, true).open(player);

            });
            lores.clear();
        }

        if(plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.SEND_REQUESTS)) {
            this.setItem(47, new ItemBuilder(Material.WRITABLE_BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Spieler einladen")).getItemStack(), inventoryClickEvent -> {
                Player player = (Player) inventoryClickEvent.getWhoClicked();

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                new PlotPlayerList(freeBuildPlayer, plot).open(player);

                /*player.closeInventory();

                this.sendSing(player, lines -> {
                    if(lines[0].isEmpty()) {
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Bitte gib einen Namen ein."));
                        return;
                    }
                    Player targetPlayer = Bukkit.getPlayer(lines[0]);

                    if(targetPlayer == null) {
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<yellow>" + lines[0] + " <red>ist nicht online."));
                        return;
                    }

                    if(targetPlayer == player) {
                        player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht selber einladen."));
                        return;
                    }
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast " + FreeBuild.getInstance().getQuestManager().decodeColorcode(PermissionCenterModulePaper.getInstance().getUserManager().getUser(targetPlayer.getUniqueId()).getDisplay().getColor()) + targetPlayer.getName() + " <gray>als <yellow>Mitglied <gray>auf deinem Grundstück eingeladen."));
                    targetPlayer.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du wurdest von " + FreeBuild.getInstance().getQuestManager().decodeColorcode(PermissionCenterModulePaper.getInstance().getUserManager().getUser(player.getUniqueId()).getDisplay().getColor()) + player.getName() + " <gray>als <yellow>Mitglied <gray>auf seinem Grundstück eingeladen."));


                });

                 */

            });
        }

        if(freeBuildPlayer == plot.getPlotOwner()) {
            this.setItem(50, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ==").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <red>Grundstück löschen")).getItemStack());

        } else if(plot.getPlotMembers().get(freeBuildPlayer) != PlotGroup.GUEST) {
            this.setItem(50, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0=").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Grundstück verlassen")).getItemStack());

        }


        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotBrowser(freeBuildPlayer).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> inventoryClickEvent.setCancelled(true));
    }


}
