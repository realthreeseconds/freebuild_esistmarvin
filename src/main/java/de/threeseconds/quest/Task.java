package de.threeseconds.quest;

import de.threeseconds.FreeBuild;
import de.threeseconds.npc.NPC;
import de.threeseconds.npc.PlayerNPC;
import de.threeseconds.quest.event.DialogCallback;
import de.threeseconds.util.FreeBuildPlayer;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum Task {

    Tutorial_0(
            0,
            Type.DIALOG,
            Mission.Tutorial,
            "Schau dich etwas genauer um",
            new Dialog(0)
                    .component(new MissionComponent())
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Hey du da! Warte mal eben.").sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Du scheinst der neue zu sein von dem alle reden.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Ich sag dir schnell wie das hier alles läuft.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Komm mal schnell eben zu mir.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent().delay(10))
    ),

    Tutorial_1(
            1,
            Type.DIALOG,
            Mission.Tutorial,
            "npcTutorial",
            "Spreche mit dem Unbekannten",
            new Dialog(0)
                    .component(new MissionComponent())
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Danke das du so schnell gekommen bist.").sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Dein Gesicht sagt mir, dass du noch nicht weißt was du als erstes tun solltest.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Ach warte, wo sind denn eigentlich meine Manieren geblieben.").delay(65).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<i><yellow>Unbekannt <reset><dark_grey>» <grey>Mein Name lautet <b><yellow>Caleb<reset><grey>.").delay(30).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Ich bin einer der Einwohner auf dieser Insel.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Verrätst du mir deinen Namen?").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<player> <reset><dark_grey>» <grey>Mein Name ist <b><player><reset><grey>.").delay(35).sound(Sound.BLOCK_NOTE_BLOCK_BELL))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Schön dich kennen zu lernen <player><reset><grey>.").delay(20).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Willkommen hier bei uns im Dorf.").delay(30).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Momentan sind viele der anderen Einwohner im nachbar Dorf auf einer Feier und kommen erst die nächsten Tage wieder.").delay(20).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Du wirst dich also noch etwas gedulden müssen bis du alle hier kennenlernen kannst.").delay(70).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Zuerst solltest du dich bei <gold>James <grey>vorstellen, der müsste auch noch da sein.").delay(70).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Er ist der <gold>Schatzmeister <grey>hier und für <blue>Bezahlung <grey>und <gold>Berufe <grey>zuständig.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Finden solltest du ihn, wenn du einfach den <yellow>Weg <grey>weiter folgst.").delay(45).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<yellow>Caleb <dark_grey>» <grey>Danach kommst du wieder zu mir.").delay(60).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent().delay(10))
    ),

    Tutorial_2(
            2,
            Type.DIALOG,
            Mission.Tutorial,
            "npcTutorial2",
            "Finde den Schatzmeister James",
            new Dialog(0)
                    .component(new MissionComponent())
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Moin.").sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Du musst der neue sein. <green><player> <reset><grey>, richtig?").delay(15).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<player> <reset><dark_grey>» <grey>Richtig.").delay(35).sound(Sound.BLOCK_NOTE_BLOCK_BELL))
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Perfekt. Dich hat bestimmt <yellow>Caleb <grey>geschickt.").delay(30).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Er hat recht. Wenn es um Geld geht bin ich die richtige Person dafür.").delay(30).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Die einfachste Methode für Geld ist ein <gold>Beruf<grey>.").delay(30).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Damit sollten wir erstmal anfangen. Momentan hätte ich für dich <gold>4 Berufe <grey>im Angebot.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Jeder <gold>Beruf <grey>hat seine Vor- als auch Nachteile.").delay(50).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
                    .component(new MissionComponent("<gold>James <reset><dark_grey>» <grey>Such dir hier einfach eben einen aus und sag mir wenn du dich entschieden hast.").delay(40).sound(Sound.BLOCK_NOTE_BLOCK_PLING))
    );


    private final int id;

    private final Type type;

    private final Mission mission;

    private String description;

    private String npcName;

    private List<Dialog> dialogs;


    Task(int id, Type type, Mission mission, String description) {
        this.id = id;
        this.type = type;
        this.mission = mission;
        this.description = description;
    }

    Task(int id, Type type, Mission mission, String description, Dialog... dialogs) {
        this.id = id;
        this.type = type;
        this.mission = mission;
        this.description = description;
        this.dialogs = Arrays.stream(dialogs).toList();
    }

    Task(int id, Type type, Mission mission, String npcName, String description, Dialog... dialogs) {
        this.id = id;
        this.type = type;
        this.mission = mission;
        this.npcName = npcName;
        this.description = description;
        this.dialogs = Arrays.stream(dialogs).toList();
    }

    public void start(FreeBuildPlayer freeBuildPlayer) {

    }

    public void playDialog(Player player) {
        if(getType() == Type.DIALOG) dialogs.get(0).play(this, player);
    }

    public void playDialog(Player player, DialogCallback callback) {
        if(getType() == Type.DIALOG) dialogs.get(0).play(this, player, callback);
    }

    public Task setDescription(String description) {
        this.description = description;
        return this;
    }

    public Task setNPCName(String npcName) {
        this.npcName = npcName;
        return this;
    }

    public String getNPCName() {
        return npcName;
    }

    public int getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public Mission getMission() {
        return mission;
    }

    public String getDescription() {
        return description;
    }

    public Section getSection() {
        return mission.getSection();
    }

    public Chapter getChapter() {
        return  getSection().getChapter();
    }

    public enum Type {
        DIALOG, KILL, DELIVER, COLLECT, VISIT
    }

}
