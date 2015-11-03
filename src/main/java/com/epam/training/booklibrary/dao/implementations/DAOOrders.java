package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOOrders;
import com.epam.training.booklibrary.dao.interfaces.IDAOSearchCriteria;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.entity.UserOrder;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Класс содержит методы для работы с заказами на книги
 */
public class DAOOrders implements IDAOOrders {
    private static final String SQL_GET_ORDERS_ORDER_DEFAULT = "\n ORDER BY userorders.preOrderDateTime DESC, users.id ASC";

    private static final String SQL_GET_FREE_BOOK_COUNT = "SELECT getFreeBookCount(?) AS freeBookCount";

    private static final String SQL_GET_ORDERS_COUNTS =
            "SELECT COUNT(userorders.id) AS count\n" +
                    "FROM userorders\n" +
                    "LEFT JOIN users ON users.id = userorders.userID\n" +
                    "LEFT JOIN ordertypes ON ordertypes.id = userorders.orderTypeID\n" +
                    "LEFT JOIN books ON books.id = userorders.bookID\n" +
                    "LEFT JOIN authors ON authors.id = books.authorID";

    private static final String SQL_GET_ORDER =
            "SELECT userorders.id,\n" +
                    "userorders.userID, users.firstName_user AS firstNameUser, users.lastName_user AS lastNameUser,\n" +
                    "userorders.orderTypeID, ordertypes.name_ru AS orderTypeNameRU, ordertypes.name_en AS orderTypeNameEN,\n" +
                    "userorders.bookID, books.name AS bookName, authors.fio_ru AS bookAuthorNameRU, authors.fio_en AS bookAuthorNameEN,\n" +
                    "userorders.preOrderDateTime, userorders.beginOrderDateTime, userorders.endOrderDateTime, userorders.isCloseOrder\n" +
                    "FROM userorders\n" +
                    "LEFT JOIN users ON users.id = userorders.userID\n" +
                    "LEFT JOIN ordertypes ON ordertypes.id = userorders.orderTypeID\n" +
                    "LEFT JOIN books ON books.id = userorders.bookID\n" +
                    "LEFT JOIN authors ON authors.id = books.authorID\n" +
                    "WHERE userorders.id = ? limit 1";

    private static final String SQL_GET_ORDERS =
            "SELECT userorders.id,\n" +
                    "userorders.userID, users.firstName_user AS firstNameUser, users.lastName_user AS lastNameUser,\n" +
                    "userorders.orderTypeID, ordertypes.name_ru AS orderTypeNameRU, ordertypes.name_en AS orderTypeNameEN,\n" +
                    "userorders.bookID, books.name AS bookName, authors.fio_ru AS bookAuthorNameRU, authors.fio_en AS bookAuthorNameEN,\n" +
                    "userorders.preOrderDateTime, userorders.beginOrderDateTime, userorders.endOrderDateTime, userorders.isCloseOrder\n" +
                    "FROM userorders\n" +
                    "LEFT JOIN users ON users.id = userorders.userID\n" +
                    "LEFT JOIN ordertypes ON ordertypes.id = userorders.orderTypeID\n" +
                    "LEFT JOIN books ON books.id = userorders.bookID\n" +
                    "LEFT JOIN authors ON authors.id = books.authorID";

    private static final String SQL_CREATE_ORDER = "{ call insertUserOrder(?, ?, ?, ?) }";
    private static final String SQL_CLOSE_ORDER = "{ call closeUserOrder(?, ?) }";
    private static final String SQL_SET_WORK_ORDER = "{ call setWorkUserOrder(?, ?) }";

    private static final int DEFAULT_RECORD_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsOrderPage", "6"));

    // number of records on the page
    private int countRecordPage;

    /**
     * Constructor of a class
     * @param countRecordPage int number of records on the page
     */
    public DAOOrders(int countRecordPage) {
        if (countRecordPage < DEFAULT_RECORD_PAGE) {
            countRecordPage = DEFAULT_RECORD_PAGE;
        }

        this.countRecordPage = countRecordPage;
    }

