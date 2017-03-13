package de.lergin.sponge.laborus.job;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import de.lergin.sponge.laborus.data.JobKeys;
import de.lergin.sponge.laborus.util.BlockStateComparator;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.*;

/**
 * a bonus that will give the player a bonus with a given probability
 */
public abstract class JobBonus {
    @Setting(value = "probability")
    private double probability = 0;
    @Setting(value = "sendMessage")
    private boolean sendMessage = false;
    @Setting(value = "message")
    private Text message = Text.EMPTY;
    private Random random = new Random();
    @Setting(value = "minLevel")
    private int minLevel = 0;
    @Setting(value = "maxLevel")
    private int maxLevel = -1;
    @Setting(value = "onlySelected")
    private boolean onlySelected = true;
    //@Setting(value = "jobItems")
    // currently not working...
    private List<String> jobItems = ImmutableList.of();
    @Setting(value = "actions")
    private List<JobAction> jobActions = ImmutableList.of();

    /**
     * @return true with the probability of probability
     */
    public boolean isHappening() {
        return random.nextFloat() < probability;
    }

    /**
     * decides if the bonus can happen with the {@link JobItem}, {@link JobAction} and {@link Player}
     *
     * @param jobAction the {@link JobAction} that should be tested
     * @param jobItem   the {@link JobItem} that should be tested
     * @param player    the {@link Player} that should be tested
     * @return true if the bonus can happen
     */
    public boolean canHappen(Job job, JobAction jobAction, JobItem jobItem, Player player) {
        return testConditions(job, jobAction, jobItem, player);
    }

    public boolean testConditions(Job job, JobAction jobAction, JobItem jobItem, Player player) {
        if (minLevel > job.getLevel(player))
            return false;

        if (maxLevel > 0 && maxLevel < job.getLevel(player))
            return false;

        if (this.onlySelected && !player.get(JobKeys.JOB_SELECTED).orElseGet(HashSet::new).contains(job.getId()))
            return false;

        if (!this.jobActions.isEmpty() && !this.jobActions.contains(jobAction))
            return false;

        return true;
      /*  if (this.jobItems.isEmpty())
            return true;

        for (String item : this.jobItems) {
            if (jobItem.getItem().equals(item) || jobItem.getItem() instanceof String &&
                    BlockStateComparator.compare(item, (String) jobItem.getItem()))
                return true;
        }

        return false;*/
    }

    /**
     * executes the bonus with the probability of {@link this.probability}
     *
     * @param item the item the {@link de.lergin.sponge.laborus.job.JobAction} is happening with
     */
    public abstract void useBonus(JobItem jobitem, Player player, Object item);

    /**
     * creates a new JobBonus
     */
    public JobBonus(List<JobAction> jobActions) {
        this.jobActions = jobActions;
    }

    /**
     * creates a new JobBonus
     */
    public JobBonus() {
        this.jobActions = ImmutableList.of();
    }

    /**
     * returns the probability with that the {@link JobBonus} will happen
     *
     * @return the probability
     */
    public double getProbability() {
        return probability;
    }

    /**
     * returns if a message should be send to the {@link Player} when this action happens
     *
     * @return true if a message should be send
     */
    public boolean isSendMessage() {
        return sendMessage;
    }

    /**
     * returns the {@link Text} of the message that may be send when this {@link JobAction} is happening
     *
     * @return the {@link Text} of the message
     */
    public Text getMessage() {
        return message;
    }
}
