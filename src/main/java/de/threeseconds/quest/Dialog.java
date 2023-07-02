package de.threeseconds.quest;

import de.privateseconds.permissioncentermodulepaper.PermissionCenterModulePaper;
import de.threeseconds.FreeBuild;
import de.threeseconds.quest.event.DialogCallback;
import de.threeseconds.quest.event.PlayerDialogEvent;
import de.threeseconds.quest.event.PlayerDialogFinishEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Dialog {

    private final int id;

    private Dialog instance;

    private HashMap<Integer, MissionComponent> text;

    public Dialog(int id) {
        this.id = id;
        this.text = new HashMap<>();
        this.instance = this;
    }

    public void play(Task task, Player player) {
        if(player == null) return;
        play(task,0, player, ()->{});
    }

    public void play(Task task, Player player, DialogCallback dialogCallback) {
        if(player == null) return;
        play(task,0, player, dialogCallback);
    }

    private void play(Task task, int id, Player player, DialogCallback dialogCallback) {

        if(text.get(id) == null) {
            FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player).setInDialog(false);
            new PlayerDialogFinishEvent(this, player).callEvent();
            dialogCallback.onFinish();
            return;
        }

        FreeBuild.getInstance().getQuestManager().getFreeBuildPlayer(player).setInDialog(true);
        MissionComponent component = text.get(id);

        if(component.getDelay() <= 0) {

            new PlayerDialogEvent(instance, player, task, id).callEvent();
            if(component.getSound() != null) player.playSound(player, component.getSound(), 0.5f, 0f);
            player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(component.getComponentString(), Placeholder.parsed("player", decodeColor(PermissionCenterModulePaper.getInstance().getUserManager().getUser(player.getUniqueId()).getDisplay().getColor()) + player.getName())));
            if(component.getAction() == MissionComponent.Action.NEXT) play(task,id+1, player, dialogCallback);


        } else {

            new BukkitRunnable() {

                @Override
                public void run() {
                    new PlayerDialogEvent(instance, player, task, id).callEvent();
                    if(component.getSound() != null) player.playSound(player, component.getSound(), 0.5f, 0f);
                    player.sendMessage(FreeBuild.getInstance().getMiniMessage().deserialize(component.getComponentString(), Placeholder.parsed("player", decodeColor(PermissionCenterModulePaper.getInstance().getUserManager().getUser(player.getUniqueId()).getDisplay().getColor()) + player.getName())));
                    if(component.getAction() == MissionComponent.Action.NEXT) play(task,id+1, player, dialogCallback);

                }

            }.runTaskLater(FreeBuild.getInstance().getPaperCore(), component.getDelay());

        }

    }

    public Dialog component(MissionComponent component) {
        text.put(text.size(), component);
        return this;
    }

    public Dialog component(int id, MissionComponent component) {
        text.put(id, component);
        return this;
    }

    public int getId() {
        return id;
    }

    public MissionComponent getCompontent(int id) {
        return text.getOrDefault(id, new MissionComponent());
    }

    public HashMap<Integer, MissionComponent> getTexts() {
        return new HashMap<>(text);
    }

    private String decodeColor(String colorCode) {
        String s = "";
        switch(colorCode) {
            case "§a" -> s = "<green>";
            case "§4" -> s = "<dark_red>";
        }
        return s;
    }

}
