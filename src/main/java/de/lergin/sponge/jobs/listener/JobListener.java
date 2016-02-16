package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;

import java.util.List;

public abstract class JobListener<T> {
    final List<T> JOB_ITEM_TYPES;
    final Job JOB;

    public JobListener(Job job, List<T> blockTypes) {
        this.JOB_ITEM_TYPES = blockTypes;
        this.JOB = job;
    }
}
