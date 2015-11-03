package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.Publisher;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface contains methods for realization in the class DAOPublishers
 */
public interface IDAOPublishers {
    /**
     * The method returns Publishing house from a DB on the specified identifier
     * @param publisherID record identifier
     * @return publishing house (type of Publisher)
     * @throws SQLException
     * @throws NamingException
     */
    Publisher getPublisher(int publisherID) throws SQLException, NamingException;

    /**
     * The method creates the new record Publishing house in a DB
     * @param nameRU the name of publishing house in Russian
     * @param nameEN the name of publishing house in English
     * @return publishing house (type of Publisher)
     * @throws SQLException
     * @throws NamingException
     */
    Publisher createPublisher(String nameRU, String nameEN) throws SQLException, NamingException;

    /**
     * The method changes data of publishing house in a DB on the specified identifier
     * @param publisherID record identifier
     * @param nameRU the name of publishing house in Russian
     * @param nameEN the name of publishing house in English
     * @return publishing house (type of Publisher)
     * @throws SQLException
     * @throws NamingException
     */
    Publisher updatePublisher(int publisherID, String nameRU, String nameEN) throws SQLException, NamingException;

    /**
     * The method returns the list of publishing houses from a DB
     * @return list of publishing houses (type of List<Publisher>)
     * @throws SQLException
     * @throws NamingException
     */
    List<Publisher> getPublishers() throws SQLException, NamingException;
}
