package de.lergin.laborus;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import de.lergin.laborus.config.JobActionsTypeSerializer;
import de.lergin.laborus.config.JobBoniTypeSerializer;
import de.lergin.laborus.job.actions.*;
import de.lergin.laborus.job.bonus.*;
import de.lergin.laborus.api.JobService;
import de.lergin.laborus.config.Config;
import de.lergin.laborus.config.TranslationHelper;
import de.lergin.laborus.data.jobs.ImmutableJobDataManipulator;
import de.lergin.laborus.data.jobs.JobData;
import de.lergin.laborus.data.jobs.JobDataManipulatorBuilder;
import de.lergin.laborus.job.Job;
import de.lergin.laborus.job.JobActions;
import de.lergin.laborus.job.JobBoni;
import de.lergin.sponge.laborus.job.bonus.*;
import de.lergin.sponge.laborus.job.actions.*;
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

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
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

        config.base.jobs.forEach(j -> jobs.put(j.getId(), j));

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

        JobService jobService = Sponge.getServiceManager().getRegistration(JobService.class).get().getProvider();

        jobService.registerJobBonus(EconomyReward.class, "economy");
        jobService.registerJobBonus(EpDrop.class, "ep");
        jobService.registerJobBonus(ItemDrop.class, "itemDrop");
        jobService.registerJobBonus(ItemRepair.class, "itemRepair");
        jobService.registerJobBonus(MultiDrop.class, "multiDrop");

        jobService.registerJobAction(BreakBlockJobAction.class, "break");
        jobService.registerJobAction(EntityDamageJobAction.class, "damage");
        jobService.registerJobAction(EntityKillJobAction.class, "kill");
        jobService.registerJobAction(EntityTameJobAction.class, "tame");
        jobService.registerJobAction(InteractJobAction.class, "use");
        jobService.registerJobAction(PlaceBlockJobAction.class, "place");
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) throws IOException, ObjectMappingException {
        config = new Config(this, loader);

        config.reload();

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