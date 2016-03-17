package de.lergin.sponge.laborus.job.bonus;

import de.lergin.sponge.laborus.JobsMain;
import de.lergin.sponge.laborus.job.JobBonus;
import de.lergin.sponge.laborus.job.JobItem;
import de.lergin.sponge.laborus.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class ItemDrop extends JobBonus {
    private ItemStack itemStack;

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
            JobsMain.instance().getLogger().warn(TranslationHelper.l("warn.not_a_item", config.getNode("item").getString()));
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
