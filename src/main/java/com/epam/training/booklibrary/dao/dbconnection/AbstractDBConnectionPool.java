package com.epam.training.booklibrary.dao.dbconnection;

import com.epam.training.booklibrary.dao.interfaces.IDBConnectionPool;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by URA on 25.09.2015.
 * Abstract class for realization of the IDBConnectionPool interface in classes successors
 */
public abstract class AbstractDBConnectionPool implements IDBConnectionPool {

    // JNDI resource name
    private String jdbcDataSourceName;
    // Pool of connections with a DB
    protected DataSource dataSource;

    /**
     * Constructor
     */
    public AbstractDBConnectionPool(String jdbcDataSourceName) {
        this.jdbcDataSourceName = jdbcDataSourceName;
    }

    // <editor-fold defaultstate="collapsed" desc="Getter methods. Click on the + sign on the left to edit the code.">
    public String getJdbcDataSourceName() {
        return jdbcDataSourceName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Override IDBConnectionPool interface methods. Click on the + sign on the left to edit the code.">
    /**
     * Method getConnection() for DBConnectionPool class.
     * @return The link to object of the class Connection from a pool of connections from DBConnectionPool.
     */
    @Override
    public abstract Connection getConnection() throws SQLException, NamingException;
    // </editor-fold>
}
