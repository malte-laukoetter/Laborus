package de.lergin.laborus.data.jobs;

import com.google.common.collect.ImmutableList;
import de.lergin.laborus.data.JobKeys;
import de.lergin.laborus.job.Job;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class JobDataManipulatorBuilder extends AbstractDataBuilder<JobData> implements DataManipulatorBuilder<JobData, ImmutableJobDataManipulator> {
    private Map<String, Double> jobs = new HashMap<>();
    private boolean jobsEnabled = true;
    private Set<String> selectedJobs = new HashSet<>();
    private Map<String, Long> abilityUsed = new HashMap<>();

    public JobDataManipulatorBuilder() {
        super(JobData.class, 8);
    }

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
    protected Optional<JobData> buildContent(DataView dataView) {
        if (!dataView.isEmpty()) {
            // the data needs to be parsed by hand because we are getting a list instead of a set
            Set<String> selectedJobs =
                    ((ImmutableList<?>) dataView.get(JobKeys.JOB_SELECTED.getQuery()).orElseGet(HashSet::new)).stream()
                            .map(string -> (String) string)
                            .collect(Collectors.toSet());

            Map<String, Double> jobData = dataView.getMap(JobKeys.JOB_DATA.getQuery())
                    .map(map -> (Map<String, Double>) map).orElseGet(HashMap::new);

            Map<String, Long> jobAbilityUsed = dataView.getMap(JobKeys.JOB_ABILITY_USED.getQuery())
                    .map(map -> (Map<String, Long>) map).orElseGet(HashMap::new);

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
