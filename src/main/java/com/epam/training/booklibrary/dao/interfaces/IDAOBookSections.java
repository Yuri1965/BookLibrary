package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.BookSection;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface for realization of methods in the class DAOBookSections
 */
public interface IDAOBookSections {
    /**
     * The method returns the section of literature in a DB on the specified record identifier
     * @param bookSectionID int record identifier
     * @return section of literature (type of BookSection)
     * @throws SQLException
     * @throws NamingException
     */
    BookSection getBookSection(int bookSectionID) throws SQLException, NamingException;

    /**
     * The method creates the new section of literature in a DB
     * @param nameRU the name of the section of literature in Russian
     * @param nameEN the name of the section of literature in English
     * @return new section of literature (type of BookSection)
     * @throws SQLException
     * @throws NamingException
     */
    BookSection createBookSection(String nameRU, String nameEN) throws SQLException, NamingException;

    /**
     * The method changes data of the section of literature in a DB on the specified identifier
     * @param bookSectionID record identifier
     * @param nameRU the name of the section of literature in Russian
     * @param nameEN the name of the section of literature in English
     * @return section of literature (type of BookSection)
     * @throws SQLException
     * @throws NamingException
     */
    BookSection updateBookSection(int bookSectionID, String nameRU, String nameEN) throws SQLException, NamingException;

    /**
     * The method returns the list of sections of literature from a DB
     * @return list of sections of literature (type of List<BookSection>)
     * @throws SQLException
     * @throws NamingException
     */
    List<BookSection> getBookSections() throws SQLException, NamingException;
}
