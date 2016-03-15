package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.jobs.job.ability.EffectAbility;
import de.lergin.sponge.jobs.job.bonus.EpDrop;
import de.lergin.sponge.jobs.job.bonus.ItemDrop;
import de.lergin.sponge.jobs.job.bonus.ItemRepair;
import de.lergin.sponge.jobs.job.bonus.MultiDrop;
import de.lergin.sponge.jobs.util.BlockStateComparator;
import de.lergin.sponge.jobs.util.ConfigHelper;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.text.chat.ChatTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class for Jobs
 */
public class Job {
    private final String NAME;
    private final String ID;
    private final JobAbility jobAbility;
    private Map<JobAction, List<JobItem>> jobActions = new HashMap<>();
    private List<GameMode> enabledGameModes = JobsMain.instance().enabledGameModes;
    private Set<JobBonus> jobBonuses = new HashSet<>();
    private List<Integer> level = new ArrayList<>();

    /**
     * create a new Job from a config node
     * @param jobConfig a config node with all the information for the job
     */
    public Job(ConfigurationNode jobConfig) {
        this.NAME = jobConfig.getNode("name").getString();

        if(jobConfig.getNode("id").getString("").equals("")){
            this.ID = jobConfig.getKey().toString();
        }else{
            this.ID = jobConfig.getNode("id").getString();
        }

        List<? extends ConfigurationNode> levelConfig;

        if(jobConfig.getNode("use_default_level").getBoolean(false)){
            levelConfig = ConfigHelper.getNode("level").getChildrenList();
        }else{
            levelConfig = jobConfig.getNode("level").getChildrenList();
        }

        this.level.addAll(
                levelConfig.stream().map(ConfigurationNode::getInt).collect(Collectors.toList())
        );

        initStringJobAction(jobConfig.getNode("destroyBlocks"), JobAction.BREAK);
        initStringJobAction(jobConfig.getNode("placeBlocks"), JobAction.PLACE);
        initJobAction(jobConfig.getNode("killEntities"), JobAction.ENTITY_KILL);
        initJobAction(jobConfig.getNode("damageEntities"), JobAction.ENTITY_DAMAGE);
        initJobAction(jobConfig.getNode("useItems"), JobAction.ITEM_USE);
        initJobAction(jobConfig.getNode("tameEntities"), JobAction.ENTITY_TAME);

        for(ConfigurationNode bonusNode : jobConfig.getNode("bonus").getChildrenList()){
            switch (bonusNode.getNode("id").getString()){
                case "multiDrop":
                    jobBonuses.add(new MultiDrop(bonusNode));
                    break;
                case "ep":
                    jobBonuses.add(new EpDrop(bonusNode));
                    break;
                case "itemRepair":
                    jobBonuses.add(new ItemRepair(bonusNode));
                    break;
                case "itemDrop":
                    jobBonuses.add(new ItemDrop(bonusNode));
                    break;
            }
        }

        ConfigurationNode abilityConfig = jobConfig.getNode("ability");

        switch (abilityConfig.getNode("id").getString("")){
            case "effect":
                this.jobAbility = new EffectAbility(this, jobConfig.getNode("ability"));
                break;
            default:
                this.jobAbility = null;
        }
    }

    /**
     * returns the name of the job
     * @return the name
     */
    public String getName() {
        return NAME;
    }

    /**
     * returns the id of the job
     * @return the id
     */
    public String getId() {
        return ID;
    }

    /**
     * returns the {@link JobAbility} of the job
     * @return the {@link JobAbility}
     */
    public JobAbility getJobAbility() {
        return jobAbility;
    }

    /**
     * tests if the job has a {@link JobAbility}
     * @return true if the job has a {@link JobAbility}
     */
    public boolean hasJobAbility() {
        return jobAbility != null;
    }

