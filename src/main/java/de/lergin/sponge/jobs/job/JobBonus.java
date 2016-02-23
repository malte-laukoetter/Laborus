package de.lergin.sponge.jobs.job;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Random;

public abstract class JobBonus {
    private final double probability;
    private final boolean sendMessage;
    private final Text message;
    private final Random random = new Random();

    /**
     * @return true with the probability of probability
     */
    public boolean isHappening(){
        return random.nextFloat() < probability;
    }

    public abstract boolean canHappen(JobItem jobItem);

    /**
     * executes the bonus with the probability of {@link this.probability}
     * @param item the item the {@link de.lergin.sponge.jobs.job.JobAction} is happening with
     */
    public abstract void useBonus(JobItem item, Player player);

    public JobBonus(double probability, boolean sendMessage, Text message){
        this.probability = probability;
        this.sendMessage = sendMessage;
        this.message = message;
    }

    public double getProbability() {
        return probability;
    }

    public boolean isSendMessage() {
        return sendMessage;
    }

    public Text getMessage() {
        return message;
    }
}
