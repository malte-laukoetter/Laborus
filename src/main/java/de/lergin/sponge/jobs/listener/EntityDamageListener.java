package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;
import java.util.Optional;

public class EntityDamageListener extends JobListener<EntityType> {
    public EntityDamageListener(Job job, List<EntityType> entityTypes) {
        super(job, entityTypes);
    }

    @Listener
    public void onEvent(DamageEntityEvent event, @First EntityDamageSource damageSource) {
        if (event.getCause().get("Source", EntityDamageSource.class).isPresent() &&
                damageSource.getSource().getType().equals(EntityTypes.PLAYER)) {
            final EntityType ENTITY_TYPE = event.getTargetEntity().getType();

            if (JOB_ITEM_TYPES.contains(ENTITY_TYPE)) {
                event.setCancelled(!JOB.onJobListener(
                        ENTITY_TYPE,
                        (Player) damageSource.getSource(),
                        JobAction.ENTITY_DAMAGE
                ));
            }
        }
    }
}