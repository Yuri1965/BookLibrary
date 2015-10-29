package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.BookSection;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by URA on 30.09.2015.
 */
public interface IDAOBookSections {
    BookSection getBookSection(int bookSectionID) throws SQLException, NamingException;

    BookSection createBookSection(String nameRU, String nameEN) throws SQLException, NamingException;

    BookSection updateBookSection(int bookSectionID, String nameRU, String nameEN) throws SQLException, NamingException;

    List<BookSection> getBookSections() throws SQLException, NamingException;
}
