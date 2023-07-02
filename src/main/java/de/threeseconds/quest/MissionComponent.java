package de.threeseconds.quest;

import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Sound;

public class MissionComponent {

    private String componentString;

    private Sound sound;

    private int delay;

    private Action action;

    public MissionComponent(Component component, Sound sound, int delay, Action action) {
        this.componentString = component.toString();
        this.sound = sound;
        this.delay = delay;
        this.action = action;
    }

    public MissionComponent(Component component) {
        this.componentString = component.toString();
        this.delay = 0;
        this.action = Action.NEXT;
    }

    public MissionComponent(String text) {
        this.componentString = text;
        this.delay = 0;
        this.action = Action.NEXT;
    }

    public MissionComponent() {
        this.componentString = "";
        this.delay = 0;
        this.action = Action.NEXT;
    }

    public MissionComponent text(String text) {
        this.componentString = text;
        return this;
    }

    public MissionComponent text(Component component) {
        this.componentString = component.toString();
        return this;
    }

    public MissionComponent sound(Sound sound) {
        this.sound = sound;
        return this;
    }

    public MissionComponent delay(int delay) {
        this.delay = delay;
        return this;
    }

    public Component getComponent() {
        return FreeBuild.getInstance().getMiniMessage().deserialize(componentString);
    }

    public String getComponentString() {
        return componentString;
    }

    public Sound getSound() {
        return sound;
    }

    public int getDelay() {
        return delay;
    }

    public Action getAction() {
        return action;
    }

    public enum Action {
        NEXT, RESPONSE
    }

}
