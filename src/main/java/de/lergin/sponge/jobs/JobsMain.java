package de.lergin.sponge.jobs;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import de.lergin.sponge.jobs.data.jobs.ImmutableJobDataManipulator;
import de.lergin.sponge.jobs.data.jobs.JobData;
import de.lergin.sponge.jobs.data.jobs.JobDataManipulatorBuilder;
import de.lergin.sponge.jobs.job.BreakBlockJob;
import de.lergin.sponge.jobs.job.Job;
import de.lergin.sponge.jobs.job.PlaceBlockJob;
import de.lergin.sponge.jobs.util.ConfigHelper;
import de.lergin.sponge.jobs.util.TranslationHelper;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStoppedEvent;
import org.spongepowered.api.event.world.SaveWorldEvent;
import org.spongepowered.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Plugin(id = "Jobs", name = "Jobs", version = "0.1")
public class JobsMain {
    @Inject
    @DefaultConfig(sharedRoot = true)
    public ConfigurationLoader<CommentedConfigurationNode> configManager;


    @Inject
    private Logger logger;

    public Logger getLogger() {
        return logger;
    }

    private static JobsMain instance;

    public static JobsMain instance() {
        return instance;
    }

    @Listener
    public void gameConstruct(GameConstructionEvent event) {
        instance = this;
    }

    @Listener
    public void onGamePreInitialization(GamePreInitializationEvent event) {
        ConfigHelper.loadConfig();

        //translation setup
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

    @Listener
    public void onGameInitialization(GameInitializationEvent event) {
        //init customData
        Sponge.getDataManager().register(JobData.class, ImmutableJobDataManipulator.class,
                new JobDataManipulatorBuilder());

        //init eventListener


        //init commands


        //init jobs
        for(ConfigurationNode node : ConfigHelper.getNode("jobs").getChildrenMap().values()){
            List<BlockType> blockTypes = new ArrayList<>();

            for(ConfigurationNode blockNode : node.getNode("destroyBlocks").getChildrenList()){
                blockTypes.add(Sponge.getRegistry().getType(
                        CatalogTypes.BLOCK_TYPE,
                        blockNode.getKey().toString()
                ).get());
            }

            Job job = new BreakBlockJob(node.getKey().toString(), node.getNode("name").getString(), blockTypes);

            logger.info(TranslationHelper.l("info.job.init", job.getId()));
        }
    }

    @Listener
    public void onGameStoppedEvent(GameStoppedEvent event){
        ConfigHelper.saveConfig();
    }
}
