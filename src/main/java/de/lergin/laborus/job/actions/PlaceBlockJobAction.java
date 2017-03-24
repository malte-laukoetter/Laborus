package de.lergin.laborus.job.actions;

import de.lergin.laborus.api.JobAction;
import de.lergin.laborus.api.JobActionState;
import de.lergin.laborus.job.items.BlockJobItem;
import de.lergin.laborus.util.AntiReplaceFarming;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;

/**
 * listener for place block jobEvents
 */
@ConfigSerializable
public class PlaceBlockJobAction extends JobAction<BlockJobItem> {
    public PlaceBlockJobAction() {}

    @Setting(value = "items")
    private List<BlockJobItem> jobItems;

    @Override
    public List<BlockJobItem> getJobItems() {
        return jobItems;
    }

    @Override
    public String getId() {
        return "PLACE";
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Place event, @First Player player) throws Exception {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            JobActionState state = super.onEvent(transaction, player,
                    () -> AntiReplaceFarming.testLocation(
                            transaction.getOriginal().getLocation().get(),
                            transaction.getOriginal().getState(),
                            "BREAK"
                    ),
                    () -> BlockJobItem.fromBlockState(transaction.getOriginal().getState()));

            if(state == JobActionState.SUCCESS){
                AntiReplaceFarming.addLocation(
                        transaction.getOriginal().getLocation().get(),
                        transaction.getOriginal().getState(),
                        "PLACE"
                );
            }
        }
    }
}
