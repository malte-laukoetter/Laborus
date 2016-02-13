package de.lergin.sponge.jobs.data;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;

import static org.spongepowered.api.data.key.KeyFactory.makeMapKey;
import static org.spongepowered.api.data.DataQuery.of;

public class JobKeys {
    public static final Key<MapValue<String, Float>> JOB_DATA = makeMapKey(String.class, Float.class, of("JobData"));
}
