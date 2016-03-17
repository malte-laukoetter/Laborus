package de.lergin.sponge.laborus.job.bonus;

import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class ItemDrop extends JobBonus {
    ItemStack itemStack;

    public ItemDrop(ConfigurationNode config) {
        super(config);

        Optional<ItemType> optional = Sponge.getRegistry().getType(
                CatalogTypes.ITEM_TYPE,
                config.getNode("item").getString("")
        );

        if(optional.isPresent()){
            itemStack = ItemStack.of(
                    optional.get(),
                    config.getNode("amount").getInt(1)
            );
        }else{
            //TODO: error message
        }
    }

    @Override
    public void useBonus(JobItem item, Player player) {
        if (!this.isHappening())
            return;

        if(BonusHelper.dropItem(player.getLocation(), itemStack, Cause.builder().owner(player).build()) && isSendMessage()){
            player.sendMessage(getMessage());
        }
    }
}
