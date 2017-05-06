package de.lergin.laborus.job;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.lergin.laborus.Laborus;
import de.lergin.laborus.api.JobAbility;
import de.lergin.laborus.api.JobAction;
import de.lergin.laborus.data.jobs.JobDataManipulatorBuilder;
import de.lergin.laborus.api.JobBonus;
import de.lergin.laborus.config.TranslationKeys;
import de.lergin.laborus.data.JobKeys;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextElement;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.*;

/**
 * class for Jobs
 */
@ConfigSerializable
public class Job {
    private Laborus plugin = Laborus.instance();

    @Setting(value = "name", comment = "name of the job shown to the user")
    private String NAME = "unknown";

    @Setting(value = "id", comment = "id of the job used for the comments and to save the data to the player")
    private String ID = "unknown";

    @Setting(value = "description", comment = "description shown on the info comment")
    private String DESCRIPTION = "unknown";

    @Setting(value = "permission", comment = "permission needed to join the job")
    private String PERMISSION = "";

    @Setting(value = "ability", comment = "the special ability of the job")
    private JobAbilities jobAbility = new JobAbilities(null);

    @Setting(value = "bonus", comment = "the boni of the job")
    private Map<String, JobBoni> jobBoni = ImmutableMap.of();

    @Setting(value = "actions", comment = "the stuff that awards ep to the player")
    private JobActions jobActions = new JobActions(ImmutableList.of());

    @Setting(value = "level", comment = "a list of ep points the player needs for each level, if not set it will use the default levels")
    private List<Long> configLevel = null;
    private List<Long> level = null;

    public void initJob(){
        jobActions.get().forEach((a)-> a.init(this));
    }

    public void unloadJob(){
        jobActions.get().forEach(JobAction::unload);
    }

    public Job() {}

    /**
     * returns the name of the job
     *
     * @return the name
     */
    public String getName() {
        return NAME;
    }

    /**
     * returns the description of the job
     *
     * @return the description
     */
    public String getDescription() {
        return DESCRIPTION;
    }

    /**
     * returns the id of the job
     *
     * @return the id
     */
    public String getId() {
        return ID;
    }

    /**
     * returns the {@link JobAbility} of the job
     *
     * @return the {@link JobAbility}
     */
    public JobAbility getJobAbility() {
        return jobAbility.get();
    }

    /**
     * tests if the job has a {@link JobAbility}
     *
     * @return true if the job has a {@link JobAbility}
     */
    public boolean hasJobAbility() {
        return jobAbility != null;
    }

    public List<JobAction> getJobActions() {
        return jobActions.get();
    }

    /**
     * adds some xp to the {@link Player} in this job
     *
     * @param player the {@link Player}
     * @param amount the amount of xp that should be added
     */
    public void addXp(Player player, double amount) {
        Map<String, Double> jobData = player.get(JobKeys.JOB_DATA).orElseGet(HashMap::new);

        double oldXp = jobData.getOrDefault(getId(), 0.0);
        double newXp = oldXp + amount;

        jobData.put(getId(), newXp);

        if (player.supports(JobKeys.JOB_DATA)) {
            player.offer(JobKeys.JOB_DATA, jobData);
        } else {
            player.offer(new JobDataManipulatorBuilder().jobs(jobData).create());
        }

        plugin.config.base.loggingConfig.jobGeneral(this,"Added {} xp to {}", amount, player.getName());

        this.getLevel().stream().filter(level -> level > oldXp && level <= newXp).forEach(level -> {
            plugin.config.base.loggingConfig
                    .jobGeneral(this,"{} reached new level {} ({} xp)", player.getName(), this.getLevel(player), level);

            player.sendMessage(
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.JOB_LEVEL_UP,
                            player,
                            this.getId()
                    ),
                    this.textArgs(player)
            );
        });

        if (amount != 0) {
            player.sendMessage(
                    ChatTypes.ACTION_BAR,
                    Laborus.instance().translationHelper.get(
                            TranslationKeys.JOB_XP_ACTION_BAR,
                            player,
                            this.getId()
                    ).apply(this.textArgs(player)).build()
            );
        }
    }

    /**
     * returns the jobXp of the player
     *
     * @param player the player from that the xp should be returned
     * @return the xp of the player
     */
    public double getXp(Player player) {
        return player.get(JobKeys.JOB_DATA).orElseGet(HashMap::new).getOrDefault(getId(), 0.0);
    }

    /**
     * is the JobSystem for the {@link Player} enabled
     *
     * @param player the {@link Player} that should be tested
     * @return true if it is enabled
     */
    public boolean enabled(Player player) {
        return player.get(JobKeys.JOB_ENABLED).orElse(true) &&
                Laborus.instance().config.base.enabledGamemodes.contains(player.get(Keys.GAME_MODE).get());
    }

    /**
     * is this {@link Job} selected
     *
     * @param player the {@link Player} that should be tested
     * @return true if the {@link Job} selected
     */
    public boolean isSelected(Player player) {
        Set<String> selectedJobs = player.get(JobKeys.JOB_SELECTED).orElseGet(HashSet::new);

        return selectedJobs.contains(getId());
    }

    /**
     * returns the level that relates to the xp
     *
     * @param xp the xp
     * @return the level
     */
    public int getCurrentLevel(double xp) {
        for (long testLevel : this.getLevel()) {
            if (testLevel > xp) {
                return this.getLevel().indexOf(testLevel) - 1;
            }
        }

        return this.getLevel().size();
    }

    /**
     * tests if a player can join the job
     *
     * @param player the player
     * @return has the player the permission to use the job
     */
    public boolean hasPermission(Player player) {
        return this.PERMISSION.equals("") || player.hasPermission(this.PERMISSION);
    }

    public int getLevel(Player player) {
        return getCurrentLevel(getXp(player));
    }


    public double getXpTillNextLevel(double xp) {
        for (long testLevel : this.getLevel()) {
            if (testLevel > xp) {
                return testLevel - xp;
            }
        }

        return 0.0;
    }

    public double getXpTillNextLevel(Player player) {
        return getXpTillNextLevel(getXp(player));
    }

    @Override
    public String toString() {
        return "Job{" +
                "NAME='" + NAME + '\'' +
                ", ID='" + ID + '\'' +
                ", jobAbility=" + jobAbility +
                ", jobActions=" + getJobActions() +
                ", jobBonuses=" + jobBoni() +
                ", level=" + getLevel() +
                '}';
    }

    public Map<String, TextElement> textArgs(Player player){
        Map<String, TextElement> args = new HashMap<>();

        args.put("job.xp", Text.of(String.format("%1$.2f", this.getXp(player))));
        args.put("job.level", Text.of(this.getLevel(player)));
        args.put("job.name", Text.of(this.getName()));
        args.put("job.selected", Text.of(this.isSelected(player)));
        args.put("job.description", Text.of(this.getDescription()));
        args.put("job.xp_till_next_level", Text.of(String.format("%1$.2f", this.getXpTillNextLevel(player))));
        args.put("job.xp_for_next_level", Text.of(String.format("%1$.2f", this.getXp(player) + this.getXpTillNextLevel(player))));

        return args;
    }

    public List<JobBonus> jobBoni(){
        List<JobBonus> jobBoni = new ArrayList<>();

        this.jobBoni.forEach((key, jobBonus) -> {
            jobBoni.addAll(jobBonus.get());
        });

        return jobBoni;
    }

    public List<Long> getLevel() {
        if(level == null || level.isEmpty()){
            if(configLevel != null){
                level = configLevel;
            }else{
                level = Laborus.instance().config.base.levels;
            }
        }

        return level;
    }
}
