package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

public class JobItem {
    private float xp = 0.0f;
    private float needXp = 0.0f;
    private Class itemType;
    private Object item;
    private Job job;

    public float getNeedXp() {
        return needXp;
    }

    public JobItem(float xp, float needXp, Job job, Object item) {
        this.xp = xp;
        this.needXp = needXp;
        this.itemType = item.getClass();
        this.item = item;
        this.job = job;
    }

    public float getXp(){
        return xp;
    }

    public boolean canDo(float xp){
        return (needXp - xp) <= 0;
    }

    public boolean canDo(Player player){
        return canDo(player.get(JobKeys.JOB_DATA).orElse(new HashMap<>()).getOrDefault(job.getId(), 0.0f));
    }

    public Object getItem(){
        return item;
    }

    public Job getJob(){
        return job;
    }
}
