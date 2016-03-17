package de.lergin.sponge.laborus.job.bonus;

import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

/**
 * bonus drop of the block
 */
public class MultiDrop extends JobBonus {
    private final int dropMultiplier;

    @Override
    public void useBonus(JobItem item, Player player) {
        if(this.isHappening()){
            Optional<ItemType> optionalItemType = ((BlockType) item.getItem()).getItem();

            if(optionalItemType.isPresent()){
                ItemStack itemStack = ItemStack.of(optionalItemType.get(), dropMultiplier);

                if(BonusHelper.dropItem(player.getLocation(), itemStack, Cause.builder().owner(player).build()) && isSendMessage()){
                    player.sendMessage(getMessage());
                }
            }
        }
    }

    @Override
    public boolean canHappen(Job job, JobAction jobAction, JobItem jobItem, Player player) {
        return jobItem.getItem() instanceof BlockType && testConditions(job, jobAction, jobItem, player);
    }

    public MultiDrop(ConfigurationNode config) {
        super(config);

        //we only need to do the extra drop
        this.dropMultiplier = config.getNode("itemMultiplier").getInt(2) - 1;
    }
}
