package de.lergin.sponge.laborus.job.items;

import de.lergin.sponge.laborus.api.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;

@ConfigSerializable
public class EntityJobItem extends JobItem<EntityType> {
    @Setting(value = "item", comment = "an entitytype, see http://minecraft.gamepedia.com/Data_values#Entity_IDs")
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
