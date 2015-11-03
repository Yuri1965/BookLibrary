package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.Author;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * The interface for realization of methods in the class DAOAuthors
 */
public interface IDAOAuthors {
    /**
     * The method returns object of the class Author on its identifier
     * @param authorID identifier
     * @return Author of the book (type of Author)
     * @throws SQLException
     * @throws NamingException
     */
    Author getAuthor(int authorID) throws SQLException, NamingException;

    /**
     * The method creates new record of the Author of the book in a DB
     * @param fioRU name and surname of the Author of the book in Russian
     * @param fioEN name and surname of the Author of the book in English
     * @param birthday date of birth of the Author of the book
     * @return new record Author of the book (type of Author)
     * @throws SQLException
     * @throws NamingException
     */
    Author createAuthor(String fioRU, String fioEN, Date birthday) throws SQLException, NamingException;

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
    Author updateAuthor(int authorID, String fioRU, String fioEN, Date birthday) throws SQLException, NamingException;

    /**
     * The method returns the list of Authors of books from a DB
     * @return (type of List<Author>) list of Authors of books
     * @throws SQLException
     * @throws NamingException
     */
    List<Author> getAuthors() throws SQLException, NamingException;
}
