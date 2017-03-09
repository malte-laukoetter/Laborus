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
    public ConfigurationLoader<CommentedConfigurationNode> loader;
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
            loader.save(node);
            logger.info("Saved the base!");
        } catch(IOException e) {
            logger.warn("Could not save the base!");
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
            logger.error("Could not load the base file!");
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
