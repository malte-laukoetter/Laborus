package de.lergin.sponge.jobs.job;

public class JobItem {
    private final double XP;
    private final int NEED_LEVEL;
    private final Object ITEM;
    private final Job JOB;

    public JobItem(double xp, int needLevel, Job job, Object item) {
        this.XP = xp;
        this.NEED_LEVEL = needLevel;
        this.ITEM = item;
        this.JOB = job;
    }

    public double getXp(){
        return XP;
    }

    public double getNeedLevel() {
        return NEED_LEVEL;
    }

    public Object getItem(){
        return ITEM;
    }

    public Job getJob(){
        return JOB;
    }

    public boolean canDo(double xp){
        return (getNeedLevel() - xp) <= 0;
    }
}
