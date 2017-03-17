package de.lergin.sponge.laborus;

import com.google.inject.Inject;
import de.lergin.sponge.laborus.config.Config;
import de.lergin.sponge.laborus.config.TranslationHelper;
import de.lergin.sponge.laborus.data.jobs.ImmutableJobDataManipulator;
import de.lergin.sponge.laborus.data.jobs.JobData;
import de.lergin.sponge.laborus.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.laborus.job.Job;
import de.lergin.sponge.laborus.util.AntiReplaceFarming;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
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

@Plugin(id = "laborus", name = "Laborus", version = "1.3.0", description = "a job plugin", authors = {"Lergin"})
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
    }

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        config = new Config(this, loader);

        config.reload();
    }

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
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
