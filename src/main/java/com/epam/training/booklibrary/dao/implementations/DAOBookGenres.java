package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOBookGenres;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.entity.BookGenre;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class contains methods for work with genres of books
 */
public class DAOBookGenres implements IDAOBookGenres {
    private static final String SQL_GET_BOOK_GENRE =
            "SELECT bookgenres.id, bookgenres.name_ru, bookgenres.name_en, bookgenres.bookSectionID,\n" +
            "    booksections.name_ru AS bookSectionNameRU, booksections.name_en AS bookSectionNameEN\n" +
            "FROM bookgenres\n" +
            "LEFT JOIN booksections ON bookgenres.bookSectionID = booksections.id\n" +
            "WHERE bookgenres.id = ? limit 1;";

    private static final String SQL_GET_BOOK_GENRES =
            "SELECT bookgenres.id, bookgenres.name_ru, bookgenres.name_en, bookgenres.bookSectionID,\n" +
                    "    booksections.name_ru AS bookSectionNameRU, booksections.name_en AS bookSectionNameEN\n" +
                    "FROM bookgenres\n" +
                    "LEFT JOIN booksections ON bookgenres.bookSectionID = booksections.id\n" +
                    "ORDER BY booksections.name_ru ASC, bookgenres.name_ru ASC;";

    private static final String SQL_GET_BOOK_GENRES_BY_SECTION_ID =
            "SELECT bookgenres.id, bookgenres.name_ru, bookgenres.name_en, bookgenres.bookSectionID,\n" +
                    "    booksections.name_ru AS bookSectionNameRU, booksections.name_en AS bookSectionNameEN\n" +
                    "FROM bookgenres\n" +
                    "LEFT JOIN booksections ON bookgenres.bookSectionID = booksections.id\n" +
                    "WHERE bookgenres.bookSectionID = ? limit 1;";

    private static final String SQL_CREATE_BOOK_GENRE = "{ call insertBookGenre(?, ?) }";
    private static final String SQL_UPDATE_BOOK_GENRE = "{ call updateBookGenre(?, ?, ?, ?) }";

    /**
     * Constructor of a class by default
     */
    public DAOBookGenres() {
    }

    /**
     * The method returns object of the class BookGenre on its identifier
     * @param bookGenreID the book genre identifier in a DB
     * @return Book genre (type of BookGenre)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public BookGenre getBookGenre(int bookGenreID) throws SQLException, NamingException {
        BookGenre bookGenre = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_BOOK_GENRE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookGenreID));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                bookGenre = DAOUtils.setBookGenre(rs);
            }

            return bookGenre;
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
     * The method creates new record of a genre of the book in a DB
     * @param nameRU the name of a genre of the book in Russian
     * @param nameEN the name of a genre of the book in English
     * @param bookSectionID identifier of the section of literature
     * @return Book genre (type of BookGenre)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public BookGenre createBookGenre(String nameRU, String nameEN, int bookSectionID) throws SQLException, NamingException {
        BookGenre bookGenre = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_BOOK_GENRE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, nameRU);
            stmt.setString(2, nameEN);
            stmt.setString(3, Integer.toString(bookSectionID));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                bookGenre = DAOUtils.setBookGenre(rs);
            }

            return bookGenre;
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
     * The method changes data of a genre of the book in a DB on its identifier
     * @param bookGenreID the record identifier in a DB
     * @param nameRU the name of a genre of the book in Russian
     * @param nameEN the name of a genre of the book in English
     * @param bookSectionID identifier of the section of literature
     * @return Book genre (type of BookGenre)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public BookGenre updateBookGenre(int bookGenreID, String nameRU, String nameEN, int bookSectionID) throws SQLException, NamingException {
        BookGenre bookGenre = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UPDATE_BOOK_GENRE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookGenreID));
            stmt.setString(2, nameRU);
            stmt.setString(3, nameEN);
            stmt.setString(4, Integer.toString(bookSectionID));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                bookGenre = DAOUtils.setBookGenre(rs);
            }

            return bookGenre;
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
     * The method returns the list of genres of books which contain in the section of literature from a DB
     * @param bookSectionID identifier of the section of literature
     * @return The list of genres of books which belong to the specified section of literature (type of List<BookGenre>)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public List<BookGenre> getBookGenresBySectionID(int bookSectionID) throws SQLException, NamingException {
        List<BookGenre> bookGenres = new ArrayList<BookGenre>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        BookGenre bookGenre = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_BOOK_GENRES_BY_SECTION_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookSectionID));

            rs = stmt.executeQuery();

            while (rs.next()) {
                bookGenre = DAOUtils.setBookGenre(rs);
                bookGenres.add(bookGenre);
            }

            return bookGenres;
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
     * The method returns the list of genres of books from a DB
     * @return List of genres of books (type of List<BookGenre>)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public List<BookGenre> getBookGenres() throws SQLException, NamingException {
        List<BookGenre> bookGenres = new ArrayList<BookGenre>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        BookGenre bookGenre = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_BOOK_GENRES,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery();

            while (rs.next()) {
                bookGenre = DAOUtils.setBookGenre(rs);
                bookGenres.add(bookGenre);
            }

            return bookGenres;
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
