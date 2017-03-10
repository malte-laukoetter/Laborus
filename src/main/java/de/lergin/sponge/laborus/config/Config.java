package de.lergin.sponge.laborus.config;

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.laborus.Laborus;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.function.Supplier;

public class Config {
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private Logger logger;
    private ConfigurationNode node;

    public BaseConfig base;

    public Config(Laborus plugin, ConfigurationLoader<CommentedConfigurationNode> loader){
        this.loader = loader;
        this.logger = plugin.getLogger();
    }

    /**
     * saves the configuration of the plugin
     */
    public void save() {
        try {
            base.saveJobFiles();
            base.saveTranslationFiles();

            node.setValue(TypeToken.of(BaseConfig.class), base);
            loader.save(node);
            logger.info("Saved the config!");
        } catch(IOException e) {
            logger.warn("Could not save the config!");
        } catch (ObjectMappingException e) {
            e.printStackTrace();
            logger.warn("Could not save the config!");
        }
    }

    /**
     * loads the configuration of the plugin
     * @throws IOException
     * @throws ObjectMappingException
     */
    public void load() throws IOException, ObjectMappingException {
        try {
            node = loader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("Could not load the config file!");
        }

        loadConfig();
    }

    public void loadConfig() {
        try {
            if(node == null){
                base = new BaseConfig();
            }else{
                base = node.getValue(TypeToken.of(BaseConfig.class), (Supplier<BaseConfig>) BaseConfig::new);
            }

            base.loadTranslationFiles();
            base.loadJobFiles();
        } catch (ObjectMappingException e) {
            e.printStackTrace();
        }
    }

    /**
     * reloads the configuration of the server
     *
     * @throws IOException
     * @throws ObjectMappingException
     */
    public void reload() throws IOException, ObjectMappingException {
        load();
        save();
    }
}
