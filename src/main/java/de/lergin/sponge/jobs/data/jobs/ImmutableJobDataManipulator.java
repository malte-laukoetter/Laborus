package de.lergin.sponge.jobs.data.jobs;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.Optional;

public class ImmutableJobDataManipulator extends AbstractImmutableData<ImmutableJobDataManipulator, JobData> {
    String jobId;
    Integer xp;

    protected ImmutableJobDataManipulator(String jobId, int xp) {
        this.jobId = jobId;
        this.xp = xp;
        registerGetters();
    }

    protected ImmutableJobDataManipulator(Job job) {
        this(job.getId(), job.getXp());
    }

    public ImmutableValue<String> jobId(){
        return Sponge.getRegistry().getValueFactory().createValue(JobKeys.JOB_ID, this.jobId, "").asImmutable();
    }

    public ImmutableValue<Integer> xp(){
        return Sponge.getRegistry().getValueFactory().createValue(JobKeys.JOB_XP, this.xp, 0).asImmutable();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(JobKeys.JOB_ID, () -> this.jobId);
        registerKeyValue(JobKeys.JOB_ID, this::jobId);

        registerFieldGetter(JobKeys.JOB_XP, () -> this.xp);
        registerKeyValue(JobKeys.JOB_XP, this::xp);
    }

    @Override
    public <E> Optional<ImmutableJobDataManipulator> with(Key<? extends BaseValue<E>> key, E e) {
        if(this.supports(key)) {
            return Optional.of(asMutable().set(key, e).asImmutable());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public JobData asMutable() {
        return new JobData(jobId, xp);
    }

    @Override
    public int compareTo(ImmutableJobDataManipulator o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }
}
