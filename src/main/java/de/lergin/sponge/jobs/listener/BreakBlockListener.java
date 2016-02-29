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

/**
 * listener for break block jobEvents
 */
public class BreakBlockListener extends JobListener<BlockType> {
    public BreakBlockListener(Job job, List<BlockType> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Break event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent() && JOB.enabled(player)) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                final BlockSnapshot ORIGINAL_BLOCK = transaction.getOriginal();
                final BlockType BLOCK_TYPE = ORIGINAL_BLOCK.getState().getType();

                if (JOB_ITEM_TYPES.contains(BLOCK_TYPE)) {
                    //test if the block currently shouldn't be rewarded
                    if(AntiReplaceFarming.testLocation(ORIGINAL_BLOCK.getLocation().get(), ORIGINAL_BLOCK.getState(), JobAction.PLACE)){
                        if(!JOB.onJobListener(BLOCK_TYPE, player, JobAction.BREAK))
                            transaction.setValid(false);
                    }
                }

                AntiReplaceFarming.addLocation(
                        ORIGINAL_BLOCK.getLocation().get(),
                        ORIGINAL_BLOCK.getState(),
                        JobAction.BREAK
                );
            }
        }
    }
}
