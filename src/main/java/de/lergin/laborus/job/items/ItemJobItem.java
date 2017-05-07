package de.lergin.laborus.job.items;

import de.lergin.laborus.api.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.text.Text;

import java.util.Locale;

@ConfigSerializable
public class ItemJobItem extends JobItem {
    @Setting(value = "item", comment = "an itemtype, see http://minecraft.gamepedia.com/Data_values#Item_IDs")
    private ItemType item = null;

    @Override
    public ItemType getItem() {
        return item;
    }

    @Override
    public boolean matches(JobItem o) {
        return super.matchesAll() || o.getItem() instanceof ItemType && matches((ItemType) o.getItem());
    }

    private boolean matches(ItemType item) {
        return this.getItem() == item;
    }

    @Override
    public boolean matches(String item) {
        return super.matchesAll() || this.getItem().getId().equals(item);
    }

    public static ItemJobItem fromItemStack(ItemStack item){
        return new ItemJobItem(item.getItem());
    }

    public static ItemJobItem fromItemStackSnapshot(ItemStackSnapshot item){
        return new ItemJobItem(item.getType());
    }

    public ItemJobItem() {}

    public ItemJobItem(ItemType item) {
        this.item = item;
    }

    @Override
    public Text getName(Locale locale) {
        return Text.of(this.getItem().getTranslation().get(locale));
    }
}

