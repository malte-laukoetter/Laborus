package de.lergin.sponge.laborus.listener;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAction;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.InteractEvent;
import org.spongepowered.api.event.entity.living.humanoid.HandInteractEvent;
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
    public void onEvent(HandInteractEvent event, @First Player player) {
        if (JOB.enabled(player)) {
            if(player.getItemInHand(event.getHandType()).isPresent()) {
                final ItemType ITEM_TYPE = player.getItemInHand(event.getHandType()).get().getItem();

                if (JOB_ITEM_TYPES.contains(ITEM_TYPE)) {
                    event.setCancelled(
                            !JOB.onJobListener(ITEM_TYPE, player, JobAction.ITEM_USE)
                    );
                }
            }
        }
    }
}
