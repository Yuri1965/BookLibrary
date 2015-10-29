package com.epam.training.booklibrary.dao.implementations;

import com.epam.training.booklibrary.dao.interfaces.IDAOSearchCriteria;

import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by URA on 18.10.2015.
 */
public class DAOSearchBookCriteria implements IDAOSearchCriteria {
    private static final int DEFAULT_RECORD_PAGE = 0;
    private static final int DEFAULT_PAGE = 0;

    private static final int TYPE_SEARCH_BY_NAME = 1;
    private static final int TYPE_SEARCH_BY_AUTHOR = 2;

    private static final int DEFAULT_BOOK_SECTION = 0;
    private static final int DEFAULT_BOOK_GENRE = 0;

    private static final int INT_EMPTY_PARAM = 1;

    private int recordCountPage;
    private int numberPage;

    private int typeSearch;
    private String searchText;
    private int bookSectionID;
    private int bookGenreID;

    public DAOSearchBookCriteria(int typeSearch, String searchText, int bookSectionID, int bookGenreID,
                                 int recordCountPage, int numberPage) {
        if (typeSearch != TYPE_SEARCH_BY_NAME && typeSearch != TYPE_SEARCH_BY_AUTHOR) {
            typeSearch = TYPE_SEARCH_BY_NAME;
        }
        this.typeSearch = typeSearch;

        if (searchText == null) {
            searchText = "";
        }
        this.searchText = searchText;

        if (bookSectionID < DEFAULT_BOOK_SECTION) {
            bookSectionID = DEFAULT_BOOK_SECTION;
        }
        this.bookSectionID = bookSectionID;

        if (bookGenreID < DEFAULT_BOOK_GENRE) {
            bookGenreID = DEFAULT_BOOK_GENRE;
        }
        this.bookGenreID = bookGenreID;

        if (recordCountPage < DEFAULT_RECORD_PAGE) {
            recordCountPage = DEFAULT_RECORD_PAGE;
        }
        this.recordCountPage = recordCountPage;

        if (numberPage < DEFAULT_PAGE) {
            numberPage = DEFAULT_PAGE;
        }
        this.numberPage = numberPage;
    }

    public int getRecordCountPage() {
        return recordCountPage;
    }

    public void setRecordCountPage(int recordCountPage) {
        this.recordCountPage = recordCountPage;
    }

    @Override
    public PreparedStatement preparedStatement(Connection conn, PreparedStatement stmt, String sqlText,
                                               String orderBYText) throws SQLException, NamingException {

        StringBuilder whereText = new StringBuilder();

        if ((bookSectionID != DEFAULT_BOOK_SECTION) || (bookGenreID != DEFAULT_BOOK_GENRE)
                || (searchText != null && !searchText.isEmpty())) {
            whereText = whereText.append("\nWHERE (books.isDeleted = 0)");

            if (bookSectionID != DEFAULT_BOOK_SECTION) {
                whereText = whereText.append(" AND (booksections.id = ?)");
            } else {
                whereText = whereText.append(" AND (1 = ?)");
            }

            if (bookGenreID != DEFAULT_BOOK_GENRE) {
                whereText = whereText.append(" AND (books.genreID = ?)");
            } else {
                whereText = whereText.append(" AND (1 = ?)");
            }

            if (searchText != null && !searchText.isEmpty()) {
                if (typeSearch == TYPE_SEARCH_BY_NAME) {
                    whereText = whereText.append(" AND (lower(books.name) LIKE ?)");
                } else {
                    whereText = whereText.append(" AND (1 = ?)");
                }

                if (typeSearch == TYPE_SEARCH_BY_AUTHOR) {
                    whereText = whereText.append(" AND ((lower(authors.fio_ru) LIKE ?) OR " +
                            "(lower(authors.fio_en) LIKE ?))");
                } else {
                    whereText = whereText.append(" AND ((1 = ?) OR (1 = ?))");
                }
            } else {
                whereText = whereText.append(" AND (1 = ?) AND ((1 = ?) OR (1 = ?))");
            }
        } else {
            whereText = whereText.append("\nWHERE (books.isDeleted = 0) AND (1 = ?)");
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

            if (bookSectionID != DEFAULT_BOOK_SECTION) {
                stmt.setInt(1, bookSectionID);
            } else {
                stmt.setInt(1, INT_EMPTY_PARAM);
            }

            if (bookGenreID != DEFAULT_BOOK_GENRE) {
                stmt.setInt(2, bookGenreID);
            } else {
                stmt.setInt(2, INT_EMPTY_PARAM);
            }

            if (searchText != null && !searchText.isEmpty()) {
                if (typeSearch == TYPE_SEARCH_BY_NAME) {
                    stmt.setString(3, "%" + searchText + "%");
                } else {
                    stmt.setInt(3, INT_EMPTY_PARAM);
                }

                if (typeSearch == TYPE_SEARCH_BY_AUTHOR) {
                    stmt.setString(4, "%" + searchText + "%");
                    stmt.setString(5, "%" + searchText + "%");
                } else {
                    stmt.setInt(4, INT_EMPTY_PARAM);
                    stmt.setInt(5, INT_EMPTY_PARAM);
                }
            } else {
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
