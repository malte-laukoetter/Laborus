package de.lergin.sponge.jobs.job;

public class JobItem {
    private final float XP;
    private final float NEED_XP;
    private final Object ITEM;
    private final Job JOB;

    public JobItem(float xp, float needXp, Job job, Object item) {
        this.XP = xp;
        this.NEED_XP = needXp;
        this.ITEM = item;
        this.JOB = job;
    }

    public float getXp(){
        return XP;
    }

    public float getNeedXp() {
        return NEED_XP;
    }

    public Object getItem(){
        return ITEM;
    }

    public Job getJob(){
        return JOB;
    }

    public boolean canDo(float xp){
        return (getNeedXp() - xp) <= 0;
    }
}
