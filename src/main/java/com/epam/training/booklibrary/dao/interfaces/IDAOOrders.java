package com.epam.training.booklibrary.dao.interfaces;

import com.epam.training.booklibrary.entity.UserOrder;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by URA on 28.10.2015.
 */
public interface IDAOOrders {
    UserOrder getOrder(int orderID) throws SQLException, NamingException;

    UserOrder closeOrder(int orderID, Date endOrderDateTime) throws SQLException, NamingException, MainExceptions.MainErrorException;

    UserOrder setWorkOrder(int orderID, Date beginOrderDateTime) throws SQLException, NamingException, MainExceptions.MainErrorException;

    UserOrder createOrder(int bookID, int userID, int orderTypeID, Date preOrderDateTime) throws SQLException, NamingException, MainExceptions.MainErrorException;

    List<UserOrder> getListOrders(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;

    int getCountOrders(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException;
    int getCountOrdersByList(List<UserOrder> listOrders);

    int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException;
    int getCountPagesByList(List<UserOrder> listOrders, int recordCountByPage);
}
