package de.lergin.laborus.job;

import de.lergin.laborus.api.JobBonus;

import java.util.List;

public class JobBoni<T extends JobBonus> {
    private final List<T> jobBoni;

    public List<T> get() {
        return jobBoni;
    }

    public JobBoni(List<T> jobBoni) {
        this.jobBoni = jobBoni;
    }
}
