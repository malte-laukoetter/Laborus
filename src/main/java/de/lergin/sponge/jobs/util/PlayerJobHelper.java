package de.lergin.sponge.jobs.util;

import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.item.JobItem;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.HashMap;

public final class PlayerJobHelper {

    static public boolean hasEnoughtXp(Player player, JobItem jobItem){
        player.sendMessage(Text.of(getCurrentXp(player, jobItem.getJob()).toString()));

        return jobItem.getNeedXp() <= getCurrentXp(player, jobItem.getJob());
    }

    static public Integer getCurrentXp(Player player, Job job){
        return player.get(JobKeys.JOB_DATA).orElse(new HashMap<>()).getOrDefault(job.getId(), 0);
    }
}
