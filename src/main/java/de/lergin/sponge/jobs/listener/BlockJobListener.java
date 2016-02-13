package de.lergin.sponge.jobs.listener;

import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.block.BlockType;

import java.util.List;

public abstract class BlockJobListener {
    List<BlockType> blockTypes;
    Job job;

    public BlockJobListener(Job job, List<BlockType> blockTypes) {
        this.blockTypes = blockTypes;
        this.job = job;
    }
}
