package de.lergin.sponge.jobs.job;

import com.google.common.collect.ImmutableMap;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.util.TranslationHelper;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColors;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.spongepowered.api.text.TextTemplate.arg;

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

    public void sendStartMessage(Player player){
        player.sendMessage(
                TranslationHelper.template(
                        TextTemplate.of(
                                TextColors.AQUA, "You started the ability ",
                                arg("abilityName").color(TextColors.GREEN).build(),
                                " from ", arg("jobName").color(TextColors.GREEN).build(),
                                ". You can use it again in ",
                                arg("coolDown").color(TextColors.GREEN), "s."
                        ),
                        "start_ability"
                ),
                ImmutableMap.of(
                        "jobName", Text.of(job.getName()),
                        "abilityName", Text.of(this.getName()),
                        "coolDown", Text.of(this.getCoolDown())
                )
        );
    }

    public void sendCoolDownNotEndedMessage(Player player){
        player.sendMessage(
                TranslationHelper.template(
                        TextTemplate.of(
                                TextColors.AQUA, "You used the ability ", arg("abilityName").color(TextColors.GREEN).build(),
                                " from ",
                                arg("jobName").color(TextColors.GREEN).build(),
                                " recently so you need to wait ",
                                arg("time").color(TextColors.GREEN), "s until you can use it again."
                        ),
                        "cannot_start_ability_coolDown"
                ),
                ImmutableMap.of(
                        "jobName", Text.of(job.getName()),
                        "abilityName", Text.of(this.getName()),
                        "time", Text.of(this.getSecondsTillEndOfCoolDown(player))
                )
        );
    }

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

    public long getSecondsTillEndOfCoolDown(Player player) {
        Map<String, Long> abilityUsed = player.get(JobKeys.JOB_ABILITY_USED).orElse(new HashMap<>());

        return abilityUsed.getOrDefault(getJob().getId(), (long) coolDown) + coolDown - Instant.now().getEpochSecond();
    }

    public String getName() {
        return name;
    }
}
