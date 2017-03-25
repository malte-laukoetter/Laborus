package de.lergin.laborus.job;

import com.google.common.reflect.TypeToken;
import de.lergin.laborus.api.JobAbility;

import java.util.Optional;

public class JobAbilities {
    private JobAbility jobAbility;

    public JobAbility get(){
        return jobAbility;
    }

    public <T extends JobAbility> Optional<T> get(TypeToken<T> typeToken) {
        if(get() != null && TypeToken.of(get().getClass()).equals(typeToken)){
            return Optional.of((T) get());
        }else {
            return Optional.empty();
        }
    }

    public void set(JobAbility ability){
        jobAbility = ability;
    }

    public JobAbilities(){}

    public JobAbilities(JobAbility jobAbility) {
        this.jobAbility = jobAbility;
    }
}
