package com.epam.training.booklibrary.controllers.utils;

import com.epam.training.booklibrary.utils.ApplicationConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by URA on 11.10.2015.
 */
public class RequestParamValidator {
    public static final String SYMBOLS_LOGIN_PATTERN = "([a-zA-Z0-9_-]*)";
    public static final String MIN_LENGTH_LOGIN = "5";
    public static final String MAX_LENGTH_LOGIN = "50";

    public static final String SYMBOLS_PASSWORD_PATTERN = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,32}";
    public static final String MIN_LENGTH_PASSWORD = "6";
    public static final String MAX_LENGTH_PASSWORD = "32";

    public static final String SYMBOLS_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,255})$";
    public static final int MIN_LENGTH_EMAIL = 0;
    public static final String MAX_LENGTH_EMAIL = "255";

    public static final String SYMBOLS_FIRST_NAME_PATTERN = "([a-яA-Я-\\ ]*)";
    public static final int MIN_LENGTH_FIRST_NAME = 0;
    public static final String MAX_LENGTH_FIRST_NAME = "128";

    public static final String SYMBOLS_LAST_NAME_PATTERN = "([a-яA-Я-\\ ]*)";
    public static final int MIN_LENGTH_LAST_NAME = 0;
    public static final String MAX_LENGTH_LAST_NAME = "128";

    public static final String SYMBOLS_NUMBERS_PATTERN = "([0-9]*)";
    public static final String SYMBOLS_TYPE_SEARCH_PATTERN = "([1-2])";

    public static final String MAX_LENGTH_BOOK_NAME = "128";
    public static final int MIN_LENGTH_BOOK_NAME = 1;
    public static final String MAX_LENGTH_BOOK_ISBN = "50";
    public static final int MIN_LENGTH_BOOK_ISBN = 1;
    public static final String MAX_LENGTH_BOOK_DESCRIPTION = "1024";
    public static final int MIN_LENGTH_BOOK_DESCRIPTION = 1;
    public static final String MIN_VALUE_BOOK_PUBLISH_YEAR = "1000";
    public static final String MIN_VALUE_BOOK_NUMBER_COPIES = "1";
    public static final String MAX_VALUE_BOOK_NUMBER_COPIES = "100";

    public static synchronized boolean checkSymbolsTypeSearch(String typeSearch) {
        String checkPattern = SYMBOLS_TYPE_SEARCH_PATTERN;
        return validateByPattern(typeSearch, checkPattern);
    }

    public static synchronized boolean checkSymbolsNumbers(String number) {
        String checkPattern = SYMBOLS_NUMBERS_PATTERN;
        return validateByPattern(number, checkPattern);
    }

    public static synchronized boolean checkSymbolsLogin(String login) {
        String checkPattern = ApplicationConfigManager.getConfigValue("loginSymbols", SYMBOLS_LOGIN_PATTERN);
        return validateByPattern(login, checkPattern);
    }

    public static synchronized boolean checkLengthLogin(String login) {
        int minLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("loginMinLength", MIN_LENGTH_LOGIN));
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("loginMaxLength", MAX_LENGTH_LOGIN));
        return validateByLength(login, minLength, maxLength);
    }

    public static synchronized boolean checkSymbolsPassword(String password) {
        String checkPattern = ApplicationConfigManager.getConfigValue("passwordSymbols", SYMBOLS_PASSWORD_PATTERN);
        return validateByPattern(password, checkPattern);
    }

    public static synchronized boolean checkLengthPassword(String password) {
        int minLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("passwordMinLength", MIN_LENGTH_PASSWORD));
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("passwordMaxLength", MAX_LENGTH_PASSWORD));
        return validateByLength(password, minLength, maxLength);
    }

    public static synchronized boolean checkSymbolsEmail(String email) {
        String checkPattern = ApplicationConfigManager.getConfigValue("emailSymbols", SYMBOLS_EMAIL_PATTERN);
        return validateByPattern(email, checkPattern);
    }

    public static synchronized boolean checkLengthEmail(String email) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("emailMaxLength", MAX_LENGTH_EMAIL));
        return validateByLength(email, MIN_LENGTH_EMAIL, maxLength);
    }

    public static synchronized boolean checkSymbolsFirstName(String firstName) {
        String checkPattern = ApplicationConfigManager.getConfigValue("firstNameSymbols", SYMBOLS_FIRST_NAME_PATTERN);
        return validateByPattern(firstName, checkPattern);
    }

    public static synchronized boolean checkLengthFirstName(String firstName) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("firstNameMaxLength", MAX_LENGTH_FIRST_NAME));
        return validateByLength(firstName, MIN_LENGTH_FIRST_NAME, maxLength);
    }

    public static synchronized boolean checkSymbolsLastName(String lastName) {
        String checkPattern = ApplicationConfigManager.getConfigValue("lastNameSymbols", SYMBOLS_LAST_NAME_PATTERN);
        return validateByPattern(lastName, checkPattern);
    }

    public static synchronized boolean checkLengthLastName(String lastName) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("lastNameMaxLength", MAX_LENGTH_LAST_NAME));
        return validateByLength(lastName, MIN_LENGTH_LAST_NAME, maxLength);
    }

    public static synchronized boolean checkLengthBookName(String bookName) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookNameMaxLength", MAX_LENGTH_BOOK_NAME));
        return validateByLength(bookName, MIN_LENGTH_BOOK_NAME, maxLength);
    }

    public static synchronized boolean checkLengthBookISBN(String bookISBN) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookISBNMaxLength", MAX_LENGTH_BOOK_ISBN));
        return validateByLength(bookISBN, MIN_LENGTH_BOOK_ISBN, maxLength);
    }

    public static synchronized boolean checkLengthBookDescription(String bookDescription) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookDescriptionMaxLength", MAX_LENGTH_BOOK_DESCRIPTION));
        return validateByLength(bookDescription, MIN_LENGTH_BOOK_DESCRIPTION, maxLength);
    }

    public static synchronized boolean checkValueBookPublishYear(int bookPublishYear, int maxValue) {
        int minValue = Integer.valueOf(ApplicationConfigManager.getConfigValue("publishYearMinValue", MIN_VALUE_BOOK_PUBLISH_YEAR));
        return validateByIntValue(bookPublishYear, minValue, maxValue);
    }

    public static synchronized boolean checkValueBookNumberCopies(int bookNumberCopies, int maxValue) {
        int minValue = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookNumberCopiesMinValue", MIN_VALUE_BOOK_NUMBER_COPIES));
        return validateByIntValue(bookNumberCopies, minValue, maxValue);
    }

    // проверка по шаблону
    private static boolean validateByPattern(final String stringValue, String stringPattern){
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(stringValue);

        return matcher.matches();
    }

    // проверка по размеру
    private static boolean validateByLength(final String stringValue, int minLength, int maxLength){
        if (minLength > 0 && stringValue.length() < minLength) {
            return false;
        }

        if (maxLength > 0 && stringValue.length() > maxLength) {
            return false;
        }

        return true;
    }

    // проверка по значению целого числа
    private static boolean validateByIntValue(final int intValue, int minValue, int maxValue){
        if (minValue > 0 && intValue < minValue) {
            return false;
        }

        if (maxValue > 0 && intValue > maxValue) {
            return false;
        }

        return true;
    }

}
