package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAction;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;

import java.util.List;

public class PlaceBlockListener extends BlockJobListener {
    public PlaceBlockListener(Job job, List<BlockType> blockTypes) {
        super(job, blockTypes);
    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player) {
        if (event.getCause().get("Source", Player.class).isPresent()) {
            for (Transaction<BlockSnapshot> transaction : event.getTransactions()) {
                BlockType blockType = transaction.getFinal().getState().getType();

                if (blockTypes.contains(blockType)) {
                    event.setCancelled(
                            !job.onBlockEvent(blockType, player, JobAction.PLACE)
                    );
                }
            }
        }
    }
}
