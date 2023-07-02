package de.threeseconds.jobs;

import de.threeseconds.util.ItemBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public enum Job implements Serializable {

    /*
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
     */

    MINER(
            "Minen-Arbeiter",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjcyMTUwYjY0NjZiOWQ2YWFhMTg1M2RkNzQ2MTYzNzhmNzg5NmI4N2FjY2FjM2Y2NGFkNDNlYzMyZGI4ODFjIn19fQ==",
            null,
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)

    ),
    HOLZFÄLLER(
            "Holzfäller",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNjMGE5MTk5MGU3NmM2ZDY1ODA1MGI3YWM3ZTQ4MmJjNTgyYjI0NTg5YmI3ZjE0NmJkMWMwM2I5Yzg0Y2RkOSJ9fX0=",
            null,
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)
    ),
    FISCHER(
            "Fischer",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDgwNGU0MmVjOWIwN2ZjZTFjZTAwNThiNzhkZjU3NjNmNmU0MTBkOWNlODJlZjFlYmI5NTk3YTE1MmI2ZDRjOCJ9fX0=",
            null,
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)
    ),
    JÄGER(
            "Jäger",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDFkMGU5MDBlNTQ4NGQ4ZDU2MmE4NTY2NTg4YzhiNjM5MDA2NWI2OGEyNzljN2M3YmYyNzVlYmRkMmVlOWIwMyJ9fX0=",
            null,
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)
    );

    @Serial
    private static final long serialVersionUID = 1L;

    private String jobName;
    private String headTexture;
    private List<JobLevel> jobLevels;
    private List<ItemBuilder> jobItems;

    Job(String jobName, String headTexture, List<ItemBuilder> jobItems, JobLevel... jobLevels) {
        this.jobName = jobName;
        this.headTexture = headTexture;
        this.jobItems = jobItems;
        this.jobLevels = List.of(jobLevels);
    }

    public List<JobLevel> getJobLevels() {
        return jobLevels;
    }

    public String getJobName() {
        return jobName;
    }

    public String getHeadTexture() {
        return headTexture;
    }

}
