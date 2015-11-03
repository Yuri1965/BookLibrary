package com.epam.training.booklibrary.utils;

import java.util.ResourceBundle;

/**
 * The class contains realization of methods for work with the configuration appconfig.properties application file
 */
public class ApplicationConfigManager {
    /**
     * The method returns value of parameter of the application from the file of a configuration on his key name
     * @param key application parameter key name
     * @param defaultValue value of parameter by default if it wasn't found in the configuration file
     * @return value of parameter of the application
     */
    public static String getConfigValue(String key, String defaultValue) {
        String result = ResourceBundle.getBundle("appconfig").getString(key);

        if (result == null || result.isEmpty()) {
            result = defaultValue;
        }

        return result;
    }
}
