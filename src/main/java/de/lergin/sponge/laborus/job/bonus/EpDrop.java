package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Sets;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.extent.Extent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;

/**
 * Bonus that drops some extra ep
 */
public class EpDrop extends JobBonus {
    private final int minEp;
    private final int maxEp;

    @Override
    public void useBonus(JobItem item, Player player) {
        if (this.isHappening()) {
            Extent extent = player.getLocation().getExtent();
            Entity itemEntity = extent.createEntity(EntityTypes.EXPERIENCE_ORB, player.getLocation().getPosition());

            itemEntity.offer(Keys.CONTAINED_EXPERIENCE, new Random().nextInt(maxEp - minEp) + minEp);

            extent.spawnEntity(itemEntity, Cause.builder().owner(player).build());

            if (isSendMessage()) {
                player.sendMessage(getMessage());
            }
        }
    }

    public EpDrop(ConfigurationNode config) {
        super(config, Sets.newHashSet(JobAction.BREAK, JobAction.ENTITY_KILL));

        this.minEp = config.getNode("minEp").getInt(1);
        this.maxEp = config.getNode("maxEp").getInt(1);
    }

}
