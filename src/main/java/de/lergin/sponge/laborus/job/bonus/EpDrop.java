package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Lists;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.api.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.world.extent.Extent;

import java.util.Random;

/**
 * Bonus that drops some extra ep
 */
@ConfigSerializable
public class EpDrop extends JobBonus {
    @Setting(value = "minEp", comment = "minimal amount of minecraft ep")
    private int minEp = 0;
    @Setting(value = "maxEp", comment = "maximal amount of minecraft ep")
    private int maxEp = 0;

    @Override
    public void useBonus(JobItem item, Player player, Object i2) {
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

    public EpDrop() {
        super(Lists.newArrayList(JobAction.BREAK, JobAction.ENTITY_KILL));
    }

}
