package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Sets;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

/**
 * Bonus that repairs the item in the hand by a percentage between maxPercent and minPercent
 */
public class ItemRepair extends JobBonus {
    private final double maxPercent;
    private final double minPercent;

    @Override
    public void useBonus(JobItem item, Player player) {
        if (this.isHappening()) {
            Optional<ItemStack> optional = player.getItemInHand(HandTypes.MAIN_HAND);

            if (!optional.isPresent())
                return;

            ItemStack itemStack = optional.get();

            int currentDurability = itemStack.getValue(Keys.ITEM_DURABILITY).get().get();

            // test if already fully repaired
            if(currentDurability != 0){
                int addDurability = Math.toIntExact(
                        Math.round(currentDurability * minPercent + Math.random() * (maxPercent - minPercent))
                );

                itemStack.offer(Keys.ITEM_DURABILITY, currentDurability + addDurability);

                player.setItemInHand(HandTypes.MAIN_HAND, itemStack);
            }
        }
    }

    /**
     * test if the item has a durability
     */
    @Override
    public boolean canHappen(Job job, JobAction jobAction, JobItem jobItem, Player player) {
        Optional<ItemStack> optional = player.getItemInHand(HandTypes.MAIN_HAND);

        return optional.isPresent() &&
                optional.get().supports(Keys.ITEM_DURABILITY) &&
                testConditions(job, jobAction, jobItem, player);

    }

    public ItemRepair(ConfigurationNode config) {
        super(config, Sets.newHashSet(JobAction.BREAK, JobAction.ENTITY_KILL));

        this.maxPercent = config.getNode("maxPercent").getDouble(0.1);
        this.minPercent = config.getNode("minPercent").getDouble(0.1);
    }

}
