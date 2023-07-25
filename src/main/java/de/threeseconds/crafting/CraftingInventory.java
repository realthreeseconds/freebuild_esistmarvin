package de.threeseconds.crafting;

import com.google.common.primitives.Ints;
import de.threeseconds.FreeBuild;
import de.threeseconds.crafting.CraftingHandle;
import de.threeseconds.util.InventoryBuilder;
import de.threeseconds.util.ItemBuilder;
import io.papermc.paper.configuration.constraint.Constraints;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventoryCrafting;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

public class CraftingInventory extends InventoryBuilder {

    private final Character[] CRAFT_CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i'};
    private final int[] INPUT_SLOTS = {10, 11, 12, 19, 20, 21, 28, 29, 30};

    public CraftingInventory() {
        super(9*5, FreeBuild.getInstance().getMiniMessage().deserialize("<dark_gray>» <gold>Crafting <dark_gray>● <gray>Menü"));

        this.setItems(0, 9*5 - 1, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName(MiniMessage.miniMessage().deserialize("<black>")).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        /* Inventory
        this.setItems(10, 12, new ItemBuilder(Material.AIR).getItemStack(), inventoryClickEvent -> CraftingHandle.craftItems(this.getInventory()));
        this.setItems(19, 21, new ItemBuilder(Material.AIR).getItemStack(), inventoryClickEvent -> CraftingHandle.craftItems(this.getInventory()));
        this.setItems(28, 30, new ItemBuilder(Material.AIR).getItemStack(), inventoryClickEvent -> CraftingHandle.craftItems(this.getInventory()));
*/
        String notCrafted = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNmZTg4NDVhOGQ1ZTYzNWZiODc3MjhjY2M5Mzg5NWQ0MmI0ZmMyZTZhNTNmMWJhNzhjODQ1MjI1ODIyIn19fQ==";
        String crafted = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYzMzlmZjJlNTM0MmJhMThiZGM0OGE5OWNjYTY1ZDEyM2NlNzgxZDg3ODI3MmY5ZDk2NGVhZDNiOGFkMzcwIn19fQ==";

        this.setItems(this.INPUT_SLOTS, new ItemBuilder(Material.AIR).getItemStack());



        this.setItem(23, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(notCrafted).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

        this.setItem(25, new ItemBuilder(Material.BARRIER).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));


        this.addClickHandler(inventoryClickEvent -> {
            if(Objects.equals(inventoryClickEvent.getClickedInventory(), this.getInventory())) {
                //if(Ints.contains(this.INPUT_SLOTS, inventoryClickEvent.getSlot())) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            Recipe recipe = Bukkit.getCraftingRecipe(getCraftingMatrix(), Bukkit.getWorld("freebuildWorld"));

                            if(recipe == null) {

                                setItem(23, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(notCrafted).getItemStack(), inventoryClickEvent1 -> inventoryClickEvent1.setCancelled(true));
                                setItem(25, new ItemBuilder(Material.BARRIER).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

                            } else {

                                if(FreeBuild.getInstance().checkPDC("copper_infused_cobblestone", recipe.getResult().getItemMeta().getPersistentDataContainer(), "<gold><b>Copper Infused Cobblestone</b>")) {
                                    for(ItemStack itemStack : getCraftingMatrix()) {
                                        if(itemStack.getType() == Material.COBBLESTONE && itemStack.getAmount() <= 32) {
                                            setItem(23, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(notCrafted).getItemStack(), inventoryClickEvent1 -> inventoryClickEvent1.setCancelled(true));
                                            setItem(25, new ItemBuilder(Material.BARRIER).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

                                            return;
                                        }
                                    }
                                }


                                setItem(23, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(crafted).getItemStack(), inventoryClickEvent1 -> inventoryClickEvent1.setCancelled(true));
                                setItem(25, recipe.getResult(), inventoryClickEvent1 -> {
                                    inventoryClickEvent1.setCancelled(true);
                                    Player player = (Player) inventoryClickEvent1.getWhoClicked();

                                    if(inventoryClickEvent1.getCurrentItem() == null) return;

                                    int multiItem = 1;

                                    for(ItemStack itemStack : getCraftingMatrix()) {
                                        if(itemStack != null) {
                                            if(inventoryClickEvent1.isShiftClick()) {
                                                if(itemStack.getAmount() > 1) {
                                                    multiItem = itemStack.getAmount();
                                                    itemStack.setAmount(itemStack.getAmount() > 1 ? 0 : itemStack.getAmount() - 1);
                                                } else itemStack.setAmount(itemStack.getAmount() - 1);
                                            } else itemStack.setAmount(itemStack.getAmount() - 1);

                                        }

                                    }

                                    player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                    player.getInventory().addItem(inventoryClickEvent1.isShiftClick() ? new ItemStack(inventoryClickEvent1.getCurrentItem().getType(), inventoryClickEvent1.getCurrentItem().getAmount() * multiItem) : inventoryClickEvent1.getCurrentItem());

                                });
                            }

                        }
                    }.runTaskLater(FreeBuild.getInstance().getPaperCore(), 1L);
                }
            //}


        });

        this.addDragHandler(inventoryDragEvent -> {
            if(Objects.equals(inventoryDragEvent.getInventory(), this.getInventory())) {
                //if(Ints.contains(this.INPUT_SLOTS, inventoryClickEvent.getSlot())) {
                new BukkitRunnable() {
                    @Override
                    public void run() {

                        Recipe recipe = Bukkit.getCraftingRecipe(getCraftingMatrix(), Bukkit.getWorld("freebuildWorld"));

                        if(recipe == null) {

                            setItem(23, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(notCrafted).getItemStack(), inventoryClickEvent1 -> inventoryClickEvent1.setCancelled(true));
                            setItem(25, new ItemBuilder(Material.BARRIER).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

                        } else {

                            if(FreeBuild.getInstance().checkPDC("copper_infused_cobblestone", recipe.getResult().getItemMeta().getPersistentDataContainer(), "<gold><b>Copper Infused Cobblestone</b>")) {
                                for(ItemStack itemStack : getCraftingMatrix()) {
                                    if(itemStack.getType() == Material.COBBLESTONE && itemStack.getAmount() <= 32) {
                                        setItem(23, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(notCrafted).getItemStack(), inventoryClickEvent1 -> inventoryClickEvent1.setCancelled(true));
                                        setItem(25, new ItemBuilder(Material.BARRIER).getItemStack(), inventoryClickEvent -> inventoryClickEvent.setCancelled(true));

                                        return;
                                    }
                                }
                            }

                            setItem(23, new ItemBuilder(Material.PLAYER_HEAD).setSkullTexture(crafted).getItemStack(), inventoryClickEvent1 -> inventoryClickEvent1.setCancelled(true));
                            setItem(25, recipe.getResult(), inventoryClickEvent1 -> {
                                inventoryClickEvent1.setCancelled(true);
                                Player player = (Player) inventoryClickEvent1.getWhoClicked();

                                if(inventoryClickEvent1.getCurrentItem() == null) return;

                                int multiItem = 1;

                                for(ItemStack itemStack : getCraftingMatrix()) {
                                    if(itemStack != null) {
                                        if(inventoryClickEvent1.isShiftClick()) {
                                            if(itemStack.getAmount() > 1) {
                                                multiItem = itemStack.getAmount();
                                                itemStack.setAmount(itemStack.getAmount() > 1 ? 0 : itemStack.getAmount() - 1);
                                            } else itemStack.setAmount(itemStack.getAmount() - 1);
                                        } else itemStack.setAmount(itemStack.getAmount() - 1);

                                    }

                                }

                                player.playSound(player, Sound.ENTITY_ITEM_PICKUP, 1, 1);
                                player.getInventory().addItem(inventoryClickEvent1.isShiftClick() ? new ItemStack(inventoryClickEvent1.getCurrentItem().getType(), inventoryClickEvent1.getCurrentItem().getAmount() * multiItem) : inventoryClickEvent1.getCurrentItem());


                            });
                        }

                    }
                }.runTaskLater(FreeBuild.getInstance().getPaperCore(), 1L);
            }
            //}
        });

        this.addCloseHandler(inventoryCloseEvent -> {
            for(ItemStack itemStack : getCraftingMatrix()) {
                if(itemStack != null) {
                    ((CraftPlayer) inventoryCloseEvent.getPlayer()).getHandle().drop(net.minecraft.world.item.ItemStack.fromBukkitCopy(itemStack), false);
                }
            }
        });

    }



    public ItemStack[] getCraftingMatrix() {
        List<ItemStack> itemStacks = new ArrayList<>();
        for(int i = 10; i <= 12; i++) itemStacks.add(this.getInventory().getItem(i));
        for(int i = 19; i <= 21; i++) itemStacks.add(this.getInventory().getItem(i));
        for(int i = 28; i <= 30; i++) itemStacks.add(this.getInventory().getItem(i));

        return itemStacks.toArray(new ItemStack[0]);
    }
}
