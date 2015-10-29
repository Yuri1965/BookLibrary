package com.epam.training.booklibrary.controllers.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by URA on 29.10.2015.
 */
public class GeneralUtils {
    // получает форматтер для даты в зависмости от локали
    public static synchronized SimpleDateFormat getDateFormatterByLocale(Locale locale, boolean useSecond) {
        SimpleDateFormat dateFormatter;

        if (locale.getDisplayName().equalsIgnoreCase("en_us")) {
            dateFormatter = useSecond ? new SimpleDateFormat("MM/dd/yyyy HH:mm:ss") : new SimpleDateFormat("MM/dd/yyyy HH:mm");
        } else {
            dateFormatter = useSecond ? new SimpleDateFormat("dd.MM.yyyy HH:mm:ss") : new SimpleDateFormat("dd.MM.yyyy HH:mm");
        }

        return dateFormatter;
    }

    // получает форматтер для даты в зависмости от локали, переданной в виде строки
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
