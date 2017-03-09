package de.lergin.sponge.laborus.job.items;

import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;

@ConfigSerializable
public class EntityJobItem extends JobItem<EntityType> {
    @Setting(value = "item")
    private EntityType item = EntityTypes.BAT;

    /**
     * returns the item
     *
     * @return the item
     */
    @Override
    public EntityType getItem() {
        return item;
    }

    public EntityJobItem() {}
}
