package de.lergin.sponge.laborus.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import de.lergin.sponge.laborus.Laborus;
import de.lergin.sponge.laborus.job.Job;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class BaseConfig {
    @Setting(value = "commands", comment = "settings for the commands")
    public CommandsConfig commandsConfig = new CommandsConfig();

    @Setting(value = "enabledGamemodes", comment = "the gamemodes the jobsystem is activated in")
    public List<GameMode> enabledGamemodes = ImmutableList.of(GameModes.ADVENTURE, GameModes.SURVIVAL);

    @Setting(value = "maxSelectedJobs", comment = "maximal amount of jobs selected at the same time")
    public int maxSelectedJobs = 1;

    @Setting(value = "xpWithoutJob", comment = "the number all ep is multiplied by if a job is not selected")
    public double xpWithoutJob = 0.5;

    @Setting(value = "antiReplaceActive", comment = "activated the anti replace farming system")
    public boolean useAntiReplace = false;

    @Setting(value = "antiReplaceTime", comment = "the time after that a location is removed from the anit replace farming list")
    public int antiReplaceTime = 48;

    @Setting(value = "level", comment = "the amount of ep needed for each level")
    public List<Long> levels = defaultLevels();

    @Setting(value = "fallbackLanguage", comment = "the language used if the language of the player is not available")
    public String fallbackLanguage = "en";

    @Setting(value = "translations", comment = "the localised messages of the plugin")
    private Map<String, TranslationConfig> mainFileTranslationConfig = ImmutableMap.of("en", new TranslationConfig());

    @Setting(value = "translationFiles", comment = "a map of languages and files that each have a translation configuration for one language. The base path is this folder")
    private Map<String, String> translationFiles = ImmutableMap.of();
    private Map<String, ConfigurationLoader> translationLoaders;

    public Map<String, TranslationConfig> translationConfig;

    @Setting(value = "jobs", comment = "the jobs")
    private List<Job> mainFileJobs = ImmutableList.of();

    @Setting(value = "jobFiles", comment = "a list of files that each have a job configuration. The base path is this folder")
    private List<String> jobFiles = ImmutableList.of();
    private Map<String, ConfigurationLoader> jobLoaders;

    public List<Job> jobs;

    public BaseConfig() {}

    public void initJobs(){
        jobs.addAll(mainFileJobs);
        jobs.forEach(Job::initJob);
    }

    public void loadJobFiles(){
        jobs = new ArrayList<>();
        jobLoaders = new HashMap<>();

        jobFiles.forEach(file -> {
            Path path = Laborus.instance().configDir.getParent().resolve(file);

            ConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(path).build();

            try {
                Job job = loader.load().getValue(TypeToken.of(Job.class));

                if(job == null){
                    Laborus.instance().getLogger().warn("Error while loading JobFile {0}", file);
                }else{
                    jobLoaders.put(job.getId(), loader);

                    jobs.add(job);
                }
            } catch (IOException | ObjectMappingException e) {
                Laborus.instance().getLogger().warn("Error while loading JobFile {0}", file);

                e.printStackTrace();
            }
        });
    }

    public void loadTranslationFiles(){
        translationConfig = new HashMap<>();
        translationLoaders = new HashMap<>();

        translationFiles.forEach((language, file) -> {
            Path path = Laborus.instance().configDir.getParent().resolve(file);

            ConfigurationLoader loader = HoconConfigurationLoader.builder().setPath(path).build();

            try {
                ConfigurationNode node = loader.load();

                TranslationConfig translation = node.getValue(TypeToken.of(TranslationConfig.class));

                if(translation == null){
                    Laborus.instance().getLogger().warn("Error while loading translation file {0}", file);
                }else{
                    translationLoaders.put(language, loader);

                    translation.initJobSpecificMessages(node);

                    translationConfig.put(language, translation);
                }
            } catch (IOException | ObjectMappingException e) {
                Laborus.instance().getLogger().warn("Error while loading translation file {0}", file);

                e.printStackTrace();
            }
        });

        mainFileTranslationConfig.forEach((lang, config)-> {
            translationConfig.put(lang, config);

            config.initJobSpecificMessages(Laborus.instance().config.node.getNode("translations", lang));
        });
    }

    public void saveJobFiles(){
        jobs.forEach(job -> jobLoaders.forEach((id, loader) -> {
            if(job.getId().equals(id)){
                ConfigurationNode node = loader.createEmptyNode();
                try {
                    node.setValue(TypeToken.of(Job.class), job);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }

                try {
                    loader.save(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }

    public void saveTranslationFiles(){
        translationConfig.forEach((lang, translationConfig) -> translationLoaders.forEach((lang2, loader) -> {
            if(lang.equals(lang2)){
                ConfigurationNode node = loader.createEmptyNode();
                try {
                    node.setValue(TypeToken.of(TranslationConfig.class), translationConfig);
                    translationConfig.saveJobSpecificMessages(node);
                } catch (ObjectMappingException e) {
                    e.printStackTrace();
                }

                try {
                    loader.save(node);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));

        mainFileTranslationConfig.forEach((key, config) ->
                config.saveJobSpecificMessages(Laborus.instance().config.node.getNode("translations", key)));
    }

    private static List<Long> defaultLevels(){
        List<Long> levels = new ArrayList<>();

        levels.add(0L);
        levels.add(50L);
        levels.add(100L);
        levels.add(175L);
        levels.add(250L);
        levels.add(400L);
        levels.add(600L);
        levels.add(750L);
        levels.add(1000L);
        levels.add(1500L);
        levels.add(2000L);
        levels.add(3000L);
        levels.add(4500L);
        levels.add(6000L);
        levels.add(8000L);
        levels.add(10000L);
        levels.add(12500L);
        levels.add(15000L);
        levels.add(18000L);
        levels.add(21500L);
        levels.add(25000L);
        levels.add(29000L);
        levels.add(33000L);
        levels.add(37000L);
        levels.add(42000L);
        levels.add(48000L);
        levels.add(55000L);
        levels.add(62000L);
        levels.add(68000L);
        levels.add(75000L);
        levels.add(83000L);
        levels.add(91000L);
        levels.add(100000L);
        levels.add(110000L);
        levels.add(120000L);
        levels.add(130000L);
        levels.add(140000L);
        levels.add(150000L);
        levels.add(160000L);
        levels.add(170000L);
        levels.add(180000L);
        levels.add(190000L);
        levels.add(200000L);
        levels.add(210000L);
        levels.add(220000L);
        levels.add(230000L);
        levels.add(240000L);
        levels.add(250000L);
        levels.add(260000L);
        levels.add(270000L);

        return levels;
    }
}
