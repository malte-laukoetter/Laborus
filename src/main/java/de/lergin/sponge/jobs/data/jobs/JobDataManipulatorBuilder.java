package de.lergin.sponge.jobs.data.jobs;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.util.persistence.InvalidDataException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JobDataManipulatorBuilder implements DataManipulatorBuilder<JobData, ImmutableJobDataManipulator> {
    Map<String, Float> jobs = new HashMap<>();

    @Override
    public JobData create() {
        return new JobData(jobs);
    }

    public JobDataManipulatorBuilder job(Job job, float xp){
        jobs.put(job.getId(), xp);

        return this;
    }

    public JobDataManipulatorBuilder jobs(Map<String, Float> jobs){
        this.jobs.putAll(jobs);

        return this;
    }

    @Override
    public Optional<JobData> createFrom(DataHolder dataHolder) {
        return Optional.of(dataHolder.get(JobData.class).orElse(new JobData(jobs)));
    }

    @Override
    public Optional<JobData> build(DataView dataView) throws InvalidDataException {
        if(dataView.contains(JobKeys.JOB_DATA.getQuery())) {
            return Optional.of(new JobData(
                    (Map<String,Float>) dataView.getMap(JobKeys.JOB_DATA.getQuery()).get()
            ));
        }
        return Optional.empty();
    }
}
