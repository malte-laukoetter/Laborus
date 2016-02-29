package de.lergin.sponge.jobs.job;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Random;

/**
 * a bonus that will give the player a bonus with a given probability
 */
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

    /**
     * decides if the bonus can happen with the {@link JobItem} and {@link Player}
     * @param jobItem the {@link JobItem} that should be tested
     * @param player the {@link Player} that should be tested
     * @return true if the bonus can happen
     */
    public abstract boolean canHappen(JobItem jobItem, Player player);

    /**
     * executes the bonus with the probability of {@link this.probability}
     * @param item the item the {@link de.lergin.sponge.jobs.job.JobAction} is happening with
     */
    public abstract void useBonus(JobItem item, Player player);

    /**
     * creates a new JobBonus
     * @param probability the probability that this bonus will be executed at each use
     * @param sendMessage if a message should be send when the bonus is executing
     * @param message the message that will be shown to the player
     */
    public JobBonus(double probability, boolean sendMessage, Text message){
        this.probability = probability;
        this.sendMessage = sendMessage;
        this.message = message;
    }

    /**
     * returns the probability with that the {@link JobBonus} will happen
     * @return the probability
     */
    public double getProbability() {
        return probability;
    }

    /**
     * returns if a message should be send to the {@link Player} when this action happens
     * @return true if a message should be send
     */
    public boolean isSendMessage() {
        return sendMessage;
    }

    /**
     * returns the {@link Text} of the message that may be send when this {@link JobAction} is happening
     * @return the {@link Text} of the message
     */
    public Text getMessage() {
        return message;
    }
}
