package de.lergin.sponge.jobs.data;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.Value;

import static org.spongepowered.api.data.key.KeyFactory.makeMapKey;
import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

public class JobKeys {
    public static final Key<MapValue<String, Float>> JOB_DATA = makeMapKey(String.class, Float.class, of("JobData"));
    public static final Key<Value<Boolean>> JOB_ENABLED = makeSingleKey(Boolean.class, Value.class, of("JobEnabled"));
}
