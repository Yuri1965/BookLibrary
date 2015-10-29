package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.dbconnection.DBConnectionPool;
import com.epam.training.booklibrary.enums.Roles;
import com.epam.training.booklibrary.exceptions.MainExceptions;
import com.epam.training.booklibrary.dao.interfaces.IDAOUser;
import com.epam.training.booklibrary.entity.User;
import com.epam.training.booklibrary.entity.UserRole;

import javax.naming.NamingException;
import java.sql.*;
import java.util.*;

/**
 * Created by URA on 13.09.2015.
 * Класс для работы с авторизованным пользователем
 */
public final class DAOUser implements IDAOUser {
    /**
     * константы для запросов в БД
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
     * Признак был ли пользователь авторизован
     */
    private boolean isAuthorized = false;

    /**
     * Имя (Login) пользователя для авторизации
     */
    private String userName;

    /**
     * Пароль пользователя для авторизации
     */
    private String userPass;

    /**
     * Роль пользователя по умолчанию (для не авторизованных GUEST)
     */
    private String userDefaultRole = Roles.GUEST_LIBRARY.getName();

    /**
     * Авторизованный пользователь
     */
    private User currentUser = null;

    /**
     * Список ролей авторизованного пользователя
     */
    private List<UserRole> currentRolesUser = null;

    /**
     * Конструктор для объекта Авторизованный пользователь
     * @param userName Имя(Login) пользователя
     * @param userPass Пароль пользователя
     */
    public DAOUser(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserDefaultRole() {
        return userDefaultRole;
    }

    /**
     * Метод возвращает был ли текущий пользователь авторизован
     * @return true - если авторизован, false - если не авторизован
     */
    public boolean isAuthorized() {
        return isAuthorized;
    }

    /**
     * Метод возвращает текущего авторизованного пользователя
     * @return Текущий авторизованный пользователь
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Метод возвращает список ролей текущего авторизованного пользователя
     * @return Список ролей текущего авторизованного пользователя
     */
    public List<UserRole> getCurrentRolesUser() {
        return currentRolesUser;
    }

    /**
     * Метод регистрации нового пользователя
     * @param name_user Login пользователя
     * @param pass_user Пароль пользователя
     * @param email_user Email пользователя
     * @param firstName_user Имя пользователя
     * @param lastName_user Фамилия пользователя
     * @return Пользователь, который был зарегистрирован
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
     * Метод авторизации пользователя
     * @return Пользователь, который был авторизован (если авторизация закончилась неудачей, то возвращается null )
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
     * Метод получает из БД роли текущего авторизованного пользователя
     * @return Список ролей текущего авторизованного пользователя (если роли отсутствуют, то возвращается null)
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
     * Метод изменяет данные текущего авторизованного пользователя
     * @param pass_user Пароль пользователя
     * @param email_user Email пользователя
     * @param firstName_user Имя пользователя
     * @param lastName_user Фамилия пользователя
     * @return Пользователь данные, которого были изменены в БД (в случае неудачи возвращается null)
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
     * Приватный метод заполняет данными, полученными из БД объект User
     * @param rs ResultSet, в котором находятся данные полученные из БД
     * @throws SQLException ошибка в случае неудачи
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
     * Реализация метода интерфейса IDAOUser. Ищет в списке ролей и возвращает роль пользователя
     * @param role
     * @return Роль пользователя
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

    public boolean isFoundUserRoleByName(String roleName) {
        boolean result = false;
        Roles role = Roles.getRoleByName(roleName);

        if (getUserRoleByName(role) != null) {
            result = true;
        }

        return result;
    }

}
