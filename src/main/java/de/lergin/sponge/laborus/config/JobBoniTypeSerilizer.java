package de.lergin.sponge.laborus.config;

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.laborus.api.JobBonus;
import de.lergin.sponge.laborus.job.JobBoni;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.*;

public class JobBoniTypeSerilizer implements TypeSerializer<JobBoni> {
    private final Map<Object, TypeToken<? extends JobBonus>> jobBoniTypeTokens;

    public JobBoniTypeSerilizer(Map<Object, TypeToken<? extends JobBonus>> jobBoni) {
        this.jobBoniTypeTokens = jobBoni;
    }

    @Override
    public JobBoni deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        Map<TypeToken<? extends JobBonus>, List<JobBonus>> jobBoni = new HashMap<>();

        value.getChildrenMap().forEach((configKey,node) -> {
            TypeToken<? extends JobBonus> typeToken = jobBoniTypeTokens.get(configKey);
            List<JobBonus> boni = new ArrayList<>();

            node.getChildrenList().forEach((n)->{
                try {
                    boni.add(n.getValue(typeToken));
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            });

            jobBoni.put(typeToken, boni);
        });

        return new JobBoni(jobBoni);
    }

    @Override
    public void serialize(TypeToken<?> type, JobBoni obj, ConfigurationNode value) throws ObjectMappingException {
        obj.getRaw().forEach((typeToken, jobBonus) -> {
            Optional<Object> optional = jobBoniTypeTokens.entrySet().stream().filter((e)-> e.getValue().equals(typeToken)).limit(1)
                    .map(Map.Entry::getKey).findFirst();

            if(!optional.isPresent()){
                throw new RuntimeException(new ObjectMappingException());
            }

            Object key = optional.get();

            value.getNode(key).setValue(jobBonus);
        });
    }
}
