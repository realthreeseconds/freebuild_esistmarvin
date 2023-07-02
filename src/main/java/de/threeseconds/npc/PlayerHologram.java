package de.threeseconds.npc;

import com.mojang.math.Transformation;
import de.threeseconds.FreeBuild;
import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.lang.reflect.Field;
import java.util.*;

public class PlayerHologram {

    private final String name;

    private final Player player;

    private Location location;

    private List<String> lines;

    private float scale, shadowRadius, shadowStrength;

    private ChatFormatting background;

    private boolean textShadow, visible;

    private Display.BillboardConstraints billboard;

    private Display.TextDisplay entity;

    private PlayerNPC linkedPlayerNPC;

    public PlayerHologram(Player player, String name, Location location, String... lines) {
        this.name = name;
        this.player = player;
        this.location = location;
        this.lines = Arrays.stream(lines).toList();

        this.billboard = Display.BillboardConstraints.CENTER;
        this.scale = 1f;
        this.shadowRadius = 0f;
        this.shadowStrength = 1f;
        this.textShadow = false;
    }

    public PlayerHologram(Player player, String name, PlayerNPC playerNPC, String... lines) {
        this.name = name;
        this.player = player;
        linkPlayerNPC(playerNPC);
        this.lines = Arrays.stream(lines).toList();

        this.billboard = Display.BillboardConstraints.CENTER;
        this.scale = 1f;
        this.shadowRadius = 0f;
        this.shadowStrength = 1f;
        this.textShadow = false;
    }

    public PlayerHologram register() {
        FreeBuild.getInstance().getHologramManager().addPlayerHologram(this);
        return this;
    }

    public PlayerHologram unregister() {
        FreeBuild.getInstance().getHologramManager().removePlayerHologram(this);
        return this;
    }

    public PlayerHologram create() {
        entity = new Display.TextDisplay(EntityType.TEXT_DISPLAY, ((CraftWorld) location.getWorld()).getHandle());
        entity.setLineWidth(1000);

        return this;
    }

    public void spawn() {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        if(!location.getWorld().getName().equalsIgnoreCase(serverPlayer.level().getWorld().getName())) return;

        ArrayList<Packet<ClientGamePacketListener>> packets = new ArrayList<>();

        if(entity == null) create();

        syncToPlayerNPC();

        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(entity);
        packets.add(addEntityPacket);

        entity.setPosRaw(location.x(), location.y(), location.z());
        entity.setYRot(location.getYaw());

        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(entity);
        packets.add(teleportEntityPacket);

        Transformation transformation = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(scale, scale, scale), new Quaternionf());
        entity.setTransformation(transformation);
        entity.setBillboardConstraints(billboard);

        if(background == ChatFormatting.RESET || background == null) entity.setBackgroundColor(Display.TextDisplay.INITIAL_BACKGROUND);
        else if(background == ChatFormatting.ITALIC)  entity.setBackgroundColor(0);
        else entity.setBackgroundColor(background.getColor() | 0xC8000000);

        entity.setShadowRadius(shadowRadius);
        entity.setShadowStrength(shadowStrength);
        entity.setText(getText());

        if(textShadow) entity.setFlags((byte)(entity.getFlags() | Display.TextDisplay.FLAG_SHADOW));
        else entity.setFlags((byte)(entity.getFlags() & ~Display.TextDisplay.FLAG_SHADOW));

        Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = (Int2ObjectMap<SynchedEntityData.DataItem<?>>) getValue(entity.getEntityData(), "e");
        ArrayList<SynchedEntityData.DataValue<?>> entityData = new ArrayList<>();
        for (SynchedEntityData.DataItem<?> dataItem : itemsById.values()) {
            entityData.add(dataItem.value());
        }
        ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(entity.getId(), entityData);
        packets.add(setEntityDataPacket);

        ClientboundBundlePacket bundlePacket = new ClientboundBundlePacket(packets);
        serverPlayer.connection.send(bundlePacket);

