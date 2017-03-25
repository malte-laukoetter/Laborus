package de.lergin.laborus.config;

import com.google.common.reflect.TypeToken;
import de.lergin.laborus.api.JobAbility;
import de.lergin.laborus.api.JobService;
import de.lergin.laborus.job.JobAbilities;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;

import java.util.Optional;

public class JobAbilitiesTypeSerializer implements TypeSerializer<JobAbilities> {
    @Override
    public JobAbilities deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        JobService service = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();

        String abilityType = value.getNode("type").getString();

        JobAbilities jobAbilities = new JobAbilities();

        service.getJobAilities().forEach((key, typeToken)->{
            if(key.equals(abilityType)){
                try {
                    jobAbilities.set(value.getValue(typeToken));
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }
            }
        });

        return jobAbilities;
    }

    @Override
    public void serialize(TypeToken<?> type, JobAbilities obj, ConfigurationNode value) throws ObjectMappingException {
        JobService service = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();

        TypeToken<? extends JobAbility> typeToken = TypeToken.of(obj.get().getClass());

        test(typeToken, value, obj);

        service.getJobAilities().forEach((key, token)->{
            if(token.equals(typeToken)){
                value.getNode("type").setValue(key);
            }
        });
    }

    private static <T extends JobAbility> void test(TypeToken<T> token, ConfigurationNode node, JobAbilities obj) throws ObjectMappingException {
        Optional<T> optional = obj.get(token);

        if(optional.isPresent()){
            node.setValue(token, optional.get());
        }
    }
}
