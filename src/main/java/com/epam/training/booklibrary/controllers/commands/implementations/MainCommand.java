package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.datamodels.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.implementations.DAOSearchBookCriteria;
import com.epam.training.booklibrary.dao.implementations.DAOSearchOrderCriteria;
import com.epam.training.booklibrary.dao.implementations.DAOSearchUserCriteria;
import com.epam.training.booklibrary.datamodels.entity.*;
import com.epam.training.booklibrary.utils.ApplicationConfigManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Class for processing of the main team
 */
public class MainCommand implements ICommand {
    private Logger logger = LogManager.getLogger(MainCommand.class.getName());
    private static final String MAIN_PAGE = "/WEB-INF/pages/main.jsp";

    // for calculation of number of records and pages by default
    private static final int COUNT_PAGE = 0;
    private static final int COUNT_RECORD_PAGE = 0;
    // active page by default
    private static final String DEFAULT_PAGE = "1";

    // number of records on the page of books
    private static final int RECORD_BOOK_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsBookPage", "3"));
    // number of records on the page of orders
    private static final int RECORD_ORDER_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsOrderPage", "4"));
    // number of records on the page of users
    private static final int RECORD_USER_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsUserPage", "4"));

    // type of search for books
    private static final String TYPE_SEARCH_BOOK_BY_NAME = "1";
    private static final String TYPE_SEARCH_BOOK_BY_AUTHOR = "2";

    // type of search for orders
    private static final String TYPE_SEARCH_ORDER_BY_USERID = "1";
    private static final String TYPE_SEARCH_ORDER_BY_NAME = "2";
    private static final String TYPE_SEARCH_ORDER_BY_AUTHOR = "3";

    // type of search for users
    private static final String TYPE_SEARCH_USER_BY_LOGIN = "1";
    private static final String TYPE_SEARCH_USER_BY_USER_NAME = "2";
    private static final String TYPE_SEARCH_USER_BY_USER_EMAIL = "3";

    //  order status
    private static final String ORDER_STATUS_ALL = "0";
    private static final String ORDER_STATUS_EXPECTED = "1";
    private static final String ORDER_STATUS_WORK = "2";
    private static final String ORDER_STATUS_CLOSE = "3";

    // status of the user
    private static final String STATUS_USER_UNBLOCKED = "0";
    private static final String STATUS_USER_BLOCKED = "1";
    private static final String STATUS_USER_ALL = "2";

    // section of literature by default (all sections)
    private static final String DEFAULT_BOOK_SECTION = "0";
    // genre of books by default (all genres)
    private static final String DEFAULT_BOOK_GENRE = "0";
    // order type by default (all types)
    private static final String DEFAULT_ORDER_TYPE = "0";

    // tabpanel Book
    private static final String TAB_BOOKS = "books";
    // tabpanel Work with orders
    private static final String TAB_ORDERS = "workOrders";
    // tabpanel Work with users
    private static final String TAB_USERS = "workUsers";

