package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.datamodels.enums.Roles;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;
import com.epam.training.booklibrary.datamodels.entity.User;
import com.epam.training.booklibrary.datamodels.entity.UserRole;

import javax.naming.NamingException;
import java.sql.*;
import java.util.*;

/**
 * The class contains methods for work with the authorized user
 */
public final class DAOUser implements IDAOUser {
    /**
     * constants for inquiries in a DB
     */
    private static final String SQL_GET_AUTHORIZATION = "{ call authorization(?, ?) }";

    private static final String SQL_GET_USER_ROLES_BY_USER_ID =
            "SELECT user_roles.id, user_roles.userID, user_roles.roleID, roles.roleName\n" +
                    "FROM user_roles\n" +
                    "LEFT JOIN roles ON user_roles.roleID = roles.id\n" +
                    "WHERE user_roles.userID = ?;";

    private static final String SQL_UPDATE_USER = "{ call updateUser(?, ?, ?, ?, ?) }";

    private static final String SQL_CREATE_USER = "{ call insertUser(?, ?, ?, ?, ?, ?) }";

    /**
     * Whether the sign the user was authorized
     */
    private boolean isAuthorized = false;

    /**
     * Name (Login) of the user for authorization
     */
    private String userName;

    /**
     * The password of the user for authorization
     */
    private String userPass;

    /**
     * Role of the user by default (for not authorized GUEST)
     */
    private String userDefaultRole = Roles.GUEST_LIBRARY.getName();

    /**
     * authorized user
     */
    private User currentUser = null;

    /**
     * The list of roles of the authorized user
     */
    private List<UserRole> currentRolesUser = null;

    /**
     * The constructor of a class for object the Authorized user
     * @param userName Name (Login) of the user
     * @param userPass user password
     */
    public DAOUser(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }

    /**
     * The method returns the user's Login
     * @return user's Login
     */
    public String getUserName() {
        return userName;
    }

    /**
     * The method returns a role of the user by default
     * @return role of the user by default
     */
    public String getUserDefaultRole() {
        return userDefaultRole;
    }

    /**
     * Whether the method is returned by the current user was authorized
     * @return true - if is authorized, false - if isn't authorized
     */
    public boolean isAuthorized() {
        return isAuthorized;
    }

    /**
     * The method returns the current authorized user
     * @return The current authorized user (type of User)
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * The method returns the list of roles of the current authorized user
     * @return The list of roles of the current authorized user (type of List<UserRole>)
     */
    public List<UserRole> getCurrentRolesUser() {
        return currentRolesUser;
    }

