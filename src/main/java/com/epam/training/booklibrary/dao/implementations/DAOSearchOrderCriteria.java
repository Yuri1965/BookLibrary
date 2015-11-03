package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.interfaces.IDAOSearchCriteria;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The class contains methods on preparation of inquiries for a DB in the specified search parameters
 * for work with the list of orders for books
 */
public class DAOSearchOrderCriteria implements IDAOSearchCriteria {
    private static final int DEFAULT_RECORD_PAGE = 0;
    private static final int DEFAULT_PAGE = 0;

    private static final int STATUS_ORDER_ALL = 0;
    private static final int STATUS_ORDER_WAITING = 1;
    private static final int STATUS_ORDER_WORKING = 2;
    private static final int STATUS_ORDER_CLOSED = 3;

    private static final int TYPE_ORDER_ALL = 0;

    private static final int TYPE_SEARCH_BY_USER_ID = 1;
    private static final int TYPE_SEARCH_BY_BOOK_NAME = 2;
    private static final int TYPE_SEARCH_BY_BOOK_AUTHOR = 3;

    private static final int INT_EMPTY_PARAM = 1;

    private int recordCountPage;
    private int numberPage;

    private int statusOrder;
    private int typeOrder;
    private int typeSearch;
    private String searchText;

    /**
     * Constructor of class
     * @param typeSearch int search type (1-on the user's ID, 2-according to the name of the book, 3-on the author of the book)
     * @param searchText the text for search
     * @param statusOrder int the status of the order (everything, 1 expectation, 2-in work, 3-closed)
     * @param typeOrder int order type (0-All, 1-Reading room, 2-On hands)
     * @param recordCountPage int number of records on the page
     * @param numberPage int number of the page
     */
    public DAOSearchOrderCriteria(int typeSearch, String searchText, int statusOrder, int typeOrder,
                                  int recordCountPage, int numberPage) {
        if (typeSearch != TYPE_SEARCH_BY_USER_ID
                && typeSearch != TYPE_SEARCH_BY_BOOK_NAME
                && typeSearch != TYPE_SEARCH_BY_BOOK_AUTHOR) {
            typeSearch = TYPE_SEARCH_BY_USER_ID;
        }
        this.typeSearch = typeSearch;
        if (typeSearch == TYPE_SEARCH_BY_USER_ID && !RequestParamValidator.checkSymbolsNumbers(searchText)) {
            searchText = "";
        }

        if (statusOrder != STATUS_ORDER_ALL
                && statusOrder != STATUS_ORDER_WAITING
                && statusOrder != STATUS_ORDER_WORKING
                && statusOrder != STATUS_ORDER_CLOSED) {
            statusOrder = STATUS_ORDER_ALL;
        }
        this.statusOrder = statusOrder;

        if (typeOrder < 0) {
            typeOrder = TYPE_ORDER_ALL;
        }
        this.typeOrder = typeOrder;

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
    public PreparedStatement preparedStatement(Connection conn, PreparedStatement stmt, String sqlText, String orderBYText) throws SQLException, NamingException {
        StringBuilder whereText = new StringBuilder();
        whereText = whereText.append("\nWHERE (1 = 1)");

        if ((statusOrder != STATUS_ORDER_ALL) || (typeOrder != TYPE_ORDER_ALL)
                || (searchText != null && !searchText.isEmpty())) {

            if (statusOrder == STATUS_ORDER_WAITING) {
                whereText = whereText.append(" AND (userorders.preOrderDateTime IS NOT NULL" +
                        " AND userorders.beginOrderDateTime IS NULL" +
                        " AND userorders.endOrderDateTime IS NULL AND userorders.isCloseOrder = 0)");
            } else
            if (statusOrder == STATUS_ORDER_WORKING) {
                whereText = whereText.append(" AND (userorders.preOrderDateTime IS NOT NULL" +
                        " AND userorders.beginOrderDateTime IS NOT NULL" +
                        " AND userorders.endOrderDateTime IS NULL AND userorders.isCloseOrder = 0)");
            } else
            if (statusOrder == STATUS_ORDER_CLOSED) {
                whereText = whereText.append(" AND (userorders.preOrderDateTime IS NOT NULL" +
                        " AND userorders.endOrderDateTime IS NOT NULL AND userorders.isCloseOrder = 1)");
            }

            if (typeOrder != TYPE_ORDER_ALL) {
                whereText = whereText.append(" AND (userorders.orderTypeID = ?)");
            } else {
                whereText = whereText.append(" AND (1 = ?)");
            }

            if (searchText != null && !searchText.isEmpty()) {
                if (typeSearch == TYPE_SEARCH_BY_USER_ID) {
                    whereText = whereText.append(" AND (userorders.userID = ?)");
                } else {
                    whereText = whereText.append(" AND (1 = ?)");
                }

                if (typeSearch == TYPE_SEARCH_BY_BOOK_NAME) {
                    whereText = whereText.append(" AND (lower(books.name) LIKE ?)");
                } else {
                    whereText = whereText.append(" AND (1 = ?)");
                }

                if (typeSearch == TYPE_SEARCH_BY_BOOK_AUTHOR) {
                    whereText = whereText.append(" AND ((lower(authors.fio_ru) LIKE ?) OR " +
                            "(lower(authors.fio_en) LIKE ?))");
                } else {
                    whereText = whereText.append(" AND ((1 = ?) OR (1 = ?))");
                }

            } else {
                whereText = whereText.append(" AND (1 = ?) AND (1 = ?) AND ((1 = ?) OR (1 = ?))");
            }
        } else {
            whereText = whereText.append(" AND (1 = ?)");
            whereText = whereText.append(" AND (1 = ?)");
            whereText = whereText.append(" AND (1 = ?)");
            whereText = whereText.append(" AND ((1 = ?) OR (1 = ?))");
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

            if (typeOrder != TYPE_ORDER_ALL) {
                stmt.setInt(1, typeOrder);
            } else {
                stmt.setInt(1, INT_EMPTY_PARAM);
            }

            if (searchText != null && !searchText.isEmpty()) {
                if (typeSearch == TYPE_SEARCH_BY_USER_ID) {
                    stmt.setInt(2, Integer.valueOf(searchText));
                } else {
                    stmt.setInt(2, INT_EMPTY_PARAM);
                }

                if (typeSearch == TYPE_SEARCH_BY_BOOK_NAME) {
                    stmt.setString(3, "%" + searchText + "%");
                } else {
                    stmt.setInt(3, INT_EMPTY_PARAM);
                }

                if (typeSearch == TYPE_SEARCH_BY_BOOK_AUTHOR) {
                    stmt.setString(4, "%" + searchText + "%");
                    stmt.setString(5, "%" + searchText + "%");
                } else {
                    stmt.setInt(4, INT_EMPTY_PARAM);
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
