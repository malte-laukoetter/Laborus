package de.lergin.sponge.laborus.api;

import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.job.Job;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.text.TextElement;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;

public abstract class JobAction<T extends JobItem> {
    public JobAction(Job job, List<T> jobItems) {
        this.jobItems = jobItems;
        this.job = job;
    }

    private Laborus plugin = Laborus.instance();
    private List<T> jobItems;
    private Job job;

    public List<T> getJobItems() {
        return jobItems;
    }

    public Job getJob() {
        return job;
    }

    public void onEvent(Cancellable event, Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
        if (job.enabled(player)) {
            if(!isWorking.getAsBoolean()){
                return;
            }

            T jobItem = getJobItem.call();

            if(getJobItems().stream().noneMatch((item)-> item.matches(jobItem))){
                return;
            }

            if(!getJobItems().contains(jobItem)){
                return;
            }

            if(jobItem.canDo(getJob().getLevel(player))){
                plugin.config.base.loggingConfig
                        .jobActions(getJob(), "Awarding JobItem ({})", jobItem.getItem());

                double newXp = jobItem.getXp() *
                        (getJob().isSelected(player) ? 1 : Laborus.instance().config.base.xpWithoutJob);

                getJob().addXp(player, newXp);
                getJob().jobBoni().stream()
                        .filter(jobBonus -> jobBonus.canHappen(getJob(), de.lergin.sponge.laborus.job.JobAction.BREAK, jobItem, player))
                        .forEach(jobBonus -> jobBonus.useBonus(jobItem, player, jobItem.getItem()));
            }else{
                plugin.config.base.loggingConfig
                        .jobActions(getJob(), "Cannot use JobItem ({})", jobItem.getName(Locale.getDefault()));

                Map<String, TextElement> args = getJob().textArgs(player);

                args.put("item", jobItem.getName(player.getLocale()));

                event.setCancelled(true);
            }
        }
    }
}
