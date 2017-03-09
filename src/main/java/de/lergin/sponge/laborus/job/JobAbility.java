package de.lergin.sponge.laborus.job;

import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.config.TranslationKeys;
import de.lergin.sponge.laborus.data.JobKeys;
import de.lergin.sponge.laborus.data.jobs.JobDataManipulatorBuilder;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class JobAbility {
    @Setting(value = "cooldown")
    private int coolDown = 0;
    @Setting(value = "name")
    private String name = "";

    public JobAbility() {}

    public abstract boolean startAbility(Job job, Player player);

    public void sendStartMessage(Job job, Player player) {
        player.sendMessage(
                Laborus.instance().translationHelper.get(
                        TranslationKeys.JOB_ABILITY_START,
                        player
                ),
                this.textArgs(job, player)
        );
    }

    public void sendCoolDownNotEndedMessage(Job job, Player player) {
        player.sendMessage(
                Laborus.instance().translationHelper.get(
                        TranslationKeys.JOB_ABILITY_CANNOT_START_COOLDOWN,
                        player
                ),
                this.textArgs(job, player)
        );
    }

    public boolean canStartAbility(Job job, Player player) {
        Map<String, Long> abilityUsed = player.get(JobKeys.JOB_ABILITY_USED).orElseGet(HashMap::new);

        return (abilityUsed.getOrDefault(job.getId(), 0L) + coolDown) < Instant.now().getEpochSecond();
    }

    public void startCoolDown(Job job, Player player) {
        Map<String, Long> abilityUsed = player.get(JobKeys.JOB_ABILITY_USED).orElseGet(HashMap::new);
        Map<String, Long> tempMap = new HashMap<>();
        tempMap.putAll(abilityUsed);
        tempMap.put(job.getId(), Instant.now().getEpochSecond());

        if(!player.offer(JobKeys.JOB_ABILITY_USED, tempMap).isSuccessful()){
            player.offer(new JobDataManipulatorBuilder().abilityUsed(tempMap).create());
        }
    }

    public int getCoolDown() {
        return coolDown;
    }

    public long getSecondsTillEndOfCoolDown(Job job, Player player) {
        Map<String, Long> abilityUsed = player.get(JobKeys.JOB_ABILITY_USED).orElseGet(HashMap::new);

        return abilityUsed.getOrDefault(job.getId(), (long) coolDown) + coolDown - Instant.now().getEpochSecond();
    }

    public String getName() {
        return name;
    }

    public Map<String, TextElement> textArgs(Job job, Player player){
        Map<String, TextElement> args = job.textArgs(player);

        args.put("ability.name", Text.of(this.getName()));
        args.put("ability.remaining_cooldown", Text.of(this.getSecondsTillEndOfCoolDown(job, player)));

        return args;
    }
}
