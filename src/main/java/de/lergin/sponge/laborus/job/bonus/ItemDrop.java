package de.lergin.sponge.laborus.job.bonus;

import com.google.common.collect.Lists;
import de.lergin.sponge.laborus.job.JobAction;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

@ConfigSerializable
public class ItemDrop extends JobBonus {
    @Setting(value = "item", comment = "an itemstack that will be droped")
    private ItemStack itemStack = ItemStack.of(ItemTypes.DIRT, 1);

    public ItemDrop() {
        super(Lists.newArrayList(JobAction.BREAK, JobAction.ENTITY_KILL));
    }

    @Override
    public void useBonus(JobItem item, Player player, Object i2) {
        if (!this.isHappening())
            return;

        if (BonusHelper.dropItem(player.getLocation(), itemStack, Cause.builder().owner(player).build()) && isSendMessage()) {
            player.sendMessage(getMessage());
        }
    }
}
