package de.lergin.laborus.api;

import com.google.common.collect.ImmutableList;
import de.lergin.laborus.job.Job;
import de.lergin.laborus.data.JobKeys;
import ninja.leaping.configurate.objectmapping.Setting;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageReceiver;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * a bonus that will give the player a bonus with a given probability
 */
public abstract class JobBonus implements Serializable{
    @Setting(value = "probability", comment = "the probability that the bonus is awarded (between 0.0 and 1.0)")
    private double probability = 0;
    @Setting(value = "sendMessage", comment = "the message will only be send if this is true")
    private boolean sendMessage = false;
    @Setting(value = "message", comment = "message send when the bonus is awarded")
    private Text message = Text.EMPTY;
    private Random random = new Random();
    @Setting(value = "minLevel", comment = "the minimum level a player need to get the bonus")
    private int minLevel = 0;
    @Setting(value = "maxLevel", comment = "maximum level the bonus is awarded at")
    private int maxLevel = -1;
    @Setting(value = "onlySelected", comment = "if true it will only be awarded if the job is selected")
    private boolean onlySelected = true;
    //@Setting(value = "jobItems")
    // currently not working...
    private List<String> jobItems = ImmutableList.of();
    @Setting(value = "actions", comment = "actions that can award this bonus (BREAK, ENTITY_DAMAGE, ENTITY_KILL, ENTITY_TAME, ITEM_USE, PLACE)")
    private List<String> jobActions = ImmutableList.of();

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
    public boolean canHappen(Job job, JobAction<?> jobAction, JobItem jobItem, Player player) {
        if (minLevel > job.getLevel(player))
            return false;

        if (maxLevel > 0 && maxLevel < job.getLevel(player))
            return false;

        if (this.onlySelected && !player.get(JobKeys.JOB_SELECTED).orElseGet(HashSet::new).contains(job.getId()))
            return false;

        if (!this.jobActions.isEmpty() && !this.jobActions.contains(jobAction.getId()))
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
     * starts the use of the bonus, test the probability calls applyBonus and sends the message
     */
    public void useBonus(JobItem jobItem, Player player, Object item){
        if (this.isHappening()) {
            this.applyBonus(jobItem, player, item);

            this.sendMessage(player);
        }
    }

    /**
     * executes the specific Bonus actions of the Bonus
     *
     * @param item the item the {@link JobAction} is happening with
     */
    public abstract void applyBonus(JobItem jobitem, Player player, Object item);

    /**
     * creates a new JobBonus
     */
    public JobBonus(List<String> jobActions) {
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
     * sends the message if isSendMessage is true
     * @param receiver
     */
    public void sendMessage(MessageReceiver receiver){
        if(isSendMessage()){
            receiver.sendMessage(getMessage());
        }
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
