package de.threeseconds.quest;

public enum Section {

    Tutorial(
            0,
            Chapter.TUTORIAL,
            "Tutorial",
            "dd"
    );

    private final int id;

    private final Chapter chapter;

    private String name;

    private String description;

    Section(int id, Chapter chapter, String name, String description) {
        this.id = id;
        this.chapter = chapter;
        this.name = name;
        this.description = description;
    }

    public Section setName(String name) {
        this.name = name;
        return this;
    }

    public Section setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getId() {
        return id;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
