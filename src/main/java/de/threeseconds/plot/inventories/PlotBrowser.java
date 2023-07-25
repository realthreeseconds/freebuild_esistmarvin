package de.threeseconds.plot.inventories;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.plot.PlotFilter;
import de.threeseconds.plot.PlotGroup;
import de.threeseconds.plot.PlotSetting;
import de.threeseconds.plot.listener.PlayerEnterPlotEvent;
import de.threeseconds.util.FreeBuildPlayer;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import de.threeseconds.util.MenuInventories;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class PlotBrowser extends InventoryBuilder {

    public PlotBrowser(FreeBuildPlayer freeBuildPlayer) {
        super(9*6, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstücke <dark_gray>● <gray>Browser"));

        /* BORDER */
        this.setItems(0, 8, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());
        this.setItems(45, 53, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<green>")).addItemFlags(ItemFlag.values()).getItemStack());


        /* INVENTORY */

        this.setItem(4, new ItemBuilder(Material.BOOKSHELF).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Grundstücke")).getItemStack());


        // TODO: 08.07.2023  ->  display all plots

        List<Component> lores = new ArrayList<>();


        AtomicInteger atomicInteger = new AtomicInteger(9);
        FreeBuild.getInstance().getPlotManager().getPlotList().forEach((plotPlayer, plot) -> {

            if(freeBuildPlayer.getPlotFilter(PlotFilter.MEMBER)) {
                if(plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    if(plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Deine Gruppe <dark_gray>» " + plot.getPlotMembers().get(freeBuildPlayer).getGroupName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    }
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Mitglieder <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group != PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Besucher <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group == PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Einschränkungen <dark_gray>(<yellow>" + plot.getPlotSettings().size() + "<dark_gray>)").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    plot.getPlotSettings().forEach((plotSetting, state) -> lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("   <dark_gray>» " + (state ? "<green>" : "<red>") + plotSetting.getTitle() + (state ? " <dark_green>✔" : " <dark_red>✘")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                    if(!plot.getSettingState(PlotSetting.BROWSER_VISIBILITY) && plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(freeBuildPlayer != plot.getPlotOwner()) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);

                                    
                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                                    
                                    
                                    
                                    //plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);
                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }
                            }
                        });
                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }

                    if(plot.getSettingState(PlotSetting.BROWSER_VISIBILITY)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);


                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);


                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }

                                return;
                            }

                            if(inventoryClickEvent.isRightClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(FreeBuild.getInstance().getPlotManager().getRequests(plot) != null && FreeBuild.getInstance().getPlotManager().getRequests(plot).contains(freeBuildPlayer)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast eine Anfrage an dieses Grundstück bereits versendet."));

                                        return;
                                    }

                                    if(FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer) != null && FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer).contains(plot)) {
                                        plot.addMember(freeBuildPlayer);
                                        return;
                                    }

                                    freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast eine Anfrage an " + plot.getPlotName() + " <gray>geschickt."));

                                    plot.getPlotMembers().forEach((plotMember, plotGroup) -> {
                                        if(plot.getPlotMembers().get(plotMember) == PlotGroup.OWNER || plot.getPlotMembers().get(plotMember) == PlotGroup.ADMIN || plot.getPlotMembers().get(plotMember) == PlotGroup.MODERATOR) {
                                            if(plotMember.getPlayer().isOnline()) {
                                                plotMember.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>hat eine Beitrittsanfrage für dieses Grundstück geschickt."));
                                                plotMember.getPlayer().playSound(plotMember.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                                            }

                                        }
                                    });


                                    FreeBuild.getInstance().getPlotManager().requestToPlot(plot, freeBuildPlayer);
                                }
                            }
                        });

                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }
                }
                return;
            }


            if(freeBuildPlayer.getPlotFilter(PlotFilter.OWNER)) {
                if(plot.getPlotOwner() == freeBuildPlayer) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    if(plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Deine Gruppe <dark_gray>» " + plot.getPlotMembers().get(freeBuildPlayer).getGroupName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    }
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Mitglieder <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group != PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Besucher <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group == PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Einschränkungen <dark_gray>(<yellow>" + plot.getPlotSettings().size() + "<dark_gray>)").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    plot.getPlotSettings().forEach((plotSetting, state) -> lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("   <dark_gray>» " + (state ? "<green>" : "<red>") + plotSetting.getTitle() + (state ? " <dark_green>✔" : " <dark_red>✘")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                    if(!plot.getSettingState(PlotSetting.BROWSER_VISIBILITY) && plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(freeBuildPlayer != plot.getPlotOwner()) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);


                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);


                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }
                            }
                        });
                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }

                    if(plot.getSettingState(PlotSetting.BROWSER_VISIBILITY)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);

                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }

                                return;
                            }

                            if(inventoryClickEvent.isRightClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(FreeBuild.getInstance().getPlotManager().getRequests(plot) != null && FreeBuild.getInstance().getPlotManager().getRequests(plot).contains(freeBuildPlayer)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast eine Anfrage an dieses Grundstück bereits versendet."));

                                        return;
                                    }

                                    if(FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer) != null && FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer).contains(plot)) {
                                        plot.addMember(freeBuildPlayer);
                                        return;
                                    }

                                    freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast eine Anfrage an " + plot.getPlotName() + " <gray>geschickt."));

                                    plot.getPlotMembers().forEach((plotMember, plotGroup) -> {
                                        if(plot.getPlotMembers().get(plotMember) == PlotGroup.OWNER || plot.getPlotMembers().get(plotMember) == PlotGroup.ADMIN || plot.getPlotMembers().get(plotMember) == PlotGroup.MODERATOR) {
                                            if(plotMember.getPlayer().isOnline()) {
                                                plotMember.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>hat eine Beitrittsanfrage für dieses Grundstück geschickt."));
                                                plotMember.getPlayer().playSound(plotMember.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                                            }

                                        }
                                    });


                                    FreeBuild.getInstance().getPlotManager().requestToPlot(plot, freeBuildPlayer);
                                }
                            }
                        });

                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }
                }
                return;
            }



            if(freeBuildPlayer.getPlotFilter(PlotFilter.JOINABLE)) {
                if(plot.getSettingState(PlotSetting.JOINABLE)) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    if(plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Deine Gruppe <dark_gray>» " + plot.getPlotMembers().get(freeBuildPlayer).getGroupName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    }
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Mitglieder <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group != PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Besucher <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group == PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Einschränkungen <dark_gray>(<yellow>" + plot.getPlotSettings().size() + "<dark_gray>)").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    plot.getPlotSettings().forEach((plotSetting, state) -> lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("   <dark_gray>» " + (state ? "<green>" : "<red>") + plotSetting.getTitle() + (state ? " <dark_green>✔" : " <dark_red>✘")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                    if(!plot.getSettingState(PlotSetting.BROWSER_VISIBILITY) && plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(freeBuildPlayer != plot.getPlotOwner()) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);


                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }
                            }
                        });
                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }

                    if(plot.getSettingState(PlotSetting.BROWSER_VISIBILITY)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);

                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);

                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }

                                return;
                            }

                            if(inventoryClickEvent.isRightClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(FreeBuild.getInstance().getPlotManager().getRequests(plot) != null && FreeBuild.getInstance().getPlotManager().getRequests(plot).contains(freeBuildPlayer)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast eine Anfrage an dieses Grundstück bereits versendet."));

                                        return;
                                    }

                                    if(FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer) != null && FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer).contains(plot)) {
                                        plot.addMember(freeBuildPlayer);
                                        return;
                                    }

                                    freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast eine Anfrage an " + plot.getPlotName() + " <gray>geschickt."));

                                    plot.getPlotMembers().forEach((plotMember, plotGroup) -> {
                                        if(plot.getPlotMembers().get(plotMember) == PlotGroup.OWNER || plot.getPlotMembers().get(plotMember) == PlotGroup.ADMIN || plot.getPlotMembers().get(plotMember) == PlotGroup.MODERATOR) {
                                            if(plotMember.getPlayer().isOnline()) {
                                                plotMember.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>hat eine Beitrittsanfrage für dieses Grundstück geschickt."));
                                                plotMember.getPlayer().playSound(plotMember.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                                            }

                                        }
                                    });


                                    FreeBuild.getInstance().getPlotManager().requestToPlot(plot, freeBuildPlayer);
                                }
                            }
                        });

                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }
                }
                return;
            }



            if(freeBuildPlayer.getPlotFilter(PlotFilter.PVP)) {
                if(plot.getSettingState(PlotSetting.PVP)) {
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    if(plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Deine Gruppe <dark_gray>» " + plot.getPlotMembers().get(freeBuildPlayer).getGroupName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    }
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Mitglieder <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group != PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Besucher <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group == PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Einschränkungen <dark_gray>(<yellow>" + plot.getPlotSettings().size() + "<dark_gray>)").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                    plot.getPlotSettings().forEach((plotSetting, state) -> lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("   <dark_gray>» " + (state ? "<green>" : "<red>") + plotSetting.getTitle() + (state ? " <dark_green>✔" : " <dark_red>✘")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
                    lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

                    if(!plot.getSettingState(PlotSetting.BROWSER_VISIBILITY) && plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(freeBuildPlayer != plot.getPlotOwner()) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);

                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);

                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }
                            }
                        });
                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }

                    if(plot.getSettingState(PlotSetting.BROWSER_VISIBILITY)) {
                        this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                            if(inventoryClickEvent.isLeftClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                        return;
                                    }
                                    Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                                    location.setY(location.getWorld().getHighestBlockYAt(location) + 1);
                                    plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);


                                    freeBuildPlayer.getPlayer().teleport(location);
                                    freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);


                                } else {
                                    Player player = (Player) inventoryClickEvent.getWhoClicked();

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                                }

                                return;
                            }

                            if(inventoryClickEvent.isRightClick()) {
                                if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                                    if(FreeBuild.getInstance().getPlotManager().getRequests(plot) != null && FreeBuild.getInstance().getPlotManager().getRequests(plot).contains(freeBuildPlayer)) {
                                        freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                        freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast eine Anfrage an dieses Grundstück bereits versendet."));

                                        return;
                                    }

                                    if(FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer) != null && FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer).contains(plot)) {
                                        plot.addMember(freeBuildPlayer);
                                        return;
                                    }

                                    freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast eine Anfrage an " + plot.getPlotName() + " <gray>geschickt."));

                                    plot.getPlotMembers().forEach((plotMember, plotGroup) -> {
                                        if(plot.getPlotMembers().get(plotMember) == PlotGroup.OWNER || plot.getPlotMembers().get(plotMember) == PlotGroup.ADMIN || plot.getPlotMembers().get(plotMember) == PlotGroup.MODERATOR) {
                                            if(plotMember.getPlayer().isOnline()) {
                                                plotMember.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>hat eine Beitrittsanfrage für dieses Grundstück geschickt."));
                                                plotMember.getPlayer().playSound(plotMember.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                                            }

                                        }
                                    });


                                    FreeBuild.getInstance().getPlotManager().requestToPlot(plot, freeBuildPlayer);
                                }
                            }
                        });

                        lores.clear();
                        atomicInteger.getAndIncrement();



                        return;
                    }
                }
                return;
            }

            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            if(plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Deine Gruppe <dark_gray>» " + plot.getPlotMembers().get(freeBuildPlayer).getGroupName()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            }
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Mitglieder <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group != PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Besucher <dark_gray>» <yellow>" + plot.getPlotMembers().values().stream().filter(group -> group == PlotGroup.GUEST).toList().size()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Einschränkungen <dark_gray>(<yellow>" + plot.getPlotSettings().size() + "<dark_gray>)").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            plot.getPlotSettings().forEach((plotSetting, state) -> lores.add(FreeBuild.getInstance().getMiniMessage().deserialize("   <dark_gray>» " + (state ? "<green>" : "<red>") + plotSetting.getTitle() + (state ? " <dark_green>✔" : " <dark_red>✘")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));

            if(!plot.getSettingState(PlotSetting.BROWSER_VISIBILITY) && plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                    if(inventoryClickEvent.isLeftClick()) {
                        if(freeBuildPlayer != plot.getPlotOwner()) {
                            if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                return;
                            }
                            Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                            location.setY(location.getWorld().getHighestBlockYAt(location) + 1);

                            plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);

                            freeBuildPlayer.getPlayer().teleport(location);
                            freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                        } else {
                            Player player = (Player) inventoryClickEvent.getWhoClicked();

                            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                            new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                        }
                    }
                });
                lores.clear();
                atomicInteger.getAndIncrement();



                return;
            }

            if(plot.getSettingState(PlotSetting.BROWSER_VISIBILITY)) {
                this.setItem(atomicInteger.get(), new ItemBuilder(plot.getPlotBlock()).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» " + plot.getPlotName())).setGlowing(plot.getPlotMembers().containsKey(freeBuildPlayer)).setLore(lores).getItemStack(), inventoryClickEvent -> {
                    if(inventoryClickEvent.isLeftClick()) {
                        if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                            if(!plot.getSettingState(PlotSetting.JOINABLE)) {
                                freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du kannst dich nicht zum <yellow>Grundstück <red>teleportieren."));
                                return;
                            }
                            Location location = new Location(plot.getPlotChunks().get(0).toChunk().getWorld(), plot.getPlotChunks().get(0).toChunk().getX() << 4, 64, plot.getPlotChunks().get(0).toChunk().getZ() << 4).add(8, 0, 8);
                            location.setY(location.getWorld().getHighestBlockYAt(location) + 1);

                            plot.enter(FreeBuild.getInstance().getPlotManager().getPlotByChunk(freeBuildPlayer.getPlayer().getChunk()), freeBuildPlayer);

                            freeBuildPlayer.getPlayer().teleport(location);
                            freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

                            
                        } else {
                            Player player = (Player) inventoryClickEvent.getWhoClicked();

                            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                            new PlotOverview(freeBuildPlayer, plot).open(freeBuildPlayer.getPlayer());
                        }

                        return;
                    }

                    if(inventoryClickEvent.isRightClick()) {
                        if(!plot.getPlotMembers().containsKey(freeBuildPlayer)) {
                            if(FreeBuild.getInstance().getPlotManager().getRequests(plot) != null && FreeBuild.getInstance().getPlotManager().getRequests(plot).contains(freeBuildPlayer)) {
                                freeBuildPlayer.getPlayer().playSound(freeBuildPlayer.getPlayer(), Sound.ENTITY_BLAZE_HURT, 1, 1);
                                freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast eine Anfrage an dieses Grundstück bereits versendet."));

                                return;
                            }

                            if(FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer) != null && FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer).contains(plot)) {
                                plot.addMember(freeBuildPlayer);
                                return;
                            }

                            freeBuildPlayer.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<gray>Du hast eine Anfrage an " + plot.getPlotName() + " <gray>geschickt."));

                            plot.getPlotMembers().forEach((plotMember, plotGroup) -> {
                                if(plot.getPlotMembers().get(plotMember) == PlotGroup.OWNER || plot.getPlotMembers().get(plotMember) == PlotGroup.ADMIN || plot.getPlotMembers().get(plotMember) == PlotGroup.MODERATOR) {
                                    if(plotMember.getPlayer().isOnline()) {
                                        plotMember.getPlayer().sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + PermissionCenterModulePaper.getAPI().getOnlineUser(freeBuildPlayer.getPlayer()).getDisplayString() + freeBuildPlayer.getUserName() + " <gray>hat eine Beitrittsanfrage für dieses Grundstück geschickt."));
                                        plotMember.getPlayer().playSound(plotMember.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
                                    }

                                }
                            });


                            FreeBuild.getInstance().getPlotManager().requestToPlot(plot, freeBuildPlayer);
                        }
                    }
                });

                lores.clear();
                atomicInteger.getAndIncrement();



                return;
            }


        });


        this.setItem(47, new ItemBuilder(Material.BOOK).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <green>Ausstehende Anfragen")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            if(FreeBuild.getInstance().getPlotManager().getRequests(freeBuildPlayer) == null ){

                player.playSound(player, Sound.ENTITY_BLAZE_HURT, 1, 1);
                player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(FreeBuild.getInstance().getPREFIX() + "<red>Du hast keine offenen Anfragen."));
                return;
            }

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new PlotRequests(freeBuildPlayer, null, false).open(player);
        });

        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        for(PlotFilter plotFilter : PlotFilter.values()) {
            lores.add(FreeBuild.getInstance().getMiniMessage().deserialize((!freeBuildPlayer.getPlotFilter(plotFilter) ? "<dark_gray>» <gray>" + plotFilter.getName() : "<dark_green>» <gray><b>" + plotFilter.getName() + "</b>")).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>» <gray>Klicke hier, um den Filter zu ändern.").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        lores.add(FreeBuild.getInstance().getMiniMessage().deserialize(""));
        this.setItem(50, new ItemBuilder(Material.HOPPER).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <yellow>Filter")).setLore(lores).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            if(freeBuildPlayer.getPlotFilter(PlotFilter.ALL)) {
                freeBuildPlayer.changePlotFilter(PlotFilter.ALL);
                freeBuildPlayer.changePlotFilter(PlotFilter.MEMBER);

            } else if(freeBuildPlayer.getPlotFilter(PlotFilter.MEMBER)) {
                freeBuildPlayer.changePlotFilter(PlotFilter.MEMBER);
                freeBuildPlayer.changePlotFilter(PlotFilter.OWNER);

            } else if(freeBuildPlayer.getPlotFilter(PlotFilter.OWNER)) {
                freeBuildPlayer.changePlotFilter(PlotFilter.OWNER);
                freeBuildPlayer.changePlotFilter(PlotFilter.JOINABLE);

            } else if(freeBuildPlayer.getPlotFilter(PlotFilter.JOINABLE)) {
                freeBuildPlayer.changePlotFilter(PlotFilter.JOINABLE);
                freeBuildPlayer.changePlotFilter(PlotFilter.PVP);

            } else if(freeBuildPlayer.getPlotFilter(PlotFilter.PVP)) {
                freeBuildPlayer.changePlotFilter(PlotFilter.PVP);
                freeBuildPlayer.changePlotFilter(PlotFilter.ALL);
            }

            new PlotBrowser(freeBuildPlayer).open(player);
        });
        lores.clear();

        this.setItem(53, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayName(FreeBuild.getInstance().getMiniMessage().deserialize("<dark_red>« <red>Zurück")).getItemStack(), inventoryClickEvent -> {
            Player player = (Player) inventoryClickEvent.getWhoClicked();

            player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);

            new MenuInventories.DefaultInventory(freeBuildPlayer).open(player);
        });

        this.addClickHandler(inventoryClickEvent -> {
            inventoryClickEvent.setCancelled(true);
        });
    }
}
