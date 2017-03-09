package de.lergin.sponge.laborus.job.items;

import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

@ConfigSerializable
public class ItemJobItem extends JobItem<ItemType> {
    @Setting(value = "item")
    private ItemType item = ItemTypes.DIRT;

    /**
     * returns the item
     *
     * @return the item
     */
    @Override
    public ItemType getItem() {
        return item;
    }

    public ItemJobItem() {}
}

