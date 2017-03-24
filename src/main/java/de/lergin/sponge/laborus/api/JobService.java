package de.lergin.sponge.laborus.api;

import com.google.common.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class JobService {
    private Map<Object, TypeToken<? extends JobBonus>> jobBoni = new HashMap<>();
    Map<Object, TypeToken<? extends JobAction>> jobAction = new HashMap<>();

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

    public void registerJobAction(Class<? extends JobAction> actionClass, String configurationName){
        registerJobAction(actionClass, configurationName, "");
    }

    public void registerJobAction(Class<? extends JobAction> actionClass, String configurationName, String configurationComment){
        System.out.println(actionClass);

        jobAction.put(configurationName, TypeToken.of(actionClass));
    }

    public Map<Object, TypeToken<? extends JobAction>> getJobAction(){
        return jobAction;
    }
}
