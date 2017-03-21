package de.lergin.sponge.laborus.api;

import com.google.common.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class JobService {
    Map<Object, TypeToken<? extends JobBonus>> jobBoni = new HashMap<>();

    public void registerJobBonus(Class<? extends JobBonus> bonusClass, String configurationName){
        registerJobBonus(bonusClass, configurationName, "");
    }

    public void registerJobBonus(Class<? extends JobBonus> bonusClass, String configurationName, String configurationComment){
        System.out.println(bonusClass);

        jobBoni.put(configurationName, TypeToken.of(bonusClass));
    }

    public Map<Object, TypeToken<? extends JobBonus>> getJobBoni(){
        return jobBoni;
    }
}
