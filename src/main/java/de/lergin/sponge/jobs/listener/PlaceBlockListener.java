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
 * listener for place block jobEvents
 */
public class PlaceBlockListener extends JobListener<BlockType> {
    public PlaceBlockListener(Job job, List<BlockType> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Place event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent() && JOB.enabled(player)) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                final BlockSnapshot FINAL_BLOCK = transaction.getFinal();
                final BlockType BLOCK_TYPE = FINAL_BLOCK.getState().getType();

                if (JOB_ITEM_TYPES.contains(BLOCK_TYPE)) {
                    //test if the block currently shouldn't be rewarded
                    if(!AntiReplaceFarming.testLocation(FINAL_BLOCK.getLocation().get(), FINAL_BLOCK.getState(), JobAction.BREAK)){
                        if(!JOB.onJobListener(BLOCK_TYPE, player, JobAction.PLACE)){
                            transaction.setValid(false);
                        }
                    }
                }

                AntiReplaceFarming.addLocation(
                        FINAL_BLOCK.getLocation().get(),
                        FINAL_BLOCK.getState(),
                        JobAction.PLACE
                );
            }
        }
    }
}
