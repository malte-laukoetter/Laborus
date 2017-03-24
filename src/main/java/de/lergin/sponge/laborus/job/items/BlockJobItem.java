package de.lergin.sponge.laborus.job.items;

import de.lergin.sponge.laborus.api.JobItem;
import de.lergin.sponge.laborus.util.BlockStateComparator;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.text.Text;

import java.util.Locale;

@ConfigSerializable
public class BlockJobItem extends JobItem {
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

    public BlockJobItem() {}

    @Override
    public boolean matches(JobItem item) {
        return item instanceof BlockJobItem && BlockStateComparator.compare(getItem(), (String) item.getItem());
    }

    @Override
    public Text getName(Locale locale) {
        return Text.of(getItem());
    }

    public BlockJobItem(String item) {
        this.item = item;
    }

    public static BlockJobItem fromBlockState(BlockState state){
        return new BlockJobItem(state.toString());
    }
}
