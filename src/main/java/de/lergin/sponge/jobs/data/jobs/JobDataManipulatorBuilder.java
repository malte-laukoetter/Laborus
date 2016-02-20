package de.lergin.sponge.jobs.data.jobs;

import com.google.common.collect.ImmutableList;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.util.persistence.InvalidDataException;

import java.util.*;

public class JobDataManipulatorBuilder implements DataManipulatorBuilder<JobData, ImmutableJobDataManipulator> {
    Map<String, Double> jobs = new HashMap<>();
    boolean jobsEnabled = true;
    Set<String> selectedJobs = new HashSet<>();

    @Override
    public JobData create() {
        return new JobData(jobs, jobsEnabled, selectedJobs);
    }

    public JobDataManipulatorBuilder job(Job job, double xp){
        jobs.put(job.getId(), xp);

        return this;
    }

    public JobDataManipulatorBuilder jobsEnabled(boolean jobsEnabled){
        this.jobsEnabled = jobsEnabled;

        return this;
    }

    public JobDataManipulatorBuilder jobs(Map<String, Double> jobs){
        this.jobs.putAll(jobs);

        return this;
    }

    @Override
    public Optional<JobData> createFrom(DataHolder dataHolder) {
        return Optional.of(dataHolder.get(JobData.class).orElse(new JobData(jobs, jobsEnabled, selectedJobs)));
    }

    @Override
    public Optional<JobData> build(DataView dataView) throws InvalidDataException {
        if(dataView.contains(JobKeys.JOB_DATA.getQuery())) {

            // the data needs to be parsed by hand because we are getting a list instead of a set
            Set<String> selectedJobs = new HashSet<>();
            for(Object string : (ImmutableList<?>) dataView.get(JobKeys.JOB_SELECTED.getQuery()).orElse(new HashSet<String>())){
                selectedJobs.add((String) string);
            }

            return Optional.of(new JobData(
                    (Map<String,Double>) dataView.getMap(JobKeys.JOB_DATA.getQuery()).get(),
                    dataView.getBoolean(JobKeys.JOB_ENABLED.getQuery()).orElse(true),
                    selectedJobs
            ));
        }
        return Optional.empty();
    }
}
