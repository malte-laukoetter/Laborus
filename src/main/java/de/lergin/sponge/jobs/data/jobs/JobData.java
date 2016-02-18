package de.lergin.sponge.jobs.data.jobs;

import com.google.common.base.Preconditions;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class JobData extends AbstractData<JobData, ImmutableJobDataManipulator> {
    Map<String, Float> jobs = new HashMap<>();
    boolean jobsEnabled = true;

    protected JobData(Map<String, Float> jobs, boolean jobsEnabled) {
        this.jobs.putAll(jobs);
        this.jobsEnabled  = jobsEnabled;

        registerGettersAndSetters();
    }

    public MapValue<String, Float> jobs(){
        return Sponge.getRegistry().getValueFactory().createMapValue(JobKeys.JOB_DATA, this.jobs);
    }

    public Value<Boolean> jobsEnabled(){
        return Sponge.getRegistry().getValueFactory().createValue(JobKeys.JOB_ENABLED, this.jobsEnabled);
    }


    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(JobKeys.JOB_DATA, () -> this.jobs);
        registerFieldSetter(JobKeys.JOB_DATA, value -> this.jobs = value);
        registerKeyValue(JobKeys.JOB_DATA, this::jobs);

        registerFieldGetter(JobKeys.JOB_ENABLED, () -> this.jobsEnabled);
        registerFieldSetter(JobKeys.JOB_ENABLED, value -> this.jobsEnabled = value);
        registerKeyValue(JobKeys.JOB_ENABLED, this::jobsEnabled);
    }

    @Override
    public Optional<JobData> fill(DataHolder dataHolder, MergeFunction mergeFunction) {
        JobData jobDataManipulator = Preconditions.checkNotNull(mergeFunction).merge(copy(),
                dataHolder.get(JobData.class).orElse(copy()));

        return Optional.of(jobDataManipulator);
    }

    @Override
    public Optional<JobData> from(DataContainer dataContainer) {
        if (!dataContainer.contains(JobKeys.JOB_DATA.getQuery())) {
            return Optional.empty();
        }
        this.jobs = (Map<String, Float>) dataContainer.getMap(JobKeys.JOB_DATA.getQuery()).get();
        this.jobsEnabled = dataContainer.getBoolean(JobKeys.JOB_ENABLED.getQuery()).get();
        return Optional.of(this);
    }

    @Override
    public JobData copy() {
        return new JobData(jobs, jobsEnabled);
    }

    @Override
    public ImmutableJobDataManipulator asImmutable() {
        return new ImmutableJobDataManipulator(jobs, jobsEnabled);
    }

    @Override
    public int compareTo(JobData o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 5;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(JobKeys.JOB_DATA, this.jobs).set(JobKeys.JOB_ENABLED, jobsEnabled);
    }
}
