package de.lergin.laborus.job.bonus;

import de.lergin.laborus.api.JobBonus;
import de.lergin.laborus.api.JobItem;
import de.lergin.laborus.util.BonusHelper;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

@ConfigSerializable
public class ItemDropJobBonus extends JobBonus {
    @Setting(value = "item", comment = "an itemstack that will be droped")
    private ItemStack itemStack = ItemStack.of(ItemTypes.DIRT, 1);

    public ItemDropJobBonus() {}

    @Override
    public void applyBonus(JobItem item, Player player, Object i2) {
        BonusHelper.dropItem(player.getLocation(), itemStack, Cause.builder().owner(player).build());
    }
}
