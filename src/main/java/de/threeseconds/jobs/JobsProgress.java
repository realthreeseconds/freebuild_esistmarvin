package de.threeseconds.jobs;

import java.util.Arrays;
import java.util.List;

public enum JobsProgress {

    MINER(
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)
    ),
    HOLZFÄLLER(
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)
    ),
    FISCHER(
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)
    ),
    JÄGER(
            new JobLevel(1, 50, null),
            new JobLevel(2, 125, null),
            new JobLevel(3, 200, null),
            new JobLevel(4, 300, null),
            new JobLevel(5, 500, null),
            new JobLevel(6, 750, null),
            new JobLevel(7, 1000, null),
            new JobLevel(8, 1500, null),
            new JobLevel(9, 2000, null),
            new JobLevel(10, 3500, null),
            new JobLevel(11, 5000, null),
            new JobLevel(12, 7500, null),
            new JobLevel(13, 10000, null),
            new JobLevel(14, 15000, null),
            new JobLevel(15, 20000, null),
            new JobLevel(16, 30000, null),
            new JobLevel(17, 50000, null),
            new JobLevel(18, 75000, null),
            new JobLevel(19, 100000, null),
            new JobLevel(20, 200000, null),
            new JobLevel(21, 300000, null),
            new JobLevel(22, 400000, null),
            new JobLevel(23, 500000, null),
            new JobLevel(24, 600000, null),
            new JobLevel(25, 700000, null)
    );

    private List<JobLevel> jobLevel;

    JobsProgress(JobLevel... jobLevel) {
        this.jobLevel = Arrays.asList(jobLevel);
    }

    public List<JobLevel> getJobLevel() {
        return jobLevel;
    }
}
