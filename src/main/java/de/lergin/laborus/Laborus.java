package de.lergin.laborus;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import de.lergin.laborus.config.*;
import de.lergin.laborus.job.JobAbilities;
import de.lergin.laborus.job.ability.EffectAbility;
import de.lergin.laborus.job.actions.*;
import de.lergin.laborus.job.bonus.*;
import de.lergin.laborus.api.JobService;
import de.lergin.laborus.data.jobs.ImmutableJobDataManipulator;
import de.lergin.laborus.data.jobs.JobData;
import de.lergin.laborus.data.jobs.JobDataManipulatorBuilder;
import de.lergin.laborus.job.Job;
import de.lergin.laborus.job.JobActions;
import de.lergin.laborus.job.JobBoni;
import de.lergin.laborus.util.AntiReplaceFarming;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Plugin(id = "laborus", name = "Laborus", version = "@version@", description = "a job plugin", authors = {"Lergin"})
public class Laborus {
    @Inject
    @DefaultConfig(sharedRoot = false)
    public Path configDir;

    @Inject
    private Metrics metrics;

    @Inject
    private Logger logger;

    @Inject
    public PluginContainer pluginContainer;

    public Logger getLogger() {
        return logger;
    }

    private static Laborus instance;

    public static Laborus instance() {
        return instance;
    }

    public Path getConfigDir() {
        return configDir;
    }

    public Map<String, Job> getJobs() {
        Map<String, Job> jobs = new HashMap<>();

        if(config != null && config.base != null && config.base.jobs != null){
            config.base.jobs.forEach(j -> jobs.put(j.getId(), j));
        }


        return jobs;
    }

    public TranslationHelper translationHelper;

    public Config config;

    @Inject
    @DefaultConfig(sharedRoot = false)
    private
    ConfigurationLoader<CommentedConfigurationNode> loader;

    @Listener
    public void gameConstruct(GameConstructionEvent event) {
        instance = this;
        Sponge.getServiceManager().setProvider(this, JobService.class, new JobService());
    }

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(JobBoni.class), new JobBoniTypeSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(JobActions.class), new JobActionsTypeSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(JobAbilities.class), new JobAbilitiesTypeSerializer());

        JobService jobService = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();

        jobService.registerJobBonus(EconomyJobBonus.class, "economy");
        jobService.registerJobBonus(EpDropJobBonus.class, "ep");
        jobService.registerJobBonus(ItemDropJobBonus.class, "itemDrop");
        jobService.registerJobBonus(ItemRepairJobBonus.class, "itemRepair");
        jobService.registerJobBonus(MultiDropJobBonus.class, "multiDrop");
        jobService.registerJobBonus(CommandExecutionJobBonus.class, "command");

        jobService.registerJobAction(BreakBlockJobAction.class, "break");
        jobService.registerJobAction(EntityDamageJobAction.class, "damage");
        jobService.registerJobAction(EntityKillJobAction.class, "kill");
        jobService.registerJobAction(EntityTameJobAction.class, "tame");
        jobService.registerJobAction(InteractJobAction.class, "use");
        jobService.registerJobAction(PlaceBlockJobAction.class, "place");
        jobService.registerJobAction(FishingJobAction.class, "fishing");

        jobService.registerJobAbility(EffectAbility.class, "effect");
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) throws IOException, ObjectMappingException {
        config = new Config(this, loader);

        config.load();
        config.saveWithBackup();

        AntiReplaceFarming.setUseAntiReplace(config.base.useAntiReplace);

        config.base.initJobs();
        config.base.commandsConfig.registerCommands(Laborus.instance());

        translationHelper =
                new TranslationHelper(config.base.translationConfig, Locale.forLanguageTag(config.base.fallbackLanguage));


        //setup the dataBase for the AntiReplaceFarming thing
        AntiReplaceFarming.setupDataBase();

        //init customData
        Sponge.getDataManager().register(JobData.class, ImmutableJobDataManipulator.class,
                new JobDataManipulatorBuilder());
    }

    @Listener
    public void onGameServerStopping(GameStoppingServerEvent event) {
        config.save();
    }
}