    /**
     * Method for realization in classes on work with the authorized user who has to allow
     * registration of the new user
     * @param name_user user login
     * @param pass_user user password
     * @param email_user User's E-mail
     * @param firstName_user user name
     * @param lastName_user Surname of the user
     * @param roleName Role of the user
     * @return The user who was registered (type of User)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    @Override
    public User createUser(String name_user, String pass_user, String email_user,
                           String firstName_user, String lastName_user, String roleName)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        User resultUser = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_CREATE_USER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, name_user);
            stmt.setString(2, pass_user);
            stmt.setString(3, email_user);
            stmt.setString(4, firstName_user);
            stmt.setString(5, lastName_user);
            stmt.setString(6, roleName);

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    resultUser = setUser(rs);
                }
            }

            return resultUser;
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
     * Method of authorization of the user in system
     * @return The user authorized in system (type of User)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    @Override
    public User getAuthorization() throws SQLException, NamingException, MainExceptions.MainErrorException {
        currentUser = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_GET_AUTHORIZATION,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, userName);
            stmt.setString(2, userPass);

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    currentUser = null;
                    currentRolesUser = null;
                    isAuthorized = false;
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    currentUser = setUser(rs);
                    currentRolesUser = getUserRoles();
                    isAuthorized = true;
                }
            }

            return currentUser;
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
     * Method for realization in classes on work with the authorized user who has to receive the list
     * of roles of the authorized user from a DB
     * @return The list of roles of the authorized user (type of List<UserRole>)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    @Override
    public List<UserRole> getUserRoles() throws SQLException, NamingException, MainExceptions.MainErrorException {
        currentRolesUser = new ArrayList<>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            if (currentUser == null ) {
                throw MainExceptions.getMainErrorException("errorUserNotFound");
            }

            conn = DBConnectionPool.getInstance().getConnection();

            stmt = conn.prepareStatement(SQL_GET_USER_ROLES_BY_USER_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setInt(1, currentUser.getId());

            rs = stmt.executeQuery();

            while (rs.next()) {
                UserRole userRole = new UserRole(rs.getInt("id"), rs.getInt("userID"), rs.getInt("roleID"));
                currentRolesUser.add(userRole);
            }

            if (currentRolesUser.size() == 0) {
                currentRolesUser = null;
                throw MainExceptions.getMainErrorException("errorUserRolesNotFound");
            }

            return currentRolesUser;
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
     * Method for realization in classes on work with the authorized user who has to allow to change data
     * of the authorized user
     * @param pass_user user password
     * @param email_user User's E-mail
     * @param firstName_user user name
     * @param lastName_user Surname of the user
     * @return The authorized user which data were changed (type of User)
     * @throws SQLException
     * @throws NamingException
     * @throws MainExceptions.MainErrorException
     */
    @Override
    public User updateUser(String pass_user, String email_user, String firstName_user, String lastName_user)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        User resultUser = null;
        CallableStatement stmt = null;
        ResultSet rs = null;
        Connection conn = null;

        try {
            if (currentUser == null ) {
                throw MainExceptions.getMainErrorException("errorUserNotFound");
            }

            conn = DBConnectionPool.getInstance().getConnection();
            stmt = conn.prepareCall(SQL_UPDATE_USER,
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            stmt.setInt(1, currentUser.getId());
            stmt.setString(2, pass_user);
            stmt.setString(3, email_user);
            stmt.setString(4, firstName_user);
            stmt.setString(5, lastName_user);

            rs = stmt.executeQuery();

            if (rs.first()) {
                if (!rs.getString("errorCode").isEmpty()) {
                    throw MainExceptions.getMainErrorException(rs.getString("errorCode"));
                } else {
                    resultUser = setUser(rs);
                    currentUser = resultUser;
                }
            }

            return resultUser;

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
     * The private method fills with the data obtained from a DB object of User
     * @param rs ResultSet in which there are data obtained from a DB
     * @throws SQLException
     */
    private User setUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setId(rs.getInt("id"));
        user.setUuid_user(rs.getString("uuid_user"));
        user.setName_user(rs.getString("name_user"));
        user.setPass_user(rs.getString("pass_user"));
        user.setEmail_user(rs.getString("email_user"));
        user.setFirstName_user(rs.getString("firstName_user"));
        user.setLastName_user(rs.getString("lastName_user"));
        user.setReg_Date(rs.getDate("reg_Date"));
        user.setBlocked(rs.getBoolean("isBlocked"));
        user.setBlockedDescription(rs.getString("blockedDescription"));

        return user;
    }

    /**
     * The method looks for in the list of roles and returns a role of the user on her name
     * @param role (type of Roles)
     * @return Role of the user (type of UserRole)
     */
    @Override
    public UserRole getUserRoleByName(Roles role) {
        UserRole result = null;

        if (currentRolesUser != null) {
            for (UserRole userRole : currentRolesUser) {
                if (userRole.getRole().getRole().equals(role)) {
                    result = userRole;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * Whether the method checks there is for the user a specified role
     * @param roleName role name
     * @return if = true that is available for the user such role
     */
    public boolean isFoundUserRoleByName(String roleName) {
        boolean result = false;
        Roles role = Roles.getRoleByName(roleName);

        if (getUserRoleByName(role) != null) {
            result = true;
        }

        return result;
    }

}
