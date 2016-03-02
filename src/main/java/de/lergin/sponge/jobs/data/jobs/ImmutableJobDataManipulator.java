package de.lergin.sponge.jobs.data.jobs;

import de.lergin.sponge.jobs.data.JobKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableMapValue;
import org.spongepowered.api.data.value.immutable.ImmutableSetValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;

import java.util.*;

public class ImmutableJobDataManipulator extends AbstractImmutableData<ImmutableJobDataManipulator, JobData> {
    Map<String, Double> jobs = new HashMap<>();
    boolean jobsEnabled = true;
    Set<String> selectedJobs = new HashSet<>();
    Map<String, Long> abilityUsed = new HashMap<>();

    protected ImmutableJobDataManipulator(Map<String, Double> jobs, boolean jobsEnabled, Set<String> selectedJobs, Map<String, Long> abilityUsed) {
        this.jobs.putAll(jobs);
        this.jobsEnabled = jobsEnabled;
        this.selectedJobs = selectedJobs;
        this.abilityUsed = abilityUsed;

        registerGetters();
    }

    public ImmutableMapValue<String, Double> jobs(){
        return Sponge.getRegistry().getValueFactory().createMapValue(JobKeys.JOB_DATA, this.jobs).asImmutable();
    }

    public ImmutableValue<Boolean> jobsEnabled(){
        return Sponge.getRegistry().getValueFactory().createValue(JobKeys.JOB_ENABLED, this.jobsEnabled).asImmutable();
    }

    public ImmutableSetValue<String> selectedJobs(){
        return Sponge.getRegistry().getValueFactory().createSetValue(JobKeys.JOB_SELECTED, this.selectedJobs).asImmutable();
    }

    public ImmutableMapValue<String, Long> abilityUsed(){
        return Sponge.getRegistry().getValueFactory().createMapValue(JobKeys.JOB_ABILITY_USED, this.abilityUsed).asImmutable();
    }

    @Override
    protected void registerGetters() {
        registerFieldGetter(JobKeys.JOB_DATA, () -> this.jobs);
        registerKeyValue(JobKeys.JOB_DATA, this::jobs);

        registerFieldGetter(JobKeys.JOB_ENABLED, () -> this.jobsEnabled);
        registerKeyValue(JobKeys.JOB_ENABLED, this::jobsEnabled);

        registerFieldGetter(JobKeys.JOB_SELECTED, () -> this.selectedJobs);
        registerKeyValue(JobKeys.JOB_SELECTED, this::selectedJobs);

        registerFieldGetter(JobKeys.JOB_ABILITY_USED, () -> this.abilityUsed);
        registerKeyValue(JobKeys.JOB_ABILITY_USED, this::abilityUsed);
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
        return new JobData(jobs, jobsEnabled, selectedJobs, abilityUsed);
    }

    @Override
    public int compareTo(ImmutableJobDataManipulator o) {
        return 0;
    }

    @Override
    public int getContentVersion() {
        return 8;
    }
}
