package de.threeseconds.jobs;

import de.threeseconds.util.FreeBuildPlayer;

import java.util.HashMap;

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
            this.jobLevel.put(jobs, 0);
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
