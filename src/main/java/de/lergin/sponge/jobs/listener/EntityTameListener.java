package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.TameEntityEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;

public class EntityTameListener extends JobListener<EntityType> {
    public EntityTameListener(Job job, List<EntityType> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onEvent(TameEntityEvent event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent() &&
                JOB.enabled(player)) {
            final EntityType ENTITY_TYPE = event.getTargetEntity().getType();

            if (JOB_ITEM_TYPES.contains(ENTITY_TYPE)) {
                event.setCancelled(!JOB.onJobListener(
                        ENTITY_TYPE,
                        player,
                        JobAction.ENTITY_TAME
                ));
            }
        }
    }
}
