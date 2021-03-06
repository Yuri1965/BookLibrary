package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOAuthors;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.datamodels.entity.Author;

import javax.naming.NamingException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The class contains methods for work with authors of books
 */
public class DAOAuthors implements IDAOAuthors {
    private static final String SQL_GET_AUTHORS = "SELECT id, fio_ru, fio_en, birthday FROM authors ORDER BY fio_ru ASC;";
    private static final String SQL_GET_AUTHOR = "SELECT id, fio_ru, fio_en, birthday FROM authors WHERE id = ? limit 1;";
    private static final String SQL_CREATE_AUTHOR = "{ call insertAuthor(?, ?, ?) }";
    private static final String SQL_UPDATE_AUTHOR = "{ call updateAuthor(?, ?, ?, ?) }";

    /**
     * Constructor of a class by default
     */
    public DAOAuthors() {
    }

    /**
     * The method returns object of the class Author on its identifier
     * @param authorID identifier
     * @return Author of the book (type of Author)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public Author getAuthor(int authorID) throws SQLException, NamingException {
        Author author = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_AUTHOR,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(authorID));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                author = DAOUtils.setAuthor(rs);
            }

            return author;
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

    /**
     * The method creates new record of the Author of the book in a DB
     * @param fioRU name and surname of the Author of the book in Russian
     * @param fioEN name and surname of the Author of the book in English
     * @param birthday date of birth of the Author of the book
     * @return new record Author of the book (type of Author)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public Author createAuthor(String fioRU, String fioEN, Date birthday) throws SQLException, NamingException {
        Author author = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_AUTHOR,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, fioRU);
            stmt.setString(2, fioEN);
            stmt.setString(3, new SimpleDateFormat("yyyy-MM-dd").format(birthday));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                author = DAOUtils.setAuthor(rs);
            }

            return author;
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

    /**
     * The method changes data of the Author of the book in a DB
     * @param authorID identifier of the Author of the book
     * @param fioRU name and surname of the Author of the book in Russian
     * @param fioEN name and surname of the Author of the book in English
     * @param birthday date of birth of the Author of the book
     * @return update record Author of the book (type of Author)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public Author updateAuthor(int authorID, String fioRU, String fioEN, Date birthday) throws SQLException, NamingException {
        Author author = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UPDATE_AUTHOR,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(authorID));
            stmt.setString(2, fioRU);
            stmt.setString(3, fioEN);
            stmt.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(birthday));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                author = DAOUtils.setAuthor(rs);
            }

            return author;
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

    /**
     * The method returns the list of Authors of books from a DB
     * @return (type of List<Author>) list of Authors of books
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public List<Author> getAuthors() throws SQLException, NamingException {
        List<Author> authors = new ArrayList<Author>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        Author author = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_AUTHORS,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery();

            while ( rs.next()) {
                author = DAOUtils.setAuthor(rs);
                authors.add(author);
            }

            return authors;
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
