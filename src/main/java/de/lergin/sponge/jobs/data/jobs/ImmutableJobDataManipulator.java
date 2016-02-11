package de.lergin.sponge.jobs.data.jobs;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableMapValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ImmutableJobDataManipulator extends AbstractImmutableData<ImmutableJobDataManipulator, JobData> {
    Map<String, Integer> jobs = new HashMap<>();

    protected ImmutableJobDataManipulator(String jobId, int xp) {
        jobs.put(jobId, xp);
        registerGetters();
    }

    protected ImmutableJobDataManipulator(Job job) {
        this(job.toMap());
    }

    protected ImmutableJobDataManipulator(Map<String, Integer> jobs) {
        this.jobs.putAll(jobs);

        registerGetters();
    }

    public ImmutableMapValue<String, Integer> jobs(){
        return Sponge.getRegistry().getValueFactory().createMapValue(JobKeys.JOB_DATA, this.jobs).asImmutable();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(JobKeys.JOB_DATA, () -> this.jobs);
        registerKeyValue(JobKeys.JOB_DATA, this::jobs);
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
        return new JobData(jobs);
    }

    @Override
    public int compareTo(ImmutableJobDataManipulator o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 3;
    }
}
