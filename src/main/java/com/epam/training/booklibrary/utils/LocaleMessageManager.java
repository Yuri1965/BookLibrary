package com.epam.training.booklibrary.utils;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by URA on 11.10.2015.
 */
public class LocaleMessageManager {
    public static String getMessageValue(String key, Locale locale) {
        String result = ResourceBundle.getBundle("messages", locale).getString(key);

        if (result == null || result.isEmpty()) {
            result = "";
        }

        return result;
    }
}
