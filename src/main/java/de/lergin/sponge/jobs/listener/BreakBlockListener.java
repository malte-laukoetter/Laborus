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
import org.spongepowered.api.text.Text;

import java.util.List;

public class BreakBlockListener {
    List<BlockType> blockTypes;
    Job job;

    public BreakBlockListener(Job job, List<BlockType> blockTypes) {
        this.blockTypes = blockTypes;
        this.job = job;
    }

    @Listener
    public void onBlockBreak(ChangeBlockEvent.Break event, @First Player player){
        for(Transaction<BlockSnapshot> transaction : event.getTransactions()){
            if(blockTypes.contains(transaction.getOriginal().getState().getType())){
                job.addXp(player, 1);

                JobDataManipulatorBuilder builder = (JobDataManipulatorBuilder)
                        Sponge.getDataManager().getManipulatorBuilder(JobData.class).get();
                JobData data = builder.job(job).create();

         //       player.offer(data);

                System.out.println(job.getXp());


                player.sendMessage(Text.of(player.get(JobKeys.JOB_XP).get()));
            }
        }
    }
}
