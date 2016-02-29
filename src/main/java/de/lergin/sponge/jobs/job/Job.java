package de.lergin.sponge.jobs.job;

import de.lergin.sponge.jobs.JobsMain;
import de.lergin.sponge.jobs.data.JobKeys;
import de.lergin.sponge.jobs.job.bonus.EpDrop;
import de.lergin.sponge.jobs.job.bonus.ItemRepair;
import de.lergin.sponge.jobs.job.bonus.MultiDrop;
import de.lergin.sponge.jobs.util.ConfigHelper;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.text.chat.ChatTypes;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class Job {
    private final String NAME;
    private final String ID;
    private Map<JobAction, List<JobItem>> jobActions = new HashMap<>();
    private List<GameMode> enabledGameModes = JobsMain.instance().enabledGameModes;
    private Set<JobBonus> jobBonuses = new HashSet<>();
    private List<Integer> level = new ArrayList<>();

    public Job(ConfigurationNode jobConfig) {
        this.NAME = jobConfig.getNode("name").getString();
        this.ID = jobConfig.getKey().toString();

        List<? extends ConfigurationNode> levelConfig;

        if(jobConfig.getNode("use_default_level").getBoolean(false)){
            levelConfig = ConfigHelper.getNode("level").getChildrenList();
        }else{
            levelConfig = jobConfig.getNode("level").getChildrenList();
        }

        this.level.addAll(
                levelConfig.stream().map(ConfigurationNode::getInt).collect(Collectors.toList())
        );

        initJobAction(jobConfig.getNode("destroyBlocks"), JobAction.BREAK);
        initJobAction(jobConfig.getNode("placeBlocks"), JobAction.PLACE);
        initJobAction(jobConfig.getNode("killEntities"), JobAction.ENTITY_KILL);
        initJobAction(jobConfig.getNode("damageEntities"), JobAction.ENTITY_DAMAGE);
        initJobAction(jobConfig.getNode("useItems"), JobAction.ITEM_USE);
        initJobAction(jobConfig.getNode("tameEntities"), JobAction.ENTITY_TAME);

        for(ConfigurationNode bonusNode : jobConfig.getNode("bonus").getChildrenMap().values()){
            switch (bonusNode.getKey().toString()){
                case "multiDrop":
                    jobBonuses.add(new MultiDrop(bonusNode));
                    break;
                case "ep":
                    jobBonuses.add(new EpDrop(bonusNode));
                    break;
                case "itemRepair":
                    jobBonuses.add(new ItemRepair(bonusNode));
                    break;
            }
        }
    }

    public String getName() {
        return NAME;
    }

    public String getId() {
        return ID;
    }

    public void addXp(Player player, double amount){
        Map<String, Double> jobData = player.get(JobKeys.JOB_DATA).orElse(new HashMap<>());

        double oldXp = jobData.getOrDefault(getId(), 0.0);
        double newXp = oldXp + amount;

        jobData.put(getId(), newXp);

        this.level.stream().filter(level -> level > oldXp && level <= newXp).forEach(level -> {
            //TODO: setting
            player.sendMessage(
                    TranslationHelper.p(player, "player.info.job.level_up", getName(), this.level.indexOf(level))
            );
        });


        //TODO: setting
        player.sendMessage(ChatTypes.ACTION_BAR,
                TranslationHelper.p(player, "player.info.job.xp.action_bar", getName(), getXp(player))
        );
    }

    public double getXp(Player player){
        return player.get(JobKeys.JOB_DATA).orElse(new HashMap<>()).getOrDefault(getId(), 0.0);
    }

    public boolean onJobListener(Object item, Player player, JobAction action){
        for(JobItem jobItem : jobActions.get(action)){
            if(jobItem.getItem().equals(item)){
                if(jobItem.canDo(getXp((player)))){
                    double newXp = jobItem.getXp() *
                         (isSelected(player)? 1 : ConfigHelper.getNode("setting", "xp_without_job").getDouble(0.5));

                    this.addXp(player, newXp);

                    jobBonuses.stream()
                            .filter(jobBonus -> jobBonus.canHappen(jobItem, player))
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

    public boolean enabled(Player player){
        return player.get(JobKeys.JOB_ENABLED).orElse(true) && enabledGameModes.contains(player.get(Keys.GAME_MODE).get());
    }

    public boolean isSelected(Player player){
        Set<String> selectedJobs = player.get(JobKeys.JOB_SELECTED).orElse(new HashSet<>());

        return selectedJobs.contains(getId());
    }


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

    private static <T> List<T> generateJobItemTypeList(List<JobItem> jobItems){
        List<T> itemTypes = new ArrayList<>();

        for(JobItem jobItem : jobItems){
            itemTypes.add((T) jobItem.getItem());
        }

        return itemTypes;
    }

    public int getCurrendLevel(double xp){
        for(int testLevel : this.level) {
            if (testLevel > xp) {
                return this.level.indexOf(testLevel) - 1;
            }
        }

        return this.level.size();
    }

}
