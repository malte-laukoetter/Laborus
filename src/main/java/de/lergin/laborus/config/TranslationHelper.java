package de.lergin.laborus.config;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.text.TextTemplate;

import java.util.*;

public class TranslationHelper {
    private final Locale fallbackLocale;
    private final Collection<Locale> locales;
    private final Map<String, TranslationConfig> translations;

    public TextTemplate get(TranslationKeys key, CommandSource src){
        return get(key, src.getLocale(), "");
    }

    public TextTemplate get(TranslationKeys key, CommandSource src, String additionalId){
        return get(key, src.getLocale(), additionalId);
    }

    public TextTemplate get(TranslationKeys key, Locale locale){
        return get(key, locale,"");
    }

    public TextTemplate get(TranslationKeys key, Locale locale, String additionalId){
        Locale matchingLocale = getMatchingLocal(locale);

        if(matchingLocale == null){
            matchingLocale = getMatchingLocal(fallbackLocale);
            if(matchingLocale == null){
                matchingLocale = getMatchingLocal(Locale.getDefault());
                if(matchingLocale == null){
                    matchingLocale = locales.iterator().next();
                    if(matchingLocale == null) {
                        return TextTemplate.of("ERROR NO TRANSLATIONS");
                    }
                }
            }
        }

        return translations.get(matchingLocale.toLanguageTag()).get(key, additionalId);
    }

    private Locale getMatchingLocal(Locale locale){
        Locale.LanguageRange range = new Locale.LanguageRange(locale.toLanguageTag());

        return Locale.lookup(Collections.singletonList(range), locales);
    }

    public TranslationHelper(Map<String, TranslationConfig> translations, Locale fallbackLocale) {
        Collection<Locale> locales = new ArrayList<>();

        translations.keySet().forEach(k -> locales.add(Locale.forLanguageTag(k)));

        this.fallbackLocale = fallbackLocale;
        this.locales = locales;
        this.translations = translations;
    }
}
