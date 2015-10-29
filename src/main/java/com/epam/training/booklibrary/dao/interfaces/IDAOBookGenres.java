package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.BookGenre;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by URA on 30.09.2015.
 */
public interface IDAOBookGenres {
    BookGenre getBookGenre(int bookGenreID) throws SQLException, NamingException;

    BookGenre createBookGenre(String nameRU, String nameEN, int bookSectionID) throws SQLException, NamingException;

    BookGenre updateBookGenre(int bookGenreID, String nameRU, String nameEN, int bookSectionID) throws SQLException, NamingException;

    List<BookGenre> getBookGenresBySectionID(int bookSectionID) throws SQLException, NamingException;

    List<BookGenre> getBookGenres() throws SQLException, NamingException;
}
