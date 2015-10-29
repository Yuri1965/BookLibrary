package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.Publisher;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by URA on 30.09.2015.
 */
public interface IDAOPublishers {
    Publisher getPublisher(int publisherID) throws SQLException, NamingException;

    Publisher createPublisher(String nameRU, String nameEN) throws SQLException, NamingException;

    Publisher updatePublisher(int publisherID, String nameRU, String nameEN) throws SQLException, NamingException;

    List<Publisher> getPublishers() throws SQLException, NamingException;
}
