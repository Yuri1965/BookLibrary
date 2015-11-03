package com.epam.training.booklibrary.dao.dbconnection;

import com.epam.training.booklibrary.dao.interfaces.IDBConnectionPool;

import javax.naming.*;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Singleton class. Pool of connections with a DB of library under MySQL
 */
public class DBConnectionPool extends AbstractDBConnectionPool implements IDBConnectionPool {
    // name of data source for a pool of connections with a DB of library under MySQL
    private static final String JDBC_MYSQL_DATA_SOURCE_NAME = "java:comp/env/jdbc/MySQLBookLibrary";

    // Creation of a copy of object of DBConnectionPoolMySQL
    private static DBConnectionPool instance;

    /**
     * Constructor for MySQL database connection pool
     */
    private DBConnectionPool() {
        super(JDBC_MYSQL_DATA_SOURCE_NAME);
    }

    /**
     * Implements method getInstance() for DBConnectionPoolMySQL class.
     * @return Link to object of the class DBConnectionPoolMySQL.
     */
    public static synchronized DBConnectionPool getInstance() {
        if (instance == null) {
            instance = new DBConnectionPool();
        }

        return instance;
    }

    // <editor-fold defaultstate="collapsed" desc="Override DBConnectionPool class methods. Click on the + sign on the left to edit the code.">
    /**
     * Implements method getConnection() for DBConnectionPoolMySQL class.
     * @return The link to object of the class Connection from a pool of connections from DBConnectionPoolMySQL.
     */
    @Override
    public Connection getConnection() throws SQLException, NamingException {
        Connection connection = null;

        if (dataSource == null) {
            dataSource = (javax.sql.DataSource) new InitialContext().lookup(JDBC_MYSQL_DATA_SOURCE_NAME);
        }
        connection = dataSource.getConnection();

        return connection;
    }
    // </editor-fold>
}