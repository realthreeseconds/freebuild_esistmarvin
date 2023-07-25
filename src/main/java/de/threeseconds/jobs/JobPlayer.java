package de.threeseconds.jobs;

import de.threeseconds.FreeBuild;
import de.threeseconds.collections.CollectionItem;
import de.threeseconds.util.FreeBuildPlayer;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

public class JobPlayer {

    private FreeBuildPlayer freeBuildPlayer;

    private Job activeJob;

    private HashMap<Job, Integer> jobLevel;
    private HashMap<Job, Integer> jobXP;

    public JobPlayer(FreeBuildPlayer freeBuildPlayer) {
        this.freeBuildPlayer = freeBuildPlayer;

        this.activeJob = null;
        this.jobLevel = new HashMap<>();
        this.jobXP = new HashMap<>();

        for(Job jobs : Job.values()) {
            this.jobLevel.put(jobs, 1);
            this.jobXP.put(jobs, 0);
        }
    }

    public void addLevelByJob(Job job) {
        this.jobLevel.put(job, this.getLevelByJob(job) + 1);
    }

    public void addXPToJob(Job job, Integer xp) {
        this.jobXP.put(job, this.getXPByJob(job) + xp);
    }

    public Integer getLevelByJob(Job job) {
        return this.jobLevel.get(job);
    }

    public Integer getXPByJob(Job job) {
        return this.jobXP.get(job);
    }

    public String getCompactXPByJob(Job job) {
        NumberFormat numberFormat = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        numberFormat.setMinimumFractionDigits(1);

        return numberFormat.format(this.jobXP.get(job)).toLowerCase();
    }

    public String getCompactMaxXPByLevel(Integer level) {
        return this.activeJob.getJobLevels().get(level).getCompactMaxXP();
    }

    public Integer getMaxXPByLevel(Integer level) {
        return this.activeJob.getJobLevels().get(level).getMaxXP();
    }

    public Job getActiveJob() {
        return activeJob;
    }

    public void setActiveJob(Job activeJob) {
        this.activeJob = activeJob;
    }

    public FreeBuildPlayer getFreeBuildPlayer() {
        return freeBuildPlayer;
    }

}
