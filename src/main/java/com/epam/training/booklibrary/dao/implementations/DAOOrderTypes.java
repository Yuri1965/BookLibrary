package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOOrderTypes;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.entity.OrderType;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The class contains methods for work with types of orders
 */
public class DAOOrderTypes implements IDAOOrderTypes {
    private static final String SQL_GET_ORDER_TYPES = "SELECT id, name_ru, name_en FROM ordertypes ORDER BY name_ru ASC;";
    private static final String SQL_GET_ORDER_TYPE = "SELECT id, name_ru, name_en FROM ordertypes WHERE id = ? limit 1;";
    private static final String SQL_CREATE_ORDER_TYPE = "{ call insertOrderType(?, ?) }";
    private static final String SQL_UPDATE_ORDER_TYPE = "{ call updateOrderType(?, ?, ?) }";

    /**
     * Constructor of a class by default
     */
    public DAOOrderTypes() {
    }

    /**
     * The method receives from a DB order Type on the specified identifier
     * @param orderTypeID int record identifier
     * @return order type (type of OrderType)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public OrderType getOrderType(int orderTypeID) throws SQLException, NamingException {
        OrderType orderType = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_ORDER_TYPE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(orderTypeID));

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                orderType = DAOUtils.setOrderType(rs);
            }

            return orderType;
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
     * The method creates new type of the order in a DB
     * @param nameRU the name like the order in Russian
     * @param nameEN the name like the order in English
     * @return order type (type of OrderType)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public OrderType createOrderType(String nameRU, String nameEN) throws SQLException, NamingException {
        OrderType orderType = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_ORDER_TYPE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, nameRU);
            stmt.setString(2, nameEN);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                orderType = DAOUtils.setOrderType(rs);
            }

            return orderType;
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
     * The method changes data like the order in a DB on the specified identifier
     * @param orderTypeID int record identifier
     * @param nameRU the name like the order in Russian
     * @param nameEN the name like the order in English
     * @return order type (type of OrderType)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public OrderType updateOrderType(int orderTypeID, String nameRU, String nameEN) throws SQLException, NamingException {
        OrderType orderType = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UPDATE_ORDER_TYPE,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(orderTypeID));
            stmt.setString(2, nameRU);
            stmt.setString(3, nameEN);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("id") > 0) {
                orderType = DAOUtils.setOrderType(rs);
            }

            return orderType;
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
     * The method receives the list of types of the order from a DB
     * @return list of types of orders (type of List<OrderType>)
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public List<OrderType> getOrderTypes() throws SQLException, NamingException {
        List<OrderType> orderTypes = new ArrayList<OrderType>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        OrderType orderType = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_ORDER_TYPES,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            rs = stmt.executeQuery();

            while (rs.next()) {
                orderType = DAOUtils.setOrderType(rs);
                orderTypes.add(orderType);
            }

            return orderTypes;
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
