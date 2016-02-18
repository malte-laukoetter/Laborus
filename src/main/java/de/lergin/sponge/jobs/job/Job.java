package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Job {
    private final String NAME;
    private final String ID;
    private Map<JobAction, List<JobItem>> jobActions = new HashMap<>();

    public Job(ConfigurationNode jobConfig) {
        this.NAME = jobConfig.getNode("name").getString();
        this.ID = jobConfig.getKey().toString();


        initJobAction(jobConfig.getNode("destroyBlocks"), JobAction.BREAK);
        initJobAction(jobConfig.getNode("placeBlocks"), JobAction.PLACE);
        initJobAction(jobConfig.getNode("killEntities"), JobAction.ENTITY_KILL);
        initJobAction(jobConfig.getNode("damageEntities"), JobAction.ENTITY_DAMAGE);
        initJobAction(jobConfig.getNode("useItems"), JobAction.ITEM_USE);
    }

    public String getName() {
        return NAME;
    }

    public String getId() {
        return ID;
    }

    public void addXp(Player player, float amount){
        Map<String, Float> jobData = player.get(JobKeys.JOB_DATA).orElse(new HashMap<>());

        jobData.put(getId(), jobData.getOrDefault(getId(), 0.0f) + amount);

        player.offer(new JobDataManipulatorBuilder().jobs(jobData).create());

        //TODO: setting
        player.sendMessage(ChatTypes.ACTION_BAR,
                TranslationHelper.p(player, "player.info.job.xp.action_bar", getName(), getXp(player))
        );
    }

    public float getXp(Player player){
        return player.get(JobKeys.JOB_DATA).orElse(new HashMap<>()).getOrDefault(getId(), 0.0f);
    }

    public boolean onJobListener(Object item, Player player, JobAction action){
        for(JobItem jobItem : jobActions.get(action)){
            if(jobItem.getItem().equals(item)){
                if(jobItem.canDo(getXp((player)))){
                    this.addXp(player, jobItem.getXp());
                    return true;
                }else{
                    player.sendMessage(ChatTypes.ACTION_BAR,
                            TranslationHelper.p(player, "player.warning.job.not_enough_xp")
                    );
                    return false;
                }
            }
        }

        return false;
    }

    private void initJobAction(ConfigurationNode jobActionNode, JobAction action){
        if(jobActionNode.getChildrenMap().isEmpty())
            return;

        jobActions.put(
                action,
                generateJobItemList(jobActionNode.getChildrenMap().values(), action.getCatalogType())
        );

        try {
            Sponge.getEventManager().registerListeners(
                    JobsMain.instance(),
                    action.getListenerConstructor().newInstance(this, generateJobItemTypeList(jobActions.get(action)))
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private List<JobItem> generateJobItemList(Collection<? extends ConfigurationNode> nodes, Class<? extends CatalogType> catalogType){
        List<JobItem> jobItems = new ArrayList<>();

        for(ConfigurationNode jobItemNode : nodes){

            Optional<? extends CatalogType> optionalJobItemItem = Sponge.getRegistry().getType(
                    catalogType,
                    jobItemNode.getKey().toString()
            );

            if(optionalJobItemItem.isPresent()){
                jobItems.add(
                        new JobItem(
                                jobItemNode.getNode("xp").getFloat(0.0f),
                                jobItemNode.getNode("needXp").getFloat(0.0f),
                                this,
                                optionalJobItemItem.get()
                        )
                );
            }
        }

        return jobItems;
    }

    private static <T> List<T> generateJobItemTypeList(List<JobItem> jobItems){
        List<T> itemTypes = new ArrayList<>();

        for(JobItem jobItem : jobItems){
            itemTypes.add((T) jobItem.getItem());
        }

        return itemTypes;
    }

}
