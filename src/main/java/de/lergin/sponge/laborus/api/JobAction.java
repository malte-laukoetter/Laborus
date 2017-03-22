package de.lergin.sponge.laborus.api;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobItem;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;

import java.util.List;
import java.util.concurrent.Callable;

public abstract class JobAction<T extends JobItem> {
    public JobAction(Job job, List<T> jobItems) {
        this.jobItems = jobItems;
        this.job = job;
    }

    private List<T> jobItems;
    private Job job;

    public void onEvent(Cancellable event, Player player, Callable<Boolean> isWorking, Callable<T> getJobItem) throws Exception {
        if (job.enabled(player)) {
            if(!isWorking.call()){
                event.setCancelled(true);
                return;
            }

            T jobItem = getJobItem.call();

            if(!jobItems.contains(jobItem)){
                event.setCancelled(true);
                return;
            }

            Boolean returnValue = job.onJobListener(
                    jobItem,
                    player,
                    de.lergin.sponge.laborus.job.JobAction.ENTITY_TAME
            );

            event.setCancelled(returnValue);
        }
    }
}
