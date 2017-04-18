package de.lergin.laborus.job.actions;

import de.lergin.laborus.api.JobAction;
import de.lergin.laborus.job.items.EntityJobItem;
import de.lergin.laborus.job.items.ItemJobItem;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.crafting.CraftingOutput;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@ConfigSerializable
public class CraftingJobAction extends JobAction<ItemJobItem> {
    public CraftingJobAction() {}

    @Setting(value = "items")
    private List<ItemJobItem> jobItems;

    @Override
    public List<ItemJobItem> getJobItems() {
        return jobItems;
    }

    @Override
    public String getId() {
        return "CRAFT";
    }

    @Listener
    public void onBlockEvent(ClickInventoryEvent event, @Root Player player, @Getter("getTargetInventory") Inventory inventory) throws Exception {
        if (inventory.getArchetype() == InventoryArchetypes.PLAYER || inventory.getArchetype() == InventoryArchetypes.WORKBENCH) {
            Inventory craftingOutputs = inventory.query(CraftingOutput.class);

            Iterator<Inventory> iterator = craftingOutputs.slots().iterator();

            while(iterator.hasNext()){
                Inventory inv = iterator.next();

                Optional<ItemStack> optional = inv.peek();

                if(optional.isPresent()){
                    super.onEvent(event, player,
                            () -> true,
                            () -> ItemJobItem.fromItemStack(optional.get()));
                }
            }
        }
    }
}
