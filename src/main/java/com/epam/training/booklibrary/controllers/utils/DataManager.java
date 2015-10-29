package com.epam.training.booklibrary.controllers.utils;

import com.epam.training.booklibrary.dao.implementations.*;
import com.epam.training.booklibrary.entity.*;
import com.epam.training.booklibrary.exceptions.MainExceptions;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by URA on 15.10.2015.
 */
public class DataManager {
    private static final int ZERO_VALUE = 0;

    // получает количество книг по критерию поиска
    public static synchronized int getCountBooks(DAOSearchBookCriteria searchCriteria) throws SQLException, NamingException {
        int result = 0;

        result = (new DAOBooks(searchCriteria.getRecordCountPage())).getCountBooks(searchCriteria);

        return result;
    }

    // получает количество страниц с книгами по критерию поиска
    public static synchronized int getCountPagesBooks(DAOSearchBookCriteria searchCriteria) throws SQLException, NamingException {
        int result = 0;

        result = (new DAOBooks(searchCriteria.getRecordCountPage())).getCountPages(searchCriteria, searchCriteria.getRecordCountPage());

        return result;
    }

    // получает список книг для страницы по критерию поиска
    public static synchronized List<Book> getListBooks(DAOSearchBookCriteria searchCriteria) throws SQLException, NamingException {
        List<Book> listBook = new ArrayList<Book>();

        listBook = (new DAOBooks(searchCriteria.getRecordCountPage())).getListBooks(searchCriteria, true);

        return listBook;
    }

    // получает данные книги
    public static synchronized Book getBook(int bookID) throws SQLException, NamingException {
        Book book = null;

        book = (new DAOBooks(ZERO_VALUE)).getBook(bookID);

        return book;
    }

    // удаляет книгу
    public static synchronized void deleteBook(int bookID) throws SQLException, NamingException {
        DAOBooks daoBooks = new DAOBooks(ZERO_VALUE);
        daoBooks.deleteBook(bookID);
    }

    // добавляет книгу
    public static synchronized Book createBook(int authorID, int genreID, int publisherID, int publishYear, String isbn,
                                               String name, String shortDescription, int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException {
        Book book = null;
        DAOBooks daoBooks = new DAOBooks(ZERO_VALUE);
        book = daoBooks.createBook(authorID, genreID, publisherID, publishYear, isbn, name, shortDescription,
                                 numberCopies, coverImage);
        return book;
    }

    // изменяет данные книги
    public static synchronized Book updateBook(int bookID, int authorID, int genreID, int publisherID, int publishYear,
                                               String isbn, String name, String shortDescription,
                                               int numberCopies, byte[] coverImage)
            throws SQLException, NamingException, MainExceptions.MainErrorException, IOException {
        Book book = null;
        DAOBooks daoBooks = new DAOBooks(ZERO_VALUE);
        book = daoBooks.updateBook(bookID, authorID, genreID, publisherID, publishYear, isbn, name, shortDescription,
                numberCopies, coverImage);
        return book;
    }

    // получает количество пользователей по критерию поиска
    public static synchronized int getCountUsers(DAOSearchUserCriteria searchCriteria) throws SQLException, NamingException {
        int result = 0;

        result = (new DAOUsers(searchCriteria.getRecordCountPage())).getCountUsers(searchCriteria);

        return result;
    }

    // получает количество страниц с пользователями по критерию поиска
    public static synchronized int getCountPagesUsers(DAOSearchUserCriteria searchCriteria) throws SQLException, NamingException {
        int result = 0;

        result = (new DAOUsers(searchCriteria.getRecordCountPage())).getCountPages(searchCriteria, searchCriteria.getRecordCountPage());

        return result;
    }

    // получает список пользователей для страницы по критерию поиска
    public static synchronized List<UserExt> getListUsers(DAOSearchUserCriteria searchCriteria) throws SQLException, NamingException {
        List<UserExt> listUser = new ArrayList<UserExt>();

        listUser = (new DAOUsers(searchCriteria.getRecordCountPage())).getListUsers(searchCriteria);

        return listUser;
    }

    // получает данные пользователя
    public static synchronized UserExt getUser(int userID) throws SQLException, NamingException {
        UserExt user = null;

        user = (new DAOUsers(ZERO_VALUE)).getUser(userID);

        return user;
    }

    // блокирует пользователя
    public static synchronized UserExt blockUser(int userID, String blockedDescription)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserExt user = null;

        user = (new DAOUsers(ZERO_VALUE)).blockUser(userID, blockedDescription);

        return user;
    }

