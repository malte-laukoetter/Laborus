package de.lergin.laborus.util;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

public class BonusHelper {
    public static boolean dropItem(Location<World> loc, ItemStack itemStack, Cause cause) {
        Extent extent = loc.getExtent();

        Entity itemEntity = extent.createEntity(EntityTypes.ITEM, loc.getPosition());

        itemEntity.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
        extent.spawnEntity(itemEntity, cause);

        return true;
    }
}
