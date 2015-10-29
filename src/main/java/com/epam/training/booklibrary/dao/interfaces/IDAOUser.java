package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.User;
import com.epam.training.booklibrary.entity.UserRole;
import com.epam.training.booklibrary.enums.Roles;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by URA on 13.09.2015.
 * Интерфейс для реализации классов по работе с авторизованным пользователем
 */
public interface IDAOUser {
    // методы по работе с User
    /**
     * Метод для реализации в классах по работе с авторизованным пользователем
     * @return Пользователь авторизованный в системе
     */
    User getAuthorization() throws SQLException, NamingException, MainExceptions.MainErrorException;

    boolean isAuthorized();

    /**
     * Метод для реализации в классах по работе с авторизованным пользователем, который должен позволять изменять
     * данные авторизованного пользователя
     * @param pass_user Пароль пользователя
     * @param email_user Email пользователя
     * @param firstName_user Имя пользователя
     * @param lastName_user Фамилия пользователя
     * @return Авторизованный пользователь данные которого были изменены
     */
    User updateUser(String pass_user, String email_user, String firstName_user, String lastName_user)
            throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * Метод для реализации в классах по работе с авторизованным пользователем, который должен позволять регистрацию
     * нового пользователя
     * @param name_user Login пользователя
     * @param pass_user Пароль пользователя
     * @param email_user Email пользователя
     * @param firstName_user Имя пользователя
     * @param lastName_user Фамилия пользователя
     * @param roleName Роль пользователя
     * @return Пользователь, который был зарегистрирован
     */
    User createUser(String name_user, String pass_user, String email_user, String firstName_user,
                    String lastName_user, String roleName)
            throws SQLException, NamingException, MainExceptions.MainErrorException;

    // методы по работе с UserRole
    /**
     * Метод для реализации в классах по работе с авторизованным пользователем, который должен получать из БД
     * список ролей авторизованного пользователя
     * @return Список ролей авторизованного пользователя
     */
    List<UserRole> getUserRoles() throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * Метод ищет в списке ролей и возвращает роль пользователя по ее имени
     * @param role
     * @return Роль пользователя (если не найдена, то = null)
     */
    UserRole getUserRoleByName(Roles role);
}
