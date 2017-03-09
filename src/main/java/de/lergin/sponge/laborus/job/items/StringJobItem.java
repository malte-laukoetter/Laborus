package de.lergin.sponge.laborus.job.items;

import de.lergin.sponge.laborus.job.JobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class StringJobItem extends JobItem<String> {
    @Setting(value = "item")
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
