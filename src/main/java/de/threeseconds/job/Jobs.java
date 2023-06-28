package de.threeseconds.job;

import de.threeseconds.FreeBuild;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.Arrays;
import java.util.List;

public enum Jobs {

    MINER(
            "Minen-Arbeiter",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjcyMTUwYjY0NjZiOWQ2YWFhMTg1M2RkNzQ2MTYzNzhmNzg5NmI4N2FjY2FjM2Y2NGFkNDNlYzMyZGI4ODFjIn19fQ==",
            JobsProgress.MINER,
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Minen-Arbeiter werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>und alle Höhlen erforschen?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>%LEVEL%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>%PROGRESS%</st><reset> <dark_gray>» <gray>%XP%/%MAXXP%XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <blue>Auswählen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Rechtsklick <dark_gray>» <blue>Informationen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize("")
    ),
    HOLZFÄLLER(
            "Holzfäller",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjMGE5MTk5MGU3NmM2ZDY1ODA1MGI3YWM3ZTQ4MmJjNTgyYjI0NTg5YmI3ZjE0NmJkMWMwM2I5Yzg0Y2RkOSJ9fX0=",
            JobsProgress.HOLZFÄLLER,
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Holzfäller werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>und den Wald roden?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>%LEVEL%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>%PROGRESS%</st><reset> <dark_gray>» <gray>%XP%/%MAXXP%XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <blue>Auswählen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Rechtsklick <dark_gray>» <blue>Informationen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize("")
    ),
    FISCHER(
            "Fischer",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgwNGU0MmVjOWIwN2ZjZTFjZTAwNThiNzhkZjU3NjNmNmU0MTBkOWNlODJlZjFlYmI5NTk3YTE1MmI2ZDRjOCJ9fX0=",
            JobsProgress.FISCHER,
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Fischer werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>und in die Ichthyologie einsteigen?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>%LEVEL%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>%PROGRESS%</st><reset> <dark_gray>» <gray>%XP%/%MAXXP%XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <blue>Auswählen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Rechtsklick <dark_gray>» <blue>Informationen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize("")
    ),
    JÄGER(
            "Jäger",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFkMGU5MDBlNTQ4NGQ4ZDU2MmE4NTY2NTg4YzhiNjM5MDA2NWI2OGEyNzljN2M3YmYyNzVlYmRkMmVlOWIwMyJ9fX0=",
            JobsProgress.JÄGER,
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>Du möchtest Jäger werden").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <gray>bis das Blut tropft?").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Level <dark_gray>» <yellow>%LEVEL%").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <green><st>%PROGRESS%</st><reset> <dark_gray>» <gray>%XP%/%MAXXP%XP").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(""),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Linksklick <dark_gray>» <blue>Auswählen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize(" <dark_gray>● <gray>Rechtsklick <dark_gray>» <blue>Informationen").decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
            FreeBuild.getInstance().getMiniMessage().deserialize("")
    );

    private String jobName;
    private String headTexture;
    private JobsProgress jobsProgress;
    private List<Component> lore;

    Jobs(String jobName, String headTexture, JobsProgress jobsProgress, Component... lore) {
        this.jobName = jobName;
        this.headTexture = headTexture;
        this.jobsProgress = jobsProgress;
        this.lore = Arrays.asList(lore);
    }

    public String getJobName() {
        return jobName;
    }

    public String getHeadTexture() {
        return headTexture;
    }

    public JobsProgress getJobsProgress() {
        return jobsProgress;
    }

    public List<Component> getLore() {
        return lore;
    }
}
