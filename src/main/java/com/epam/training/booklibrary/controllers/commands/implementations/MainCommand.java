package com.epam.training.booklibrary.controllers.commands.implementations;

import com.epam.training.booklibrary.controllers.commands.interfaces.ICommand;
import com.epam.training.booklibrary.controllers.utils.DataManager;
import com.epam.training.booklibrary.controllers.utils.RequestParamValidator;
import com.epam.training.booklibrary.dao.implementations.DAOSearchBookCriteria;
import com.epam.training.booklibrary.dao.implementations.DAOSearchOrderCriteria;
import com.epam.training.booklibrary.dao.implementations.DAOSearchUserCriteria;
import com.epam.training.booklibrary.entity.*;
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
 * Created by URA on 22.10.2015.
 */
public class MainCommand  implements ICommand {
    private Logger logger = LogManager.getLogger(MainCommand.class.getName());
    private static final String MAIN_PAGE = "/pages/main.jsp";

    // для подсчета кол-ва записей и страниц по умолчанию
    private static final int COUNT_PAGE = 0;
    private static final int COUNT_RECORD_PAGE = 0;
    // активная страница по умолчанию
    private static final String DEFAULT_PAGE = "1";

    // кол-во записей на странице книг
    private static final int RECORD_BOOK_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsBookPage", "3"));
    // кол-во записей на странице заказов
    private static final int RECORD_ORDER_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsOrderPage", "4"));
    // кол-во записей на странице пользователей
    private static final int RECORD_USER_PAGE = Integer.valueOf(ApplicationConfigManager.getConfigValue("recordsUserPage", "4"));

    // вид поиска для книг
    private static final String TYPE_SEARCH_BOOK_BY_NAME = "1";
    private static final String TYPE_SEARCH_BOOK_BY_AUTHOR = "2";

    // вид поиска для заказов
    private static final String TYPE_SEARCH_ORDER_BY_USERID = "1";
    private static final String TYPE_SEARCH_ORDER_BY_NAME = "2";
    private static final String TYPE_SEARCH_ORDER_BY_AUTHOR = "3";

    // вид поиска для пользователей
    private static final String TYPE_SEARCH_USER_BY_LOGIN = "1";
    private static final String TYPE_SEARCH_USER_BY_USER_NAME = "2";
    private static final String TYPE_SEARCH_USER_BY_USER_EMAIL = "3";

    // статус заказа
    private static final String ORDER_STATUS_ALL = "0";
    private static final String ORDER_STATUS_EXPECTED = "1";
    private static final String ORDER_STATUS_WORK = "2";
    private static final String ORDER_STATUS_CLOSE = "3";

    // статус пользователя
    private static final String STATUS_USER_UNBLOCKED = "0";
    private static final String STATUS_USER_BLOCKED = "1";
    private static final String STATUS_USER_ALL = "2";

    // раздел литературы по умолчанию (все разделы)
    private static final String DEFAULT_BOOK_SECTION = "0";
    // жанр книг по умолчанию (все жанры)
    private static final String DEFAULT_BOOK_GENRE = "0";
    // тип заказа по умолчанию (все типы)
    private static final String DEFAULT_ORDER_TYPE = "0";

    // табпанель Книги
    private static final String TAB_BOOKS = "books";
    // табпанель Работа с заказами
    private static final String TAB_ORDERS = "workOrders";
    // табпанель Работа с пользователями
    private static final String TAB_USERS = "workUsers";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        //признак первого обращения к ресурсу
        boolean isFirstExecuteCommand = false;
        if (session.getAttribute("pageBookNumber") == null
                || session.getAttribute("pageOrderNumber") == null
                || session.getAttribute("pageUserNumber") == null
                ) {
            isFirstExecuteCommand = true;
        }

        //извлечение из запроса общих параметров
        String paramСurrentTab = request.getParameter("currentTab");

        String paramErrorClear = request.getParameter("errorClear");
        if (paramErrorClear !=null && paramErrorClear.equalsIgnoreCase("true")) {
            session.removeAttribute("currentError");
            session.removeAttribute("autoShowModalForm");
        }

        //извлечение общих атрибутов из сессии
        String attrСurrentTab = (String) session.getAttribute("currentTab");

        //Активная табпанель
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

