package com.epam.training.booklibrary.controllers.utils;

import com.epam.training.booklibrary.utils.ApplicationConfigManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The class contains methods for check of parameters of request for a correctness
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

    public static final String MAX_LENGTH_BOOK_NAME = "128";
    public static final int MIN_LENGTH_BOOK_NAME = 1;
    public static final String MAX_LENGTH_BOOK_ISBN = "50";
    public static final int MIN_LENGTH_BOOK_ISBN = 1;
    public static final String MAX_LENGTH_BOOK_DESCRIPTION = "1024";
    public static final int MIN_LENGTH_BOOK_DESCRIPTION = 1;
    public static final String MIN_VALUE_BOOK_PUBLISH_YEAR = "1000";
    public static final String MIN_VALUE_BOOK_NUMBER_COPIES = "1";
    public static final String MAX_VALUE_BOOK_NUMBER_COPIES = "100";

    /**
     * Method of check of a line on existence only of the whole figures
     * @param number value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkSymbolsNumbers(String number) {
        String checkPattern = SYMBOLS_NUMBERS_PATTERN;
        return validateByPattern(number, checkPattern);
    }

    /**
     * Method of check of Login of the user on existence of obligatory symbols
     * @param login value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkSymbolsLogin(String login) {
        String checkPattern = ApplicationConfigManager.getConfigValue("loginSymbols", SYMBOLS_LOGIN_PATTERN);
        return validateByPattern(login, checkPattern);
    }

    /**
     * Method of check of Login of the user at the minimum length
     * @param login value of a line for check
     * @return result of check
    */
    public static synchronized boolean checkLengthLogin(String login) {
        int minLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("loginMinLength", MIN_LENGTH_LOGIN));
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("loginMaxLength", MAX_LENGTH_LOGIN));
        return validateByLength(login, minLength, maxLength);
    }

    /**
     * Method of check of Password of the user on existence of obligatory symbols
     * @param password value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkSymbolsPassword(String password) {
        String checkPattern = ApplicationConfigManager.getConfigValue("passwordSymbols", SYMBOLS_PASSWORD_PATTERN);
        return validateByPattern(password, checkPattern);
    }

    /**
     * Method of check of Password of the user at the minimum length
     * @param password value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkLengthPassword(String password) {
        int minLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("passwordMinLength", MIN_LENGTH_PASSWORD));
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("passwordMaxLength", MAX_LENGTH_PASSWORD));
        return validateByLength(password, minLength, maxLength);
    }

    /**
     * Method of check of E-mail of the user on existence of correct symbols
     * @param email value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkSymbolsEmail(String email) {
        String checkPattern = ApplicationConfigManager.getConfigValue("emailSymbols", SYMBOLS_EMAIL_PATTERN);
        return validateByPattern(email, checkPattern);
    }

    /**
     * Method of check of E-mail of the user at the maximum length
     * @param email value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkLengthEmail(String email) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("emailMaxLength", MAX_LENGTH_EMAIL));
        return validateByLength(email, MIN_LENGTH_EMAIL, maxLength);
    }

    /**
     * Method of check of first name of the user on existence of correct symbols
     * @param firstName value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkSymbolsFirstName(String firstName) {
        String checkPattern = ApplicationConfigManager.getConfigValue("firstNameSymbols", SYMBOLS_FIRST_NAME_PATTERN);
        return validateByPattern(firstName, checkPattern);
    }

    /**
     * Method of check of first name of the user at the maximum length
     * @param firstName value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkLengthFirstName(String firstName) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("firstNameMaxLength", MAX_LENGTH_FIRST_NAME));
        return validateByLength(firstName, MIN_LENGTH_FIRST_NAME, maxLength);
    }

    /**
     * Method of check of last name of the user on existence of correct symbols
     * @param lastName value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkSymbolsLastName(String lastName) {
        String checkPattern = ApplicationConfigManager.getConfigValue("lastNameSymbols", SYMBOLS_LAST_NAME_PATTERN);
        return validateByPattern(lastName, checkPattern);
    }

    /**
     * Method of check of last name of the user at the maximum length
     * @param lastName value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkLengthLastName(String lastName) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("lastNameMaxLength", MAX_LENGTH_LAST_NAME));
        return validateByLength(lastName, MIN_LENGTH_LAST_NAME, maxLength);
    }

    /**
     * Method of verification of the name of the book at the maximum length
     * @param bookName value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkLengthBookName(String bookName) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookNameMaxLength", MAX_LENGTH_BOOK_NAME));
        return validateByLength(bookName, MIN_LENGTH_BOOK_NAME, maxLength);
    }

    /**
     * Method of verification of the ISBN code of the book at the maximum length
     * @param bookISBN value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkLengthBookISBN(String bookISBN) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookISBNMaxLength", MAX_LENGTH_BOOK_ISBN));
        return validateByLength(bookISBN, MIN_LENGTH_BOOK_ISBN, maxLength);
    }

    /**
     * Method of verification of the description of the book at the maximum length
     * @param bookDescription value of a line for check
     * @return result of check
     */
    public static synchronized boolean checkLengthBookDescription(String bookDescription) {
        int maxLength = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookDescriptionMaxLength", MAX_LENGTH_BOOK_DESCRIPTION));
        return validateByLength(bookDescription, MIN_LENGTH_BOOK_DESCRIPTION, maxLength);
    }

    /**
     * Method of check of year of the edition of the book on the minimum and maximum value
     * @param bookPublishYear int value for check
     * @param maxValue int peak value
     * @return result of check
     */
    public static synchronized boolean checkValueBookPublishYear(int bookPublishYear, int maxValue) {
        int minValue = Integer.valueOf(ApplicationConfigManager.getConfigValue("publishYearMinValue", MIN_VALUE_BOOK_PUBLISH_YEAR));
        return validateByIntValue(bookPublishYear, minValue, maxValue);
    }

    /**
     * Method of check of number of copies of the book on the minimum and maximum value
     * @param bookNumberCopies int value for check
     * @param maxValue int peak value
     * @return result of check
     */
    public static synchronized boolean checkValueBookNumberCopies(int bookNumberCopies, int maxValue) {
        int minValue = Integer.valueOf(ApplicationConfigManager.getConfigValue("bookNumberCopiesMinValue", MIN_VALUE_BOOK_NUMBER_COPIES));
        return validateByIntValue(bookNumberCopies, minValue, maxValue);
    }

    /**
     * Method of check of value on the transferred template of symbols
     * @param stringValue value for check
     * @param stringPattern template for check
     * @return result of check
     */
    private static boolean validateByPattern(final String stringValue, String stringPattern){
        Pattern pattern = Pattern.compile(stringPattern);
        Matcher matcher = pattern.matcher(stringValue);

        return matcher.matches();
    }

    /**
     * Method of check of value at the maximum and minimum length
     * @param stringValue value for check
     * @param minLength int minimum value
     * @param maxLength int maximum value
     * @return result of check
     */
    private static boolean validateByLength(final String stringValue, int minLength, int maxLength){
        if (minLength > 0 && stringValue.length() < minLength) {
            return false;
        }

        if (maxLength > 0 && stringValue.length() > maxLength) {
            return false;
        }

        return true;
    }

    /**
     * Method of check whole value on a maximum and a minimum
     * @param intValue int value for check
     * @param minValue int minimum value
     * @param maxValue int maximum value
     * @return result of check
     */
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
