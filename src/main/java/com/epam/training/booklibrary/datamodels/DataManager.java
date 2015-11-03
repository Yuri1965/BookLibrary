package com.epam.training.booklibrary.datamodels;

import com.epam.training.booklibrary.dao.implementations.*;
import com.epam.training.booklibrary.entity.*;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The class contains methods for receiving with a DB of data for operation of application
 */
public class DataManager {
    private static final int ZERO_VALUE = 0;

    /**
     * receives number of books by criterion of search
     * @param searchCriteria criteria of search (type of DAOSearchBookCriteria)
     * @return int number of books
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized int getCountBooks(DAOSearchBookCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOBooks(searchCriteria.getRecordCountPage())).getCountBooks(searchCriteria);
    }

    /**
     * receives number of pages with books by criterion of search
     * @param searchCriteria criteria of search (type of DAOSearchBookCriteria)
     * @return int number of pages with books
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized int getCountPagesBooks(DAOSearchBookCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOBooks(searchCriteria.getRecordCountPage())).getCountPages(searchCriteria, searchCriteria.getRecordCountPage());
    }

    /**
     *receives the list of books for the page by criterion of search
     * @param searchCriteria criteria of search (type of DAOSearchBookCriteria)
     * @return the list of books for the page (type of List<Book>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<Book> getListBooks(DAOSearchBookCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOBooks(searchCriteria.getRecordCountPage())).getListBooks(searchCriteria, true);
    }

    /**
     * obtains data of the book with a DB
     * @param bookID int book identifier
     * @return book (type of Book)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized Book getBook(int bookID) throws SQLException, NamingException {
        return (new DAOBooks(ZERO_VALUE)).getBook(bookID);
    }

    /**
     * Marks the book in a DB as remove
     * @param bookID int book identifier
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized void deleteBook(int bookID) throws SQLException, NamingException {
        DAOBooks daoBooks = new DAOBooks(ZERO_VALUE);
        daoBooks.deleteBook(bookID);
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
    public static synchronized Book createBook(int authorID, int genreID, int publisherID, int publishYear, String isbn,
                                               String name, String shortDescription, int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException {
        DAOBooks daoBooks = new DAOBooks(ZERO_VALUE);
        return daoBooks.createBook(authorID, genreID, publisherID, publishYear, isbn, name, shortDescription,
                numberCopies, coverImage);
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
    public static synchronized Book updateBook(int bookID, int authorID, int genreID, int publisherID, int publishYear,
                                               String isbn, String name, String shortDescription,
                                               int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException {
        DAOBooks daoBooks = new DAOBooks(ZERO_VALUE);
        return daoBooks.updateBook(bookID, authorID, genreID, publisherID, publishYear, isbn, name, shortDescription,
                numberCopies, coverImage);
    }

    /**
     * The method receives number of users in a DB in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return int number of users
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized int getCountUsers(DAOSearchUserCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOUsers(searchCriteria.getRecordCountPage())).getCountUsers(searchCriteria);
    }

    /**
     * The method receives number of pages with users in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @param recordCountByPage number of users on the page
     * @return int number of pages with users in the specified search parameters
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized int getCountPagesUsers(DAOSearchUserCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOUsers(searchCriteria.getRecordCountPage())).getCountPages(searchCriteria, searchCriteria.getRecordCountPage());
    }

    /**
     * The method receives the list users from a DB in the specified search parameters
     * @param searchCriteria  search option (type of IDAOSearchCriteria)
     * @return list of users (type of List<UserExt>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<UserExt> getListUsers(DAOSearchUserCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOUsers(searchCriteria.getRecordCountPage())).getListUsers(searchCriteria);
    }

    /**
     * The method obtains data of the user from a DB on his identifier
     * @param userID record identifier
     * @return user (type of UserExt)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized UserExt getUser(int userID) throws SQLException, NamingException {
        return (new DAOUsers(ZERO_VALUE)).getUser(userID);
    }

    /**
     * The method allows to block the user in system
     * @param userID record identifier
     * @param blockedDescription blocking reason
     * @return the blocked user (type of UserExt)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    public static synchronized UserExt blockUser(int userID, String blockedDescription)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        return (new DAOUsers(ZERO_VALUE)).blockUser(userID, blockedDescription);
    }

    /**
     * The method allows to unblock the user in system
     * @param userID record identifier
     * @return the unblocked user (type of UserExt)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    public static synchronized UserExt unBlockUser(int userID)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        return (new DAOUsers(ZERO_VALUE)).unBlockUser(userID);
    }

    /**
     * The method receives number of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return int number of orders for books
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized int getCountOrders(DAOSearchOrderCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOOrders(searchCriteria.getRecordCountPage())).getCountOrders(searchCriteria);
    }

    /**
     * The method receives from a DB quantity of free copies of the book for delivery by request
     * @param bookID book identifier
     * @return int quantity of free copies of the book
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized int getCountFreeBook(int bookID) throws SQLException, NamingException {
        return (new DAOOrders(ZERO_VALUE)).getCountFreeBook(bookID);
    }

    /**
     * The method receives number of pages with the list of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @param recordCountByPage int number of orders for books on the page
     * @return int number of pages with orders for books
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized int getCountPagesOrders(DAOSearchOrderCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOOrders(searchCriteria.getRecordCountPage())).
                    getCountPages(searchCriteria, searchCriteria.getRecordCountPage());
    }

    /**
     * The method receives from a DB the list of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return list of orders for books (type of List<UserOrder>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<UserOrder> getListOrders(DAOSearchOrderCriteria searchCriteria) throws SQLException, NamingException {
        return (new DAOOrders(searchCriteria.getRecordCountPage())).getListOrders(searchCriteria);
    }

    /**
     * The method obtains data of the order for the book on the specified identifier from a DB
     * @param orderID int record identifier
     * @return Order for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized UserOrder getOrder(int orderID) throws SQLException, NamingException {
        return (new DAOOrders(ZERO_VALUE)).getOrder(orderID);
    }

    /**
     * The method closes the order for the book in a DB
     * @param orderID int record identifier
     * @param endOrderDateTime datetime Date and closing-time of the order
     * @return Order for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    public static synchronized UserOrder closeOrder(int orderID, Date endOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        return (new DAOOrders(ZERO_VALUE)).closeOrder(orderID, endOrderDateTime);
    }

    /**
     * The method transfers to a DB the order for the book in a state = In work
     * @param orderID int record identifier
     * @param beginOrderDateTime datetime Date and time of the beginning of the order
     * @return Order for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    public static synchronized UserOrder setWorkOrder(int orderID, Date beginOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        return (new DAOOrders(ZERO_VALUE)).setWorkOrder(orderID, beginOrderDateTime);
    }

    /**
     * The method creates the new order for the book in a DB
     * @param bookID int book identifier
     * @param userID int user ID of the customer
     * @param orderTypeID int the identifier like the order
     * @param preOrderDateTime datetime Date and time of the order (preliminary)
     * @return ЗOrder for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    public static synchronized UserOrder createOrder(int bookID, int userID, int orderTypeID, Date preOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        DAOOrders daoOrders = new DAOOrders(ZERO_VALUE);
        return daoOrders.createOrder(bookID, userID, orderTypeID, preOrderDateTime);
    }

    /**
     * The method returns the list of sections of literature from a DB
     * @return list of sections of literature (type of List<BookSection>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<BookSection> getBookSections() throws SQLException, NamingException {
        return (new DAOBookSections()).getBookSections();
    }

    /**
     * The method returns the list of genres of books from a DB
     * @return List of genres of books (type of List<BookGenre>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<BookGenre> getBookGenres() throws SQLException, NamingException {
        return (new DAOBookGenres()).getBookGenres();
    }

    /**
     * The method returns the list of genres of books which contain in the section of literature from a DB
     * @param bookSectionID identifier of the section of literature
     * @return The list of genres of books which belong to the specified section of literature (type of List<BookGenre>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<BookGenre> getBookGenresBySectionID(int bookSectionID) throws SQLException, NamingException {
        return (new DAOBookGenres()).getBookGenresBySectionID(bookSectionID);
    }

    /**
     * The method returns object of the class BookGenre on its identifier
     * @param bookGenreID the book genre identifier in a DB
     * @return Book genre (type of BookGenre)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized BookGenre getBookGenre(int bookGenreID) throws SQLException, NamingException {
        return (new DAOBookGenres()).getBookGenre(bookGenreID);
    }

    /**
     * The method receives the list of types of the order from a DB
     * @return list of types of orders (type of List<OrderType>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<OrderType> getOrderTypes() throws SQLException, NamingException {
        return (new DAOOrderTypes()).getOrderTypes();
    }

    /**
     * The method returns the list of Authors of books from a DB
     * @return (type of List<Author>) list of Authors of books
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<Author> getAuthors() throws SQLException, NamingException {
        return (new DAOAuthors()).getAuthors();
    }

    /**
     * The method returns the list of publishing houses from a DB
     * @return list of publishing houses (type of List<Publisher>)
     * @throws SQLException
     * @throws NamingException
     */
    public static synchronized List<Publisher> getPublishers() throws SQLException, NamingException {
        return (new DAOPublishers()).getPublishers();
    }
}