    /**
     * Method handler of inquiry of the client
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @return returns a resource for formation of the answer to the client
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        try {
            //sign of the first appeal to a resource
            boolean isFirstExecuteCommand = false;
            if (session.getAttribute("pageBookNumber") == null
                    || session.getAttribute("pageOrderNumber") == null
                    || session.getAttribute("pageUserNumber") == null
                    ) {
                isFirstExecuteCommand = true;
            }

            //extraction from inquiry of the general parameters
            String paramСurrentTab = request.getParameter("currentTab");

            String paramErrorClear = request.getParameter("errorClear");
            if (paramErrorClear != null && paramErrorClear.equalsIgnoreCase("true")) {
                session.removeAttribute("currentError");
                session.removeAttribute("autoShowModalForm");
            }

            //extraction of the general attributes from session
            String attrСurrentTab = (String) session.getAttribute("currentTab");

            //Active tabpanel
            if (paramСurrentTab != null && !paramСurrentTab.isEmpty()) {
                if (!paramСurrentTab.equalsIgnoreCase(TAB_BOOKS)
                        && !paramСurrentTab.equalsIgnoreCase(TAB_ORDERS)
                        && !paramСurrentTab.equalsIgnoreCase(TAB_USERS)) {

                    paramСurrentTab = TAB_BOOKS;
                }

                session.setAttribute("currentTab", paramСurrentTab);
            } else {
                if (attrСurrentTab == null) {
                    session.setAttribute("currentTab", TAB_BOOKS);
                }
            }
            paramСurrentTab = (String) session.getAttribute("currentTab");

            if (paramСurrentTab.equalsIgnoreCase(TAB_BOOKS) || isFirstExecuteCommand) {
                // reading and installation of parameters for Book tabpanel
                getParameterTabBooks(session, request);

                // obtaining list of sections of literature
                List<BookSection> listBookSection = DataManager.getBookSections();
                session.setAttribute("listBookSection", listBookSection);

                // obtaining list of genres of books
                List<BookGenre> listBookGenre = DataManager.getBookGenres();
                session.setAttribute("listBookGenre", listBookGenre);
            }

            if (paramСurrentTab.equalsIgnoreCase(TAB_ORDERS) || isFirstExecuteCommand) {
                // reading and installation of parameters for tabpanel Work with orders
                getParameterTabWorkOrders(session, request);

                // obtaining list of types of the order
                List<OrderType> listTypeOrder = DataManager.getOrderTypes();
                session.setAttribute("listTypeOrder", listTypeOrder);
            }

            if (paramСurrentTab.equalsIgnoreCase(TAB_USERS) || isFirstExecuteCommand) {
                // reading and installation of parameters for tabpanel Work with users
                getParameterTabWorkUsers(session, request);
            }

            session.setAttribute("currentPage", "main");

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            throw new ServletException(ex);
        } finally {
        }

        return MAIN_PAGE;
    }

    /**
     * Method of check of parameters of inquiry on work with users and
     * preparation of data for formation of the answer to the client
     * @param session HttpSession
     * @param request HttpServletRequest
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     * @throws NamingException
     */
    private void getParameterTabWorkUsers(HttpSession session, HttpServletRequest request)
            throws ServletException, IOException, SQLException, NamingException {
        //extraction from inquiry of parameters
        String paramTypeSearchUser = request.getParameter("typeSearchUser");
        String paramSearchTextUser = request.getParameter("searchTextUser");
        String paramStatusUser = request.getParameter("statusUser");
        String paramUserPageNumber = request.getParameter("pageUserNumber");

        //extraction of attributes from session
        String attrTypeSearchUser = (String) session.getAttribute("typeSearchUser");
        String attrSearchTextUser = (String) session.getAttribute("searchTextUser");
        String attrStatusUser = (String) session.getAttribute("statusUser");
        String attrUserPageNumber = (String) session.getAttribute("pageUserNumber");

        //Number of the page
        if (paramUserPageNumber != null && !paramUserPageNumber.isEmpty()) {
            if (!RequestParamValidator.checkSymbolsNumbers(paramUserPageNumber)) {
                paramUserPageNumber = DEFAULT_PAGE;
            }

            session.setAttribute("pageUserNumber", paramUserPageNumber);
        } else {
            if (attrUserPageNumber == null) {
                session.setAttribute("pageUserNumber", DEFAULT_PAGE);
            }
        }
        paramUserPageNumber = (String) session.getAttribute("pageUserNumber");

        //Type of search
        if (paramTypeSearchUser != null && !paramTypeSearchUser.isEmpty()) {
            if (!paramTypeSearchUser.equals(TYPE_SEARCH_USER_BY_LOGIN)
                    && !paramTypeSearchUser.equals(TYPE_SEARCH_USER_BY_USER_NAME)
                    && !paramTypeSearchUser.equals(TYPE_SEARCH_USER_BY_USER_EMAIL)) {
                session.setAttribute("typeSearchUser", TYPE_SEARCH_USER_BY_LOGIN);
            } else {
                session.setAttribute("typeSearchUser", paramTypeSearchUser);
            }
        } else {
            if (attrTypeSearchUser == null) {
                session.setAttribute("typeSearchUser", TYPE_SEARCH_USER_BY_LOGIN);
            }
        }
        paramTypeSearchUser = (String) session.getAttribute("typeSearchUser");

        //Text of search
        if (paramSearchTextUser != null) {
            session.setAttribute("searchTextUser", paramSearchTextUser);
        } else {
            if (attrSearchTextUser == null) {
                session.setAttribute("searchTextUser", "");
            }
        }
        paramSearchTextUser = (String) session.getAttribute("searchTextUser");

        //Status of the user
        if (paramStatusUser != null && !paramStatusUser.isEmpty()) {
            if (!paramStatusUser.equals(STATUS_USER_UNBLOCKED)
                    && !paramStatusUser.equals(STATUS_USER_BLOCKED)
                    && !paramStatusUser.equals(STATUS_USER_ALL)) {
                session.setAttribute("statusUser", STATUS_USER_ALL);
            } else {
                session.setAttribute("statusUser", paramStatusUser);
            }
        } else {
            if (attrStatusUser == null) {
                session.setAttribute("statusUser", ORDER_STATUS_ALL);
            }
        }
        paramStatusUser = (String) session.getAttribute("statusUser");

        //we receive object with criteria of search depending on the transferred inquiry parameters
        DAOSearchUserCriteria userSearchCriteriaByPager =
                new DAOSearchUserCriteria(Integer.valueOf(paramTypeSearchUser), paramSearchTextUser,
                        Integer.valueOf(paramStatusUser), COUNT_RECORD_PAGE, COUNT_PAGE);

        // we receive number of the found users
        int countUsers = DataManager.getCountUsers(userSearchCriteriaByPager);
        session.setAttribute("countUsers", countUsers);

        // we receive number of pages with the found users
        int countUserPages = COUNT_PAGE;
        if (countUsers > 0) {
            userSearchCriteriaByPager.setRecordCountPage(RECORD_USER_PAGE);
            countUserPages = DataManager.getCountPagesUsers(userSearchCriteriaByPager);
        }

        if (countUserPages > 0 && paramUserPageNumber.equals(Integer.toString(COUNT_PAGE))) {
            paramUserPageNumber = DEFAULT_PAGE;
            session.setAttribute("pageUserNumber", paramUserPageNumber);
        }

        session.setAttribute("countUserPages", countUserPages);
        if (Integer.valueOf(paramUserPageNumber) > countUserPages) {
            paramUserPageNumber = Integer.toString(countUserPages);
            session.setAttribute("pageUserNumber", paramUserPageNumber);
        }

        // we receive the list of users for the specified page
        DAOSearchUserCriteria userSearchCriteriaByListUser =
                new DAOSearchUserCriteria(Integer.valueOf(paramTypeSearchUser), paramSearchTextUser,
                        Integer.valueOf(paramStatusUser), RECORD_USER_PAGE, Integer.valueOf(paramUserPageNumber));
        List<UserExt> listUserPage = DataManager.getListUsers(userSearchCriteriaByListUser);
        session.setAttribute("listUserPage", listUserPage);
    }

