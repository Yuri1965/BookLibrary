package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.dao.interfaces.IDAOSearchCriteria;
import com.epam.training.booklibrary.dao.interfaces.IDAOUsers;
import com.epam.training.booklibrary.dao.utils.DAOUtils;
import com.epam.training.booklibrary.entity.UserExt;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by URA on 24.10.2015.
 */
public class DAOUsers implements IDAOUsers {
    private static final String SQL_GET_USERS_ORDER_DEFAULT = "\n ORDER BY users.name_user ASC";

    private static final String SQL_GET_USERS_COUNTS =
            "SELECT COUNT(users.id) AS count\n" +
            "FROM users\n" +
            "RIGHT JOIN user_roles ON user_roles.userID = users.id\n" +
            "LEFT JOIN roles ON roles.id = user_roles.roleID";

    private static final String SQL_GET_USERS =
            "SELECT users.id AS userID, user_roles.id AS userRoleID, roles.id AS roleID,\n" +
            "users.uuid_user AS uuidUser, users.name_user AS nameUser, users.email_user AS emailUser,\n" +
            "users.reg_Date AS regDateUser, users.firstName_user AS firstNameUser, users.lastName_user AS lastNameUser,\n" +
            "users.isBlocked AS isBlocked, users.blockedDescription AS blockedDescription\n" +
            "FROM users\n" +
            "RIGHT JOIN user_roles ON user_roles.userID = users.id\n" +
            "LEFT JOIN roles ON roles.id = user_roles.roleID";

    private static final String SQL_GET_USER =
            "SELECT users.id AS userID, user_roles.id AS userRoleID, roles.id AS roleID,\n" +
            "users.uuid_user AS uuidUser, users.name_user AS nameUser, users.email_user AS emailUser,\n" +
            "users.reg_Date AS regDateUser, users.firstName_user AS firstNameUser, users.lastName_user AS lastNameUser,\n" +
            "users.isBlocked AS isBlocked, users.blockedDescription AS blockedDescription\n" +
            "FROM users\n" +
            "RIGHT JOIN user_roles ON user_roles.userID = users.id\n" +
            "LEFT JOIN roles ON roles.id = user_roles.roleID\n" +
            "WHERE users.id = ? limit 1";

    private static final String SQL_BLOCK_USER = "{ call blockUser(?, ?) }";
    private static final String SQL_UNBLOCK_USER = "{ call unblockUser(?) }";

    private static final int DEFAULT_RECORD_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsUserPage", "9"));

    private int countRecordPage;

    public DAOUsers(int countRecordPage) {
        if (countRecordPage < DEFAULT_RECORD_PAGE) {
            countRecordPage = DEFAULT_RECORD_PAGE;
        }

        this.countRecordPage = countRecordPage;
    }

    @Override
    public UserExt getUser(int userID) throws SQLException, NamingException {
        UserExt userExt = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareStatement(SQL_GET_USER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, userID);

            rs = stmt.executeQuery();

            if (rs.first() && rs.getInt("userID") > 0) {
                userExt = DAOUtils.setUserExt(rs);
            }

            return userExt;
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

    @Override
    public UserExt blockUser(int userID, String blockedDescription)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserExt userExt = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_BLOCK_USER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(userID));
            stmt.setString(2, blockedDescription);

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    userExt = DAOUtils.setUserExt(rs);
                }
            }

            return userExt;
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

    @Override
    public UserExt unBlockUser(int userID) throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserExt userExt = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UNBLOCK_USER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, Integer.toString(userID));

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    userExt = DAOUtils.setUserExt(rs);
                }
            }

            return userExt;
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

    @Override
    public List<UserExt> getListUsers(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException {
        List<UserExt> listUsers = new ArrayList<UserExt>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;
        UserExt userExt = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();

            stmt = searchCriteria.preparedStatement(conn, stmt, SQL_GET_USERS, SQL_GET_USERS_ORDER_DEFAULT);

            rs = stmt.executeQuery();

            while (rs.next()) {
                userExt = DAOUtils.setUserExt(rs);
                listUsers.add(userExt);
            }

            return listUsers;
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

    @Override
    public int getCountUsers(IDAOSearchCriteria searchCriteria) throws SQLException, NamingException {
        int countUser = 0;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = searchCriteria.preparedStatement(conn, stmt, SQL_GET_USERS_COUNTS, null);

            rs = stmt.executeQuery();

            rs.first();
            countUser = rs.getInt("count");

            return countUser;
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

    @Override
    public int getCountUsersByList(List<UserExt> listUsers) {
        return listUsers.size();
    }

    @Override
    public int getCountPages(IDAOSearchCriteria searchCriteria, int recordCountByPage) throws SQLException, NamingException {
        int result = 0;

        if (recordCountByPage > 0) {
            try {
                int recordCount = getCountUsers(searchCriteria);

                if (recordCount > 0) {
                    float tmpValue = Math.abs((float) recordCount/(float) recordCountByPage);
                    result = Math.round(tmpValue);

                    if ((tmpValue - result) > 0) {
                        result = result + 1;
                    }
                }

                return result;
            }
            finally {
            }
        }

        return result;
    }

    @Override
    public int getCountPagesByList(List<UserExt> listUsers, int recordCountByPage) {
        int result = 0;

        if (recordCountByPage > 0) {
            if (listUsers.size() > 0) {
                float tmpValue = Math.abs((float) listUsers.size()/(float) recordCountByPage);
                result = Math.round(tmpValue);

                if ((tmpValue - result) > 0) {
                    result = result + 1;
                }
            }
        }

        return result;
    }
}
