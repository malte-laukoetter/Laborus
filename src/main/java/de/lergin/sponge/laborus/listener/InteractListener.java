package de.lergin.sponge.laborus.listener;

import de.lergin.sponge.laborus.api.JobAction;
import de.lergin.sponge.laborus.job.Job;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.HandInteractEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemType;

import java.util.List;

/**
 * listener for interacting jobEvents
 */
public class InteractListener extends JobAction<ItemType> {
    public InteractListener(Job job, List<ItemType> itemTypes) {
        super(job, itemTypes);
    }

    @Listener
    public void onEvent(HandInteractEvent event, @First Player player) throws Exception {
        super.onEvent(event, player,
                () -> player.getItemInHand(event.getHandType()).isPresent(),
                () -> player.getItemInHand(event.getHandType()).get().getItem());
    }
}
