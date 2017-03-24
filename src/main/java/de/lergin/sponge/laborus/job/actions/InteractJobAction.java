package de.lergin.sponge.laborus.job.actions;

import de.lergin.sponge.laborus.api.JobAction;
import de.lergin.sponge.laborus.job.items.ItemJobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.living.humanoid.HandInteractEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;

/**
 * listener for interacting jobEvents
 */
@ConfigSerializable
public class InteractJobAction extends JobAction<ItemJobItem> {
    public InteractJobAction(){}

    @Setting(value = "items")
    private List<ItemJobItem> jobItems;

    @Override
    public List<ItemJobItem> getJobItems() {
        return jobItems;
    }

    @Override
    public String getId() {
        return "ITEM_USE";
    }

    @Listener
    public void onEvent(HandInteractEvent event, @First Player player) throws Exception {
        super.onEvent(event, player,
                () -> player.getItemInHand(event.getHandType()).isPresent(),
                () -> ItemJobItem.fromItemStack(player.getItemInHand(event.getHandType()).get()));
    }
}
