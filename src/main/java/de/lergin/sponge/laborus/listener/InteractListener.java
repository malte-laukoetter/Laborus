package de.lergin.sponge.laborus.listener;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAction;
import org.spongepowered.api.data.type.HandTypes;
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
        if (JOB.enabled(player)) {
            if(player.getItemInHand(HandTypes.MAIN_HAND).isPresent()) {
                final ItemType ITEM_TYPE = player.getItemInHand(HandTypes.MAIN_HAND).get().getItem();

                if (JOB_ITEM_TYPES.contains(ITEM_TYPE)) {
                    event.setCancelled(
                            !JOB.onJobListener(ITEM_TYPE, player, JobAction.ITEM_USE)
                    );
                }
            }

            if(player.getItemInHand(HandTypes.OFF_HAND).isPresent()) {
                final ItemType ITEM_TYPE = player.getItemInHand(HandTypes.OFF_HAND).get().getItem();

                if (JOB_ITEM_TYPES.contains(ITEM_TYPE)) {
                    event.setCancelled(
                            !JOB.onJobListener(ITEM_TYPE, player, JobAction.ITEM_USE)
                    );
                }
            }
        }
    }
}
