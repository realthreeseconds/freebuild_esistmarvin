package de.threeseconds.plot.inventories;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.Plot;
import de.threeseconds.plot.PlotGroup;
import de.threeseconds.plot.PlotGroupPermission;
import de.threeseconds.plot.PlotSetting;
import de.threeseconds.sign.PacketHandler;
import de.threeseconds.sign.PacketInjector;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.network.protocol.game.ServerboundSignUpdatePacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class PlotSettings extends InventoryBuilder {

    public PlotSettings(FreeBuildPlayer freeBuildPlayer, Plot plot) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstück <dark_gray>● <gray>Einstellungen"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */
        this.setItem(2, new ItemBuilder(Material.ARMOR_STAND).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Mitglieder")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
        });
        this.setItem(4, new ItemBuilder(Material.NAME_TAG).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Gruppen")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.GROUP_PERMS)) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                return;
            }
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
            new PlotGroups(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
        });
        this.setItem(6, new ItemBuilder(Material.COMPARATOR).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Einstellungen")).setGlowing().getItemStack());


        this.setItem(19, new ItemBuilder(Material.LEATHER_BOOTS).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Spieler können Beitreten")).getItemStack());
        this.setItem(20, new ItemBuilder(Material.IRON_SWORD).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>PvP erlauben")).getItemStack());

        this.setItem(22, new ItemBuilder(Material.WRITABLE_BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Anfragen erlauben")).getItemStack());
        this.setItem(23, new ItemBuilder(Material.BOOKSHELF).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Sichtbarkeit")).getItemStack());
        this.setItem(24, new ItemBuilder((plot.getPlotBlock() != null ? plot.getPlotBlock() : Material.GRASS_BLOCK)).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Icon")).getItemStack());
        this.setItem(25, new ItemBuilder(Material.OAK_SIGN).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Umbenennen")).getItemStack());

        this.setItem(28, new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.JOINABLE) ? (plot.getSettingState(PlotSetting.JOINABLE) ? Material.LIME_CONCRETE : Material.RED_CONCRETE) : Material.BARRIER)).setKey("plotJoinable").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Spieler können Beitreten")).getItemStack());
        this.setItem(29, new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.TOGGLE_PVP) ? (plot.getSettingState(PlotSetting.PVP) ? Material.LIME_CONCRETE : Material.RED_CONCRETE) : Material.BARRIER)).setKey("plotTogglePvP").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>PvP erlauben")).getItemStack());
        this.setItem(31, new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.TOGGLE_REQUESTS) ? (plot.getSettingState(PlotSetting.RECEIVE_REQUESTS) ? Material.LIME_CONCRETE : Material.RED_CONCRETE) : Material.BARRIER)).setKey("plotToggleRequests").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Anfragen erlauben")).getItemStack());
        this.setItem(32, new ItemBuilder((plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.BROWSER_VISIBILITY) ? (plot.getSettingState(PlotSetting.BROWSER_VISIBILITY) ? Material.LIME_CONCRETE : Material.RED_CONCRETE) : Material.BARRIER)).setKey("plotBrowserVisibilitity").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Sichtbarkeit")).getItemStack());
        this.setItem(33, new ItemBuilder(plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.ICON) ? Material.PAPER : Material.BARRIER).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Icon")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.ICON)) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                return;
            }

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotIconBlocks(freeBuildPlayer, plot).open(player);

        });

        this.setItem(34, new ItemBuilder(plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.NAME) ? Material.NAME_TAG : Material.BARRIER).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Umbenennen")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.NAME)) {
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                return;
            }

            player.closeInventory();
            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            this.sign(player, lines -> {
                if(lines[0].isEmpty()) {
                    return;
                }

                plot.setPlotName("<yellow>'" + lines[0] + "' <gray>von " + PermissionCenterModulePaper.getAPI().getOnlineUser(plot.getPlotOwner().getPlayer()).getDisplayString() + plot.getPlotOwner().getUserName());

                new PlotSettings(freeBuildPlayer, plot).open(player);
            });
        });


        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotOverview(freeBuildPlayer, plot).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);

            if(inventoryClickEvent.getCurrentItem() == null) return;

            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if(FreeBuild.getInstance().checkPDC("plotJoinable", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_gray>● <green>Spieler können Beitreten")) {

                if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.JOINABLE)) {
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    return;
                }

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                plot.setSettingState(PlotSetting.JOINABLE);

                this.setItem(inventoryClickEvent.getSlot(), new ItemBuilder((plot.getSettingState(PlotSetting.JOINABLE) ? Material.LIME_CONCRETE : Material.RED_CONCRETE)).setKey("plotJoinable").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Spieler können Beitreten")).getItemStack());

                return;
            }

            if(FreeBuild.getInstance().checkPDC("plotBrowserVisibilitity", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_gray>● <green>Sichtbarkeit")) {

                if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.BROWSER_VISIBILITY)) {
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    return;
                }

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                plot.setSettingState(PlotSetting.BROWSER_VISIBILITY);

                this.setItem(inventoryClickEvent.getSlot(), new ItemBuilder((plot.getSettingState(PlotSetting.BROWSER_VISIBILITY) ? Material.LIME_CONCRETE : Material.RED_CONCRETE)).setKey("plotBrowserVisibilitity").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Sichtbarkeit")).getItemStack());

                return;
            }

            if(FreeBuild.getInstance().checkPDC("plotToggleRequests", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_gray>● <green>Anfragen erlauben")) {

                if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.TOGGLE_REQUESTS)) {
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    return;
                }

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                plot.setSettingState(PlotSetting.RECEIVE_REQUESTS);

                this.setItem(inventoryClickEvent.getSlot(), new ItemBuilder((plot.getSettingState(PlotSetting.RECEIVE_REQUESTS) ? Material.LIME_CONCRETE : Material.RED_CONCRETE)).setKey("plotToggleRequests").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>Anfragen erlauben")).getItemStack());

                return;
            }

            if(FreeBuild.getInstance().checkPDC("plotTogglePvP", inventoryClickEvent.getCurrentItem().getItemMeta().getPersistentDataContainer(), "<dark_gray>● <green>PvP erlauben")) {

                if(!plot.getPlotMembers().get(freeBuildPlayer).getGroupPermissions().contains(PlotGroupPermission.TOGGLE_PVP)) {
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast dafür keine Rechte."));
                    player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                    return;
                }

                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

                plot.setSettingState(PlotSetting.PVP);

                this.setItem(inventoryClickEvent.getSlot(), new ItemBuilder((plot.getSettingState(PlotSetting.PVP) ? Material.LIME_CONCRETE : Material.RED_CONCRETE)).setKey("plotTogglePvP").setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>● <green>PvP erlauben")).getItemStack());

                return;
            }
        });
    }

    public void sign(Player player, Consumer<String[]> lines) {
        PacketInjector.addPacketInjector(player);

        Location l = player.getLocation();
        BlockPos pos = new BlockPos(l.getBlockX(), l.getBlockY(), l.getBlockZ());
        BlockState old = ((CraftWorld) l.getWorld()).getHandle().getBlockState(pos);

        ClientboundBlockUpdatePacket sent1 = new ClientboundBlockUpdatePacket(pos, Blocks.OAK_SIGN.defaultBlockState());
        ((CraftPlayer) player).getHandle().connection.send(sent1);

        /*
        SignBlockEntity signBlockEntity = new SignBlockEntity(sent1.getPos(), sent1.getBlockState());
        SignText signText = signBlockEntity.getText(true);
        signText.setColor(DyeColor.WHITE);
        signText.setMessage(1, net.minecraft.network.chat.Component.nullToEmpty("test"));
        signBlockEntity.setText(signText, true);
        ((CraftPlayer) player).getHandle().connection.send(signBlockEntity.getUpdatePacket());
        */

        ClientboundOpenSignEditorPacket sent2 = new ClientboundOpenSignEditorPacket(pos, true);
        ((CraftPlayer) player).getHandle().connection.send(sent2); // Open the sign editor

        PacketHandler.PACKET_HANDLERS.put(player.getUniqueId(), packetO -> {
            if (!(packetO instanceof ServerboundSignUpdatePacket packet)) return false; // Only intercept sign packets

            ClientboundBlockUpdatePacket sent3 = new ClientboundBlockUpdatePacket(pos, old);
            ((CraftPlayer) player).getHandle().connection.send(sent3); // Reset the block state for that packet

            lines.accept(packet.getLines());

            return true;
        });
    }
}
