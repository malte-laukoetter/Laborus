package de.lergin.sponge.laborus.listener;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAction;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;

/**
 * listener for entity kill jobEvents
 */
public class EntityKillListener extends JobListener<EntityType> {
    public EntityKillListener(Job job, List<EntityType> entityTypes) {
        super(job, entityTypes);
    }

    @Listener
    public void onEvent(DestructEntityEvent.Death event, @First EntityDamageSource damageSource) {
        if (event.getCause().get("Source", EntityDamageSource.class).isPresent() &&
                damageSource.getSource().getType().equals(EntityTypes.PLAYER) &&
                JOB.enabled((Player) damageSource.getSource())) {
            final EntityType ENTITY_TYPE = event.getTargetEntity().getType();

            if (JOB_ITEM_TYPES.contains(ENTITY_TYPE)) {
                JOB.onJobListener(ENTITY_TYPE, (Player) damageSource.getSource(), JobAction.ENTITY_KILL);
            }
        }
    }
}
