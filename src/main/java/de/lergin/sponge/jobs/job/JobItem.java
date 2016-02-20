package de.lergin.sponge.jobs.job;

public class JobItem {
    private final double XP;
    private final double NEED_XP;
    private final Object ITEM;
    private final Job JOB;

    public JobItem(double xp, double needXp, Job job, Object item) {
        this.XP = xp;
        this.NEED_XP = needXp;
        this.ITEM = item;
        this.JOB = job;
    }

    public double getXp(){
        return XP;
    }

    public double getNeedXp() {
        return NEED_XP;
    }

    public Object getItem(){
        return ITEM;
    }

    public Job getJob(){
        return JOB;
    }

    public boolean canDo(double xp){
        return (getNeedXp() - xp) <= 0;
    }
}
