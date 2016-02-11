package de.lergin.sponge.jobs.data.jobs;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.util.persistence.InvalidDataException;

import java.util.Optional;

/**
 * Created by Malte on 10.02.2016.
 */
public class JobDataManipulatorBuilder implements DataManipulatorBuilder<JobData, ImmutableJobDataManipulator> {
    @Override
    public JobData create() {
        return new JobData(id, xp);
    }

    private String id = "";

    public JobDataManipulatorBuilder id(String id){
        this.id = id;
        return this;
    }

    private Integer xp = 0;

    public JobDataManipulatorBuilder xp(Integer xp){
        this.xp = xp;
        return this;
    }

    public JobDataManipulatorBuilder job(Job job){
        this.id = job.getId();
        this.xp = job.getXp();

        return this;
    }

    @Override
    public Optional<JobData> createFrom(DataHolder dataHolder) {
        return Optional.of(dataHolder.get(JobData.class).orElse(new JobData("",0)));
    }

    @Override
    public Optional<JobData> build(DataView dataView) throws InvalidDataException {
        if(dataView.contains(JobKeys.JOB_ID.getQuery(), JobKeys.JOB_XP.getQuery())) {
            return Optional.of(new JobData(
                    dataView.getString(JobKeys.JOB_ID.getQuery()).get(),
                    dataView.getInt(JobKeys.JOB_XP.getQuery()).get()
            ));
        }
        return Optional.empty();
    }
}
