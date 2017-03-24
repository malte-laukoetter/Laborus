package de.lergin.sponge.laborus.config;

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.laborus.api.JobAction;
import de.lergin.sponge.laborus.api.JobService;
import de.lergin.sponge.laborus.job.JobActions;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.Sponge;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobActionsTypeSerializer implements TypeSerializer<JobActions> {
    @Override
    public JobActions deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        List<JobAction> actions = new ArrayList<>();

        JobService service = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();

        value.getChildrenMap().forEach((key, node)->{
            TypeToken<? extends JobAction> typeToken = service.getJobAction().get(key);

            try {
                actions.add(node.getValue(typeToken));
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        });

        return new JobActions(actions);
    }

    @Override
    public void serialize(TypeToken<?> type, JobActions obj, ConfigurationNode value) throws ObjectMappingException {
        JobService service = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();

        service.getJobAction().forEach((key, token)->{
            try {
                test(token, value.getNode(key), obj);
            } catch (ObjectMappingException e) {
                e.printStackTrace();
            }
        });
    }

    private static <T extends JobAction> void test(TypeToken<T> token, ConfigurationNode node, JobActions obj) throws ObjectMappingException {
        Optional<T> optional = obj.get(token);

        if(optional.isPresent()){
            node.setValue(token, optional.get());
        }
    }
}
