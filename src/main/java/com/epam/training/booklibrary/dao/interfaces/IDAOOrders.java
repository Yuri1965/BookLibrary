package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.UserOrder;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * The interface contains methods for realization in the class DAOOrders
 */
public interface IDAOOrders {
    /**
     * The method obtains data of the order for the book on the specified identifier from a DB
     * @param orderID int record identifier
     * @return Order for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     */
    UserOrder getOrder(int orderID) throws SQLException, NamingException;

    /**
     * The method closes the order for the book in a DB
     * @param orderID int record identifier
     * @param endOrderDateTime datetime Date and closing-time of the order
     * @return Order for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    UserOrder closeOrder(int orderID, Date endOrderDateTime) throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * The method transfers to a DB the order for the book in a state = In work
     * @param orderID int record identifier
     * @param beginOrderDateTime datetime Date and time of the beginning of the order
     * @return Order for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    UserOrder setWorkOrder(int orderID, Date beginOrderDateTime) throws SQLException, NamingException, MainExceptions.MainErrorException;

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
    UserOrder createOrder(int bookID, int userID, int orderTypeID, Date preOrderDateTime) throws SQLException, NamingException, MainExceptions.MainErrorException;

    /**
     * The method receives from a DB the list of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return list of orders for books (type of List<UserOrder>)
     * @throws SQLException
     * @throws NamingException
     */
    List<UserOrder> getListOrders(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;

    /**
     * The method receives number of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return int number of orders for books
     * @throws SQLException
     * @throws NamingException
     */
    int getCountOrders(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;

    /**
     * The method receives number of orders for books according to the specified list of orders
     * @param listOrders list of orders for books (type of List<UserOrder>)
     * @return int number of orders for books
     */
    int getCountOrdersByList(List<UserOrder> listOrders);

    /**
     * The method receives number of pages with the list of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @param recordCountByPage int number of orders for books on the page
     * @return int number of pages with orders for books
     * @throws SQLException
     * @throws NamingException
     */
    int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException;

    /**
     * The method receives number of pages with orders for books according to the specified list of orders for books
     * @param listOrders list of orders for books (type of List<UserOrder>)
     * @param recordCountByPage int number of orders for books on the page
     * @return int number of pages with orders for books
     */
    int getCountPagesByList(List<UserOrder> listOrders, int recordCountByPage);

    /**
     * The method receives from a DB quantity of free copies of the book for delivery by request
     * @param bookID book identifier
     * @return int quantity of free copies of the book
     * @throws SQLException
     * @throws NamingException
     */
    int getCountFreeBook(int bookID) throws SQLException, NamingException;
}
