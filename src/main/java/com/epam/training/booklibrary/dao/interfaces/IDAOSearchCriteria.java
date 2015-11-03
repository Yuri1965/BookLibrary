package com.epam.training.booklibrary.dao.interfaces;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * The interface contains methods for realization in the class DAOSearchCriteria
 */
public interface IDAOSearchCriteria {
    /**
     * Method of preparation of parameters of search and creation of inquiry to a DB
     * @param conn Connection with a DB (type of Connection)
     * @param stmt Inquiry to a DB (type of PreparedStatement)
     * @param sqlText the text of inquiry to a DB
     * @param orderBYText part of inquiry to a DB which is responsible for an order of sorting of data
     * @return the inquiry (type of PreparedStatement) prepared for performance
     * @throws SQLException
     * @throws NamingException
     */
    PreparedStatement preparedStatement(Connection conn, PreparedStatement stmt, String sqlText, String orderBYText) throws SQLException, NamingException;
}
