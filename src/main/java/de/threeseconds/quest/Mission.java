package de.threeseconds.quest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Mission {

    Tutorial(
            0,
            Section.Tutorial,
            "Tutorial",
            ""
    );

    private final int id;

    private final Section section;

    private String name;

    private String description;

    private List<Task> tasks;

    Mission(int id, Section section, String name, String description) {
        this.id = id;
        this.section = section;
        this.name = name;
        this.description = description;
        this.tasks = new ArrayList<>();
    }

    Mission(int id, Section section, String name, String description, Task... tasks) {
        this.id = id;
        this.section = section;
        this.name = name;
        this.description = description;
        this.tasks = Arrays.stream(tasks).toList();
    }

    public Mission setName(String name) {
        this.name = name;
        return this;
    }

    public Mission setDescription(String description) {
        this.description = description;
        return this;
    }

    public int getId() {
        return id;
    }

    public Section getSection() {
        return section;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Chapter getChapter() {
        return section.getChapter();
    }

    public List<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public Task getTask(int id) {
        for(Task task : tasks) if(task.getId() == id) return task;
        return null;
    }

    public Mission addTask(Task task) {
        tasks.add(task);
        return this;
    }

    public Mission removeTask(Task task) {
        tasks.remove(task);
        return this;
    }

}
