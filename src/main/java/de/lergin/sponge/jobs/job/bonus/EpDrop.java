package de.lergin.sponge.jobs.job.bonus;

import de.lergin.sponge.jobs.job.JobBonus;
import de.lergin.sponge.jobs.job.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.extent.Extent;

import java.util.Optional;
import java.util.Random;

/**
 * Bonus that drops some extra ep
 */
public class EpDrop extends JobBonus {
    int minEp;
    int maxEp;

    @Override
    public void useBonus(JobItem item, Player player) {
        if(this.isHappening()){
            Extent extent = player.getLocation().getExtent();
            Optional<Entity> itemEntity = extent.createEntity(EntityTypes.EXPERIENCE_ORB, player.getLocation().getPosition());

            if(itemEntity.isPresent()){
                Entity entity = itemEntity.get();
                entity.offer(Keys.CONTAINED_EXPERIENCE, new Random().nextInt(maxEp - minEp) + minEp);

                extent.spawnEntity(entity, Cause.of(player));

                if(isSendMessage()){
                    player.sendMessage(getMessage());
                }
            }
        }
    }

    @Override
    public boolean canHappen(JobItem jobItem, Player player) {
        return true;
    }

    public EpDrop(ConfigurationNode config) {
        super(
                config.getNode("probability").getDouble(0.05),
                config.getNode("sendMessage").getBoolean(false),
                Text.of(config.getNode("message").getString(""))
        );

        this.minEp = config.getNode("minEp").getInt(1);
        this.maxEp = config.getNode("maxEp").getInt(1);
    }

}
