package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.UserExt;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by URA on 01.10.2015.
 */
public interface IDAOUsers {
    UserExt getUser(int userID) throws SQLException, NamingException;

    UserExt blockUser(int userID, String blockedDescription) throws SQLException, NamingException, MainExceptions.MainErrorException;

    UserExt unBlockUser(int userID) throws SQLException, NamingException, MainExceptions.MainErrorException;

    List<UserExt> getListUsers(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;

    int getCountUsers(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;
    int getCountUsersByList(List<UserExt> listUsers);

    int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException;
    int getCountPagesByList(List<UserExt> listUsers, int recordCountByPage);
}
