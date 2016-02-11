package de.lergin.sponge.jobs.job;

import org.spongepowered.api.entity.living.player.Player;

public class Job {
    private String name;
    private String id;
    private int xp;

    public Job(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public Integer getXp(){
        return xp;
    }

    public void addXp(Player player, int amount){
        this.xp = this.xp + amount;
    }
}
