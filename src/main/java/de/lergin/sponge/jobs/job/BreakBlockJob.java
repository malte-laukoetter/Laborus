package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.job.item.JobItem;
import de.lergin.sponge.jobs.listener.BreakBlockListener;
import de.lergin.sponge.jobs.util.PlayerJobHelper;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BreakBlockJob extends Job {
    private List<JobItem> jobItems = new ArrayList<>();

    public BreakBlockJob(String id, String name, Collection<? extends ConfigurationNode> blockTypeNodes) {
        super(id, name);



        for(ConfigurationNode blockNode : blockTypeNodes){
            jobItems.add(
                    new JobItem(
                            blockNode.getNode("xp").getInt(0),
                            blockNode.getNode("needXp").getInt(0),
                            this,
                            Sponge.getRegistry().getType(
                                    CatalogTypes.BLOCK_TYPE,
                                    blockNode.getKey().toString()
                            ).get()
                    )
            );
        }


        List<BlockType> blockTypes = new ArrayList<>();

        for(JobItem jobItem : jobItems){
            blockTypes.add((BlockType) jobItem.getItem());
        }

        Sponge.getEventManager().registerListeners(JobsMain.instance(), new BreakBlockListener(this, blockTypes));
    }


    public boolean blockBreak(BlockType blockType, Player player){
        for(JobItem jobItem : jobItems){
            if(jobItem.getItem().equals(blockType)){
                if(PlayerJobHelper.hasEnoughtXp(player, jobItem)){
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
}
