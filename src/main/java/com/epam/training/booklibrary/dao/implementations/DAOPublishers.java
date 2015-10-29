package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOPublishers;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.entity.Publisher;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by URA on 30.09.2015.
 */
public class DAOPublishers implements IDAOPublishers {
    private static final String SQL_GET_PUBLISHERS = "SELECT id, name_ru, name_en FROM publishers ORDER BY name_ru ASC;";
    private static final String SQL_GET_PUBLISHER = "SELECT id, name_ru, name_en FROM publishers WHERE id = ? limit 1;";
    private static final String SQL_CREATE_PUBLISHER = "{ call insertPublisher(?, ?) }";
    private static final String SQL_UPDATE_PUBLISHER = "{ call updatePublisher(?, ?, ?) }";

    public DAOPublishers() {
    }

    @Override
    public Publisher getPublisher(int publisherID) throws SQLException, NamingException {
        Publisher publisher = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_PUBLISHER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(publisherID));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                publisher = DAOUtils.setPublisher(rs);
            }

            return publisher;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }

    @Override
    public Publisher createPublisher(String nameRU, String nameEN) throws SQLException, NamingException {
        Publisher publisher = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_PUBLISHER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, nameRU);
            stmt.setString(2, nameEN);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                publisher = DAOUtils.setPublisher(rs);
            }

            return publisher;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }

    @Override
    public Publisher updatePublisher(int publisherID, String nameRU, String nameEN) throws SQLException, NamingException {
        Publisher publisher = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UPDATE_PUBLISHER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(publisherID));
            stmt.setString(2, nameRU);
            stmt.setString(3, nameEN);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                publisher = DAOUtils.setPublisher(rs);
            }

            return publisher;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }

    @Override
    public List<Publisher> getPublishers() throws SQLException, NamingException {
        List<Publisher> publishers = new ArrayList<Publisher>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        Publisher publisher = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_PUBLISHERS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery();

            while ( rs.next()) {
                publisher = DAOUtils.setPublisher(rs);
                publishers.add(publisher);
            }

            return publishers;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }
}
