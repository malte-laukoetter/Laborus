package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.data.jobs.JobData;
import de.lergin.sponge.jobs.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.jobs.job.item.JobItem;
import de.lergin.sponge.jobs.listener.BlockJobListener;
import de.lergin.sponge.jobs.listener.BreakBlockListener;
import de.lergin.sponge.jobs.listener.PlaceBlockListener;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;

import java.util.*;

public class Job {
    private String name;
    private String id;
    private List<JobItem> breakBlockItems = new ArrayList<>();
    private List<JobItem> placeBlockItems = new ArrayList<>();

    public Job(ConfigurationNode jobConfig) {
        this.name = jobConfig.getNode("name").getString();
        this.id = jobConfig.getKey().toString();

        initBreakBlocks(jobConfig.getNode("destroyBlocks"));
        initPlaceBlocks(jobConfig.getNode("destroyBlocks"));
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
    }

    public boolean onBlockEvent(BlockType blockType, Player player){
        for(JobItem jobItem : breakBlockItems){
            if(jobItem.getItem().equals(blockType)){
                if(jobItem.canDo(player)){
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

    private void initBreakBlocks(ConfigurationNode destroyBlockNode){
        if(destroyBlockNode.getChildrenMap().isEmpty())
            return;

        breakBlockItems = generateJobItemList(destroyBlockNode.getChildrenMap().values(), CatalogTypes.BLOCK_TYPE);

        Sponge.getEventManager().registerListeners(
                JobsMain.instance(),
                new BreakBlockListener(this, generateBlockTypeList(breakBlockItems))
        );
    }

    private void initPlaceBlocks(ConfigurationNode placeBlockNode){
        if(placeBlockNode.getChildrenMap().isEmpty())
            return;

        placeBlockItems = generateJobItemList(placeBlockNode.getChildrenMap().values(), CatalogTypes.BLOCK_TYPE);

        Sponge.getEventManager().registerListeners(
                JobsMain.instance(),
                new PlaceBlockListener(this, generateBlockTypeList(placeBlockItems))
        );
    }

    private List<JobItem> generateJobItemList(Collection<? extends ConfigurationNode> nodes, Class catalogType){
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
