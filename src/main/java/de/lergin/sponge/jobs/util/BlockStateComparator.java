package de.lergin.sponge.jobs.util;

import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;

import java.util.Optional;

public class BlockStateComparator {
    /**
     * Compares a {@link BlockState} and the result of the {@link BlockState} toString() method.
     * If the blockState has more dataValues than the blockString these will be ignored:
     *
     * blockState.toString() | blockString | result
     * minecraft:snow | minecraft:snow | true
     * minecraft:snow | minecraft:dirt | false
     * minecraft:stone | minecraft:stone[variant=granit] | false
     * minecraft:stone[variant=granit] | minecraft:stone | true
     * @param blockState the {@link BlockState} that will be compared
     * @param blockString the String of a {@link BlockState} that will be compared
     * @return true if the blockState has all data that the blockString has
     */
    public static boolean compare(BlockState blockState, String blockString){
        Optional<BlockType> optional = Sponge.getRegistry().getType(CatalogTypes.BLOCK_TYPE, blockString.split("\\[")[0]);

        if(!optional.isPresent() || !blockState.getType().equals(optional.get()))
            return false;

        if(!blockString.contains("["))
            return true;


        String[] dataStrings = blockString.split("\\[")[1].split("\\]")[0].split(",");
        String[] dataStrings2 = blockState.toString().split("\\[")[1].split("\\]")[0].split(",");

        for (String dataString : dataStrings) {
            boolean wasTrue = false;

            for (int j = 0; j < dataStrings2.length && !wasTrue; j++) {
                wasTrue = dataStrings2[j].equals(dataString);
            }

            if (!wasTrue)
                return false;
        }

        return true;
    }
}
