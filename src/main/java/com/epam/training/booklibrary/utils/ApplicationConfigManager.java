package com.epam.training.booklibrary.utils;

import java.util.ResourceBundle;

/**
 * Created by URA on 11.10.2015.
 */
public class ApplicationConfigManager {
    public static String getConfigValue(String key, String defaultValue) {
        String result = ResourceBundle.getBundle("appconfig").getString(key);

        if (result == null || result.isEmpty()) {
            result = defaultValue;
        }

        return result;
    }
}
