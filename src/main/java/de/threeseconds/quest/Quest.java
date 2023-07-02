package de.threeseconds.quest;

import de.threeseconds.npc.NPC;
import net.minecraft.ChatFormatting;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import javax.annotation.Nullable;
import java.util.List;

public enum Quest {

    QUEST_1(
            "<white>Rede mit <gold>Olaf<white>!",
            "WiP",
            null,
            CompletionType.TALK,
            "<gray>Das Wichtigste zuerst: Um dich zurecht zu finden, wirst du <gold>Geld verdienen <gray>müssen. Dafür gibt es verschiedene <gold>Jobs<gray>, welche du ausüben kannst.",
            "<gray>Besuche doch Vera, um mehr über deinen ersten <gold>Job <gray>zu erfahren!"
    ),
    QUEST_2(
            "<white>Besuche <gold>Vera<white>!",
            "WiP",
            new NPC("Vera", new Location(Bukkit.getWorld("world"), 34, 70, 50, 127, 0))
                    .setGlowing(true)
                    .setGlowingColor(ChatFormatting.WHITE)
                    .setTurnToPlayer(true)
                    .register(),
            CompletionType.VISIT
    );

    private final String questName;
    private String questDescription;
    private NPC questNPC;
    private List<String> questDialoge;
    private CompletionType completionType;

    Quest(String questName, String questDescription, NPC questNPC, CompletionType completionType, @Nullable String... questDialoge) {
        this.questName = questName;
        this.questDescription = questDescription;
        this.questNPC = questNPC;
        this.completionType = completionType;
        this.questDialoge = List.of(questDialoge != null ? questDialoge : new String[0]);
    }

    public String getQuestName() {
        return questName;
    }

    public NPC getQuestNPC() {
        return questNPC;
    }

    public List<String> getQuestDialoge() {
        return questDialoge;
    }

    public enum CompletionType {
        TALK,
        COLLECT,
        VISIT,
        DELIVER,
        KILL;

        CompletionType() {
        }

    }
}
