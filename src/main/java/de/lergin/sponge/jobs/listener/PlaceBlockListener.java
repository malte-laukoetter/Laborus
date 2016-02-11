package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.data.jobs.JobData;
import de.lergin.sponge.jobs.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.ChangeBlockEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Last;
import org.spongepowered.api.text.Text;

import java.util.List;

public class PlaceBlockListener {
    List<BlockType> blockTypes;
    Job job;

    public PlaceBlockListener(Job job, List<BlockType> blockTypes) {
        this.blockTypes = blockTypes;
        this.job = job;
    }

    @Listener
    public void onBlockPlace(ChangeBlockEvent.Place event, @First Player player){
        if(event.getCause().get("Source", Player.class).isPresent()){
            for(Transaction<BlockSnapshot> transaction : event.getTransactions()){
                if(blockTypes.contains(transaction.getFinal().getState().getType())){
                    job.addXp(player, 1);

                    player.sendMessage(Text.of(player.get(JobKeys.JOB_DATA).get().get(job.getId())));
                }
            }
        }
    }
}
