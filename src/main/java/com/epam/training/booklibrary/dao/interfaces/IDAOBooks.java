package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.Book;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by URA on 18.10.2015.
 */
public interface IDAOBooks {
    List<Book> getListBooks(IDAOSearchCriteria searchCriteria, boolean byBlob) throws SQLException, NamingException;

    Book getBook(int bookID) throws SQLException, NamingException;
    Book createBook(int authorID, int genreID, int publisherID, int publishYear,
                    String isbn, String name, String shortDescription,
                    int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException;
    Book updateBook(int bookID, int authorID, int genreID, int publisherID, int publishYear,
                    String isbn, String name, String shortDescription,
                    int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException;
    void deleteBook(int bookID) throws SQLException, NamingException;

    int getCountBooks(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;
    int getCountBooksByList(List<Book> listBooks);

    int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException;
    int getCountPagesByList(List<Book> listBooks, int recordCountByPage);
}
