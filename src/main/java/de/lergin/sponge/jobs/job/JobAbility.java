package de.lergin.sponge.jobs.job;

import org.spongepowered.api.entity.living.player.Player;

public abstract class JobAbility {
    private final Job job;
    private final int coolDown;
    private final String name;

    public JobAbility(Job job, String name, int coolDown) {
        this.name = name;
        this.job = job;
        this.coolDown = coolDown;
    }

    public abstract boolean startAbility(Player player);

    public abstract boolean canStartAbility(Player player);
}
