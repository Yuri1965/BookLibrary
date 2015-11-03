package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.OrderType;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * The interface contains methods for realization in the class DAOOrderTypes
 */
public interface IDAOOrderTypes {
    /**
     * The method receives from a DB order Type on the specified identifier
     * @param orderTypeID int record identifier
     * @return order type (type of OrderType)
     * @throws SQLException
     * @throws NamingException
     */
    OrderType getOrderType(int orderTypeID) throws SQLException, NamingException;

    /**
     * The method creates new type of the order in a DB
     * @param nameRU the name like the order in Russian
     * @param nameEN the name like the order in English
     * @return order type (type of OrderType)
     * @throws SQLException
     * @throws NamingException
     */
    OrderType createOrderType(String nameRU, String nameEN) throws SQLException, NamingException;

    /**
     * The method changes data like the order in a DB on the specified identifier
     * @param orderTypeID int record identifier
     * @param nameRU the name like the order in Russian
     * @param nameEN the name like the order in English
     * @return order type (type of OrderType)
     * @throws SQLException
     * @throws NamingException
     */
    OrderType updateOrderType(int orderTypeID, String nameRU, String nameEN) throws SQLException, NamingException;

    /**
     * The method receives the list of types of the order from a DB
     * @return list of types of orders (type of List<OrderType>)
     * @throws SQLException
     * @throws NamingException
     */
    List<OrderType> getOrderTypes() throws SQLException, NamingException;
}

