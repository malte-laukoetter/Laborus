package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemType;

import java.util.List;

/**
 * listener for interacting jobEvents
 */
public class InteractListener extends JobListener<ItemType> {
    public InteractListener(Job job, List<ItemType> itemTypes) {
        super(job, itemTypes);
    }

    @Listener
    public void onEvent(InteractEvent event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent() && JOB.enabled(player) &&
                player.getItemInHand().isPresent()) {
            final ItemType ITEM_TYPE = player.getItemInHand().get().getItem();

            if (JOB_ITEM_TYPES.contains(ITEM_TYPE)) {
                event.setCancelled(
                        !JOB.onJobListener(ITEM_TYPE, player, JobAction.ITEM_USE)
                );
            }
        }
    }
}
