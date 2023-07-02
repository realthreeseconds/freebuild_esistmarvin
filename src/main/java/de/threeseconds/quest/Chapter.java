package de.threeseconds.quest;

import java.util.List;

public enum Chapter {

    TUTORIAL(
            0,
            "Tutorial",
            "Aller Anfang ist schwer!"
    );

    private final int id;

    private String name;

    private String description;

    Chapter(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Chapter setName(String name) {
        this.name = name;
        return this;
    }

    public Chapter setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
