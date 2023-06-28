package de.threeseconds.quest;

import java.util.List;

public enum Chapter {

    CHAPTER_TUT(
            0,
            "Tutorial:",
            Quest.QUEST_1, Quest.QUEST_2
    ),
    CHAPTER_1(
            1,
            "Der Beginn:"
    );

    private Integer chapterID;
    private String chapterName;
    private List<Quest> chapterQuestList;

    Chapter(Integer chapterID, String chapterName, Quest... chapterQuestList) {
        this.chapterID = chapterID;
        this.chapterName = chapterName;
        this.chapterQuestList = List.of(chapterQuestList);
    }

    public Integer getChapterID() {
        return chapterID;
    }

    public void setChapterID(Integer chapterID) {
        this.chapterID = chapterID;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public List<Quest> getChapterQuestList() {
        return chapterQuestList;
    }

    public void setChapterQuestList(List<Quest> chapterQuestList) {
        this.chapterQuestList = chapterQuestList;
    }
}
