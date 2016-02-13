package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.data.jobs.JobData;
import de.lergin.sponge.jobs.data.jobs.JobDataManipulatorBuilder;
import org.spongepowered.api.entity.living.player.Player;

import java.util.HashMap;
import java.util.Map;

public class Job {
    private String name;
    private String id;

    public Job(String id, String name) {
        this.name = name;
        this.id = id;
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
}
