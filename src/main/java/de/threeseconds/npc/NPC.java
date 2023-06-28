package de.threeseconds.npc;

import com.destroystokyo.paper.entity.Pathfinder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import de.threeseconds.FreeBuild;
import io.papermc.paper.adventure.PaperAdventure;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class NPC {

    private Entity entity;

    private EntityType<?> entityType;

    private final HashMap<UUID, Boolean> isTeamCreated = new HashMap<>();
    private final HashMap<UUID, Boolean> isVisibleForPlayer = new HashMap<>();

    private final ArrayList<Player> sneakingForPlayer = new ArrayList<>();

    private String name, internName, displayName;

    private String skinValue, skinSignature;

    private Location location;

    private double visibiltyDistance, turnToDistance;

    private boolean showInTab, glowing, turnToPlayer, sneaking, collidable;

    private ChatFormatting glowingColor;

    private HashMap<EquipmentSlot, ItemStack> equipment;

    public NPC(String name, Location location) {
        this.name = name;
        this.entityType = EntityType.PLAYER;
        this.displayName = "";
        this.location = location;
        this.showInTab = false;
        this.turnToPlayer = false;
        this.glowing = false;
        this.glowingColor = ChatFormatting.WHITE;
        this.collidable = false;
        this.sneaking = false;

        this.visibiltyDistance = 35D;
        this.turnToDistance = 15D;

        setSkin("", "");

        internName = generateInternName();
    }

    public NPC(String name, EntityType<?> entityType, Location location) {
        this.name = name;
        this.entityType = entityType;
        this.displayName = "";
        this.location = location;
        this.showInTab = false;
        this.turnToPlayer = false;
        this.glowing = false;
        this.glowingColor = ChatFormatting.WHITE;
        this.collidable = false;
        this.sneaking = false;

        this.visibiltyDistance = 20D;
        this.turnToDistance = 5D;

        setSkin("", "");

        internName = generateInternName();
    }

    public String generateInternName() {
        final char[] nameChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o', 'r'};
        StringBuilder returnValue = new StringBuilder();
        for (int i = 0; i < 8; i++)
            returnValue.append("§").append(nameChars[(int) (ThreadLocalRandom.current().nextDouble() * nameChars.length)]);
        return returnValue.toString();
    }

    public NPC register() {
        FreeBuild.getInstance().getNPCManager().addNPC(this);
        return this;
    }

    public NPC unregister() {
        FreeBuild.getInstance().getNPCManager().removeNPC(this);
        return this;
    }

    public NPC create() {

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), internName);

        if(!skinValue.isEmpty() && !skinSignature.isEmpty())
            gameProfile.getProperties().put("textures", new Property("textures", skinValue, skinSignature));


        this.entity = entityType == EntityType.PLAYER
                ? new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) location.getWorld()).getHandle(), gameProfile)
                : ((EntityType.EntityFactory) getValue(entityType, "bA")).create(entityType, ((CraftWorld) location.getWorld()).getHandle());

        return this;
    }

    private Hologram linkedHologram;

    public NPC linkHologram(Hologram hologram) {
        this.linkedHologram = hologram;
        return this;
    }

    public Hologram getLinkedHologram() {
        return linkedHologram;
    }

    public NPC unlinkHologram() {
        this.linkedHologram = null;
        return this;
    }

    public void spawn(Player bukkitPlayer) {
        ServerPlayer serverPlayer = ((CraftPlayer) bukkitPlayer).getHandle();

        if(entity == null) return;

        if (!location.getWorld().getName().equalsIgnoreCase(serverPlayer.level().getWorld().getName())) return;

        ArrayList<Packet<ClientGamePacketListener>> packets = new ArrayList<>();

        Component vanillaComponent = PaperAdventure.asVanilla(MiniMessage.miniMessage().deserialize(displayName));

        entity.setCustomName(null);
        entity.setCustomNameVisible(true);

        if(entity instanceof ServerPlayer player) {
            player.listName = vanillaComponent;

            EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.noneOf(ClientboundPlayerInfoUpdatePacket.Action.class);
            actions.add(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER);
            actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME);
            if(showInTab) actions.add(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED);

            ClientboundPlayerInfoUpdatePacket playerInfoPacket = new ClientboundPlayerInfoUpdatePacket(actions, List.of(player));
            packets.add(playerInfoPacket);

            entity.setPos(location.x(), location.y(), location.z());
            entity.noPhysics = false;
            ClientboundAddPlayerPacket spawnPlayerPacket = new ClientboundAddPlayerPacket(player);
            packets.add(spawnPlayerPacket);


            String teamName = "NPC-" + internName;

            PlayerTeam team = new PlayerTeam(serverPlayer.getScoreboard(), teamName);
            team.setColor(glowingColor);
            team.setNameTagVisibility(Team.Visibility.NEVER);

            team.getPlayers().clear();
            team.getPlayers().add(player.getGameProfile().getName());
            team.setPlayerPrefix(vanillaComponent);

            boolean isTeamCreatedForPlayer = isTeamCreated.getOrDefault(serverPlayer.getUUID(), false);
            ClientboundSetPlayerTeamPacket setPlayerTeamPacket = ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(team, !isTeamCreatedForPlayer);
            packets.add(setPlayerTeamPacket);
            if (!isTeamCreatedForPlayer) isTeamCreated.put(serverPlayer.getUUID(), true);

        } else {
            ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(entity);
            packets.add(addEntityPacket);
        }

        entity.setGlowingTag(glowing);

        if(equipment != null && equipment.size() > 0) {
            List<Pair<EquipmentSlot, ItemStack>> equipmentList = new ArrayList<>();

            equipment.keySet().forEach(slot -> {
                equipmentList.add(new Pair<>(slot, equipment.get(slot)));
            });

            ClientboundSetEquipmentPacket setEquipmentPacket = new ClientboundSetEquipmentPacket(entity.getId(), equipmentList);
            packets.add(setEquipmentPacket);
        }

        ClientboundBundlePacket bundlePacket = new ClientboundBundlePacket(packets);
        serverPlayer.connection.send(bundlePacket);

        if(entity instanceof ServerPlayer) entity.getEntityData().set(net.minecraft.world.entity.player.Player.DATA_PLAYER_MODE_CUSTOMISATION, (byte) (0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40));

        refreshEntityData(serverPlayer);

        if(location != null) move(bukkitPlayer, location);

        isVisibleForPlayer.put(serverPlayer.getUUID(), true);

        if(linkedHologram !=null) linkedHologram.spawn(bukkitPlayer);

    }

    public NPC spawnForAll() {
        Bukkit.getOnlinePlayers().forEach(this::spawn);
        return this;
    }

    public NPC swingMainHand(Player player) {

        ClientboundAnimatePacket animatePacket = new ClientboundAnimatePacket(entity, 0);
        ((CraftPlayer) player).getHandle().connection.send(animatePacket);

        return this;
    }

    public NPC swingOffHand(Player player) {

        ClientboundAnimatePacket animatePacket = new ClientboundAnimatePacket(entity, 3);
        ((CraftPlayer) player).getHandle().connection.send(animatePacket);

        return this;
    }

    public NPC sneak(Player player) {
        SynchedEntityData synchedEntityData = entity.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<Pose>(6, EntityDataSerializers.POSE), Pose.CROUCHING);

        refreshEntityData(((CraftPlayer) player).getHandle());

        if(!sneaking) synchedEntityData.set(new EntityDataAccessor<Pose>(6, EntityDataSerializers.POSE), Pose.STANDING);

        return this;
    }

    public NPC unsneak(Player player) {
        SynchedEntityData synchedEntityData = entity.getEntityData();
        synchedEntityData.set(new EntityDataAccessor<Pose>(6, EntityDataSerializers.POSE), Pose.STANDING);

        refreshEntityData(((CraftPlayer) player).getHandle());

        return this;
    }

    private NPC move(Player player, Location location) {
        this.location = location;

        entity.setPosRaw(location.x(), location.y(), location.z());
        entity.setRot(location.getYaw(), location.getPitch());
        entity.setYHeadRot(location.getYaw());
        entity.setXRot(location.getPitch());
        entity.setYRot(location.getYaw());

        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(entity);
        setValue(teleportEntityPacket, "b", location.x());
        setValue(teleportEntityPacket, "c", location.y());
        setValue(teleportEntityPacket, "d", location.z());
        ((CraftPlayer) player).getHandle().connection.send(teleportEntityPacket);

        float angelMultiplier = 256f / 360f;
        ClientboundRotateHeadPacket rotateHeadPacket = new ClientboundRotateHeadPacket(entity, (byte) (location.getYaw() * angelMultiplier));
        ((CraftPlayer) player).getHandle().connection.send(rotateHeadPacket);

        return this;
    }

    public NPC moveRelative(Player player, Location location, boolean onGround) {
        ((CraftPlayer) player).getHandle().connection.send(this.getEntityMovePacket(location.x(), location.y(), location.z(), onGround));
        return this;
    }

    private ClientboundMoveEntityPacket.Pos getEntityMovePacket(double x, double y, double z, boolean onGround) {
        return new ClientboundMoveEntityPacket.Pos(this.entity.getId(), (short)(x ), (short)(y ), (short)(z ), onGround);
    }

    public NPC moveForAll(Location location) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            move(player, location);
        });

        return this;
    }

    public NPC lookAt(Player player, Location location) {
        entity.setRot(location.getYaw(), location.getPitch());
        entity.setYHeadRot(location.getYaw());
        entity.setXRot(location.getPitch());
        entity.setYRot(location.getYaw());

        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(entity);
        ((CraftPlayer) player).getHandle().connection.send(teleportEntityPacket);

        float angelMultiplier = 256f / 360f;
        ClientboundRotateHeadPacket rotateHeadPacket = new ClientboundRotateHeadPacket(entity, (byte) (location.getYaw() * angelMultiplier));
        ((CraftPlayer) player).getHandle().connection.send(rotateHeadPacket);

        return this;
    }

    public NPC remove(Player player) {
        if (showInTab)  removeFromTab(player);

        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(entity.getId());
        ((CraftPlayer) player).getHandle().connection.send(removeEntitiesPacket);

        isVisibleForPlayer.put(player.getUniqueId(), false);

        if(linkedHologram !=null) linkedHologram.remove(player);

        return this;
    }

    public NPC removeForAll() {
        Bukkit.getOnlinePlayers().forEach(this::remove);
        return this;
    }

    public NPC removeFromTab(Player player) {
        if(!(entity instanceof ServerPlayer npc)) return this;

        ClientboundPlayerInfoUpdatePacket playerInfoUpdatePacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LISTED, npc);

        ClientboundPlayerInfoUpdatePacket.Entry entry = playerInfoUpdatePacket.entries().get(0);
        ClientboundPlayerInfoUpdatePacket.Entry newEntry = new ClientboundPlayerInfoUpdatePacket.Entry(
                entry.profileId(),
                entry.profile(),
                false,
                entry.latency(),
                entry.gameMode(),
                entry.displayName(),
                entry.chatSession()
        );

        setValue(playerInfoUpdatePacket, "b", List.of(newEntry)); // 'entries'

        ((CraftPlayer) player).getHandle().connection.send(playerInfoUpdatePacket);

        return this;
    }

    public void refreshEntityData(ServerPlayer serverPlayer) {
        Int2ObjectMap<SynchedEntityData.DataItem<?>> itemsById = (Int2ObjectMap<SynchedEntityData.DataItem<?>>) getValue(entity.getEntityData(), "e"); // itemsById
        List<SynchedEntityData.DataValue<?>> entityData = new ArrayList<>();
        itemsById.values().forEach(dataItem -> {
            entityData.add(dataItem.value());
        });

        ClientboundSetEntityDataPacket setEntityDataPacket = new ClientboundSetEntityDataPacket(entity.getId(), entityData);
        serverPlayer.connection.send(setEntityDataPacket);
    }

    private float[] getRotations(Location one, Location two) {
        double diffX = two.getX() - one.getX();
        double diffZ = two.getZ() - one.getZ();
        double diffY = two.getY() + 2.0 - 0.4 - (one.getY() + 2.0);
        double dist = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        float pitch = (float) (-Math.atan2(diffY, dist) * 180.0 / 3.141592653589793);
        return new float[]{yaw, pitch};
    }

    public NPC setTurnToDistance(double turnToDistance) {
        this.turnToDistance = turnToDistance;
        return this;
    }

    public NPC setVisibiltyDistance(double visibiltyDistance) {
        this.visibiltyDistance = visibiltyDistance;
        return this;
    }

    public NPC setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        return this;
    }

    public NPC setSkin(String skinValue, String skinSignature) {
        this.skinSignature = skinSignature;
        this.skinValue = skinValue;

        return this;
    }

    public NPC setTurnToPlayer(boolean turnToPlayer) {
        this.turnToPlayer = turnToPlayer;
        return this;
    }

    public NPC setGlowingColor(ChatFormatting glowingColor) {
        this.glowingColor = glowingColor;
        return this;
    }

    public NPC setGlowing(boolean glowing) {
        this.glowing = glowing;
        return this;
    }

    public NPC setEquipment(HashMap<EquipmentSlot, ItemStack> equipment) {
        this.equipment = new HashMap<>(equipment);
        return this;
    }

    public NPC setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public NPC setShowInTab(boolean showInTab) {
        this.showInTab = showInTab;
        return this;
    }

    public double getTurnToDistance() {
        return turnToDistance;
    }

    public double getVisibiltyDistance() {
        return visibiltyDistance;
    }

    public String getName() {
        return name;
    }

    public Entity getNPC() {
        return entity;
    }

    public ChatFormatting getGlowingColor() {
        return glowingColor;
    }

    public EntityType<?> getEntityType() {
        return entityType;
    }

    public Location getLocation() {
        return location;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isGlowing() {
        return glowing;
    }

    public boolean isShowInTab() {
        return showInTab;
    }

    public boolean isTurnToPlayer() {
        return turnToPlayer;
    }

    public HashMap<UUID, Boolean> getIsTeamCreated() {
        return new HashMap<>(isTeamCreated);
    }

    public HashMap<UUID, Boolean> getIsVisibleForPlayer() {
        return new HashMap<>(isVisibleForPlayer);
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
