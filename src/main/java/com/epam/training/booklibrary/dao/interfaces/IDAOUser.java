package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.User;
import com.epam.training.booklibrary.entity.UserRole;
import com.epam.training.booklibrary.enums.Roles;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface contains methods for realization in the class DAOUser
 */
public interface IDAOUser {

    /**
     * Method of authorization of the user in system
     * @return The user authorized in system (type of User)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    User getAuthorization() throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * Whether the method is checked by the user was authorized in system
     * @return if = true, the user is authorized
     */
    boolean isAuthorized();

    /**
     * Method for realization in classes on work with the authorized user who has to allow to change data
     * of the authorized user
     * @param pass_user user password
     * @param email_user User's E-mail
     * @param firstName_user user name
     * @param lastName_user Surname of the user
     * @return The authorized user which data were changed (type of User)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    User updateUser(String pass_user, String email_user, String firstName_user, String lastName_user)
            throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * Method for realization in classes on work with the authorized user who has to allow
     * registration of the new user
     * @param name_user user login
     * @param pass_user user password
     * @param email_user User's E-mail
     * @param firstName_user user name
     * @param lastName_user Surname of the user
     * @param roleName Role of the user
     * @return The user who was registered (type of User)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    User createUser(String name_user, String pass_user, String email_user, String firstName_user,
                    String lastName_user, String roleName)
            throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * Method for realization in classes on work with the authorized user who has to receive the list
     * of roles of the authorized user from a DB
     * @return The list of roles of the authorized user (type of List<UserRole>)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    List<UserRole> getUserRoles() throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * The method looks for in the list of roles and returns a role of the user on her name
     * @param role (type of Roles)
     * @return Role of the user (type of UserRole)
     */
    UserRole getUserRoleByName(Roles role);
}
