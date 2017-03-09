package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Sets;
import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.config.Config;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import de.lergin.sponge.laborus.job.items.StringJobItem;
import de.lergin.sponge.laborus.util.BlockStateComparator;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import sun.plugin.dom.core.Comment;

import java.util.Optional;

/**
 * bonus drop of the block
 */
@ConfigSerializable
public class MultiDrop extends JobBonus {
    @Setting(value = "extraDrops")
    private int extraDrops = 0;

    @Override
    public void useBonus(JobItem item, Player player, Object item2) {
        if (this.isHappening() && item2 instanceof BlockState) {
            ItemStack itemStack = ItemStack.builder().fromBlockState((BlockState) item2).quantity(extraDrops).build();

            if (BonusHelper.dropItem(player.getLocation(), itemStack, Cause.builder().owner(player).build()) && isSendMessage()) {
                player.sendMessage(getMessage());
            }
        }
    }

    @Override
    public boolean canHappen(Job job, JobAction jobAction, JobItem jobItem, Player player) {
        return jobItem.getItem() instanceof String && testConditions(job, jobAction, jobItem, player);
    }

    public MultiDrop() {
        super(Sets.newHashSet(JobAction.BREAK, JobAction.ENTITY_KILL));
    }
}
