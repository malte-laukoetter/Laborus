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
 * listener for place block jobEvents
 */
public class PlaceBlockListener extends JobListener<String> {
    public PlaceBlockListener(Job job, List<String> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onEvent(ChangeBlockEvent.Place event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent() && JOB.enabled(player)) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                final BlockSnapshot FINAL_BLOCK = transaction.getFinal();
                final BlockState BLOCK_STATE = FINAL_BLOCK.getState();

                JOB_ITEM_TYPES.stream()
                        .filter(item -> BlockStateComparator.compare(BLOCK_STATE, item))
                        .filter(item ->
                                AntiReplaceFarming.testLocation(
                                        FINAL_BLOCK.getLocation().get(),
                                        FINAL_BLOCK.getState(),
                                        JobAction.BREAK
                                )
                        )
                        .filter(item -> !JOB.onJobListener(BLOCK_STATE, player, JobAction.PLACE))
                        .forEach(item -> transaction.setValid(false));

                AntiReplaceFarming.addLocation(
                        FINAL_BLOCK.getLocation().get(),
                        FINAL_BLOCK.getState(),
                        JobAction.PLACE
                );
            }
        }
    }
}
