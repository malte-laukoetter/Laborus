package de.lergin.sponge.laborus.data.jobs;

import com.google.common.collect.ImmutableList;
import de.lergin.sponge.laborus.data.JobKeys;
import de.lergin.sponge.laborus.job.Job;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class JobDataManipulatorBuilder implements DataManipulatorBuilder<JobData, ImmutableJobDataManipulator> {
    private Map<String, Double> jobs = new HashMap<>();
    private boolean jobsEnabled = true;
    private Set<String> selectedJobs = new HashSet<>();
    private Map<String, Long> abilityUsed = new HashMap<>();

    @Override
    public JobData create() {
        return new JobData(jobs, jobsEnabled, selectedJobs, abilityUsed);
    }

    public JobDataManipulatorBuilder job(Job job, double xp) {
        jobs.put(job.getId(), xp);

        return this;
    }

    public JobDataManipulatorBuilder jobsEnabled(boolean jobsEnabled) {
        this.jobsEnabled = jobsEnabled;

        return this;
    }

    public JobDataManipulatorBuilder jobs(Map<String, Double> jobs) {
        this.jobs.putAll(jobs);

        return this;
    }

    public JobDataManipulatorBuilder selectedJobs(Set<String> jobs) {
        this.selectedJobs.addAll(jobs);

        return this;
    }

    public JobDataManipulatorBuilder abilityUsed(Map<String, Long> abilityUsed) {
        this.abilityUsed.putAll(abilityUsed);

        return this;
    }

    @Override
    public Optional<JobData> createFrom(DataHolder dataHolder) {
        return Optional.of(dataHolder.get(JobData.class).orElse(new JobData(jobs, jobsEnabled, selectedJobs, abilityUsed)));
    }

    @Override
    public Optional<JobData> build(DataView dataView) {
        if (dataView.contains(JobKeys.JOB_DATA.getQuery())) {

            // the data needs to be parsed by hand because we are getting a list instead of a set
            Set<String> selectedJobs =
                    ((ImmutableList<?>) dataView.get(JobKeys.JOB_SELECTED.getQuery()).orElse(new HashSet<>())).stream()
                            .map(string -> (String) string)
                            .collect(Collectors.toSet());

            Map<String, Double> jobData;
            Optional<? extends Map<?, ?>> optional = dataView.getMap(JobKeys.JOB_DATA.getQuery());
            if (optional.isPresent()) {
                jobData = (Map<String, Double>) optional.get();
            } else {
                jobData = new HashMap<>();
            }

            Map<String, Long> jobAbilityUsed;
            Optional<? extends Map<?, ?>> optional2 = dataView.getMap(JobKeys.JOB_ABILITY_USED.getQuery());
            if (optional2.isPresent()) {
                jobAbilityUsed = (Map<String, Long>) optional2.get();
            } else {
                jobAbilityUsed = new HashMap<>();
            }

            return Optional.of(new JobData(
                    jobData,
                    dataView.getBoolean(JobKeys.JOB_ENABLED.getQuery()).orElse(true),
                    selectedJobs,
                    jobAbilityUsed
            ));
        }
        return Optional.empty();
    }
}
