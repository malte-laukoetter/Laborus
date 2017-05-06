package de.lergin.laborus.implementation.items;

import com.google.common.reflect.TypeToken;
import de.lergin.laborus.api.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.text.Text;

import java.util.Locale;

public class EntityJobItem extends JobItem {
    @Setting(value = "item", comment = "an entitytype, see http://minecraft.gamepedia.com/Data_values#Entity_IDs")
    private EntityType item = EntityTypes.BAT;

    private boolean matchesAll = false;

    @Override
    public EntityType getItem() {
        return item;
    }

    @Override
    public boolean matches(JobItem o) {
        return matchesAll || o.getItem() instanceof EntityType && matches((EntityType) o.getItem());
    }

    private boolean matches(EntityType item) {
        return this.getItem() == item;
    }

    @Override
    public boolean matches(String item) {
        return matchesAll || getItem().getId().equals(item);
    }

    public static EntityJobItem fromEntity(Entity entity){
        return new EntityJobItem(entity.getType());
    }

    @Override
    public Text getName(Locale locale) {
        return Text.of(getItem().getTranslation().get(locale));
    }

    public EntityJobItem(Boolean matchesAll) {
        this.matchesAll = matchesAll;
    }

    public EntityJobItem(EntityType item) {
        this.item = item;
    }

    public static class Serializer implements TypeSerializer<EntityJobItem> {
        @Override
        public EntityJobItem deserialize(TypeToken<?> typeToken, ConfigurationNode configurationNode) throws ObjectMappingException {
            Object o = configurationNode.getNode("item").getString();

            if(o.equals("*")){
                return new EntityJobItem(true);
            }else{
                return new EntityJobItem(configurationNode.getValue(TypeToken.of(EntityType.class)));
            }
        }

        @Override
        public void serialize(TypeToken<?> typeToken, EntityJobItem entityJobItem, ConfigurationNode configurationNode) throws ObjectMappingException {
            if(entityJobItem.matchesAll){
                configurationNode.getNode("item").setValue("*");
            }else{
                configurationNode.getNode("item").setValue(TypeToken.of(EntityType.class), entityJobItem.getItem());
            }

            if(configurationNode instanceof CommentedConfigurationNode){
                ((CommentedConfigurationNode) configurationNode).setComment("an entitytype, see http://minecraft.gamepedia.com/Data_values#Entity_IDs, * for any");
            }
        }
    }
}
