package com.epam.training.booklibrary.controllers.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * The class contains methods for data acquisition in the necessary localization
 */
public class GeneralUtils {
    /**
     * The method receives a date format mask in the necessary localization
     * @param locale localization (type of Locale)
     * @param useSecond flag of existence of seconds (if true, a format of date contains seconds)
     * @return формат даты и времени (type of SimpleDateFormat)
     */
    public static synchronized SimpleDateFormat getDateFormatterByLocale(Locale locale, boolean useSecond) {
        SimpleDateFormat dateFormatter;

        if (locale.getDisplayName().equalsIgnoreCase("en_us")) {
            dateFormatter = useSecond ? new SimpleDateFormat("MM/dd/yyyy HH:mm:ss") : new SimpleDateFormat("MM/dd/yyyy HH:mm");
        } else {
            dateFormatter = useSecond ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss") : new SimpleDateFormat("dd.MM.yyyy HH:mm");
        }

        return dateFormatter;
    }

    /**
     * The method receives a date format mask in the necessary localization
     * @param locale String localization
     * @param useSecond flag of existence of seconds (if true, a format of date contains seconds)
     * @return format of date and time (type of SimpleDateFormat)
     */
    public static synchronized SimpleDateFormat getDateFormatterByLocale(String locale, boolean useSecond) {
        SimpleDateFormat dateFormatter;

        if (locale.equalsIgnoreCase("en_us")) {
            dateFormatter = useSecond ? new SimpleDateFormat("MM/dd/yyyy HH:mm:ss") : new SimpleDateFormat("MM/dd/yyyy HH:mm");
        } else {
            dateFormatter = useSecond ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss") : new SimpleDateFormat("dd.MM.yyyy HH:mm");
        }

        return dateFormatter;
    }


}
