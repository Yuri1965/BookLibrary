package com.epam.training.booklibrary.exceptions;

/**
 * Created by URA on 15.09.2015.
 * Класс фабрика для генерации собственных исключений в системе
 */
public class MainExceptions {
    /**
     * Исключение
     */
    public static class MainErrorException extends Exception {
        MainErrorException(String error) { super(error); }
    }

    /**
     * Генерация исключения MainErrorException
     * @param error Код ошибки
     * @return MainErrorException
     */
    public static MainErrorException getMainErrorException(String error) {
        return new MainErrorException(error);
    }






    /**
     * Исключение = Пользователь не найден в системе
     */
    public static class UserNotFoundException extends Exception {
        UserNotFoundException(String error) { super(error); }
    }

    /**
     * Генерация исключения UserNotFoundException
     * @param userName Имя (Login) пользователя, который не найден в системе
     * @return UserNotFoundException
     */
    public static UserNotFoundException getUserNotFoundException(String error) {
        return new UserNotFoundException("User = " + error + " is not found..");
    }

    /**
     * Исключение = У пользователя отсутствует список ролей
     */
    public static class UserRolesNotFoundException extends Exception {
        UserRolesNotFoundException(String s) { super(s); }
    }

    /**
     * Генерация исключения UserRolesNotFoundException
     * @param userName Имя (Login) пользователя, у которого отсутствует список ролей
     * @return UserRolesNotFoundException
     */
    public static UserRolesNotFoundException getUserRolesNotFoundException(String userName) {
        return new UserRolesNotFoundException("Roles for User = " + userName + " is not found..");
    }

    /**
     * Исключение = Ошибка доступа к ресурсу системы (не хватает прав у роли пользователя)
     */
    public static class AccessDeniedResourceException extends Exception {
        AccessDeniedResourceException(String s) { super(s); }
    }

    /**
     * Генерация исключения AccessDeniedResourceException
     * @param userName Имя (Login) пользователя, который выполнил запрос к ресурсу
     * @return AccessDeniedResourceException
     */
    public static AccessDeniedResourceException getAccessDeniedResourceException(String userName) {
        return new AccessDeniedResourceException("Access to the specified resource is forbidden the user = " +
                userName);
    }

}
