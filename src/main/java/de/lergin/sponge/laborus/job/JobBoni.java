package de.lergin.sponge.laborus.job;

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.laborus.api.JobBonus;
import de.lergin.sponge.laborus.api.JobService;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JobBoni {
    private final Map<TypeToken<? extends JobBonus>, List<JobBonus>> jobBoni;

    public Map<TypeToken<? extends JobBonus>, List<JobBonus>> getRaw(){
        return jobBoni;
    }

    public List<JobBonus> get() {
        List<JobBonus> jobBoni = new ArrayList<>();

        return jobBoni;
    }

    public JobBoni(Map<TypeToken<? extends JobBonus>, List<JobBonus>> jobBoni) {
        this.jobBoni = jobBoni;
    }
}
