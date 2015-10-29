package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.Author;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by URA on 30.09.2015.
 */
public interface IDAOAuthors {
    Author getAuthor(int authorID) throws SQLException, NamingException;

    Author createAuthor(String fioRU, String fioEN, Date birthday) throws SQLException, NamingException;

    Author updateAuthor(int authorID, String fioRU, String fioEN, Date birthday) throws SQLException, NamingException;

    List<Author> getAuthors() throws SQLException, NamingException;
}
