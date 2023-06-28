package de.threeseconds;

import de.privateseconds.coresystem.module.Module;
import de.threeseconds.commands.SpawnCommand;
import de.threeseconds.job.JobManager;
import de.threeseconds.listener.*;
import de.threeseconds.npc.*;
import de.threeseconds.quest.QuestManager;
import de.threeseconds.util.MenuManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class FreeBuild extends Module {

    private static FreeBuild instance;

    private final String PREFIX = "<gradient:#33F702:#1B660F>FreeBuild</gradient> <dark_gray>┃ ";
    private final Location hubLocation = new Location(Bukkit.getWorld("world"), 26.5, 64, -23.5, 0, 0);
    private MiniMessage miniMessage;
    private NonPlayerCharacterManager npcManager;
    private HologramManager hologramManager;
    private QuestManager questManager;
    private JobManager jobManager;
    private MenuManager menuManager;


    @Override
    public void onEnable() {
        instance = this;
        this.init();

        this.createWorld("freebuildWorld");

        this.initListener();
        this.initCommands();

        new MainLobbyRunnable().runTaskTimer(this.getPaperCore(), 0L, 0L);

    }

    private void createWorld(String worldName) {
        if(Bukkit.getWorld(worldName) == null) {
            new WorldCreator(worldName)
                    .environment(World.Environment.NORMAL)
                    .createWorld();
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().getPendingTasks().forEach(BukkitTask::cancel);
    }

    private void init() {
        this.miniMessage = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolver(StandardTags.color())
                        .resolver(StandardTags.decorations())
                        .resolver(StandardTags.gradient())
                        .resolver(StandardTags.reset())
                        .resolver(StandardTags.keybind())
                        .resolver(StandardTags.newline())
                        .build()
                )
                .build();

        this.npcManager = new NonPlayerCharacterManager();
        this.hologramManager = new HologramManager();
        this.questManager = new QuestManager();
        this.jobManager = new JobManager();
        this.menuManager = new MenuManager();
    }

    private void initListener() {
        getPaperCore().getServer().getMessenger().registerOutgoingPluginChannel((Plugin)getPaperCore(), "BungeeCord");
        new JoinListener();
        new FreeBuildListener();
        new NPCInteractListener();
        new InventoryClickListener();
        new ItemInteractListener();
    }

    private void initCommands() {
        new SpawnCommand();
    }

    public boolean checkPDC(String itemKey, PersistentDataContainer persistentDataContainer, String checkedValue) {
        NamespacedKey menuItem = new NamespacedKey(this.getPaperCore(), itemKey);

        if(persistentDataContainer.has(menuItem, PersistentDataType.STRING)) {
            String value = persistentDataContainer.get(menuItem, PersistentDataType.STRING);
            assert value != null;

            return value.equals(this.getMiniMessage().serialize(this.getMiniMessage().deserialize(checkedValue)));
        }
        return false;
    }

    public static FreeBuild getInstance() {
        return instance;
    }

    public String getPREFIX() {
        return PREFIX;
    }

    public Location getHubLocation() {
        return hubLocation;
    }

    public NonPlayerCharacterManager getNPCManager() {
        return npcManager;
    }

    public HologramManager getHologramManager() {
        return hologramManager;
    }

    public QuestManager getQuestManager() {
        return questManager;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public MenuManager getMenuManager() {
        return menuManager;
    }

    public MiniMessage getMiniMessage() {
        return miniMessage;
    }

}
