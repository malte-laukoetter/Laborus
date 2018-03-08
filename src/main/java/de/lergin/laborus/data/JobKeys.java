package de.lergin.laborus.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;

import static org.spongepowered.api.data.DataQuery.of;

public class JobKeys {
    public static final Key<MapValue<String, Double>> JOB_DATA = Key.builder()
        .type(new TypeToken<MapValue<String, Double>>() { })
        .id("jobdata")
        .name("Job Data")
        .query(of("JobData"))
        .build();

    public static final Key<Value<Boolean>> JOB_ENABLED = Key.builder()
        .type(new TypeToken<Value<Boolean>>() { })
        .id("job_enabled")
        .name("Jobs Enabled")
        .query(of("JobEnabled"))
        .build();

    public static final Key<SetValue<String>> JOB_SELECTED = Key.builder()
        .type(new TypeToken<SetValue<String>>() { })
        .id("jobselected")
        .name("Job Selected")
        .query(of("JobSelected"))
        .build();

    public static final Key<MapValue<String, Long>> JOB_ABILITY_USED = Key.builder()
        .type(new TypeToken<MapValue<String, Long>>() { })
        .id("job_ability_used")
        .name("Job Ability used")
        .query(of("JobAbilityUsed"))
        .build();
}