    /**
     * Method of check of parameters of inquiry on work with orders and
     * preparation of data for formation of the answer to the client
     * @param session HttpSession
     * @param request HttpServletRequest
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     * @throws NamingException
     */
    private void getParameterTabWorkOrders(HttpSession session, HttpServletRequest request)
            throws ServletException, IOException, SQLException, NamingException {
        //extraction from inquiry of parameters
        String paramTypeSearchOrder = request.getParameter("typeSearchOrder");
        String paramSearchTextOrder = request.getParameter("searchTextOrder");
        String paramTypeOrderID = request.getParameter("typeOrderID");
        String paramStatusOrder = request.getParameter("statusOrder");
        String paramOrderPageNumber = request.getParameter("pageOrderNumber");

        //extraction of attributes from session
        String attrTypeSearchOrder = (String) session.getAttribute("typeSearchOrder");
        String attrSearchTextOrder = (String) session.getAttribute("searchTextOrder");
        String attrTypeOrderID = (String) session.getAttribute("typeOrderID");
        String attrStatusOrder = (String) session.getAttribute("statusOrder");
        String attrOrderPageNumber = (String) session.getAttribute("pageOrderNumber");

        //Number of the page
        if (paramOrderPageNumber != null && !paramOrderPageNumber.isEmpty()) {
            if (!RequestParamValidator.checkSymbolsNumbers(paramOrderPageNumber)) {
                paramOrderPageNumber = DEFAULT_PAGE;
            }

            session.setAttribute("pageOrderNumber", paramOrderPageNumber);
        } else {
            if (attrOrderPageNumber == null) {
                session.setAttribute("pageOrderNumber", DEFAULT_PAGE);
            }
        }
        paramOrderPageNumber = (String) session.getAttribute("pageOrderNumber");

        //Type of search
        if (paramTypeSearchOrder != null && !paramTypeSearchOrder.isEmpty()) {
            if (!paramTypeSearchOrder.equals(TYPE_SEARCH_ORDER_BY_NAME)
                    && !paramTypeSearchOrder.equals(TYPE_SEARCH_ORDER_BY_AUTHOR)
                    && !paramTypeSearchOrder.equals(TYPE_SEARCH_ORDER_BY_USERID)) {
                session.setAttribute("typeSearchOrder", TYPE_SEARCH_ORDER_BY_USERID);
            } else {
                session.setAttribute("typeSearchOrder", paramTypeSearchOrder);
            }
        } else {
            if (attrTypeSearchOrder == null) {
                session.setAttribute("typeSearchOrder", TYPE_SEARCH_ORDER_BY_USERID);
            }
        }
        paramTypeSearchOrder = (String) session.getAttribute("typeSearchOrder");

        //Text of search
        if (paramSearchTextOrder != null) {
            session.setAttribute("searchTextOrder", paramSearchTextOrder);
        } else {
            if (attrSearchTextOrder == null) {
                session.setAttribute("searchTextOrder", "");
            }
        }
        paramSearchTextOrder = (String) session.getAttribute("searchTextOrder");

        //order type
        if (paramTypeOrderID != null && !paramTypeOrderID.isEmpty()) {
            if (!RequestParamValidator.checkSymbolsNumbers(paramTypeOrderID)) {
                paramTypeOrderID = DEFAULT_ORDER_TYPE;
            }

            session.setAttribute("typeOrderID", paramTypeOrderID);
        } else {
            if (attrTypeOrderID == null) {
                session.setAttribute("typeOrderID", DEFAULT_ORDER_TYPE);
            }
        }
        paramTypeOrderID = (String) session.getAttribute("typeOrderID");

        //order status
        if (paramStatusOrder != null && !paramStatusOrder.isEmpty()) {
            if (!paramStatusOrder.equals(ORDER_STATUS_ALL)
                    && !paramStatusOrder.equals(ORDER_STATUS_EXPECTED)
                    && !paramStatusOrder.equals(ORDER_STATUS_WORK)
                    && !paramStatusOrder.equals(ORDER_STATUS_CLOSE)) {
                session.setAttribute("statusOrder", ORDER_STATUS_ALL);
            } else {
                session.setAttribute("statusOrder", paramStatusOrder);
            }
        } else {
            if (attrStatusOrder == null) {
                session.setAttribute("statusOrder", ORDER_STATUS_ALL);
            }
        }
        paramStatusOrder = (String) session.getAttribute("statusOrder");

        //we receive object with criteria of search depending on the transferred inquiry parameters
        DAOSearchOrderCriteria orderSearchCriteriaByPager =
                new DAOSearchOrderCriteria(Integer.valueOf(paramTypeSearchOrder), paramSearchTextOrder,
                        Integer.valueOf(paramStatusOrder), Integer.valueOf(paramTypeOrderID),
                        COUNT_RECORD_PAGE, COUNT_PAGE);

        // we receive number of the found orders
        int countOrders = DataManager.getCountOrders(orderSearchCriteriaByPager);
        session.setAttribute("countOrders", countOrders);

        // we receive number of pages with the found orders
        int countOrderPages = COUNT_PAGE;
        if (countOrders > 0) {
            orderSearchCriteriaByPager.setRecordCountPage(RECORD_ORDER_PAGE);
            countOrderPages = DataManager.getCountPagesOrders(orderSearchCriteriaByPager);
        }

        if (countOrderPages > 0 && paramOrderPageNumber.equals(Integer.toString(COUNT_PAGE))) {
            paramOrderPageNumber = DEFAULT_PAGE;
            session.setAttribute("pageOrderNumber", paramOrderPageNumber);
        }

        session.setAttribute("countOrderPages", countOrderPages);
        if (Integer.valueOf(paramOrderPageNumber) > countOrderPages) {
            paramOrderPageNumber = Integer.toString(countOrderPages);
            session.setAttribute("pageOrderNumber", paramOrderPageNumber);
        }

        // we receive the list of orders for the specified page
        DAOSearchOrderCriteria orderSearchCriteriaByListOrder =
                new DAOSearchOrderCriteria(Integer.valueOf(paramTypeSearchOrder), paramSearchTextOrder,
                        Integer.valueOf(paramStatusOrder), Integer.valueOf(paramTypeOrderID),
                        RECORD_ORDER_PAGE, Integer.valueOf(paramOrderPageNumber));
        List<UserOrder> listOrderPage = DataManager.getListOrders(orderSearchCriteriaByListOrder);
        session.setAttribute("listOrderPage", listOrderPage);
    }

