package de.threeseconds.util;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;

import javax.naming.Name;
import javax.xml.stream.events.Namespace;
import java.util.*;

public class ItemBuilder {

    private ItemStack itemStack;
    private String key;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder setDamage(int damage) {
        if (!(this.itemStack.getItemMeta() instanceof Damageable))
            return this;
        ((Damageable)this.itemStack.getItemMeta()).setDamage(damage);
        return this;
    }

    public ItemBuilder addItemFlag(ItemFlag itemFlag) {
        return addItemFlags(Collections.singletonList(itemFlag));
    }

    public ItemBuilder addItemFlags(List<ItemFlag> itemFlags) {
        return addItemFlags(itemFlags.toArray(new ItemFlag[0]));
    }

    public ItemBuilder removeItemFlag(ItemFlag itemFlag) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.removeItemFlags(itemFlag);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setSkullMeta(SkullMeta skullMeta) {
        this.itemStack.setItemMeta((ItemMeta)skullMeta);
        return this;
    }

    public ItemBuilder setSkullTexture(PlayerTextures playerTextures) {
        SkullMeta skullMeta = (SkullMeta)this.itemStack.getItemMeta();
        PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
        playerProfile.setTextures(playerTextures);
        skullMeta.setPlayerProfile(playerProfile);
        setSkullMeta(skullMeta);
        return this;
    }

    public ItemBuilder setSkullTexture(String skinValue) {
        SkullMeta skullMeta = (SkullMeta)this.itemStack.getItemMeta();
        PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
        playerProfile.setProperty(new ProfileProperty("textures", skinValue));
        skullMeta.setPlayerProfile(playerProfile);
        setSkullMeta(skullMeta);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag[] itemFlags) {
        ItemMeta itemMeta = this.itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        this.itemStack.getItemMeta().addEnchant(enchantment, level, ignoreLevelRestriction);
        return this;
    }

    public ItemBuilder addEnchantments(HashMap<Enchantment, Integer> enchantmentLevelsMap, boolean ignoreLevelRestriction) {
        enchantmentLevelsMap.forEach((enchantment, level) -> this.itemStack.getItemMeta().addEnchant(enchantment, level.intValue(), ignoreLevelRestriction));
        return this;
    }

    public ItemBuilder setGlowing() {
        Enchantment enchantment;
        if (this.itemStack.getType() == Material.BOW) {
            enchantment = Enchantment.LURE;
        } else {
            enchantment = Enchantment.ARROW_INFINITE;
        }
        addEnchantment(enchantment, 0, true);
        addItemFlag(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        this.itemStack.getItemMeta().setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder addLore(List<Component> loreLines) {
        List<Component> loreList = this.itemStack.lore();
        assert loreList != null;
        loreList.addAll(loreLines);
        return this;
    }

    public ItemBuilder addLore(Component lore) {
        List<Component> loreList = this.itemStack.lore();
        loreList.add(lore);
        return this;
    }

    public ItemBuilder setLore(String lore) {
        this.itemStack.getItemMeta().lore(Collections.singletonList(Component.text(lore)));
        return this;
    }

    public ItemBuilder setLore(List<Component> loreLines) {
        ItemMeta itemMeta = getItemStack().getItemMeta();
        itemMeta.lore(loreLines);
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public ItemBuilder setDisplayName(Component displayName) {
        ItemMeta itemMeta = getItemStack().getItemMeta();
        itemMeta.displayName(displayName.decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        if(this.key != null) itemMeta.getPersistentDataContainer().set(new NamespacedKey(FreeBuild.getInstance().getPaperCore(), this.key), PersistentDataType.STRING, FreeBuild.getInstance().getMiniMessage().serialize(displayName));
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta itemMeta) {
        this.itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setMaterial(Material material) {
        this.itemStack.setType(material);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }

    public ItemStack getItemStack() {
        return this.itemStack;
    }
}