        try {
            if (paramСurrentTab.equalsIgnoreCase(TAB_BOOKS) || isFirstExecuteCommand) {
                // считывание и установка параметров для табпанели Книги
                getParameterTabBooks(session, request);

                // получение списка разделов литературы
                List<BookSection> listBookSection = DataManager.getBookSections();
                session.setAttribute("listBookSection", listBookSection);

                // получение списка жанров книг
                List<BookGenre> listBookGenre = DataManager.getBookGenres();
                session.setAttribute("listBookGenre", listBookGenre);
            }

            if (paramСurrentTab.equalsIgnoreCase(TAB_ORDERS) || isFirstExecuteCommand) {
                // считывание и установка параметров для табпанели Работа с заказами
                getParameterTabWorkOrders(session, request);

                // получение списка типов заказа
                List<OrderType> listTypeOrder = DataManager.getOrderTypes();
                session.setAttribute("listTypeOrder", listTypeOrder);
            }

            if (paramСurrentTab.equalsIgnoreCase(TAB_USERS) || isFirstExecuteCommand) {
                // считывание и установка параметров для табпанели Работа с пользователями
                getParameterTabWorkUsers(session, request);
            }

        } catch (SQLException ex) {
            logger.error(ex.getMessage());
        } catch (NamingException ex) {
            logger.error(ex.getMessage());
        } finally {
        }

