package com.epam.training.booklibrary.dao.interfaces;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Interface for implements in classes DBConnectionPool
 */
public interface IDBConnectionPool {
    /**
     * Method getConnection() for implements classes.
     * @return The link to object of the class Connection from a pool of connections from database.
     * @throws SQLException
     * @throws NamingException
     */
    Connection getConnection() throws SQLException, NamingException;
}

