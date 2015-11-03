package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.BookGenre;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface for realization of methods in the class DAOBookGenres
 */
public interface IDAOBookGenres {
    /**
     * The method returns object of the class BookGenre on its identifier
     * @param bookGenreID the book genre identifier in a DB
     * @return Book genre (type of BookGenre)
     * @throws SQLException
     * @throws NamingException
     */
    BookGenre getBookGenre(int bookGenreID) throws SQLException, NamingException;

    /**
     * The method creates new record of a genre of the book in a DB
     * @param nameRU the name of a genre of the book in Russian
     * @param nameEN the name of a genre of the book in English
     * @param bookSectionID identifier of the section of literature
     * @return Book genre (type of BookGenre)
     * @throws SQLException
     * @throws NamingException
     */
    BookGenre createBookGenre(String nameRU, String nameEN, int bookSectionID) throws SQLException, NamingException;

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
    BookGenre updateBookGenre(int bookGenreID, String nameRU, String nameEN, int bookSectionID) throws SQLException, NamingException;

    /**
     * The method returns the list of genres of books which contain in the section of literature from a DB
     * @param bookSectionID identifier of the section of literature
     * @return The list of genres of books which belong to the specified section of literature (type of List<BookGenre>)
     * @throws SQLException
     * @throws NamingException
     */
    List<BookGenre> getBookGenresBySectionID(int bookSectionID) throws SQLException, NamingException;

    /**
     * The method returns the list of genres of books from a DB
     * @return List of genres of books (type of List<BookGenre>)
     * @throws SQLException
     * @throws NamingException
     */
    List<BookGenre> getBookGenres() throws SQLException, NamingException;
}
