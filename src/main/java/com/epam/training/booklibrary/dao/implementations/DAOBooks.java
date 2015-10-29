package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOBooks;
import com.epam.training.booklibrary.dao.interfaces.IDAOSearchCriteria;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.entity.Book;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;

import javax.naming.NamingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by URA on 18.10.2015.
 */
public class DAOBooks implements IDAOBooks {
    private static final String SQL_GET_BOOKS_ORDER_DEFAULT = "\n ORDER BY books.name ASC";

    private static final String SQL_GET_BOOKS_COUNTS = "SELECT COUNT(books.id) AS count\n" +
            "FROM books\n" +
            "LEFT JOIN authors ON authors.id = books.authorID\n" +
            "LEFT JOIN publishers ON publishers.id = books.publisherID\n" +
            "LEFT JOIN bookgenres ON bookgenres.id = books.genreID\n" +
            "LEFT JOIN booksections ON booksections.id = bookgenres.bookSectionID\n";

    private static final String SQL_GET_BOOKS_NO_BLOB =
            "SELECT books.id,\n" +
                    "books.authorID, authors.fio_ru AS authorFIO_RU, authors.fio_en AS authorFIO_EN,\n" +
                    "authors.birthday AS authorBirthday, books.genreID, bookgenres.bookSectionID,\n" +
                    "bookgenres.name_ru AS bookGenreNameRU, bookgenres.name_en AS bookGenreNameEN,\n" +
                    "booksections.name_ru AS bookSectionNameRU, bookSections.name_en AS bookSectionNameEN,\n" +
                    "books.publisherID, publishers.name_ru AS publisherNameRU, publishers.name_en AS publisherNameEN,\n" +
                    "books.publishYear, books.isbn, books.isDeleted,\n" +
                    "books.name, books.shortDescription, books.numberCopies, null AS coverImage\n" +
                    "FROM books\n" +
                    "LEFT JOIN authors ON authors.id = books.authorID\n" +
                    "LEFT JOIN publishers ON publishers.id = books.publisherID\n" +
                    "LEFT JOIN bookgenres ON bookgenres.id = books.genreID\n" +
                    "LEFT JOIN booksections ON booksections.id = bookgenres.bookSectionID\n";

    private static final String SQL_GET_BOOKS_BY_BLOB =
            "SELECT books.id,\n" +
                    "books.authorID, authors.fio_ru AS authorFIO_RU, authors.fio_en AS authorFIO_EN,\n" +
                    "authors.birthday AS authorBirthday, books.genreID, bookgenres.bookSectionID,\n" +
                    "bookgenres.name_ru AS bookGenreNameRU, bookgenres.name_en AS bookGenreNameEN,\n" +
                    "booksections.name_ru AS bookSectionNameRU, bookSections.name_en AS bookSectionNameEN,\n" +
                    "books.publisherID, publishers.name_ru AS publisherNameRU, publishers.name_en AS publisherNameEN,\n" +
                    "books.publishYear, books.isbn, books.isDeleted,\n" +
                    "books.name, books.shortDescription, books.numberCopies, books.coverImage\n" +
                    "FROM books\n" +
                    "LEFT JOIN authors ON authors.id = books.authorID\n" +
                    "LEFT JOIN publishers ON publishers.id = books.publisherID\n" +
                    "LEFT JOIN bookgenres ON bookgenres.id = books.genreID\n" +
                    "LEFT JOIN booksections ON booksections.id = bookgenres.bookSectionID\n";

    private static final String SQL_GET_BOOK =
            "SELECT books.id,\n" +
                    "books.authorID, authors.fio_ru AS authorFIO_RU, authors.fio_en AS authorFIO_EN,\n" +
                    "authors.birthday AS authorBirthday, books.genreID, bookgenres.bookSectionID,\n" +
                    "bookgenres.name_ru AS bookGenreNameRU, bookgenres.name_en AS bookGenreNameEN,\n" +
                    "booksections.name_ru AS bookSectionNameRU, bookSections.name_en AS bookSectionNameEN,\n" +
                    "books.publisherID, publishers.name_ru AS publisherNameRU, publishers.name_en AS publisherNameEN,\n" +
                    "books.publishYear, books.isbn, books.isDeleted,\n" +
                    "books.name, books.shortDescription, books.numberCopies, books.coverImage\n" +
                    "FROM books\n" +
                    "LEFT JOIN authors ON authors.id = books.authorID\n" +
                    "LEFT JOIN publishers ON publishers.id = books.publisherID\n" +
                    "LEFT JOIN bookgenres ON bookgenres.id = books.genreID\n" +
                    "LEFT JOIN booksections ON booksections.id = bookgenres.bookSectionID\n" +
                    "WHERE books.id = ? and books.isDeleted = 0 limit 1";

