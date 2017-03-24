package de.lergin.laborus.job;

import com.google.common.reflect.TypeToken;
import de.lergin.laborus.api.JobAction;

import java.util.List;
import java.util.Optional;

public class JobActions {
    private final List<JobAction> jobActions;

    public List<JobAction> get(){
        return jobActions;
    }

    public <T extends JobAction> Optional<T> get(TypeToken<T> typeToken) {
        return (Optional<T>) get().stream().filter((o)-> o != null && typeToken.equals(TypeToken.of(o.getClass()))).findAny();
    }

    public JobActions(List<JobAction> jobActions) {
        this.jobActions = jobActions;
    }
}
