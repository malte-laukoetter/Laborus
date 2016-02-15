package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

public class JobItem {
    private float xp = 0.0f;
    private float needXp = 0.0f;
    private Object item;
    private Job job;

    public float getNeedXp() {
        return needXp;
    }

    public JobItem(float xp, float needXp, Job job, Object item) {
        this.xp = xp;
        this.needXp = needXp;
        this.item = item;
        this.job = job;
    }

    public float getXp(){
        return xp;
    }

    public boolean canDo(float xp){
        return (needXp - xp) <= 0;
    }

    public Object getItem(){
        return item;
    }

    public Job getJob(){
        return job;
    }
}
