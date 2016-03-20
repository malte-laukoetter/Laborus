package de.lergin.sponge.laborus.job;

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.laborus.data.JobKeys;
import de.lergin.sponge.laborus.util.BlockStateComparator;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.*;

/**
 * a bonus that will give the player a bonus with a given probability
 */
public abstract class JobBonus {
    private final double probability;
    private final boolean sendMessage;
    private final Text message;
    private final Random random = new Random();
    private final int minLevel;
    private final int maxLevel;
    private final boolean onlySelected;
    private final List<String> jobItems;// = new ArrayList<>();
    private final Set<JobAction> jobActions = new HashSet<>();

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

        if (maxLevel < job.getLevel(player))
            return false;

        if (!(this.onlySelected && player.get(JobKeys.JOB_SELECTED).orElse(new HashSet<>()).contains(job.getId())))
            return false;

        if (!(this.jobActions.isEmpty() || this.jobActions.contains(jobAction)))
            return false;

        if (this.jobItems.isEmpty())
            return true;

        for (String item : this.jobItems) {
            if (jobItem.getItem().equals(item) || jobItem.getItem() instanceof String &&
                    BlockStateComparator.compare(item, (String) jobItem.getItem()))
                return true;
        }

        return false;
    }

    /**
     * executes the bonus with the probability of {@link this.probability}
     *
     * @param item the item the {@link de.lergin.sponge.laborus.job.JobAction} is happening with
     */
    public abstract void useBonus(JobItem item, Player player);

    /**
     * creates a new JobBonus
     *
     * @param config the {@link ConfigurationNode} with the data for the Bonus
     */
    public JobBonus(ConfigurationNode config) {
        this.probability = config.getNode("probability").getDouble(0.05);
        this.sendMessage = config.getNode("sendMessage").getBoolean(false);
        this.message = Text.of(config.getNode("message").getString(""));
        this.minLevel = config.getNode("condition", "minLevel").getInt(Integer.MIN_VALUE);
        this.maxLevel = config.getNode("condition", "maxLevel").getInt(Integer.MAX_VALUE);
        this.onlySelected = config.getNode("condition", "onlySelected").getBoolean(true);

        List<String> jobItems1;
        try {
            jobItems1 = config.getNode("condition", "jobItems").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            jobItems1 = new ArrayList<>();
            e.printStackTrace();
        }
        this.jobItems = jobItems1;
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
