package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;

import java.util.List;

public abstract class JobListener<T> {
    List<T> jobItemTypes;
    Job job;

    public JobListener(Job job, List<T> blockTypes) {
        this.jobItemTypes = blockTypes;
        this.job = job;
    }
}
