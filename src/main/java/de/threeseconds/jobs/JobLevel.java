package de.threeseconds.jobs;

import java.util.List;

public class JobLevel {

    private Integer level;
    private Integer maxXP;
    private Integer totalXPNeeded;
    private List<Object> rewards;

    public JobLevel(Integer level, Integer maxXP, Object... rewards) {
        this.level = level;
        this.maxXP = maxXP;
        this.totalXPNeeded = 50;
        //this.rewards = Arrays.asList(rewards);
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getMaxXP() {
        return maxXP;
    }

    public Integer getTotalXPNeeded() {
        return totalXPNeeded;
    }

    public void setMaxXP(Integer maxXP) {
        this.maxXP = maxXP;
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
