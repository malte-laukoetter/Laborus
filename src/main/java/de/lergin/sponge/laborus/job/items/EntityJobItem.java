package de.lergin.sponge.laborus.job.items;

import de.lergin.sponge.laborus.api.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.text.Text;

import java.util.Locale;

@ConfigSerializable
public class EntityJobItem extends JobItem {
    @Setting(value = "item", comment = "an entitytype, see http://minecraft.gamepedia.com/Data_values#Entity_IDs")
    private EntityType item = EntityTypes.BAT;

    @Override
    public EntityType getItem() {
        return item;
    }

    @Override
    public boolean matches(JobItem o) {
        return o.getItem() instanceof EntityType && matches((EntityType) o.getItem());
    }

    private boolean matches(EntityType item) {
        return this.getItem() == item;
    }

    @Override
    public Text getName(Locale locale) {
        return Text.of(getItem().getTranslation().get(locale));
    }

    public EntityJobItem() {}
}
