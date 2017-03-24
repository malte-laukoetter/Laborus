package de.lergin.sponge.laborus.job.bonus;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.api.JobAction;
import de.lergin.sponge.laborus.api.JobBonus;
import de.lergin.sponge.laborus.api.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

/**
 * Bonus that repairs the item in the hand by a percentage between maxPercent and minPercent
 */
@ConfigSerializable
public class ItemRepair extends JobBonus {
    @Setting(value = "maxPercent", comment = "maximal percent")
    private double maxPercent = 0;
    @Setting(value = "minPercent", comment = "minimal percent")
    private double minPercent = 0;

    @Override
    public void applyBonus(JobItem item, Player player, Object i2) {
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

    /**
     * test if the item has a durability
     */
    @Override
    public boolean canHappen(Job job, JobAction jobAction, JobItem jobItem, Player player) {
        Optional<ItemStack> optional = player.getItemInHand(HandTypes.MAIN_HAND);

        return optional.isPresent() &&
                optional.get().supports(Keys.ITEM_DURABILITY) &&
                super.canHappen(job, jobAction, jobItem, player);

    }

    public ItemRepair() {}

}
