package com.epam.training.booklibrary.exceptions;

/**
 * Class factory for generation of own exceptions in system
 */
public class MainExceptions {
    /**
     * Exception of universal type
     */
    public static class MainErrorException extends Exception {
        MainErrorException(String error) { super(error); }
    }

    /**
     * Generation of an exception of MainErrorException
     * @param error Error code
     * @return MainErrorException
     */
    public static MainErrorException getMainErrorException(String error) {
        return new MainErrorException(error);
    }
}
