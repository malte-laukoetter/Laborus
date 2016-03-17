package de.lergin.sponge.laborus;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import de.lergin.sponge.laborus.command.*;
import de.lergin.sponge.laborus.data.jobs.ImmutableJobDataManipulator;
import de.lergin.sponge.laborus.data.jobs.JobData;
import de.lergin.sponge.laborus.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.util.AntiReplaceFarming;
import de.lergin.sponge.laborus.util.ConfigHelper;
import de.lergin.sponge.laborus.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCallable;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@Plugin(id = "de.lergin.sponge.laborus", name = "Laborus", version = "0.1", description = "a job plugin", authors = {"Lergin"})
public class JobsMain {
    @Inject
    @DefaultConfig(sharedRoot = false)
    public ConfigurationLoader<CommentedConfigurationNode> configManager;

    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path configDir;


    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    private static JobsMain instance;

    public static JobsMain instance() {
        return instance;
    }

    public List<GameMode> enabledGameModes = new ArrayList<>();

    public Map<String, Job> jobs = new HashMap<>();

    @Listener
    public void gameConstruct(GameConstructionEvent event) {
        instance = this;
    }

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        ConfigHelper.loadConfig();

        //translation setup
        initTranslations();
        TranslationHelper.init();
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        //setup the dataBase for the AntiReplaceFarming thing
        AntiReplaceFarming.setupDataBase();

        //init customData
        Sponge.getDataManager().register(JobData.class, ImmutableJobDataManipulator.class,
                new JobDataManipulatorBuilder());

        //load some data from the config
        initEnabledGameModes();

        //init jobs
        initJobs();

        //init commands
        initCommands();
    }

    @Listener
    public void onGameStoppedEvent(GameStoppedEvent event){
        ConfigHelper.saveConfig();
    }

    private void initEnabledGameModes(){
        List<String> enabledGameModeStrings;

        try {
            enabledGameModeStrings =
                    ConfigHelper.getNode("setting", "enabled_gamemodes").getList(TypeToken.of(String.class));
        } catch (ObjectMappingException e) {
            enabledGameModeStrings = new ArrayList<>();
            e.printStackTrace();
        }

        for(String gamemode : enabledGameModeStrings){
            Optional<GameMode> gameModeOptional = Sponge.getRegistry().getType(
                    CatalogTypes.GAME_MODE,
                    gamemode
            );

            if(gameModeOptional.isPresent()){
                this.enabledGameModes.add(gameModeOptional.get());
            }else{
                //todo send a message to the log
            }
        }
    }

    private void initJobs(){
        for(ConfigurationNode node : ConfigHelper.getNode("jobs").getChildrenMap().values()){

            String jobConfDir = node.getString("");

            if(!node.hasMapChildren()){
                try {
                    ConfigurationNode jobNode =
                            HoconConfigurationLoader.builder().setPath(configDir.getParent().resolve(jobConfDir)).build().load();

                    jobNode.getNode("id").setValue(node.getKey().toString());

                    node = jobNode;
                } catch (IOException e) {
                    logger.warn(TranslationHelper.l("warn.config.could_not_load.job", jobConfDir));
                }
            }

            Job job = new Job(node);

            jobs.put(job.getId(), job);

            logger.info(TranslationHelper.l("info.job.init", job.getId()));
        }
    }

    private void initCommands(){
        Map<List<String>, CommandCallable> childCommands = new HashMap<>();

        final JobCommand abilityStartCommand = new AbilityStartCommand();
        childCommands.put(abilityStartCommand.getCommandAliases(), abilityStartCommand.getCommandSpec());

        final JobCommand addXpCommand = new AddXpCommand();
        childCommands.put(addXpCommand.getCommandAliases(), addXpCommand.getCommandSpec());

        final JobCommand changeJobCommand = new ChangeJobCommand();
        childCommands.put(changeJobCommand.getCommandAliases(), changeJobCommand.getCommandSpec());

        final JobCommand toggleJobStatusCommand = new ToggleJobStatusCommand();
        childCommands.put(toggleJobStatusCommand.getCommandAliases(), toggleJobStatusCommand.getCommandSpec());

        Sponge.getCommandManager().register(
                JobsMain.instance(),
                CommandSpec.builder().children(childCommands).build(),
                ConfigHelper.getNode("commands", "mainCommand").getString("jobs")
        );
    }

    private void initTranslations(){
        Locale.setDefault(
                Locale.forLanguageTag(
                        ConfigHelper.getNode("translation", "defaultLanguage").getString("en")
                )
        );

        logger.info(
                TranslationHelper.s(Locale.ENGLISH, "translation.default.set", Locale.getDefault().toLanguageTag())
        );

        final Locale logLanguage = Locale.forLanguageTag(
                ConfigHelper.getNode("translation", "logLanguage").getString("en")
        );

        TranslationHelper.setLogLanguage(logLanguage);
        logger.info(TranslationHelper.l("translation.log.set", logLanguage.toLanguageTag()));
    }
}
