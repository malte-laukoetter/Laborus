package de.lergin.sponge.jobs.data;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;

import static org.spongepowered.api.data.key.KeyFactory.makeMapKey;
import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeSetKey;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

public class JobKeys {
    public static final Key<MapValue<String, Double>> JOB_DATA = makeMapKey(String.class, Double.class, of("JobData"));
    public static final Key<Value<Boolean>> JOB_ENABLED = makeSingleKey(Boolean.class, Value.class, of("JobEnabled"));
    public static final Key<SetValue<String>> JOB_SELECTED = makeSetKey(String.class, of("JobSelected"));
    public static final Key<MapValue<String, Long>> JOB_ABILITY_USED = makeMapKey(String.class, Long.class, of("JobAbilityUsed"));
}
