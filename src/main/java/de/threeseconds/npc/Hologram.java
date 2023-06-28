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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Hologram {

    private final String name;

    private Location location;

    private ArrayList<String> lines;

    private float scale, shadowRadius, shadowStrength;

    private ChatFormatting background;

    private boolean textShadow;

    private Display.BillboardConstraints billboard;

    private final HashMap<UUID, Boolean> isVisibleForPlayer = new HashMap<>();

    private Display.TextDisplay entity;

    private NPC linkedNPC;

    public Hologram(String name, Location location, ArrayList<String> lines) {
        this.name = name;
        this.location = location;
        this.lines = lines;

        this.billboard = Display.BillboardConstraints.CENTER;
        this.scale = 1f;
        this.shadowRadius = 0f;
        this.shadowStrength = 1f;
        this.textShadow = false;
    }

    public Hologram(String name, NPC npc, ArrayList<String> lines) {
        this.name = name;
        syncToNPC(npc);
        this.lines = lines;

        this.billboard = Display.BillboardConstraints.CENTER;
        this.scale = 1f;
        this.shadowRadius = 0f;
        this.shadowStrength = 1f;
        this.textShadow = false;
    }

    public Hologram create() {
        entity = new Display.TextDisplay(EntityType.TEXT_DISPLAY, ((CraftWorld) location.getWorld()).getHandle());
        entity.setLineWidth(1000);

        FreeBuild.getInstance().getHologramManager().addHologram(this);

        return this;
    }

    public void delete() {
        FreeBuild.getInstance().getHologramManager().removeHologram(this);
        entity = null;
    }

    public void spawn(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();

        if(!location.getWorld().getName().equalsIgnoreCase(serverPlayer.level().getWorld().getName())) return;

        ArrayList<Packet<ClientGamePacketListener>> packets = new ArrayList<>();

        if(entity == null) create();

        syncToNPC();

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

        isVisibleForPlayer.put(player.getUniqueId(), true);

    }

    public void remove(Player player) {
        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(entity.getId());
        ((CraftPlayer) player).getHandle().connection.send(removeEntitiesPacket);
        isVisibleForPlayer.put(player.getUniqueId(), false);
    }

    public void syncToNPC() {
        if(linkedNPC == null) return;

        double offset = 0.5d;
        if(linkedNPC.getNPC().getType() == EntityType.WITCH) offset = 1.0D;

        location = linkedNPC.getLocation().clone().add(0, linkedNPC.getNPC().getEyeHeight() + offset, 0);
        linkedNPC.linkHologram(this);
    }

    public void syncToNPC(NPC npc) {
        this.linkedNPC = npc;
        syncToNPC();
    }

    public void refreshEntityData(Player player){
        Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = (Int2ObjectMap<SynchedEntityData.DataItem<?>>) getValue(entity.getEntityData(), "e");
        ArrayList<SynchedEntityData.DataValue<?>> entityData = new ArrayList<>();
        for (SynchedEntityData.DataItem<?> dataItem : itemsById.values()) {
            entityData.add(dataItem.value());
        }
        ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(entity.getId(), entityData);
        ((CraftPlayer) player).getHandle().connection.send(setEntityDataPacket);
    }

    public Hologram setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
        return this;
    }

    public Hologram setShadowStrength(float shadowStrength) {
        this.shadowStrength = shadowStrength;
        return this;
    }

    public Hologram setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public Hologram setBackground(ChatFormatting background) {
        this.background = background;
        return this;
    }

    public Hologram setBillboard(Display.BillboardConstraints billboard) {
        this.billboard = billboard;
        return this;
    }

    public Hologram setLines(ArrayList<String> lines) {
        this.lines = lines;
        return this;
    }

    private Component getText() {
        String t = String.join("\n", lines);
        TagResolver resolver = TagResolver.empty();
        return PaperAdventure.asVanilla(MiniMessage.miniMessage().deserialize(t, resolver));
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public ArrayList<String> getLines() {
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

    public NPC getLinkedNPC() {
        return linkedNPC;
    }

    public HashMap<UUID, Boolean> getIsVisibleForPlayer() {
        return isVisibleForPlayer;
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
