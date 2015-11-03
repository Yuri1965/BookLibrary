package com.epam.training.booklibrary.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class which contains realization of methods for extraction of the localized messages from files of resources
 */
public class LocaleMessageManager {
    /**
     * The method receives the localized message from files of resources
     * @param key message key name in resources
     * @param locale localization (language)
     * @return the localized message from resources
     */
    public static String getMessageValue(String key, Locale locale) {
        String resourceName = "messages_" + (locale.toString()).toLowerCase();
        String result = ResourceBundle.getBundle(resourceName).getString(key);

        if (result == null || result.isEmpty()) {
            result = "";
        }

        return result;
    }
}
