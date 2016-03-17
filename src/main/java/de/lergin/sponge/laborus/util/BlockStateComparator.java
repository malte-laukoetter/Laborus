package de.lergin.sponge.laborus.util;

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

        return compareData(blockString, blockState.toString());
    }

    public static boolean compare(String blockString1, String blockString2) {
        Optional<BlockType> optional1 = Sponge.getRegistry().getType(CatalogTypes.BLOCK_TYPE, blockString1.split("\\[")[0]);
        Optional<BlockType> optional2 = Sponge.getRegistry().getType(CatalogTypes.BLOCK_TYPE, blockString2.split("\\[")[0]);

        return !(!optional1.isPresent() || !optional2.isPresent() || optional1.get() != optional2.get()) &&
                (!blockString1.contains("[") && !blockString2.contains("[") ||
                compareData(blockString1, blockString2));

    }

    private static boolean compareData(String dataString1, String dataString2){
        String[] dataStrings = dataString1.split("\\[")[1].split("\\]")[0].split(",");
        String[] dataStrings2 = dataString2.split("\\[")[1].split("\\]")[0].split(",");

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
