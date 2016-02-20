package de.lergin.sponge.jobs.data.jobs;

import com.google.common.base.Preconditions;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.*;

public class JobData extends AbstractData<JobData, ImmutableJobDataManipulator> {
    Map<String, Double> jobs = new HashMap<>();
    boolean jobsEnabled = true;
    List<String> selectedJobs;

    protected JobData(Map<String, Double> jobs, boolean jobsEnabled, List<String> selectedJobs) {
        this.jobs.putAll(jobs);
        this.jobsEnabled  = jobsEnabled;
        this.selectedJobs = selectedJobs;

        registerGettersAndSetters();
    }

    public MapValue<String, Double> jobs(){
        return Sponge.getRegistry().getValueFactory().createMapValue(JobKeys.JOB_DATA, this.jobs);
    }

    public Value<Boolean> jobsEnabled(){
        return Sponge.getRegistry().getValueFactory().createValue(JobKeys.JOB_ENABLED, this.jobsEnabled);
    }

    public ListValue<String> selectedJobs(){
        return Sponge.getRegistry().getValueFactory().createListValue(JobKeys.JOB_SELECTED, this.selectedJobs);
    }


    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(JobKeys.JOB_DATA, () -> this.jobs);
        registerFieldSetter(JobKeys.JOB_DATA, value -> this.jobs = value);
        registerKeyValue(JobKeys.JOB_DATA, this::jobs);

        registerFieldGetter(JobKeys.JOB_ENABLED, () -> this.jobsEnabled);
        registerFieldSetter(JobKeys.JOB_ENABLED, value -> this.jobsEnabled = value);
        registerKeyValue(JobKeys.JOB_ENABLED, this::jobsEnabled);

        registerFieldGetter(JobKeys.JOB_SELECTED, () -> this.selectedJobs);
        registerFieldSetter(JobKeys.JOB_SELECTED, value -> this.selectedJobs = value);
        registerKeyValue(JobKeys.JOB_SELECTED, this::selectedJobs);
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
        this.jobs = (Map<String, Double>) dataContainer.getMap(JobKeys.JOB_DATA.getQuery()).get();
        this.jobsEnabled = dataContainer.getBoolean(JobKeys.JOB_ENABLED.getQuery()).get();
        this.selectedJobs = (List<String>) dataContainer.getList(JobKeys.JOB_SELECTED.getQuery()).get();
        return Optional.of(this);
    }

    @Override
    public JobData copy() {
        return new JobData(jobs, jobsEnabled, selectedJobs);
    }

    @Override
    public ImmutableJobDataManipulator asImmutable() {
        return new ImmutableJobDataManipulator(jobs, jobsEnabled, selectedJobs);
    }

    @Override
    public int compareTo(JobData o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 7;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(JobKeys.JOB_DATA, this.jobs).set(JobKeys.JOB_ENABLED, jobsEnabled)
                .set(JobKeys.JOB_SELECTED, this.selectedJobs);
    }
}
