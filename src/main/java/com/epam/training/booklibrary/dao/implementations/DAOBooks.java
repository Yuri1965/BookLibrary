package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOBooks;
import com.epam.training.booklibrary.dao.interfaces.IDAOSearchCriteria;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.datamodels.entity.Book;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;

import javax.naming.NamingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class contains methods for work with books
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

    // number of records on the page
    private int countRecordPage;

    /**
     * Constructor of a class
     * @param countRecordPage int number of records on the page
     */
    public DAOBooks(int countRecordPage) {
        if (countRecordPage < DEFAULT_RECORD_PAGE) {
            countRecordPage = DEFAULT_RECORD_PAGE;
        }

        this.countRecordPage = countRecordPage;
    }

    /**
     * The method returns the list of books from a DB, in the specified search parameters
     * @param searchCriteria search parameters (type of IDAOSearchCriteria)
     * @param byBlob if value = true, data contain book cover drawing
     * @return list of books (type of List<Book>)
     * @throws SQLException
     * @throws NamingException
     */
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

    /**
     * The method returns from a DB the book on its identifier of record
     * @param bookID record identifier
     * @return book (type of Book)
     * @throws SQLException
     * @throws NamingException
     */
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

    /**
     * The method creates the new book in a DB
     * @param authorID identifier of the author of the book
     * @param genreID book genre identifier
     * @param publisherID identifier of publishing house
     * @param publishYear year of the edition
     * @param isbn ISBN code
     * @param name name of the book
     * @param shortDescription short description of the book
     * @param numberCopies number of copies of the book
     * @param coverImage book cover
     * @return new book (type of Book)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     * @throws IOException
     */
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

    /**
     * The method changes these books in a DB on its identifier
     * @param bookID record identifier
     * @param authorID identifier of the author of the book
     * @param genreID book genre identifier
     * @param publisherID identifier of publishing house
     * @param publishYear year of the edition
     * @param isbn ISBN code
     * @param name name of the book
     * @param shortDescription short description of the book
     * @param numberCopies number of copies of the book
     * @param coverImage book cover
     * @return new book (type of Book)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     * @throws IOException
     */
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

            if (coverImage != null && coverImage.length > 0) {
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

    /**
     *The method marks in a DB the book as remove
     * @param bookID record identifier
     * @throws SQLException
     * @throws NamingException
     */
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

    /**
     * The method returns number of books in the DB found in the specified search parameters
     * @param searchCriteria search parameters (type of IDAOSearchCriteria)
     * @return int number of the books found in the specified search parameters
     * @throws SQLException
     * @throws NamingException
     */
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

    /**
     * The method returns number of books in the list
     * @param listBooks list of books (type of List<Book>)
     * @return int number of books in the list
     */
    @Override
    public int getCountBooksByList(List<Book> listBooks) {
        return listBooks.size();
    }

    /**
     * The method returns number of pages with books which were found in a DB in the specified search parameters
     * @param searchCriteria search parameters (type of IDAOSearchCriteria)
     * @param recordCountByPage int number of books on the page
     * @return int number of pages with books which are found in search parameters
     * @throws SQLException
     * @throws NamingException
     */
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
                        result++;
                    }
                }

                return result;
            }
            finally {
            }
        }

        return result;
    }

    /**
     * The method returns number of pages with books according to the specified list of books
     * @param listBooks list of books (type of List<Book>)
     * @param recordCountByPage int number of books on the page
     * @return int number of pages with books
     */
    @Override
    public int getCountPagesByList(List<Book> listBooks, int recordCountByPage) {
        int result = 0;

        if (recordCountByPage > 0) {
            if (listBooks.size() > 0) {
                float tmpValue = Math.abs((float) listBooks.size()/(float) recordCountByPage);
                result = Math.round(tmpValue);

                if ((tmpValue - result) > 0) {
                    result++;
                }
            }
        }

        return result;
    }
}
