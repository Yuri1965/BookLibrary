package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.datamodels.entity.Book;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface for realization of methods in the class DAOBooks
 */
public interface IDAOBooks {
    /**
     * The method returns the list of books from a DB, in the specified search parameters
     * @param searchCriteria search parameters (type of IDAOSearchCriteria)
     * @param byBlob if value = true, data contain book cover drawing
     * @return list of books (type of List<Book>)
     * @throws SQLException
     * @throws NamingException
     */
    List<Book> getListBooks(IDAOSearchCriteria searchCriteria, boolean byBlob) throws SQLException, NamingException;

    /**
     * The method returns from a DB the book on its identifier of record
     * @param bookID record identifier
     * @return book (type of Book)
     * @throws SQLException
     * @throws NamingException
     */
    Book getBook(int bookID) throws SQLException, NamingException;

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
    Book createBook(int authorID, int genreID, int publisherID, int publishYear,
                    String isbn, String name, String shortDescription,
                    int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException;

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
    Book updateBook(int bookID, int authorID, int genreID, int publisherID, int publishYear,
                    String isbn, String name, String shortDescription,
                    int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException;

    /**
     *The method marks in a DB the book as remove
     * @param bookID record identifier
     * @throws SQLException
     * @throws NamingException
     */
    void deleteBook(int bookID) throws SQLException, NamingException;

    /**
     * The method returns number of books in the DB found in the specified search parameters
     * @param searchCriteria search parameters (type of IDAOSearchCriteria)
     * @return int number of the books found in the specified search parameters
     * @throws SQLException
     * @throws NamingException
     */
    int getCountBooks(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;

    /**
     * The method returns number of books in the list
     * @param listBooks list of books (type of List<Book>)
     * @return int number of books in the list
     */
    int getCountBooksByList(List<Book> listBooks);

    /**
     * The method returns number of pages with books which were found in a DB in the specified search parameters
     * @param searchCriteria search parameters (type of IDAOSearchCriteria)
     * @param recordCountByPage int number of books on the page
     * @return int number of pages with books which are found in search parameters
     * @throws SQLException
     * @throws NamingException
     */
    int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException;

    /**
     * The method returns number of pages with books according to the specified list of books
     * @param listBooks list of books (type of List<Book>)
     * @param recordCountByPage int number of books on the page
     * @return int number of pages with books
     */
    int getCountPagesByList(List<Book> listBooks, int recordCountByPage);
}
