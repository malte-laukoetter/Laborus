package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Lists;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.api.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;

/**
 * bonus drop of the block
 */
@ConfigSerializable
public class MultiDrop extends JobBonus {
    @Setting(value = "extraDrops", comment = "amount of times the item gets dropped extra")
    private int extraDrops = 0;

    @Override
    public void applyBonus(JobItem item, Player player, Object item2) {
        if (item2 instanceof BlockState) {
            ItemStack itemStack = ItemStack.builder().fromBlockState((BlockState) item2).quantity(extraDrops).build();

            BonusHelper.dropItem(player.getLocation(), itemStack, Cause.builder().owner(player).build());
        }
    }

    @Override
    public boolean canHappen(Job job, JobAction jobAction, JobItem jobItem, Player player) {
        return jobItem.getItem() instanceof String && super.canHappen(job, jobAction, jobItem, player);
    }

    public MultiDrop() {
        super(Lists.newArrayList(JobAction.BREAK, JobAction.ENTITY_KILL));
    }
}
