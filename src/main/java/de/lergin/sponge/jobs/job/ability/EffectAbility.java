package de.lergin.sponge.jobs.job.ability;

import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.JobAbility;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class EffectAbility extends JobAbility {
    public EffectAbility(Job job, ConfigurationNode node) {
        super(
                job,
                node.getNode("name").getString("EffectAbility"),
                node.getNode("coolDown").getInt(60)
        );
    }

    @Override
    public boolean startAbility(Player player) {
        player.sendMessage(Text.of("START"));

        return false;
    }

    @Override
    public boolean canStartAbility(Player player) {
        return false;
    }
}
