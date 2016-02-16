package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;
import java.util.Optional;

public class EntityKillListener extends JobListener<EntityType> {
    public EntityKillListener(Job job, List<EntityType> entityTypes) {
        super(job, entityTypes);
    }

    @Listener
    public void onEvent(DestructEntityEvent.Death event, @First EntityDamageSource damageSource) {
        Optional<EntityDamageSource> optionalDamageSource = event.getCause().get("Source", EntityDamageSource.class);

        if (optionalDamageSource.isPresent() && optionalDamageSource.get().getSource().getType().equals(EntityTypes.PLAYER)) {
            final EntityType ENTITY_TYPE = event.getTargetEntity().getType();

            if (JOB_ITEM_TYPES.contains(ENTITY_TYPE)) {
                JOB.onJobListener(ENTITY_TYPE, (Player) optionalDamageSource.get().getSource(), JobAction.ENTITY_KILL);
            }
        }
    }
}
