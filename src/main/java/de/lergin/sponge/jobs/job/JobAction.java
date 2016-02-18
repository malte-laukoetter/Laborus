package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.listener.*;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;

import java.lang.reflect.Constructor;
import java.util.List;

public enum JobAction {
    BREAK(BreakBlockListener.class, CatalogTypes.BLOCK_TYPE),
    ENTITY_KILL(EntityKillListener.class, CatalogTypes.ENTITY_TYPE),
    ENTITY_DAMAGE(EntityDamageListener.class, CatalogTypes.ENTITY_TYPE),
    ITEM_USE(InteractListener.class, CatalogTypes.ITEM_TYPE),
    PLACE(PlaceBlockListener.class, CatalogTypes.BLOCK_TYPE);

    Class listener;
    Class<? extends CatalogType> catalogType;

    JobAction(Class listener, Class<? extends CatalogType> catalogType){
        this.listener = listener;
        this.catalogType = catalogType;
    }

    protected Class getListener(){
        return listener;
    }

    public Class<? extends CatalogType> getCatalogType(){
        return catalogType;
    }

    public Constructor getListenerConstructor() throws NoSuchMethodException {
        return getListener().getConstructor(Job.class, List.class);
    }
}