    /**
     * The method obtains data of the order for the book on the specified identifier from a DB
     * @param orderID int record identifier
     * @return Order for the book (type of UserOrder)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public UserOrder getOrder(int orderID) throws SQLException, NamingException {
        UserOrder userOrder = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_ORDER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, orderID);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                userOrder = DAOUtils.setOrder(rs);
            }

            return userOrder;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
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
    @Override
    public UserOrder closeOrder(int orderID, Date endOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserOrder userOrder = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CLOSE_ORDER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(orderID));
            stmt.setTimestamp(2, new Timestamp(endOrderDateTime.getTime()));

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    userOrder = DAOUtils.setOrder(rs);
                }
            }

            return userOrder;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
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
    @Override
    public UserOrder setWorkOrder(int orderID, Date beginOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserOrder userOrder = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_SET_WORK_ORDER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(orderID));
            stmt.setTimestamp(2, new Timestamp(beginOrderDateTime.getTime()));

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    userOrder = DAOUtils.setOrder(rs);
                }
            }

            return userOrder;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
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
    @Override
    public UserOrder createOrder(int bookID, int userID, int orderTypeID, Date preOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserOrder userOrder = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_ORDER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(bookID));
            stmt.setString(2, Integer.toString(userID));
            stmt.setString(3, Integer.toString(orderTypeID));
            stmt.setTimestamp(4, new Timestamp(preOrderDateTime.getTime()));

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    userOrder = DAOUtils.setOrder(rs);
                }
            }

            return userOrder;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }

    /**
     * The method receives from a DB the list of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return list of orders for books (type of List<UserOrder>)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public List<UserOrder> getListOrders(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException {
        List<UserOrder> listOrders = new ArrayList<UserOrder>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        UserOrder userOrder = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();

            stmt = searchCriteria.preparedStatement(conn, stmt, SQL_GET_ORDERS, SQL_GET_ORDERS_ORDER_DEFAULT);

            rs = stmt.executeQuery();

            while (rs.next()) {
                userOrder = DAOUtils.setOrder(rs);
                listOrders.add(userOrder);
            }

            return listOrders;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }

    /**
     * The method receives number of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @return int number of orders for books
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public int getCountOrders(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException {
        int countOrder = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = searchCriteria.preparedStatement(conn, stmt, SQL_GET_ORDERS_COUNTS, null);

            rs = stmt.executeQuery();

            rs.first();
            countOrder = rs.getInt("count");

            return countOrder;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }

    /**
     * The method receives number of orders for books according to the specified list of orders
     * @param listOrders list of orders for books (type of List<UserOrder>)
     * @return int number of orders for books
     */
    @Override
    public int getCountOrdersByList(List<UserOrder> listOrders) {
        return listOrders.size();
    }

    /**
     * The method receives number of pages with the list of orders for books in the specified search parameters
     * @param searchCriteria search option (type of IDAOSearchCriteria)
     * @param recordCountByPage int number of orders for books on the page
     * @return int number of pages with orders for books
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException {
        int result = 0;

        if (recordCountByPage > 0) {
            try {
                int recordCount = getCountOrders(searchCriteria);

                if (recordCount > 0) {
                    float tmpValue = Math.abs((float) recordCount/(float) recordCountByPage);
                    result = Math.round(tmpValue);

                    if ((tmpValue - result) > 0) {
                        result++;
                    }
                }

                return result;
            }
            finally {
            }
        }

        return result;
    }

    /**
     * The method receives number of pages with orders for books according to the specified list of orders for books
     * @param listOrders list of orders for books (type of List<UserOrder>)
     * @param recordCountByPage int number of orders for books on the page
     * @return int number of pages with orders for books
     */
    @Override
    public int getCountPagesByList(List<UserOrder> listOrders, int recordCountByPage) {
        int result = 0;

        if (recordCountByPage > 0) {
            if (listOrders.size() > 0) {
                float tmpValue = Math.abs((float) listOrders.size()/(float) recordCountByPage);
                result = Math.round(tmpValue);

                if ((tmpValue - result) > 0) {
                    result++;
                }
            }
        }

        return result;
    }

    /**
     * The method receives from a DB quantity of free copies of the book for delivery by request
     * @param bookID book identifier
     * @return int quantity of free copies of the book
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public int getCountFreeBook(int bookID) throws SQLException, NamingException {
        int countFreeBook = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_FREE_BOOK_COUNT,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, bookID);

            rs = stmt.executeQuery();

            rs.first();
            countFreeBook = rs.getInt("freeBookCount");

            return countFreeBook;
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } finally {
            }
        }
    }
}
