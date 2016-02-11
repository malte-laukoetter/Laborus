package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.listener.BreakBlockListener;
import de.lergin.sponge.jobs.listener.PlaceBlockListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;

import java.util.ArrayList;
import java.util.List;

public class PlaceBlockJob extends Job {
    public PlaceBlockJob() {
        super("placeBlock", "Place Block saosadjop");

        List<BlockType> blockTypes = new ArrayList<>();

        blockTypes.add(BlockTypes.DIRT);
        blockTypes.add(BlockTypes.GRASS);
        blockTypes.add(BlockTypes.STONE);

        Sponge.getEventManager().registerListeners(JobsMain.instance(), new PlaceBlockListener(this, blockTypes));
    }
}
