package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.UserExt;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface contains methods for realization in the class DAOUsers
 */
public interface IDAOUsers {
    /**
     * The method obtains data of the user from a DB on his identifier
     * @param userID record identifier
     * @return user (type of UserExt)
     * @throws SQLException
     * @throws NamingException
     */
    UserExt getUser(int userID) throws SQLException, NamingException;

    /**
     * The method allows to block the user in system
     * @param userID record identifier
     * @param blockedDescription blocking reason
     * @return the blocked user (type of UserExt)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    UserExt blockUser(int userID, String blockedDescription) throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * The method allows to unblock the user in system
     * @param userID record identifier
     * @return the unblocked user (type of UserExt)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    UserExt unBlockUser(int userID) throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * The method receives the list пользователй from a DB in the specified search parameters
     * @param searchCriteria  search option (type of IDAOSearchCriteria)
     * @return list of users (type of List<UserExt>)
     * @throws SQLException
     * @throws NamingException
     */
    List<UserExt> getListUsers(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;

    /**
     * The method receives number of users in a DB in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return int number of users
     * @throws SQLException
     * @throws NamingException
     */
    int getCountUsers(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;

    /**
     * The method receives number of users in the list
     * @param listUsers list of users (type of List<UserExt>)
     * @return int number of users
     */
    int getCountUsersByList(List<UserExt> listUsers);

    /**
     * The method receives number of pages with users in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @param recordCountByPage number of users on the page
     * @return int number of pages with users in the specified search parameters
     * @throws SQLException
     * @throws NamingException
     */
    int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException;

    /**
     * The method receives number of pages with users according to the specified list
     * @param listUsers list of users (type of List<UserExt>)
     * @param recordCountByPage number of users on the page
     * @return int number of pages with users
     */
    int getCountPagesByList(List<UserExt> listUsers, int recordCountByPage);
}
