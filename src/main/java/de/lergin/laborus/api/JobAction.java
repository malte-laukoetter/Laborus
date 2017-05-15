package de.lergin.laborus.api;

import de.lergin.laborus.Laborus;
import de.lergin.laborus.config.TranslationKeys;
import de.lergin.laborus.job.Job;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.text.TextElement;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;

import static de.lergin.laborus.api.JobActionState.*;

/**
 * an action that can award some xp and so one
 *
 * it needs to be registered with the JobService in the GamePreInitialization phase:
 * JobService jobService = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();
 * jobService.registerJobAction(Class.class, "nameForTheConfig");
 *
 * The JobAction need to implement Serializable which is done in the Simplest way by using the @ConfigSerializable
 * annotation.
 *
 * The JobAction should have a Listener that listens to an event and then calls on of the onEvent() methods. The Listener
 * will be registered by Laborus in the init() method.
 *
 *
 * For an example you can look at (@see EntityDamageJobAction)
 *
 * @param <T> the Type of the JobItems used by this JobAction should be set to a type of JobItems in the implementation
 */
public abstract class JobAction<T extends JobItem> implements Serializable {
    /**
     * should return the id of the JobAction used in the config to block certain JobBoni
     * @return a unique string for the JobAction Class
     */
    public abstract String getId();

    private Laborus plugin = Laborus.instance();

    private Job job;

    /**
     * should return a list of all JobItems that can be used with the action
     * @return a list of JobItems
     */
    public abstract List<T> getJobItems();

    /**
     * gets the job that uses this JobAction
     * @return the job that uses the JobAction
     */
    public Job getJob() {
        return job;
    }

    /**
     * used by Laborus to initialize the Listener and set the Job variable
     * @param job
     */
    public void init(Job job){
        this.job = job;
        Sponge.getEventManager().registerListeners(plugin, this);
    }

    /**
     * used by Laborus to unload the JobAction (delete the listeners)
     */
    public void unload(){
        Sponge.getEventManager().unregisterListeners(this);
    }

    /**
     * handles the awarding of the JobAction
     *
     * @param player the player that has done the action
     * @param isWorking a supplier that returns if the jobAction can be awarded (eg. to check if all data is available
     * @param getJobItem a supplier that returns the JobItem that was used in the action
     * @return the state of the action
     * @throws Exception exception if something in one of the suppliers isn't working
     */
    public JobActionState onEvent(Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
        if(!isWorking.getAsBoolean()){
            return IGNORE;
        }

        T jobItem = getJobItem(getJobItem.call());

        if(jobItem == null){
            return IGNORE;
        }

        JobActionState state = onBlockEvent(player, isWorking, jobItem);

        if(state != SUCCESS){
            return state;
        }

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

    /**
     * does the same as (@see onEvent) but also handles the canceling of the event and sending of messages to the player
     * if he cannot do the action
     * @param event the event that has started the action
     */
    public JobActionState onEvent(Cancellable event, Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
         JobActionState state = this.onEvent(player, isWorking, getJobItem);

         if (state == BLOCK){
             JobItem jobItem = getJobItem.call();

             sendBlockMessage(player, jobItem);

             event.setCancelled(true);
         }

         return state;
    }

    /**
     * does the same as (@see onEvent) but also handles the canceling of the transaction and sending of messages to the
     * player if he cannot do the action
     * @param transaction the transaction that has to do with the action
     */
    public JobActionState onEvent(Transaction<?> transaction, Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
         JobActionState state = this.onEvent(player, isWorking, getJobItem);

         if (state == BLOCK){
             JobItem jobItem = getJobItem.call();

             sendBlockMessage(player, jobItem);

             transaction.setValid(false);
         }

         return state;
    }

    public JobActionState onBlockEvent(Transaction<?> transaction, Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
        JobActionState state = this.onBlockEvent(player, isWorking, getJobItem);

        if (state == BLOCK){
            JobItem jobItem = getJobItem.call();

            sendBlockMessage(player, jobItem);

            transaction.setValid(false);
        }

        return state;
    }

    public JobActionState onBlockEvent(Cancellable event, Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
        JobActionState state = this.onBlockEvent(player, isWorking, getJobItem);

        if (state == BLOCK){
            JobItem jobItem = getJobItem.call();

            sendBlockMessage(player, jobItem);

            event.setCancelled(true);
        }

        return state;
    }

    public JobActionState onBlockEvent(Player player, BooleanSupplier isWorking, JobItem jobItem) throws Exception {
        if (!job.enabled(player) || !isWorking.getAsBoolean()) return IGNORE;

        if (!jobItem.canDo(getJob().getLevel(player))) return BLOCK;

        if(jobItem.isAlsoInOtherJob()){
            // some crazy lambda stuff to test if any item in any action of any job matches the item
            if(plugin.getJobs().values().stream().anyMatch(
                    job-> job.getJobActions().stream()
                            .filter(jobAction -> Objects.equals(jobAction.getId(), this.getId()))
                            .filter(jobAction -> jobAction.getJobItems().getClass().equals(this.getJobItems().getClass()))
                            .anyMatch(jobAction -> ((List<T>) jobAction.getJobItems()).stream()
                                    .filter(item-> item.equals(jobItem))
                                    .anyMatch(item->!item.canDo(jobAction.getJob().getLevel(player)))))){
                return BLOCK_OTHER;
            }
        }

        return SUCCESS;
    }


    public JobActionState onBlockEvent(Player player, BooleanSupplier isWorking, Callable<T> getJobItem) throws Exception {
        if (!job.enabled(player) || !isWorking.getAsBoolean()) return IGNORE;

        T jobItem = getJobItem(getJobItem.call());

        if(jobItem == null){
            return IGNORE;
        }

        return onBlockEvent(player, isWorking, jobItem);
    }

    public void sendBlockMessage(Player player, JobItem jobItem){
        plugin.config.base.loggingConfig
                .jobActions(getJob(), "Cannot use JobItem ({})", jobItem.getName(Locale.getDefault()));

        Map<String, TextElement> args = getJob().textArgs(player);

        args.put("item", jobItem.getName(player.getLocale()));

        player.sendMessage(
                plugin.translationHelper.get(TranslationKeys.JOB_LEVEL_NOT_HIGH_ENOUGH, player.getLocale()),
                args);
    }

    private T getJobItem(T item){
        List<T> items = getJobItems();

        T jobItem;

        if(items.isEmpty()){
            jobItem = item;
        }else{
            Optional<T> optional = getJobItems().stream().filter((i) -> i.matches(item)).findAny();

            if(!optional.isPresent()) return null;

            jobItem = optional.get();
        }

        return jobItem;
    }
}