    /**
     * adds some xp to the {@link Player} in this job
     * @param player the {@link Player}
     * @param amount the amount of xp that should be added
     */
    public void addXp(Player player, double amount){
        Map<String, Double> jobData = player.get(JobKeys.JOB_DATA).orElse(new HashMap<>());

        double oldXp = jobData.getOrDefault(getId(), 0.0);
        double newXp = oldXp + amount;

        jobData.put(getId(), newXp);

        if(player.supports(JobKeys.JOB_DATA)){
            player.offer(JobKeys.JOB_DATA, jobData);
        }else{
            player.offer(new JobDataManipulatorBuilder().jobs(jobData).create());
        }

        this.level.stream().filter(level -> level > oldXp && level <= newXp).forEach(level -> {
            //TODO: setting
            player.sendMessage(
                    TranslationHelper.p(player, "player.info.job.level_up", getName(), this.level.indexOf(level))
            );
        });


        if(newXp != 0){
            //TODO: setting
            player.sendMessage(ChatTypes.ACTION_BAR,
                    TranslationHelper.p(player, "player.info.job.xp.action_bar", getName(), getXp(player))
            );
        }
    }

    /**
     * returns the jobXp of the player
     * @param player the player from that the xp should be returned
     * @return the xp of the player
     */
    public double getXp(Player player){
        return player.get(JobKeys.JOB_DATA).orElse(new HashMap<>()).getOrDefault(getId(), 0.0);
    }

    /**
     * called by the {@link de.lergin.sponge.jobs.listener.JobListener} and handles the awarding of the player
     * @param item the item of the event
     * @param player the {@link Player} that has caused the event
     * @param action the {@link JobAction} of the {@link de.lergin.sponge.jobs.listener.JobListener}
     * @return true if the {@link Player} was awarded
     */
    public boolean onJobListener(Object item, Player player, JobAction action){
        for(JobItem jobItem : jobActions.get(action)){
            if(
                    jobItem.getItem().equals(item) ||
                    (
                            jobItem.getItem() instanceof  String &&
                            item instanceof BlockState &&
                            BlockStateComparator.compare((BlockState) item, (String) jobItem.getItem())
                    )
                    ){
                if(jobItem.canDo(getXp((player)))){
                    double newXp = jobItem.getXp() *
                         (isSelected(player) ? 1 : ConfigHelper.getNode("setting", "xp_without_job").getDouble(0.5));

                    this.addXp(player, newXp);
                    jobBonuses.stream()
                            .filter(jobBonus -> jobBonus.canHappen(this, action, jobItem, player))
                            .forEach(jobBonus -> jobBonus.useBonus(jobItem, player));

                    return true;
                }else{
                    player.sendMessage(ChatTypes.ACTION_BAR,
                            TranslationHelper.p(player, "player.warning.job.not_enough_xp")
                    );
                    return false;
                }
            }
        }

        return false;
    }

    /**
     * is the JobSystem for the {@link Player} enabled
     * @param player the {@link Player} that should be tested
     * @return true if it is enabled
     */
    public boolean enabled(Player player){
        return player.get(JobKeys.JOB_ENABLED).orElse(true) && enabledGameModes.contains(player.get(Keys.GAME_MODE).get());
    }

    /**
     * is this {@link Job} selected
     * @param player the {@link Player} that should be tested
     * @return true if the {@link Job} selected
     */
    public boolean isSelected(Player player){
        Set<String> selectedJobs = player.get(JobKeys.JOB_SELECTED).orElse(new HashSet<>());

        return selectedJobs.contains(getId());
    }

