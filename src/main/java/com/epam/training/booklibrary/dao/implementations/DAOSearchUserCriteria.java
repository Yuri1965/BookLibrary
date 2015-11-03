package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.interfaces.IDAOSearchCriteria;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class contains methods on preparation of inquiries for a DB in the
 * specified search parameters for work with the list of users
 */
public class DAOSearchUserCriteria implements IDAOSearchCriteria {
    private static final int DEFAULT_RECORD_PAGE = 0;
    private static final int DEFAULT_PAGE = 0;

    private static final int TYPE_SEARCH_BY_LOGIN = 1;
    private static final int TYPE_SEARCH_BY_USER_NAME = 2;
    private static final int TYPE_SEARCH_BY_USER_EMAIL = 3;

    private static final int STATUS_UNBLOCKED = 0;
    private static final int STATUS_BLOCKED = 1;
    private static final int STATUS_ALL = 2;

    private static final int INT_EMPTY_PARAM = 1;

    private int recordCountPage;
    private int numberPage;

    private int status;
    private int typeSearch;
    private String searchText;

    /**
     * Constructor of class
     * @param typeSearch int search type (1-on the user's Login, 2-on a name and a surname of the user, by the user's e-mail)
     * @param searchText the text for search
     * @param status int статус пользователя (0 открытый, 1 заблокированный, 2 - все)
     * @param recordCountPage int number of records on the page
     * @param numberPage int number of the page
     */
    public DAOSearchUserCriteria(int typeSearch, String searchText, int status, int recordCountPage, int numberPage) {
        if (typeSearch != TYPE_SEARCH_BY_LOGIN
                && typeSearch != TYPE_SEARCH_BY_USER_NAME
                && typeSearch != TYPE_SEARCH_BY_USER_EMAIL) {
            typeSearch = TYPE_SEARCH_BY_LOGIN;
        }
        this.typeSearch = typeSearch;

        if (status != STATUS_ALL
                && status != STATUS_UNBLOCKED
                && status != STATUS_BLOCKED) {
            status = STATUS_ALL;
        }
        this.status = status;

        if (searchText == null) {
            searchText = "";
        }
        this.searchText = searchText;

        if (recordCountPage < DEFAULT_RECORD_PAGE) {
            recordCountPage = DEFAULT_RECORD_PAGE;
        }
        this.recordCountPage = recordCountPage;

        if (numberPage < DEFAULT_PAGE) {
            numberPage = DEFAULT_PAGE;
        }
        this.numberPage = numberPage;
    }

    // getters and setters
    public int getRecordCountPage() {
        return recordCountPage;
    }

    public void setRecordCountPage(int recordCountPage) {
        this.recordCountPage = recordCountPage;
    }

    /**
     * Method of preparation of parameters of search and creation of inquiry to a DB
     * @param conn Connection with a DB (type of Connection)
     * @param stmt Inquiry to a DB (type of PreparedStatement)
     * @param sqlText the text of inquiry to a DB
     * @param orderBYText part of inquiry to a DB which is responsible for an order of sorting of data
     * @return the inquiry (type of PreparedStatement) prepared for performance
     * @throws SQLException
     * @throws NamingException
     */
    @Override
    public PreparedStatement preparedStatement(Connection conn, PreparedStatement stmt, String sqlText,
                                               String orderBYText) throws SQLException, NamingException {
        StringBuilder whereText = new StringBuilder();
        whereText = whereText.append("\nWHERE (roles.roleName='READER')");

        if ((status != STATUS_ALL) || (searchText != null && !searchText.isEmpty())) {
            if (status != STATUS_ALL) {
                whereText = whereText.append(" AND (users.isBlocked = ?)");
            } else {
                whereText = whereText.append(" AND (1 = ?)");
            }

            if (searchText != null && !searchText.isEmpty()) {
                if (typeSearch == TYPE_SEARCH_BY_LOGIN) {
                    whereText = whereText.append(" AND (lower(users.name_user) LIKE ?)");
                } else {
                    whereText = whereText.append(" AND (1 = ?)");
                }

                if (typeSearch == TYPE_SEARCH_BY_USER_NAME) {
                    whereText = whereText.append(" AND ((lower(users.firstName_user) LIKE ?) OR " +
                            "(lower(users.lastName_user) LIKE ?))");
                } else {
                    whereText = whereText.append(" AND ((1 = ?) OR (1 = ?))");
                }

                if (typeSearch == TYPE_SEARCH_BY_USER_EMAIL) {
                    whereText = whereText.append(" AND (lower(users.email_use) LIKE ?)");
                } else {
                    whereText = whereText.append(" AND (1 = ?)");
                }
            } else {
                whereText = whereText.append(" AND (1 = ?) AND ((1 = ?) OR (1 = ?)) AND (1 = ?)");
            }
        } else {
            whereText = whereText.append(" AND (1 = ?)");
            whereText = whereText.append(" AND (1 = ?)");
            whereText = whereText.append(" AND ((1 = ?) OR (1 = ?))");
            whereText = whereText.append(" AND (1 = ?)");
        }

        String limitText = "";
        if (recordCountPage != DEFAULT_RECORD_PAGE && numberPage != DEFAULT_PAGE) {
            limitText = "\n limit ?, ?";
        } else {
            whereText = whereText.append(" AND (1 = ?) AND (1 = ?)");
        }

        if (orderBYText == null) {
            orderBYText = "";
        }

        sqlText = sqlText + whereText + orderBYText + limitText;
        try {
            stmt = conn.prepareStatement(sqlText, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            if (status != STATUS_ALL) {
                stmt.setInt(1, status);
            } else {
                stmt.setInt(1, INT_EMPTY_PARAM);
            }

            if (searchText != null && !searchText.isEmpty()) {
                if (typeSearch == TYPE_SEARCH_BY_LOGIN) {
                    stmt.setString(2, "%" + searchText + "%");
                } else {
                    stmt.setInt(2, INT_EMPTY_PARAM);
                }

                if (typeSearch == TYPE_SEARCH_BY_USER_NAME) {
                    stmt.setString(3, "%" + searchText + "%");
                    stmt.setString(4, "%" + searchText + "%");
                } else {
                    stmt.setInt(3, INT_EMPTY_PARAM);
                    stmt.setInt(4, INT_EMPTY_PARAM);
                }

                if (typeSearch == TYPE_SEARCH_BY_USER_EMAIL) {
                    stmt.setString(5, "%" + searchText + "%");
                } else {
                    stmt.setInt(5, INT_EMPTY_PARAM);
                }
            } else {
                stmt.setInt(2, INT_EMPTY_PARAM);
                stmt.setInt(3, INT_EMPTY_PARAM);
                stmt.setInt(4, INT_EMPTY_PARAM);
                stmt.setInt(5, INT_EMPTY_PARAM);
            }

            if (recordCountPage != DEFAULT_RECORD_PAGE && numberPage != DEFAULT_PAGE) {
                stmt.setInt(6, ((numberPage - 1) * recordCountPage));
                stmt.setInt(7, recordCountPage);
            } else {
                stmt.setInt(6, INT_EMPTY_PARAM);
                stmt.setInt(7, INT_EMPTY_PARAM);
            }
        }
        finally {
        }

        return stmt;
    }
}
