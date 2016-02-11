package de.lergin.sponge.jobs.data;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;
import static org.spongepowered.api.data.DataQuery.of;

public class JobKeys {
    public static final Key<Value<String>> JOB_ID = makeSingleKey(String.class, Value.class, of("JobId"));
    public static final Key<Value<Integer>> JOB_XP = makeSingleKey(Integer.class, Value.class, of("JobXp"));
}
