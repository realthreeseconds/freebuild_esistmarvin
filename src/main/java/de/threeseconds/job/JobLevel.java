package de.threeseconds.job;

import java.util.Arrays;
import java.util.List;

public class JobLevel {

    private Integer level;
    private Integer xpNeeded;
    private Integer totalXPNeeded;
    private List<Object> rewards;

    public JobLevel(Integer level, Integer xpNeeded, Object... rewards) {
        this.level = level;
        this.xpNeeded = xpNeeded;
        this.totalXPNeeded = 50;
        //this.rewards = Arrays.asList(rewards);
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getXpNeeded() {
        return xpNeeded;
    }

    public Integer getTotalXPNeeded() {
        return totalXPNeeded;
    }

    public void setXpNeeded(Integer xpNeeded) {
        this.xpNeeded = xpNeeded;
    }

    public void setTotalXPNeeded(Integer totalXPNeeded) {
        this.totalXPNeeded = totalXPNeeded;
    }

    public List<Object> getRewards() {
        return rewards;
    }

    public void setRewards(List<Object> rewards) {
        this.rewards = rewards;
    }
}
