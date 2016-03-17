package de.lergin.sponge.laborus.util;

import de.lergin.sponge.laborus.JobsMain;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.ConfigurationOptions;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * Class for working with the configuration
 */
public final class ConfigHelper {
    private static final ConfigurationLoader<CommentedConfigurationNode> LOADER =
            JobsMain.instance().configManager;

    private static final Logger LOGGER = JobsMain.instance().getLogger();

    private static ConfigurationNode rootNode = LOADER.createEmptyNode(ConfigurationOptions.defaults());

    /**
     * loads the config file
     */
    public static void loadConfig(){
        try {
            rootNode = LOADER.load();
        } catch(IOException e) {
            LOGGER.warn(TranslationHelper.l("warn.config.could_not_load"));
        }

        final ConfigurationLoader<CommentedConfigurationNode> DEFAULT_LOADER =
                HoconConfigurationLoader.builder().setURL(ConfigHelper.class.getResource("defaultConfig.conf")).build();

        try {
            rootNode.mergeValuesFrom(DEFAULT_LOADER.load());
        } catch (IOException e1) {
            LOGGER.error(TranslationHelper.l("error.config.could_not_load_default"));
        }
    }

    /**
     * saves the config
     */
    public static void saveConfig(){
        try {
            LOADER.save(rootNode);
            LOGGER.info(TranslationHelper.l("info.config.saved"));
        } catch(IOException e) {
            LOGGER.warn(TranslationHelper.l("warn.config.could_not_save"));
        }
    }

    /**
     * returns the configuration node of the given path
     * @param objects the path to the node
     * @return the configuration node
     */
    public static ConfigurationNode getNode(Object... objects){
        return rootNode.getNode(objects);
    }
}
