package de.lergin.laborus.config;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import de.lergin.laborus.api.JobBonus;
import de.lergin.laborus.api.JobService;
import de.lergin.laborus.job.JobBoni;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;

import java.util.List;

public class JobBoniTypeSerializer<T extends JobBonus> implements TypeSerializer<JobBoni<T>> {
    @Override
    public JobBoni<T> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        TypeToken typeToken = Sponge.getServiceManager().getRegistration(JobService.class).get()
                .getProvider().getJobBoni().get(value.getKey());

        return new JobBoni(value.getList(typeToken));
    }

    @Override
    public void serialize(TypeToken<?> type, JobBoni<T> obj, ConfigurationNode value) throws ObjectMappingException {
        if(obj.get().isEmpty()) {
            value.setValue(ImmutableList.of());
        }

        TypeToken<T> token = TypeToken.of((Class<T>) obj.get().get(0).getClass());

        value.setValue(listOf(token), obj.get());
    }

    private static <T> TypeToken<List<T>> listOf(TypeToken<T> elementType) {
        return new TypeToken<List<T>>() {}
                .where(new TypeParameter<T>() {}, elementType);
    }
}
