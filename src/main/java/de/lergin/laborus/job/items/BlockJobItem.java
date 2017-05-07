package de.lergin.laborus.job.items;

import de.lergin.laborus.api.JobItem;
import de.lergin.laborus.util.BlockStateComparator;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.text.Text;

import java.util.Locale;

@ConfigSerializable
public class BlockJobItem extends JobItem {
    @Setting(value = "item", comment = "an blockstate or blocktype as a string, see http://minecraft.gamepedia.com/Data_values#Block_IDs it also supports blockdata")
    private String item = null;

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
        return super.matchesAll() || item instanceof BlockJobItem && matches((String) item.getItem());
    }

    @Override
    public boolean matches(String item) {
        return super.matchesAll() || BlockStateComparator.compare(getItem(), item);
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
