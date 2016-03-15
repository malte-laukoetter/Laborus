package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.data.JobKeys;
import org.spongepowered.api.entity.living.player.Player;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class JobAbility {
    private final Job job;
    private final int coolDown;
    private final String name;

    public JobAbility(Job job, String name, int coolDown) {
        this.name = name;
        this.job = job;
        this.coolDown = coolDown;
    }

    public abstract boolean startAbility(Player player);

    public boolean canStartAbility(Player player){
        Map<String, Long> abilityUsed = player.get(JobKeys.JOB_ABILITY_USED).orElse(new HashMap<>());

        return (abilityUsed.getOrDefault(getJob().getId(), 0L) + coolDown) < Instant.now().getEpochSecond();
    }

    public void startCoolDown(Player player){
        Map<String, Long> abilityUsed = player.get(JobKeys.JOB_ABILITY_USED).orElse(new HashMap<>());
        Map<String,  Long> tempMap = new HashMap<>();
        tempMap.putAll(abilityUsed);
        tempMap.put(this.getJob().getId(), Instant.now().getEpochSecond());

        player.offer(JobKeys.JOB_ABILITY_USED, tempMap);
    }

    public Job getJob() {
        return job;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public String getName() {
        return name;
    }
}
