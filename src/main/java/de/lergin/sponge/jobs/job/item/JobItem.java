package de.lergin.sponge.jobs.job.item;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;

public class JobItem {
    private int xp = 0;
    private int needXp = 0;
    private Class itemType;
    private Object item;
    private Job job;

    public JobItem(int xp, int needXp, Job job, Object item) {
        this.xp = xp;
        this.needXp = needXp;
        this.itemType = item.getClass();
        this.item = item;
        this.job = job;
    }

    public Integer getXp(){
        return xp;
    }

    public boolean canDo(int xp){
        return (needXp - xp) > 0;
    }

    public boolean canDo(Player player){
        return canDo(player.get(JobKeys.JOB_DATA).orElse(new HashMap<>()).getOrDefault(job.getId(), 0));
    }
}
