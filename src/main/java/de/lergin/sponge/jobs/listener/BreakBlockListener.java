package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import de.lergin.sponge.jobs.util.AntiReplaceFarming;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;

public class BreakBlockListener extends JobListener<BlockType> {
    public BreakBlockListener(Job job, List<BlockType> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Break event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent() && JOB.enabled(player)) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                //test if the block currently shouldn't be rewarded
                if(!AntiReplaceFarming.testLocation(transaction.getOriginal().getLocation().get(), JobAction.BREAK)){
                    event.setCancelled(false);
                    return;
                }

                final BlockType BLOCK_TYPE = transaction.getOriginal().getState().getType();

                if (JOB_ITEM_TYPES.contains(BLOCK_TYPE)) {
                    if(JOB.onJobListener(BLOCK_TYPE, player, JobAction.BREAK)){
                        AntiReplaceFarming.addLocation(
                                transaction.getOriginal().getLocation().get(),
                                transaction.getOriginal().getState(),
                                JobAction.BREAK
                        );

                        event.setCancelled(false);
                    }else{
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