    /**
     * Method of check of parameters of inquiry on work with books and
     * preparation of data for formation of the answer to the client
     * @param session HttpSession
     * @param request HttpServletRequest
     * @throws ServletException
     * @throws IOException
     * @throws SQLException
     * @throws NamingException
     */
    private void getParameterTabBooks(HttpSession session, HttpServletRequest request)
            throws ServletException, IOException, SQLException, NamingException {
        //extraction from inquiry of parameters
        String paramTypeSearchBook = request.getParameter("typeSearchBook");
        String paramSearchTextBook = request.getParameter("searchTextBook");
        String paramBookSectionID = request.getParameter("bookSectionID");
        String paramBookGenreID = request.getParameter("bookGenreID");
        String paramBookPageNumber = request.getParameter("pageBookNumber");

        //extraction of attributes from session
        String attrTypeSearchBook = (String) session.getAttribute("typeSearchBook");
        String attrSearchTextBook = (String) session.getAttribute("searchTextBook");
        String attrBookSectionID = (String) session.getAttribute("bookSectionID");
        String attrBookGenreID = (String) session.getAttribute("bookGenreID");
        String attrBookPageNumber = (String) session.getAttribute("pageBookNumber");

        //Number of the page
        if (paramBookPageNumber != null && !paramBookPageNumber.isEmpty()) {
            if (!RequestParamValidator.checkSymbolsNumbers(paramBookPageNumber)) {
                paramBookPageNumber = DEFAULT_PAGE;
            }

            session.setAttribute("pageBookNumber", paramBookPageNumber);
        } else {
            if (attrBookPageNumber == null) {
                session.setAttribute("pageBookNumber", DEFAULT_PAGE);
            }
        }
        paramBookPageNumber = (String) session.getAttribute("pageBookNumber");

        //Type of search
        if (paramTypeSearchBook != null && !paramTypeSearchBook.isEmpty()) {
            if (!paramTypeSearchBook.equals(TYPE_SEARCH_BOOK_BY_NAME) && !paramTypeSearchBook.equals(TYPE_SEARCH_BOOK_BY_AUTHOR)) {
                session.setAttribute("typeSearchBook", TYPE_SEARCH_BOOK_BY_NAME);
            } else {
                session.setAttribute("typeSearchBook", paramTypeSearchBook);
            }
        } else {
            if (attrTypeSearchBook == null) {
                session.setAttribute("typeSearchBook", TYPE_SEARCH_BOOK_BY_NAME);
            }
        }
        paramTypeSearchBook = (String) session.getAttribute("typeSearchBook");

        //Text of search
        if (paramSearchTextBook != null) {
            session.setAttribute("searchTextBook", paramSearchTextBook);
        } else {
            if (attrSearchTextBook == null) {
                session.setAttribute("searchTextBook", "");
            }
        }
        paramSearchTextBook = (String) session.getAttribute("searchTextBook");

        //Book genre
        if (paramBookGenreID != null && !paramBookGenreID.isEmpty()) {
            if (!RequestParamValidator.checkSymbolsNumbers(paramBookGenreID)) {
                paramBookGenreID = DEFAULT_BOOK_GENRE;
            }

            session.setAttribute("bookGenreID", paramBookGenreID);
        } else {
            if (attrBookGenreID == null) {
                session.setAttribute("bookGenreID", DEFAULT_BOOK_GENRE);
            }
        }
        paramBookGenreID = (String) session.getAttribute("bookGenreID");

        //Section of literature
        if (paramBookSectionID != null && !paramBookSectionID.isEmpty()) {
            if (!RequestParamValidator.checkSymbolsNumbers(paramBookSectionID)) {
                paramBookSectionID = DEFAULT_BOOK_SECTION;
            }

            session.setAttribute("bookSectionID", paramBookSectionID);
        } else {
            if (attrBookSectionID == null) {
                session.setAttribute("bookSectionID", DEFAULT_BOOK_SECTION);
            }
        }
        paramBookSectionID = (String) session.getAttribute("bookSectionID");

        //we receive object with criteria of search depending on the transferred inquiry parameters
        DAOSearchBookCriteria bookSearchCriteriaByPager =
                new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                        Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                        COUNT_RECORD_PAGE, COUNT_PAGE);

