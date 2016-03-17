package de.lergin.sponge.laborus.listener;

import de.lergin.sponge.laborus.job.Job;

import java.util.List;

/**
 * basic listener for all JobListener. It saves the data that every listerner has (JobItemTypes and the Job)
 * @param <T> the type of the JobItems
 */
public abstract class JobListener<T> {
    final List<T> JOB_ITEM_TYPES;
    final Job JOB;

    /**
     * creates a new JobListener
     * @param job the job that this listener is related to
     * @param jobItemTypes a list of T that the listener should react to
     */
    public JobListener(Job job, List<T> jobItemTypes) {
        this.JOB_ITEM_TYPES = jobItemTypes;
        this.JOB = job;
    }
}
