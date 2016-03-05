package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import de.lergin.sponge.jobs.util.AntiReplaceFarming;
import de.lergin.sponge.jobs.util.BlockStateComparator;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockState;
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
public class BreakBlockListener extends JobListener<String> {
    public BreakBlockListener(Job job, List<String> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Break event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent() && JOB.enabled(player)) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                final BlockSnapshot ORIGINAL_BLOCK = transaction.getOriginal();
                final BlockState BLOCK_STATE = ORIGINAL_BLOCK.getState();

                JOB_ITEM_TYPES.stream()
                        .filter(item -> BlockStateComparator.compare(BLOCK_STATE, item))
                        .filter(item ->
                                AntiReplaceFarming.testLocation(
                                        ORIGINAL_BLOCK.getLocation().get(),
                                        ORIGINAL_BLOCK.getState(),
                                        JobAction.PLACE
                                )
                        )
                        .filter(item -> !JOB.onJobListener(BLOCK_STATE, player, JobAction.BREAK))
                        .forEach(item -> transaction.setValid(false));

                AntiReplaceFarming.addLocation(
                        ORIGINAL_BLOCK.getLocation().get(),
                        ORIGINAL_BLOCK.getState(),
                        JobAction.BREAK
                );
            }
        }
    }
}