    /**
     * initialize a {@link JobAction} for this {@link Job}
     * @param jobActionNode a {@link ConfigurationNode} that has the settings for the {@link JobAction}
     * @param action the {@link JobAction} that should be initialized
     */
    private void initJobAction(ConfigurationNode jobActionNode, JobAction action){
        if(jobActionNode.getChildrenMap().isEmpty())
            return;

        jobActions.put(
                action,
                generateJobItemList(jobActionNode.getChildrenMap().values(), action.getCatalogType())
        );

        try {
            Sponge.getEventManager().registerListeners(
                    JobsMain.instance(),
                    action.getListenerConstructor().newInstance(this, generateJobItemTypeList(jobActions.get(action)))
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * initialize a {@link JobAction} for this {@link Job}
     * @param jobActionNode a {@link ConfigurationNode} that has the settings for the {@link JobAction}
     * @param action the {@link JobAction} that should be initialized
     */
    private void initStringJobAction(ConfigurationNode jobActionNode, JobAction action){
        if(jobActionNode.getChildrenMap().isEmpty())
            return;

        jobActions.put(
                action,
                generateStringJobItemList(jobActionNode.getChildrenMap().values(), action.getCatalogType())
        );

        try {
            Sponge.getEventManager().registerListeners(
                    JobsMain.instance(),
                    action.getListenerConstructor().newInstance(this, generateJobItemTypeList(jobActions.get(action)))
            );
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * generates a {@link List} of {@link JobItem}s from a {@link Collection} of {@link ConfigurationNode}s
     * @param nodes a {@link Collection} of {@link ConfigurationNode}s with the data for the {@link JobItem}s
     * @param catalogType the {@link Class} of the {@link CatalogType} of the {@link JobItem}s
     * @return a {@link List} of the {@link JobItem}s that are created by the Config
     */
    private List<JobItem> generateJobItemList(Collection<? extends ConfigurationNode> nodes, Class<? extends CatalogType> catalogType){
        List<JobItem> jobItems = new ArrayList<>();

        for(ConfigurationNode jobItemNode : nodes){

            Optional<? extends CatalogType> optionalJobItemItem = Sponge.getRegistry().getType(
                    catalogType,
                    jobItemNode.getKey().toString()
            );

            if(optionalJobItemItem.isPresent()){
                jobItems.add(
                        new JobItem(
                                jobItemNode.getNode("xp").getDouble(0.0),
                                jobItemNode.getNode("needLevel").getInt(0),
                                this,
                                optionalJobItemItem.get()
                        )
                );
            }
        }

        return jobItems;
    }

    /**
     * generates a {@link List} of {@link JobItem}s from a {@link Collection} of {@link ConfigurationNode}s
     * @param nodes a {@link Collection} of {@link ConfigurationNode}s with the data for the {@link JobItem}s
     * @param catalogType the {@link Class} of the {@link CatalogType} of the {@link JobItem}s
     * @return a {@link List} of the {@link JobItem}s that are created by the Config
     */
    private List<JobItem> generateStringJobItemList(Collection<? extends ConfigurationNode> nodes, Class<? extends CatalogType> catalogType){
        return nodes.stream().map(jobItemNode -> new JobItem(
                jobItemNode.getNode("xp").getDouble(0.0),
                jobItemNode.getNode("needLevel").getInt(0),
                this,
                jobItemNode.getKey().toString()
        )).collect(Collectors.toList());
    }

    /**
     * creates a {@link List} of T (Type of the {@link JobItem}s) from the {@link List} of {@link JobItem}s
     * @param jobItems the {@link JobItem}s
     * @param <T> the Type of the {@link JobItem}s
     * @return a {@link List} of T
     */
    private static <T> List<T> generateJobItemTypeList(List<JobItem> jobItems){
        return jobItems.stream().map(jobItem -> (T) jobItem.getItem()).collect(Collectors.toList());
    }

    /**
     * returns the level that relates to the xp
     * @param xp the xp
     * @return the level
     */
    public int getCurrentLevel(double xp){
        for(int testLevel : this.level) {
            if (testLevel > xp) {
                return this.level.indexOf(testLevel) - 1;
            }
        }

        return this.level.size();
    }

    public int getLevel(Player player){
        return getCurrentLevel(getXp(player));
    }

    @Override
    public String toString() {
        return "Job{" +
                "NAME='" + NAME + '\'' +
                ", ID='" + ID + '\'' +
                ", jobAbility=" + jobAbility +
                ", jobActions=" + jobActions +
                ", enabledGameModes=" + enabledGameModes +
                ", jobBonuses=" + jobBonuses +
                ", level=" + level +
                '}';
    }
}
