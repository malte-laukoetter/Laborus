package de.lergin.laborus.implementation.items;

import com.google.common.reflect.TypeToken;
import de.lergin.laborus.api.JobItem;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Locale;

public class ItemJobItem extends JobItem {
    private ItemType item = ItemTypes.DIRT;

    private Boolean matchesAll = false;

    @Override
    public ItemType getItem() {
        return item;
    }

    @Override
    public boolean matches(JobItem o) {
        return o.getItem() instanceof ItemType && matches((ItemType) o.getItem());
    }

    private boolean matches(ItemType item) {
        return this.getItem() == item;
    }

    @Override
    public boolean matches(String item) {
        return this.getItem().getId().equals(item);
    }

    public static ItemJobItem fromItemStack(ItemStack item){
        return new ItemJobItem(item.getItem());
    }

    public ItemJobItem(Boolean matchesAll) {
        this.matchesAll = matchesAll;
    }

    public ItemJobItem(ItemType item) {
        this.item = item;
    }

    @Override
    public Text getName(Locale locale) {
        return Text.of(this.getItem().getTranslation().get(locale));
    }

    public static class Serializer implements TypeSerializer<ItemJobItem> {
        @Override
        public ItemJobItem deserialize(TypeToken<?> typeToken, ConfigurationNode configurationNode) throws ObjectMappingException {
            Object o = configurationNode.getNode("item").getString();

            if(o.equals("*")){
                return new ItemJobItem(true);
            }else{
                return new ItemJobItem(configurationNode.getValue(TypeToken.of(ItemType.class)));
            }
        }

        @Override
        public void serialize(TypeToken<?> typeToken, ItemJobItem itemJobItem, ConfigurationNode configurationNode) throws ObjectMappingException {
            if(itemJobItem.matchesAll){
                configurationNode.getNode("item").setValue("*");
            }else{
                configurationNode.getNode("item").setValue(TypeToken.of(ItemType.class), itemJobItem.getItem());
            }

            if(configurationNode instanceof CommentedConfigurationNode){
                ((CommentedConfigurationNode) configurationNode).setComment("an itemtype, see http://minecraft.gamepedia.com/Data_values#Item_IDs, * for any");
            }
        }
    }
}