        // we receive number of the found books
        int countBooks = DataManager.getCountBooks(bookSearchCriteriaByPager);
        session.setAttribute("countBooks", countBooks);

        // we receive number of pages with the found books
        int countBookPages = COUNT_PAGE;
        if (countBooks > 0) {
            bookSearchCriteriaByPager.setRecordCountPage(RECORD_BOOK_PAGE);
            countBookPages = DataManager.getCountPagesBooks(bookSearchCriteriaByPager);
        }

        if (countBookPages > 0 && paramBookPageNumber.equals(Integer.toString(COUNT_PAGE))) {
            paramBookPageNumber = DEFAULT_PAGE;
            session.setAttribute("pageBookNumber", paramBookPageNumber);
        }

        session.setAttribute("countBookPages", countBookPages);
        if (Integer.valueOf(paramBookPageNumber) > countBookPages) {
            paramBookPageNumber = Integer.toString(countBookPages);
            session.setAttribute("pageBookNumber", paramBookPageNumber);
        }

        // we receive the list of books for the specified page
        DAOSearchBookCriteria bookSearchCriteriaByListBook =
                new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                        Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                        RECORD_BOOK_PAGE, Integer.valueOf(paramBookPageNumber));
        List<Book> listBookPage = DataManager.getListBooks(bookSearchCriteriaByListBook);
        session.setAttribute("listBookPage", listBookPage);
    }
}
