package de.lergin.sponge.jobs.job;

/**
 * an object with that something can happen in a job
 */
public class JobItem {
    private final double XP;
    private final int NEED_LEVEL;
    private final Object ITEM;
    private final Job JOB;

    /**
     * creates a new {@link JobItem}
     * @param xp the amount of xp someone gets when he is finishing an action with this item
     * @param needLevel the level that is needed for doing an action with this item
     * @param job the {@link Job} that this item is related to
     * @param item the {@link Object} of the item
     */
    public JobItem(double xp, int needLevel, Job job, Object item) {
        this.XP = xp;
        this.NEED_LEVEL = needLevel;
        this.ITEM = item;
        this.JOB = job;
    }

    /**
     * returns the amount of xp someone gets when he is finishing an action with this item
     * @return the amount of xp
     */
    public double getXp(){
        return XP;
    }

    /**
     * returns the level that is needed to do an action with this item
     * @return the level
     */
    public double getNeedLevel() {
        return NEED_LEVEL;
    }

    /**
     * returns the item
     * @return the item
     */
    public Object getItem(){
        return ITEM;
    }

    /**
     * returns the {@link Job} that this item is related to
     * @return the {@link Job}
     */
    public Job getJob(){
        return JOB;
    }

    /**
     * decides if the given amount of xp is a height enough to do a action with this item
     * @param xp the xp that should be tested
     * @return true: can do the action, otherwise false
     */
    public boolean canDo(double xp){
        return canDo(getJob().getCurrendLevel(xp));
    }

    /**
     * decides if the given level is a height enough to do a action with this item
     * @param level the level that should be tested
     * @return true: can do the action, otherwise false
     */
    public boolean canDo(int level){
        return (getNeedLevel() - level) <= 0;
    }
}