    private static final String SQL_CREATE_BOOK = "{ call insertBook(?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String SQL_UPDATE_BOOK = "{ call updateBook(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
    private static final String SQL_DELETE_BOOK = "{ call deleteBook(?) }";

    private static final int DEFAULT_RECORD_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsBookPage", "3"));

    private int countRecordPage;

    public DAOBooks(int countRecordPage) {
        if (countRecordPage < DEFAULT_RECORD_PAGE) {
            countRecordPage = DEFAULT_RECORD_PAGE;
        }

        this.countRecordPage = countRecordPage;
    }

    @Override
    public List<Book> getListBooks(IDAOSearchCriteria searchCriteria, boolean byBlob) throws SQLException, NamingException {
        List<Book> listBooks = new ArrayList<Book>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        Book book = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();

            if (byBlob) {
                stmt = searchCriteria.preparedStatement(conn, stmt, SQL_GET_BOOKS_BY_BLOB, SQL_GET_BOOKS_ORDER_DEFAULT);
            } else {
                stmt = searchCriteria.preparedStatement(conn, stmt, SQL_GET_BOOKS_NO_BLOB, SQL_GET_BOOKS_ORDER_DEFAULT);
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                book = DAOUtils.setBook(rs);
                listBooks.add(book);
            }

            return listBooks;
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
    public Book getBook(int bookID) throws SQLException, NamingException {
        Book book = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_BOOK,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, bookID);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                book = DAOUtils.setBook(rs);
            }

            return book;
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
    public Book createBook(int authorID, int genreID, int publisherID, int publishYear, String isbn,
                           String name, String shortDescription, int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException {
        Book book = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_BOOK,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(authorID));
            stmt.setString(2, Integer.toString(genreID));
            stmt.setString(3, Integer.toString(publisherID));
            stmt.setString(4, Integer.toString(publishYear));
            stmt.setString(5, isbn);
            stmt.setString(6, name);
            stmt.setString(7, shortDescription);
            stmt.setString(8, Integer.toString(numberCopies));

            if (coverImage != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(coverImage);
                stmt.setBinaryStream(9, inputStream);

                inputStream.close();
            } else {
                stmt.setNull(9, Types.BLOB);
            }

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    book = DAOUtils.setBook(rs);
                }
            }

            return book;
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
    public Book updateBook(int bookID, int authorID, int genreID, int publisherID, int publishYear,
                           String isbn, String name, String shortDescription, int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException {

        Book book = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UPDATE_BOOK,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookID));
            stmt.setString(2, Integer.toString(authorID));
            stmt.setString(3, Integer.toString(genreID));
            stmt.setString(4, Integer.toString(publisherID));
            stmt.setString(5, Integer.toString(publishYear));
            stmt.setString(6, isbn);
            stmt.setString(7, name);
            stmt.setString(8, shortDescription);
            stmt.setString(9, Integer.toString(numberCopies));

            if (coverImage != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(coverImage);
                stmt.setBinaryStream(10, inputStream);

                inputStream.close();
            } else {
                stmt.setNull(10, Types.BLOB);
            }

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    book = DAOUtils.setBook(rs);
                }
            }

            return book;
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
    public void deleteBook(int bookID) throws SQLException, NamingException {
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_DELETE_BOOK,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookID));

            rs = stmt.executeQuery();

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
    public int getCountBooks(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException {
        int countBook = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = searchCriteria.preparedStatement(conn, stmt, SQL_GET_BOOKS_COUNTS, null);

            rs = stmt.executeQuery();

            rs.first();
            countBook = rs.getInt("count");

            return countBook;
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
    public int getCountBooksByList(List<Book> listBooks) {
        return listBooks.size();
    }

    @Override
    public int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException {
        int result = 0;

        if (recordCountByPage > 0) {
            try {
                int recordCount = getCountBooks(searchCriteria);

                if (recordCount > 0) {
                    float tmpValue = Math.abs((float) recordCount/(float) recordCountByPage);
                    result = Math.round(tmpValue);

                    if ((tmpValue - result) > 0) {
                        result = result + 1;
                    }
                }

                return result;
            }
            finally {
            }
        }

        return result;
    }

    @Override
    public int getCountPagesByList(List<Book> listBooks, int recordCountByPage) {
        int result = 0;

        if (recordCountByPage > 0) {
            if (listBooks.size() > 0) {
                float tmpValue = Math.abs((float) listBooks.size()/(float) recordCountByPage);
                result = Math.round(tmpValue);

                if ((tmpValue - result) > 0) {
                    result = result + 1;
                }
            }
        }

        return result;
    }
}
