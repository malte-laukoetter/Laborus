package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.listener.BreakBlockListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;

import java.util.List;

public class BreakBlockJob extends Job {
    public BreakBlockJob(String id, String name, List<BlockType> blockTypes) {
        super(id, name);

        Sponge.getEventManager().registerListeners(JobsMain.instance(), new BreakBlockListener(this, blockTypes));
    }
}
