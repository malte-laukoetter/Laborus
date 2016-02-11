package de.lergin.sponge.jobs.data.jobs;

import com.google.common.base.Preconditions;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class JobData extends AbstractData<JobData, ImmutableJobDataManipulator> {
    String jobId;
    Integer xp;

    protected JobData(String jobId, int xp) {
        this.jobId = jobId;
        this.xp = xp;
        registerGettersAndSetters();
    }

    protected JobData(Job job) {
        this(job.getId(), job.getXp());
    }

    public Value<String> jobId(){
        return Sponge.getRegistry().getValueFactory().createValue(JobKeys.JOB_ID, this.jobId, "");
    }

    public Value<Integer> xp(){
        return Sponge.getRegistry().getValueFactory().createValue(JobKeys.JOB_XP, this.xp, 0);
    }


    @Override
    protected void registerGettersAndSetters() {
        registerFieldGetter(JobKeys.JOB_ID, () -> this.jobId);
        registerFieldSetter(JobKeys.JOB_ID, value -> this.jobId = value);
        registerKeyValue(JobKeys.JOB_ID, this::jobId);

        registerFieldGetter(JobKeys.JOB_XP, () -> this.xp);
        registerFieldSetter(JobKeys.JOB_XP, value -> this.xp = value);
        registerKeyValue(JobKeys.JOB_XP, this::xp);
    }

    @Override
    public Optional<JobData> fill(DataHolder dataHolder, MergeFunction mergeFunction) {
        JobData jobDataManipulator = Preconditions.checkNotNull(mergeFunction).merge(copy(),
                dataHolder.get(JobData.class).orElse(copy()));

        return Optional.of(jobDataManipulator);
    }

    @Override
    public Optional<JobData> from(DataContainer dataContainer) {
        if (!dataContainer.contains(JobKeys.JOB_ID.getQuery(), JobKeys.JOB_XP.getQuery())) {
            return Optional.empty();
        }
        this.jobId = dataContainer.getString(JobKeys.JOB_ID.getQuery()).get();
        this.xp = dataContainer.getInt(JobKeys.JOB_XP.getQuery()).get();
        return Optional.of(this);
    }

    @Override
    public JobData copy() {
        return new JobData(jobId, xp);
    }

    @Override
    public ImmutableJobDataManipulator asImmutable() {
        return new ImmutableJobDataManipulator(jobId, xp);
    }

    @Override
    public int compareTo(JobData o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(JobKeys.JOB_ID, this.jobId).set(JobKeys.JOB_XP, this.xp);
    }
}
