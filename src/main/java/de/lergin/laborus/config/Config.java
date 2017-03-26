package de.lergin.laborus.config;

import com.google.common.reflect.TypeToken;
import de.lergin.laborus.Laborus;
import de.lergin.laborus.util.AntiReplaceFarming;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Supplier;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Config {
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private Path confDir;
    private Logger logger;


    public ConfigurationNode node;

    public BaseConfig base;

    public Config(Laborus plugin, ConfigurationLoader<CommentedConfigurationNode> loader){
        this.loader = loader;
        this.logger = plugin.getLogger();
        this.confDir= plugin.getConfigDir().getParent();
    }

    /**
     *
     */
    public void saveWithBackup() throws IOException {
        File[] files = confDir.toFile().listFiles();

        if(files != null){
            for (File file : files) {
                if(file.getName().endsWith(".conf")){
                    Files.copy(file.toPath(), confDir.resolve(file.getName().concat(".old")), REPLACE_EXISTING);
                }
            }
        }

        save();
    }

    /**
     * saves the configuration of the plugin
     */
    public void save() {
        try {
            if(Objects.equals(Laborus.instance().pluginContainer.getVersion().orElse("unknown"), base.version)){
                saveCurrentVersion();
            }else{
                if(base.version.contains("1.4.")){
                    saveCurrentVersion();
                }else{
                    saveCurrentVersion();
                }
            }

        } catch(IOException | ObjectMappingException e) {
            logger.warn("Could not save the config!");
            logger.warn(e.getLocalizedMessage());
        }
    }

    private void saveCurrentVersion() throws ObjectMappingException, IOException {
        base.saveJobFiles();
        base.saveTranslationFiles();
        base.version = Laborus.instance().pluginContainer.getVersion().orElse("unknown");

        node.setValue(TypeToken.of(BaseConfig.class), base);

     /*   confDir.iterator().forEachRemaining(path->{
            if(path.endsWith(".conf")){
                System.out.println(path);
                path.toFile().renameTo(path.resolve(".old").toFile());
            }
        });
*/

        loader.save(node);
        logger.info("Saved the config!");
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
            logger.warn("Could not load the config!");
            logger.warn(e.getLocalizedMessage());
        }
    }

    /**
     * reloads the configuration of the server
     *
     * @throws IOException
     * @throws ObjectMappingException
     */
    public void reload() throws IOException, ObjectMappingException {
        logger.info("Started reloading the config!");

        Sponge.getCommandManager().getOwnedBy(Laborus.instance()).forEach(Sponge.getCommandManager()::removeMapping);
        Laborus.instance().getJobs().forEach((key, job)-> job.unloadJob());

        load();
        save();

        AntiReplaceFarming.setUseAntiReplace(base.useAntiReplace);

        base.initJobs();
        base.commandsConfig.registerCommands(Laborus.instance());

        Laborus.instance().translationHelper =
                new TranslationHelper(base.translationConfig, Locale.forLanguageTag(base.fallbackLanguage));
        logger.info("Finished reloading the config!");
    }
}
