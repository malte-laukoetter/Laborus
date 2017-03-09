package de.lergin.sponge.laborus.config;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.lergin.sponge.laborus.job.Job;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.gamemode.GameMode;
import org.spongepowered.api.entity.living.player.gamemode.GameModes;

import java.util.ArrayList;
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

    @Setting(value = "translations", comment = "the localised messages of the plugin")
    public Map<String, TranslationConfig> translationConfig = ImmutableMap.of("en", new TranslationConfig());

    @Setting(value = "jobs", comment = "the jobs")
    public List<Job> jobs = ImmutableList.of();

    public BaseConfig() {}

    public void initJobs(){
        jobs.forEach(Job::initJob);
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
