package de.threeseconds.crafting;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PacketHandler {

    public PacketHandler() {
        this.registerListener(FreeBuild.getInstance());
    }

    private void registerListener(FreeBuild freeBuild) {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(freeBuild.getPaperCore(), PacketType.Play.Server.SET_SLOT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                ItemStack item = packet.getItemModifier().read(0);

                packet.getItemModifier().write(0, replaceData(item));
            }
        });

        manager.addPacketListener(new PacketAdapter(freeBuild.getPaperCore(), PacketType.Play.Server.WINDOW_ITEMS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();

                List<ItemStack> items = packet.getItemListModifier().readSafely(0);

                items.replaceAll(item -> replaceData(item));

                packet.getItemListModifier().write(0, items);
            }
        });

    }

    private ItemStack replaceData(ItemStack item) {
        if (item == null || item.getType().name().contains("AIR"))
            return item;

        ItemStack copy = item.clone();
        ItemMeta copyMeta = copy.getItemMeta();

        List<Component> newLore = copyMeta.lore();

        if (newLore == null)
            newLore = new ArrayList<>();

        newLore = removeLore(newLore);

        newLore.add(MiniMessage.miniMessage().deserialize(" "));
        newLore.add(MiniMessage.miniMessage().deserialize("<b>" + ItemRarity.COMMON.getColorCode() + ItemRarity.COMMON.name()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));

        copyMeta.lore(newLore);

        copy.setItemMeta(copyMeta);

        return copy;
    }

    private List<Component> removeLore(List<Component> original) {
        List<Component> copy = new ArrayList<>(original);

        List<Component> secondCopy = new ArrayList<>(copy);
        for (Component line : copy)
            if (line.contains(MiniMessage.miniMessage().deserialize("<b>" + ItemRarity.COMMON.getColorCode() + ItemRarity.COMMON.name()).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE))) {
                secondCopy.remove(line);
            }

        copy = secondCopy;


        return copy;
    }

}
