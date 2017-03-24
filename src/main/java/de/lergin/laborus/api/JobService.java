package de.lergin.laborus.api;

import com.google.common.reflect.TypeToken;
import de.lergin.laborus.Laborus;

import java.util.HashMap;
import java.util.Map;

public class JobService {
    private Map<Object, TypeToken<? extends JobBonus>> jobBoni = new HashMap<>();
    private Map<Object, TypeToken<? extends JobAction>> jobAction = new HashMap<>();

    private Laborus plugin = Laborus.instance();

    public void registerJobBonus(Class<? extends JobBonus> bonusClass, String configurationName){
        registerJobBonus(bonusClass, configurationName, "");
    }

    public void registerJobBonus(Class<? extends JobBonus> bonusClass, String configurationName, String configurationComment){
        jobBoni.put(configurationName, TypeToken.of(bonusClass));

        plugin.getLogger().info("Registered JobBonus {}", bonusClass.getCanonicalName());
    }

    public Map<Object, TypeToken<? extends JobBonus>> getJobBoni(){
        return jobBoni;
    }

    public void registerJobAction(Class<? extends JobAction> actionClass, String configurationName){
        registerJobAction(actionClass, configurationName, "");
    }

    public void registerJobAction(Class<? extends JobAction> actionClass, String configurationName, String configurationComment){
        jobAction.put(configurationName, TypeToken.of(actionClass));

        plugin.getLogger().info("Registered JobAction {}", actionClass.getCanonicalName());
    }

    public Map<Object, TypeToken<? extends JobAction>> getJobAction(){
        return jobAction;
    }
}
