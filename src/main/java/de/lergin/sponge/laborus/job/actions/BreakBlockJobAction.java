package de.lergin.sponge.laborus.job.actions;

import de.lergin.sponge.laborus.api.JobAction;
import de.lergin.sponge.laborus.api.JobActionState;
import de.lergin.sponge.laborus.job.items.BlockJobItem;
import de.lergin.sponge.laborus.util.AntiReplaceFarming;
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
 * listener for break block jobEvents
 */
@ConfigSerializable
public class BreakBlockJobAction extends JobAction<BlockJobItem> {
    public BreakBlockJobAction() {}

    @Setting(value = "items")
    private List<BlockJobItem> jobItems;

    @Override
    public List<BlockJobItem> getJobItems() {
        return jobItems;
    }

    @Override
    public String getId() {
        return "BREAK";
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Break event, @First Player player) throws Exception {
        for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
            JobActionState state = super.onEvent(transaction, player,
                    () -> AntiReplaceFarming.testLocation(
                            transaction.getOriginal().getLocation().get(),
                            transaction.getOriginal().getState(),
                            "PLACE"
                    ),
                    () -> BlockJobItem.fromBlockState(transaction.getOriginal().getState()));

            if(state == JobActionState.SUCCESS){
                AntiReplaceFarming.addLocation(
                        transaction.getOriginal().getLocation().get(),
                        transaction.getOriginal().getState(),
                        "BREAK"
                );
            }
        }
    }
}
