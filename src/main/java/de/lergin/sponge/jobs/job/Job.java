package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.jobs.listener.BreakBlockListener;
import de.lergin.sponge.jobs.listener.PlaceBlockListener;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Job {
    private String name;
    private String id;
    private Map<JobAction, List<JobItem>> jobActions = new HashMap<>();

    public Job(ConfigurationNode jobConfig) {
        this.name = jobConfig.getNode("name").getString();
        this.id = jobConfig.getKey().toString();


        initBlockAction(jobConfig.getNode("destroyBlocks"), JobAction.BREAK);
        initBlockAction(jobConfig.getNode("placeBlocks"), JobAction.PLACE);
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void addXp(Player player, float amount){
        Map<String, Float> jobData = player.get(JobKeys.JOB_DATA).orElse(new HashMap<>());

        jobData.put(getId(), jobData.getOrDefault(getId(), 0.0f) + amount);

        player.offer(new JobDataManipulatorBuilder().jobs(jobData).create());

        //TODO: translate, format and setting
        player.sendMessage(ChatTypes.ACTION_BAR, Text.of(getName() + ": " + getXp(player)));
    }

    public float getXp(Player player){
        return player.get(JobKeys.JOB_DATA).orElse(new HashMap<>()).getOrDefault(getId(), 0.0f);
    }

    public boolean onBlockEvent(BlockType blockType, Player player, JobAction action){
        for(JobItem jobItem : jobActions.get(action)){
            if(jobItem.getItem().equals(blockType)){
                if(jobItem.canDo(getXp((player)))){
                    this.addXp(player, jobItem.getXp());
                    return true;
                }else{
                    player.sendMessage(TranslationHelper.p(player, "player.warning.job.not_enough_xp"));
                    return false;
                }
            }
        }

        return false;
    }

    private void initBlockAction(ConfigurationNode blockActionNode, JobAction action){
        if(blockActionNode.getChildrenMap().isEmpty())
            return;

        jobActions.put(
                action,
                generateJobItemList(blockActionNode.getChildrenMap().values(), CatalogTypes.BLOCK_TYPE)
        );

        try {
            Sponge.getEventManager().registerListeners(
                    JobsMain.instance(),
                    action.getListenerConstructor().newInstance(this, generateBlockTypeList(jobActions.get(action)))
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private List<JobItem> generateJobItemList(Collection<? extends ConfigurationNode> nodes, Class<? extends CatalogType> catalogType){
        List<JobItem> jobItems = new ArrayList<>();

        for(ConfigurationNode jobItemNode : nodes){
            jobItems.add(
                    new JobItem(
                            jobItemNode.getNode("xp").getFloat(0.0f),
                            jobItemNode.getNode("needXp").getFloat(0.0f),
                            this,
                            Sponge.getRegistry().getType(
                                    catalogType,
                                    jobItemNode.getKey().toString()
                            ).get()
                    )
            );
        }

        return jobItems;
    }

    private static List<BlockType> generateBlockTypeList(List<JobItem> jobItems){
        List<BlockType> blockTypes = new ArrayList<>();

        for(JobItem jobItem : jobItems){
            blockTypes.add((BlockType) jobItem.getItem());
        }

        return blockTypes;
    }

}
