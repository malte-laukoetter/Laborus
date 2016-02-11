package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.listener.BreakBlockListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malte on 10.02.2016.
 */
public class BreakBlockJob extends Job {
    public BreakBlockJob() {
        super("id", "jsaosadjop");

        List<BlockType> blockTypes = new ArrayList<>();

        blockTypes.add(BlockTypes.DIRT);
        blockTypes.add(BlockTypes.GRASS);
        blockTypes.add(BlockTypes.STONE);

        Sponge.getEventManager().registerListeners(JobsMain.instance(), new BreakBlockListener(this, blockTypes));
    }
}
