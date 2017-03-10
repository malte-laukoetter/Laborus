package de.lergin.sponge.laborus.job;

import ninja.leaping.configurate.objectmapping.Setting;

/**
 * an object with that something can happen in a job
 */
public abstract class JobItem<T> {
    @Setting(value = "xp", comment = "amount of job xp gained after finishing the action with this item")
    private double XP = 0;
    @Setting(value = "needLevel", comment = "level needed to use this item with the action")
    private int NEED_LEVEL = 0;

    /**
     * returns the amount of xp someone gets when he is finishing an action with this item
     *
     * @return the amount of xp
     */
    public double getXp() {
        return XP;
    }

    /**
     * returns the level that is needed to do an action with this item
     *
     * @return the level
     */
    public double getNeedLevel() {
        return NEED_LEVEL;
    }

    /**
     * returns the item
     *
     * @return the item
     */
    public abstract T getItem();

    /**
     * decides if the given amount of xp is a height enough to do a action with this item
     *
     * @param xp the xp that should be tested
     * @return true: can do the action, otherwise false
     */
    public boolean canDo(Job job, double xp) {
        return canDo(job.getCurrentLevel(xp));
    }

    /**
     * decides if the given level is a height enough to do a action with this item
     *
     * @param level the level that should be tested
     * @return true: can do the action, otherwise false
     */
    public boolean canDo(int level) {
        return (getNeedLevel() - level) <= 0;
    }
}
