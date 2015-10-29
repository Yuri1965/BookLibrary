package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOBookSections;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.entity.BookSection;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by URA on 30.09.2015.
 */
public class DAOBookSections implements IDAOBookSections {
    private static final String SQL_GET_BOOK_SECTIONS = "SELECT id, name_ru, name_en FROM booksections ORDER BY name_ru ASC;";
    private static final String SQL_GET_BOOK_SECTION = "SELECT id, name_ru, name_en FROM booksections WHERE id = ? limit 1;";
    private static final String SQL_CREATE_BOOK_SECTION = "{ call insertBookSection(?, ?) }";
    private static final String SQL_UPDATE_BOOK_SECTION = "{ call updateBookSection(?, ?, ?) }";

    public DAOBookSections() {
    }

    @Override
    public BookSection getBookSection(int bookSectionID) throws SQLException, NamingException {
        BookSection bookSection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_BOOK_SECTION,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookSectionID));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                bookSection = DAOUtils.setBookSection(rs);
            }

            return bookSection;
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
    public BookSection createBookSection(String nameRU, String nameEN) throws SQLException, NamingException {
        BookSection bookSection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_BOOK_SECTION,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, nameRU);
            stmt.setString(2, nameEN);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                bookSection = DAOUtils.setBookSection(rs);
            }

            return bookSection;
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
    public BookSection updateBookSection(int bookSectionID, String nameRU, String nameEN) throws SQLException, NamingException {
        BookSection bookSection = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UPDATE_BOOK_SECTION,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookSectionID));
            stmt.setString(2, nameRU);
            stmt.setString(3, nameEN);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                bookSection = DAOUtils.setBookSection(rs);
            }

            return bookSection;
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
    public List<BookSection> getBookSections() throws SQLException, NamingException {
        List<BookSection> bookSections = new ArrayList<BookSection>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        BookSection bookSection = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_BOOK_SECTIONS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery();

            while (rs.next()) {
                bookSection = DAOUtils.setBookSection(rs);
                bookSections.add(bookSection);
            }

            return bookSections;
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
