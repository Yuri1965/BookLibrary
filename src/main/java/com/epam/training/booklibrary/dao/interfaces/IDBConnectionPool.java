package com.epam.training.booklibrary.dao.interfaces;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by URA on 25.09.2015.
 * Interface for implements in classes
 */
public interface IDBConnectionPool {
    /**
     * Method getConnection() for implements classes.
     * @return The link to object of the class Connection from a pool of connections from database.
     */
    Connection getConnection() throws SQLException, NamingException;
}

