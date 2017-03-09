package de.lergin.sponge.laborus.job;

import de.lergin.sponge.laborus.listener.*;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.block.BlockState;

import java.lang.reflect.Constructor;
import java.util.List;

public enum JobAction {
    BREAK(BreakBlockListener.class, BlockState.class),
    ENTITY_DAMAGE(EntityDamageListener.class, CatalogTypes.ENTITY_TYPE),
    ENTITY_KILL(EntityKillListener.class, CatalogTypes.ENTITY_TYPE),
    ENTITY_TAME(EntityTameListener.class, CatalogTypes.ENTITY_TYPE),
    ITEM_USE(InteractListener.class, CatalogTypes.ITEM_TYPE),
    PLACE(PlaceBlockListener.class, BlockState.class);

    private final Class listener;
    private final Class<? extends CatalogType> catalogType;

    /**
     * creates a new JobAction
     *
     * @param listener    the listener for this action
     * @param catalogType the {@link CatalogType} that all actions of this {@link JobAction} have
     */
    JobAction(Class listener, Class<? extends CatalogType> catalogType) {
        this.listener = listener;
        this.catalogType = catalogType;
    }

    /**
     * returns the {@link JobListener} of this {@link JobAction}
     *
     * @return
     */
    protected Class getListener() {
        return listener;
    }

    /**
     * gets the {@link CatalogType} of the {@link JobAction}
     *
     * @return the {@link CatalogType}
     */
    public Class<? extends CatalogType> getCatalogType() {
        return catalogType;
    }

    /**
     * returns the Constructor for the listener
     *
     * @return the constructor
     * @throws NoSuchMethodException
     */
    public Constructor getListenerConstructor() throws NoSuchMethodException {
        return getListener().getConstructor(Job.class, List.class);
    }
}
