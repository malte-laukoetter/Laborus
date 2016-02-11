package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.data.JobKeys;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;

public class Job {
    private String name;
    private String id;
    private int xp;

    public Job(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Integer getXp(){
        return xp;
    }

    public Map<String, Integer> toMap(){
        Map<String, Integer> map = new HashMap<>();

        map.put(getId(), getXp());

        return map;
    }

    public void addXp(Player player, int amount){
        Map<String, Integer> jobData = player.get(JobKeys.JOB_DATA).orElse(new HashMap<>());

        jobData.put(getId(), jobData.getOrDefault(getId(), 0) + amount);

        player.offer(JobKeys.JOB_DATA, jobData);
    }
}
