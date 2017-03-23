package de.lergin.sponge.laborus.job.items;

import de.lergin.sponge.laborus.api.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class StringJobItem extends JobItem<String> {
    @Setting(value = "item", comment = "an blockstate or blocktype as a string, see http://minecraft.gamepedia.com/Data_values#Block_IDs it also supports blockdata")
    private String item = "";

    /**
     * returns the item
     *
     * @return the item
     */
    @Override
    public String getItem() {
        return item;
    }

    public StringJobItem() {}
}
