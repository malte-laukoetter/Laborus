package de.lergin.laborus.job.actions;

import de.lergin.laborus.api.JobAction;
import de.lergin.laborus.job.items.ItemJobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.action.FishingEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.List;

@ConfigSerializable
public class FishingJobAction extends JobAction<ItemJobItem> {
    public FishingJobAction(){}

    @Setting(value = "items")
    private List<ItemJobItem> jobItems;

    @Override
    public List<ItemJobItem> getJobItems() {
        return jobItems;
    }

    @Override
    public String getId() {
        return "FISHING";
    }

    @Listener
    public void onEvent(FishingEvent.Stop event, @First Player player) throws Exception {
        for (Transaction<ItemStackSnapshot> transaction : event.getItemStackTransaction()) {
            super.onEvent(transaction, player, ()->true,
                    ()->ItemJobItem.fromItemStackSnapshot(transaction.getDefault()));
        }
    }
}
