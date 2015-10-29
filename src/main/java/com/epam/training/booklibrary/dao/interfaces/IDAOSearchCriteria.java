package com.epam.training.booklibrary.dao.interfaces;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by URA on 18.10.2015.
 */
public interface IDAOSearchCriteria {
    PreparedStatement preparedStatement(Connection conn, PreparedStatement stmt, String sqlText, String orderBYText) throws SQLException, NamingException;
}
