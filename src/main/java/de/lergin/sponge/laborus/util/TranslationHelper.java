package de.lergin.sponge.laborus.util;

import com.google.common.reflect.TypeToken;
import de.lergin.sponge.laborus.JobsMain;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.translation.ResourceBundleTranslation;
import org.spongepowered.api.text.translation.Translatable;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;

/**
 * class for translating messages
 */
public final class TranslationHelper {

    private final static String BUNDLE_NAME = "de.lergin.sponge.laborus.util.Jobs";

    private final static Logger LOGGER = JobsMain.instance().getLogger();

    private static final Function<Locale, ResourceBundle> LOOKUP_FUNC = new Function<Locale, ResourceBundle>() {
        @Nullable
        @Override
        public ResourceBundle apply(Locale input) {
            return ResourceBundle.getBundle(BUNDLE_NAME, input);
        }
    };

    private TranslationHelper() {}

    private static Locale logLanguage = Locale.getDefault();

    /**
     * returns the translation of the key (as text) in the language of the server
     * @param key resourceBundle key
     * @param args replace arguments
     * @return the translated text
     */
    public static Text t(String key, Object... args) {
        return Text.of(s(key, args));
    }

    /**
     * returns the translation of the key (as text) in the given language
     * @param locale the language
     * @param key resourceBundle key
     * @param args replace arguments
     * @return the translated text
     */
    public static Text t(Locale locale, String key, Object... args) {
        return Text.of(s(locale, key, args));
    }

    /**
     * returns the translation of the key in the language of the server
     * @param key resourceBundle key
     * @param args replace arguments
     * @return the translated string
     */
    public static String s(String key, Object... args) {
        return s(Locale.getDefault(), key, args);
    }

    /**
     * returns the translation of the key in the given language
     * @param local the language
     * @param key resourceBundle key
     * @param args replace arguments
     * @return the translated string
     */
    public static String s(Locale local, String key, Object... args) {
        try {
            ArrayList<Object> argsTranslated = new ArrayList<>();

            for(Object arg: args){
                if(arg instanceof Translatable){
                    arg = ((Translatable) arg).getTranslation().get(local);
                }

                argsTranslated.add(arg);
            }


            return new ResourceBundleTranslation(key, LOOKUP_FUNC).get(local, argsTranslated.toArray());
        }catch(MissingFormatArgumentException e){
            // we need to check if the message is our error message so we are not creating a endless loop
            if(key.equals("warn.translation.too_many_arguments")){
                LOGGER.warn(String.format("Translation for \"warn.translation.too_many_arguments\" in language \"%s\" " +
                        "wants too many arguments", local.toLanguageTag()));
            }else{
                LOGGER.warn(l("warn.translation.too_many_arguments", local.toLanguageTag(), key) + " (ERROR CODE: 1)");
            }

            return new ResourceBundleTranslation(key, LOOKUP_FUNC).get(local);
        }
    }

    /**
     * returns the translation of the key (as text) in the language of the player
     * @param player player that's language should be used
     * @param key resourceBundle key
     * @param args replace arguments
     * @return the translated text
     */
    public static Text p(Player player, String key, Object... args){
        if(ConfigHelper.getNode("translation", "usePlayerLanguage").getBoolean()){
            return t(player.getLocale(), key, args);
        }else{
            return t(key, args);
        }

    }

    /**
     * returns the translation of the key (as string) in the language of the player
     * @param player player that's language should be used
     * @param key resourceBundle key
     * @param args replace arguments
     * @return the translated string
     */
    public static String ps(Player player, String key, Object... args){
        if(ConfigHelper.getNode("translation", "usePlayerLanguage").getBoolean()){
            return s(player.getLocale(), key, args);
        }else{
            return s(key, args);
        }

    }

    /**
     * returns the translation of the key in the log language
     * @param key resourceBundle key
     * @param args replace arguments
     * @return the translated string
     */
    public static String l(String key, Object... args) {
        return s(logLanguage, key, args);
    }


    private static Map<String, ConfigurationNode> translationNodes = new HashMap<>();

    public static TextTemplate template(TextTemplate baseTemplate, String lang, String... path){
        try {
            // create a node from the template and replace the content with the stuff from the config so no one can mess
            // with the args and the config is smaller
            ConfigurationNode node = SimpleConfigurationNode.root();
            node.setValue(TypeToken.of(TextTemplate.class), baseTemplate);

            if(translationNodes.containsKey(lang) &&
                    translationNodes.get(lang).getNode((Object[]) path).hasMapChildren()){
                node.getNode("content").setValue(
                        translationNodes.get(lang).getNode((Object[]) path).getValue()
                );
            }else if(translationNodes.containsKey(lang.split("-")[0]) &&
                    translationNodes.get(lang.split("-")[0]).getNode((Object[]) path).hasMapChildren()){
                node.getNode("content").setValue(
                        translationNodes.get(lang.split("-")[0]).getNode((Object[]) path).getValue()
                );
            }else if(translationNodes.containsKey("default") &&
                    translationNodes.get("default").getNode((Object[]) path).hasMapChildren()){
                node.getNode("content").setValue(
                        translationNodes.get("default").getNode((Object[]) path).getValue()
                );
            }

            return node.getValue(TypeToken.of(TextTemplate.class));
        } catch (ObjectMappingException e) {
            JobsMain.instance().getLogger().warn(TranslationHelper.l("message.error_with_load_of_text_template", (Object[]) path));

            return baseTemplate;
        }
    }

    public static void init(){
        for(ConfigurationNode node : ConfigHelper.getNode("messages").getChildrenMap().values()){
            if(node.hasMapChildren()){
                translationNodes.put((String) node.getKey(), node);
                JobsMain.instance().getLogger().info(
                        TranslationHelper.l("info.translation.init", (String) node.getKey())
                );
            }else{
                String path = node.getString("");

                if(!path.equals("")){
                    try {
                        HoconConfigurationLoader loader =
                                HoconConfigurationLoader.builder()
                                        .setPath(JobsMain.instance().configDir.getParent().resolve(path))
                                        .build();

                        translationNodes.put((String) node.getKey(), loader.load());
                        JobsMain.instance().getLogger().info(
                                TranslationHelper.l("info.translation.init", (String) node.getKey())
                        );
                    } catch (IOException e) {
                        JobsMain.instance().getLogger().warn("warn.config.could_not_load.translation", node.getKey());
                    }
                }else{
                    JobsMain.instance().getLogger().warn("warn.config.could_not_load.translation", node.getKey());
                }
            }
        }
    }

    /**
     * sets the language for the logging to the give local
     * @param locale the local of the language
     */
    public static void setLogLanguage(Locale locale){
        logLanguage = locale;
    }
}