        visible = true;

    }

    public void remove() {
        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(entity.getId());
        ((CraftPlayer) player).getHandle().connection.send(removeEntitiesPacket);
        visible = false;
    }

    private void syncToPlayerNPC() {
        if(linkedPlayerNPC == null) return;

        double offset = 0.5d;
        if(linkedPlayerNPC.getNPC().getType() == EntityType.WITCH) offset = 1.0D;

        location = linkedPlayerNPC.getLocation().clone().add(0, linkedPlayerNPC.getNPC().getEyeHeight() + offset, 0);
        linkedPlayerNPC.linkPlayerHologram(this);
    }

    public void linkPlayerNPC(PlayerNPC playerNPC) {
        this.linkedPlayerNPC = playerNPC;
        syncToPlayerNPC();
    }

    public void refreshEntityData(){

        Transformation transformation = new Transformation(new Vector3f(), new Quaternionf(), new Vector3f(scale, scale, scale), new Quaternionf());
        entity.setTransformation(transformation);
        entity.setBillboardConstraints(billboard);

        if(background == ChatFormatting.RESET || background == null) entity.setBackgroundColor(Display.TextDisplay.INITIAL_BACKGROUND);
        else if(background == ChatFormatting.ITALIC)  entity.setBackgroundColor(0);
        else entity.setBackgroundColor(background.getColor() | 0xC8000000);

        entity.setShadowRadius(shadowRadius);
        entity.setShadowStrength(shadowStrength);
        entity.setText(getText());

        if(textShadow) entity.setFlags((byte)(entity.getFlags() | Display.TextDisplay.FLAG_SHADOW));
        else entity.setFlags((byte)(entity.getFlags() & ~Display.TextDisplay.FLAG_SHADOW));

        Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = (Int2ObjectMap<SynchedEntityData.DataItem<?>>) getValue(entity.getEntityData(), "e");
        ArrayList<SynchedEntityData.DataValue<?>> entityData = new ArrayList<>();
        for (SynchedEntityData.DataItem<?> dataItem : itemsById.values()) {
            entityData.add(dataItem.value());
        }
        ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(entity.getId(), entityData);
        ((CraftPlayer) player).getHandle().connection.send(setEntityDataPacket);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public PlayerHologram setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        return this;
    }

    public PlayerHologram setShadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
        return this;
    }

    public PlayerHologram setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public PlayerHologram setBackground(ChatFormatting background) {
        this.background = background;
        return this;
    }

    public PlayerHologram setBillboard(Display.BillboardConstraints billboard) {
        this.billboard = billboard;
        return this;
    }

    public PlayerHologram setLines(String... lines) {
        this.lines = List.of(lines);
        return this;
    }

    public PlayerHologram setLines(ArrayList<String> lines) {
        this.lines = lines;
        return this;
    }

    public Display.TextDisplay getEntity() {
        return entity;
    }

    private Component getText() {
        String t = String.join("\n", lines);
        TagResolver resolver = TagResolver.empty();
        return PaperAdventure.asVanilla(MiniMessage.miniMessage().deserialize(t, resolver));
    }

    public String getName() {
        return name;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getLines() {
        return lines;
    }

    public Display.BillboardConstraints getBillboard() {
        return billboard;
    }

    public float getScale() {
        return scale;
    }

    public ChatFormatting getBackground() {
        return background;
    }

    public float getShadowRadius() {
        return shadowRadius;
    }

    public float getShadowStrength() {
        return shadowStrength;
    }

    public boolean hasTextShadow() {
        return textShadow;
    }

    public PlayerNPC getLinkedPlayerNPC() {
        return linkedPlayerNPC;
    }

    public boolean isVisible() {
        return visible;
    }

    private void setValue(Object instance, String name, Object value) {
        try {
            Field field = instance.getClass().getDeclaredField(name);

            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Object getValue(Object instance, String name) {
        Object result = null;

        try {
            Field field = instance.getClass().getDeclaredField(name);

            field.setAccessible(true);
            result = field.get(instance);
            field.setAccessible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