    // разблокирует пользователя
    public static synchronized UserExt unBlockUser(int userID)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserExt user = null;

        user = (new DAOUsers(ZERO_VALUE)).unBlockUser(userID);

        return user;
    }

    // получает количество заказов на книги по критерию поиска
    public static synchronized int getCountOrders(DAOSearchOrderCriteria searchCriteria) throws SQLException, NamingException {
        int result = 0;

        result = (new DAOOrders(searchCriteria.getRecordCountPage())).getCountOrders(searchCriteria);

        return result;
    }

    // получает количество страниц с заказами на книги по критерию поиска
    public static synchronized int getCountPagesOrders(DAOSearchOrderCriteria searchCriteria) throws SQLException, NamingException {
        int result = 0;

        result = (new DAOOrders(searchCriteria.getRecordCountPage())).getCountPages(searchCriteria, searchCriteria.getRecordCountPage());

        return result;
    }

    // получает список заказов на книги для страницы по критерию поиска
    public static synchronized List<UserOrder> getListOrders(DAOSearchOrderCriteria searchCriteria) throws SQLException, NamingException {
        List<UserOrder> listOrder = new ArrayList<UserOrder>();


        listOrder = (new DAOOrders(searchCriteria.getRecordCountPage())).getListOrders(searchCriteria);

        return listOrder;
    }

    // получает данные заказа по книге
    public static synchronized UserOrder getOrder(int orderID) throws SQLException, NamingException {
        UserOrder userOrder = null;

        userOrder = (new DAOOrders(ZERO_VALUE)).getOrder(orderID);

        return userOrder;
    }

    // переводит заказ на книгу в состояние = Закрыт
    public static synchronized UserOrder closeOrder(int orderID, Date endOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserOrder userOrder = null;

        userOrder = (new DAOOrders(ZERO_VALUE)).closeOrder(orderID, endOrderDateTime);

        return userOrder;
    }

    // переводит заказ на книгу в состояние = в Работе
    public static synchronized UserOrder setWorkOrder(int orderID, Date beginOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserOrder userOrder = null;

        userOrder = (new DAOOrders(ZERO_VALUE)).setWorkOrder(orderID, beginOrderDateTime);

        return userOrder;
    }

    // добавляет заказ на книгу
    public static synchronized UserOrder createOrder(int bookID, int userID, int orderTypeID, Date preOrderDateTime)
            throws SQLException, NamingException, MainExceptions.MainErrorException {
        UserOrder userOrder = null;
        DAOOrders daoOrders = new DAOOrders(ZERO_VALUE);
        userOrder = daoOrders.createOrder(bookID, userID, orderTypeID, preOrderDateTime);
        return userOrder;
    }

    // получает список разделов литературы
    public static synchronized List<BookSection> getBookSections() throws SQLException, NamingException {
        List<BookSection> result = new ArrayList<BookSection>();

        result = (new DAOBookSections()).getBookSections();

        return result;
    }

    // получает список жанров книг
    public static synchronized List<BookGenre> getBookGenres() throws SQLException, NamingException {
        List<BookGenre> result = new ArrayList<BookGenre>();

        result = (new DAOBookGenres()).getBookGenres();

        return result;
    }

    // получает список жанров книг для указанного раздела литературы
    public static synchronized List<BookGenre> getBookGenresBySectionID(int bookSectionID) throws SQLException, NamingException {
        List<BookGenre> result = new ArrayList<BookGenre>();

        result = (new DAOBookGenres()).getBookGenresBySectionID(bookSectionID);

        return result;
    }

    // получает жанр книги по указанному идентификатору записи
    public static synchronized BookGenre getBookGenre(int bookGenreID) throws SQLException, NamingException {
        BookGenre result = null;

        result = (new DAOBookGenres()).getBookGenre(bookGenreID);

        return result;
    }

    // получает список типов заказов
    public static synchronized List<OrderType> getOrderTypes() throws SQLException, NamingException {
        List<OrderType> result = new ArrayList<OrderType>();

        result = (new DAOOrderTypes()).getOrderTypes();

        return result;
    }

    // получает список авторов книг
    public static synchronized List<Author> getAuthors() throws SQLException, NamingException {
        List<Author> result = new ArrayList<Author>();

        result = (new DAOAuthors()).getAuthors();

        return result;
    }

    // получает список издательств
    public static synchronized List<Publisher> getPublishers() throws SQLException, NamingException {
        List<Publisher> result = new ArrayList<Publisher>();

        result = (new DAOPublishers()).getPublishers();

        return result;
    }
}
