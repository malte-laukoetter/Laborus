package de.lergin.laborus.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;

import static org.spongepowered.api.data.DataQuery.of;

public class JobKeys {
    public static final Key<MapValue<String, Double>> JOB_DATA = Key.builder()
        .id("jobdata")
        .name("Job Data")
        .query(of("JobData"))
        .type(new TypeToken<MapValue<String, Double>>() { })
        .build();

    public static final Key<Value<Boolean>> JOB_ENABLED = Key.builder()
        .id("job_enabled")
        .name("Jobs Enabled")
        .query(of("JobEnabled"))
        .type(new TypeToken<Value<Boolean>>() { })
        .build();

    public static final Key<SetValue<String>> JOB_SELECTED = Key.builder()
        .id("jobselected")
        .name("Job Selected")
        .query(of("JobSelected"))
        .type(new TypeToken<SetValue<String>>() { })
        .build();

    public static final Key<MapValue<String, Long>> JOB_ABILITY_USED = Key.builder()
        .id("job_ability_used")
        .name("Job Ability used")
        .query(of("JobAbilityUsed"))
        .type(new TypeToken<MapValue<String, Long>>() { })
        .build();
}
