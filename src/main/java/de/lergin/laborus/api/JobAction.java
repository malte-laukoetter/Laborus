package de.lergin.laborus.api;

import de.lergin.laborus.Laborus;
import de.lergin.laborus.config.TranslationKeys;
import de.lergin.laborus.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.Event;
import org.spongepowered.api.text.TextElement;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;

import static de.lergin.laborus.api.JobActionState.BLOCK;
import static de.lergin.laborus.api.JobActionState.IGNORE;
import static de.lergin.laborus.api.JobActionState.SUCCESS;

/**
 * an action that can award some xp and so one
 *
 * it needs to be registered with the JobService:
 * JobService jobService = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();
 * jobService.registerJobAction(Class.class, "nameForTheConfig");
 *
 * The JobAction need to implement Serializable which is done in the Simplest way by using the @ConfigSerializable
 * annotation.
 *
 * The JobAction should have a Listener that listens to an event and then calls the onEvent() method.
 *
 *
 * For an example you can look at (@see EntityDamageJobAction)
 * 
 * @param <T> the Type of the JobItems used by this JobAction
 */
public abstract class JobAction<T extends JobItem> implements Serializable {
    public JobAction() {}

    public abstract String getId();

    private Laborus plugin = Laborus.instance();

    private Job job;

    public abstract List<T> getJobItems();

    public Job getJob() {
        return job;
    }

    public void init(Job job){
        this.job = job;
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    public JobActionState onEvent(Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
        if (!job.enabled(player) || !isWorking.getAsBoolean()) return IGNORE;

        T actionJobItem = getJobItem.call();

        Optional<T> optional = getJobItems().stream().filter((item) -> item.matches(actionJobItem)).findAny();

        if(!optional.isPresent()) return IGNORE;

        T jobItem = optional.get();

        if (!jobItem.canDo(getJob().getLevel(player))) return BLOCK;


        plugin.config.base.loggingConfig
                .jobActions(getJob(), "Awarding JobItem ({})", jobItem.getItem());

        double newXp = jobItem.getXp() *
                (getJob().isSelected(player) ? 1 : Laborus.instance().config.base.xpWithoutJob);

        getJob().addXp(player, newXp);
        getJob().jobBoni().stream()
                .filter(jobBonus -> jobBonus.canHappen(getJob(), this, jobItem, player))
                .forEach(jobBonus -> jobBonus.useBonus(jobItem, player, jobItem.getItem()));

        return SUCCESS;
    }

    public JobActionState onEvent(Event event, Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
         JobActionState state = this.onEvent(player, isWorking, getJobItem);

         if (state == BLOCK){
             JobItem jobItem = getJobItem.call();

             if(event instanceof Cancellable) {
                 plugin.config.base.loggingConfig
                         .jobActions(getJob(), "Cannot use JobItem ({})", jobItem.getName(Locale.getDefault()));

                 Map<String, TextElement> args = getJob().textArgs(player);

                 args.put("item", jobItem.getName(player.getLocale()));

                 player.sendMessage(
                         plugin.translationHelper.get(TranslationKeys.JOB_LEVEL_NOT_HIGH_ENOUGH, player.getLocale()),
                         args);

                 ((Cancellable) event).setCancelled(true);
             }else{
                 plugin.config.base.loggingConfig
                         .jobActions(getJob(), "Cannot cancel JobAction ({})", jobItem.getName(Locale.getDefault()));
             }
         }

         return state;
    }

    public JobActionState onEvent(Transaction<?> transaction, Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
         JobActionState state = this.onEvent(player, isWorking, getJobItem);

         if (state == BLOCK){
             JobItem jobItem = getJobItem.call();

             plugin.config.base.loggingConfig
                     .jobActions(getJob(), "Cannot use JobItem ({})", jobItem.getName(Locale.getDefault()));

             Map<String, TextElement> args = getJob().textArgs(player);

             args.put("item", jobItem.getName(player.getLocale()));

             player.sendMessage(
                     plugin.translationHelper.get(TranslationKeys.JOB_LEVEL_NOT_HIGH_ENOUGH, player.getLocale()),
                     args);

             transaction.setValid(false);
         }

         return state;
    }
}