        return MAIN_PAGE;
    }

    private void getParameterTabWorkUsers (HttpSession session, HttpServletRequest request)
            throws ServletException, IOException, SQLException, NamingException {
        //извлечение из запроса параметров
        String paramTypeSearchUser = request.getParameter("typeSearchUser");
        String paramSearchTextUser = request.getParameter("searchTextUser");
        String paramStatusUser = request.getParameter("statusUser");
        String paramUserPageNumber = request.getParameter("pageUserNumber");

        //извлечение атрибутов из сессии
        String attrTypeSearchUser = (String) session.getAttribute("typeSearchUser");
        String attrSearchTextUser = (String) session.getAttribute("searchTextUser");
        String attrStatusUser = (String) session.getAttribute("statusUser");
        String attrUserPageNumber = (String) session.getAttribute("pageUserNumber");

        //Номер страницы
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

        //Вид поиска
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

        //Текст поиска
        if (paramSearchTextUser != null) {
            session.setAttribute("searchTextUser", paramSearchTextUser);
        } else {
            if (attrSearchTextUser == null) {
                session.setAttribute("searchTextUser", "");
            }
        }
        paramSearchTextUser = (String) session.getAttribute("searchTextUser");

        //Статус пользователя
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

        DAOSearchUserCriteria userSearchCriteriaByPager =
                new DAOSearchUserCriteria(Integer.valueOf(paramTypeSearchUser), paramSearchTextUser,
                        Integer.valueOf(paramStatusUser), COUNT_RECORD_PAGE, COUNT_PAGE);

        // получаем кол-во найденных пользователей
        int countUsers = DataManager.getCountUsers(userSearchCriteriaByPager);
        session.setAttribute("countUsers", countUsers);

        // получаем кол-во страниц с найденными пользователями
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

        // получаем список пользователей для указанной страницы
        DAOSearchUserCriteria userSearchCriteriaByListUser =
                new DAOSearchUserCriteria(Integer.valueOf(paramTypeSearchUser), paramSearchTextUser,
                        Integer.valueOf(paramStatusUser), RECORD_USER_PAGE, Integer.valueOf(paramUserPageNumber));
        List<UserExt> listUserPage = DataManager.getListUsers(userSearchCriteriaByListUser);
        session.setAttribute("listUserPage", listUserPage);
    }

    private void getParameterTabWorkOrders (HttpSession session, HttpServletRequest request)
            throws ServletException, IOException, SQLException, NamingException {
        //извлечение из запроса параметров
        String paramTypeSearchOrder = request.getParameter("typeSearchOrder");
        String paramSearchTextOrder = request.getParameter("searchTextOrder");
        String paramTypeOrderID = request.getParameter("typeOrderID");
        String paramStatusOrder = request.getParameter("statusOrder");
        String paramOrderPageNumber = request.getParameter("pageOrderNumber");

        //извлечение атрибутов из сессии
        String attrTypeSearchOrder = (String) session.getAttribute("typeSearchOrder");
        String attrSearchTextOrder = (String) session.getAttribute("searchTextOrder");
        String attrTypeOrderID = (String) session.getAttribute("typeOrderID");
        String attrStatusOrder = (String) session.getAttribute("statusOrder");
        String attrOrderPageNumber = (String) session.getAttribute("pageOrderNumber");

        //Номер страницы
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

        //Вид поиска
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

        //Текст поиска
        if (paramSearchTextOrder != null) {
            session.setAttribute("searchTextOrder", paramSearchTextOrder);
        } else {
            if (attrSearchTextOrder == null) {
                session.setAttribute("searchTextOrder", "");
            }
        }
        paramSearchTextOrder = (String) session.getAttribute("searchTextOrder");

        //Тип заказа
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

        DAOSearchOrderCriteria orderSearchCriteriaByPager =
                new DAOSearchOrderCriteria(Integer.valueOf(paramTypeSearchOrder), paramSearchTextOrder,
                        Integer.valueOf(paramStatusOrder), Integer.valueOf(paramTypeOrderID),
                        COUNT_RECORD_PAGE, COUNT_PAGE);

        // получаем кол-во найденных заказов
        int countOrders = DataManager.getCountOrders(orderSearchCriteriaByPager);
        session.setAttribute("countOrders", countOrders);

        // получаем кол-во страниц с найденными заказами
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

        // получаем список заказов для указанной страницы
        DAOSearchOrderCriteria orderSearchCriteriaByListOrder =
                new DAOSearchOrderCriteria(Integer.valueOf(paramTypeSearchOrder), paramSearchTextOrder,
                        Integer.valueOf(paramStatusOrder), Integer.valueOf(paramTypeOrderID),
                        RECORD_ORDER_PAGE, Integer.valueOf(paramOrderPageNumber));
        List<UserOrder> listOrderPage = DataManager.getListOrders(orderSearchCriteriaByListOrder);
        session.setAttribute("listOrderPage", listOrderPage);
    }

    private void getParameterTabBooks (HttpSession session, HttpServletRequest request)
            throws ServletException, IOException, SQLException, NamingException {
        //извлечение из запроса параметров
        String paramTypeSearchBook = request.getParameter("typeSearchBook");
        String paramSearchTextBook = request.getParameter("searchTextBook");
        String paramBookSectionID = request.getParameter("bookSectionID");
        String paramBookGenreID = request.getParameter("bookGenreID");
        String paramBookPageNumber = request.getParameter("pageBookNumber");

        //извлечение атрибутов из сессии
        String attrTypeSearchBook = (String) session.getAttribute("typeSearchBook");
        String attrSearchTextBook = (String) session.getAttribute("searchTextBook");
        String attrBookSectionID = (String) session.getAttribute("bookSectionID");
        String attrBookGenreID = (String) session.getAttribute("bookGenreID");
        String attrBookPageNumber = (String) session.getAttribute("pageBookNumber");

        //Номер страницы
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

        //Вид поиска
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

        //Текст поиска
        if (paramSearchTextBook != null) {
            session.setAttribute("searchTextBook", paramSearchTextBook);
        } else {
            if (attrSearchTextBook == null) {
                session.setAttribute("searchTextBook", "");
            }
        }
        paramSearchTextBook = (String) session.getAttribute("searchTextBook");

        //Жанр книги
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

        //Раздел литературы
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

        DAOSearchBookCriteria bookSearchCriteriaByPager =
                new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                        Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                        COUNT_RECORD_PAGE, COUNT_PAGE);

        // получаем кол-во найденных книг
        int countBooks = DataManager.getCountBooks(bookSearchCriteriaByPager);
        session.setAttribute("countBooks", countBooks);

        // получаем кол-во страниц с найденными книгами
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

        // получаем список книг для указанной страницы
        DAOSearchBookCriteria bookSearchCriteriaByListBook =
                new DAOSearchBookCriteria(Integer.valueOf(paramTypeSearchBook), paramSearchTextBook,
                        Integer.valueOf(paramBookSectionID), Integer.valueOf(paramBookGenreID),
                        RECORD_BOOK_PAGE, Integer.valueOf(paramBookPageNumber));
        List<Book> listBookPage = DataManager.getListBooks(bookSearchCriteriaByListBook);
        session.setAttribute("listBookPage", listBookPage);
    }
}
