package de.lergin.sponge.laborus.job.bonus;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import java.util.Optional;

public class BonusHelper {
    public static boolean dropItem(Location<World> loc, ItemStack itemStack, Cause cause){
        Extent extent = loc.getExtent();

        Optional<Entity> itemEntity = extent.createEntity(EntityTypes.ITEM, loc.getPosition());

        if(itemEntity.isPresent()){
            Entity entity = itemEntity.get();

            entity.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
            extent.spawnEntity(entity, cause);

            return true;
        }

        return false;
    }

}
