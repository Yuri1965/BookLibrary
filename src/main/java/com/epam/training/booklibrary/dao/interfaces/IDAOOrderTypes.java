package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.OrderType;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by URA on 30.09.2015.
 */
public interface IDAOOrderTypes {
    OrderType getOrderType(int orderTypeID) throws SQLException, NamingException;

    OrderType createOrderType(String nameRU, String nameEN) throws SQLException, NamingException;

    OrderType updateOrderType(int orderTypeID, String nameRU, String nameEN) throws SQLException, NamingException;

    List<OrderType> getOrderTypes() throws SQLException, NamingException;
}

