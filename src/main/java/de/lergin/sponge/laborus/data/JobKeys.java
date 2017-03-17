package de.lergin.sponge.laborus.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.KeyFactory;
import org.spongepowered.api.data.value.mutable.MapValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Map;
import java.util.Set;

import static org.spongepowered.api.data.DataQuery.of;
import static org.spongepowered.api.data.key.KeyFactory.makeSingleKey;

public class JobKeys {
    public static final Key<Value<String>> DEFAULT_HOME = KeyFactory.makeSingleKey(
            TypeToken.of(String.class),
            new TypeToken<Value<String>>() {},
            of("DefaultHome"), "myhomes:default_home", "Default Home");

    public static final Key<MapValue<String, Double>> JOB_DATA = KeyFactory.makeMapKey(
            new TypeToken<Map<String, Double>>() { },
            new TypeToken<MapValue<String, Double>>() { },
            of("JobData"),
            "laborus:jobdata",
            "Job Data"
    );

    public static final Key<Value<Boolean>> JOB_ENABLED = makeSingleKey(
            TypeToken.of(Boolean.class),
            new TypeToken<Value<Boolean>>() {},
            of("JobEnabled"),
            "laborus:job_enabled",
            "Jobs Enabled"
    );

    public static final Key<SetValue<String>> JOB_SELECTED = KeyFactory.makeSetKey(
            new TypeToken<Set<String>>() { },
            new TypeToken<SetValue<String>>() { },
            of("JobSelected"),
            "laborus:jobselected",
            "Job Selected"
    );

    public static final Key<MapValue<String, Long>> JOB_ABILITY_USED = KeyFactory.makeMapKey(
            new TypeToken<Map<String, Long>>() { },
            new TypeToken<MapValue<String, Long>>() { },
            of("JobAbilityUsed"),
            "laborus:job_ability_used",
            "Job Ability used"
    );
}
