package com.epam.training.booklibrary.datamodels.entity;

import com.epam.training.booklibrary.controllers.utils.GeneralUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * Class which contains realization of object - the Order of the user for the book
 */
public class UserOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    // Order ID
    private int id;
    // User data
    private int userID;
    private String firstNameUser;
    private String lastNameUser;
    // Type order data
    private int orderTypeID;
    private String orderTypeNameRU;
    private String orderTypeNameEN;
    // Book data
    private int bookID;
    private String bookName;
    private String bookAuthorNameRU;
    private String bookAuthorNameEN;

    // Order Dates
    private Date preOrderDateTime;
    private Date beginOrderDateTime;
    private Date endOrderDateTime;
    private boolean isCloseOrder;

    /**
     * Constructor of a class by default
     */
    public UserOrder() {
    }

    /**
     * Constructor of a class
     * @param id
     * @param userID
     * @param firstNameUser
     * @param lastNameUser
     * @param orderTypeID
     * @param orderTypeNameRU
     * @param orderTypeNameEN
     * @param bookID
     * @param bookName
     * @param bookAuthorNameRU
     * @param bookAuthorNameEN
     * @param preOrderDateTime
     * @param beginOrderDateTime
     * @param endOrderDateTime
     * @param isCloseOrder
     */
    public UserOrder(int id, int userID, String firstNameUser, String lastNameUser, int orderTypeID,
                     String orderTypeNameRU, String orderTypeNameEN, int bookID, String bookName,
                     String bookAuthorNameRU, String bookAuthorNameEN, Date preOrderDateTime,
                     Date beginOrderDateTime, Date endOrderDateTime, boolean isCloseOrder) {
        this.id = id;
        this.userID = userID;
        this.firstNameUser = firstNameUser;
        this.lastNameUser = lastNameUser;
        this.orderTypeID = orderTypeID;
        this.orderTypeNameRU = orderTypeNameRU;
        this.orderTypeNameEN = orderTypeNameEN;
        this.bookID = bookID;
        this.bookName = bookName;
        this.bookAuthorNameRU = bookAuthorNameRU;
        this.bookAuthorNameEN = bookAuthorNameEN;
        this.preOrderDateTime = preOrderDateTime;
        this.beginOrderDateTime = beginOrderDateTime;
        this.endOrderDateTime = endOrderDateTime;
        this.isCloseOrder = isCloseOrder;
    }

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getFirstNameUser() {
        return firstNameUser;
    }

    public void setFirstNameUser(String firstNameUser) {
        this.firstNameUser = firstNameUser;
    }

    public String getLastNameUser() {
        return lastNameUser;
    }

    public void setLastNameUser(String lastNameUser) {
        this.lastNameUser = lastNameUser;
    }

    public int getOrderTypeID() {
        return orderTypeID;
    }

    public void setOrderTypeID(int orderTypeID) {
        this.orderTypeID = orderTypeID;
    }

    public String getOrderTypeNameRU() {
        return orderTypeNameRU;
    }

    public void setOrderTypeNameRU(String orderTypeNameRU) {
        this.orderTypeNameRU = orderTypeNameRU;
    }

    public String getOrderTypeNameEN() {
        return orderTypeNameEN;
    }

    public void setOrderTypeNameEN(String orderTypeNameEN) {
        this.orderTypeNameEN = orderTypeNameEN;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthorNameRU() {
        return bookAuthorNameRU;
    }

    public void setBookAuthorNameRU(String bookAuthorNameRU) {
        this.bookAuthorNameRU = bookAuthorNameRU;
    }

    public String getBookAuthorNameEN() {
        return bookAuthorNameEN;
    }

    public void setBookAuthorNameEN(String bookAuthorNameEN) {
        this.bookAuthorNameEN = bookAuthorNameEN;
    }

    public Date getPreOrderDateTime() {
        return preOrderDateTime;
    }

    public void setPreOrderDateTime(Date preOrderDateTime) {
        this.preOrderDateTime = preOrderDateTime;
    }

    public Date getBeginOrderDateTime() {
        return beginOrderDateTime;
    }

    public void setBeginOrderDateTime(Date beginOrderDateTime) {
        this.beginOrderDateTime = beginOrderDateTime;
    }

    public Date getEndOrderDateTime() {
        return endOrderDateTime;
    }

    public void setEndOrderDateTime(Date endOrderDateTime) {
        this.endOrderDateTime = endOrderDateTime;
    }

    public boolean isCloseOrder() {
        return isCloseOrder;
    }

    public void setCloseOrder(boolean isCloseOrder) {
        this.isCloseOrder = isCloseOrder;
    }

    // office methods for receiving dates of the order depending on localization

    public String getOrderEndDateByLocale(Locale locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getEndOrderDateTime());
    }

    public String getOrderBeginDateByLocale(Locale locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getBeginOrderDateTime());
    }

    public String getOrderPreDateByLocale(Locale locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getPreOrderDateTime());
    }

    public String getOrderEndDateByLocale(String locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getEndOrderDateTime());
    }

    public String getOrderBeginDateByLocale(String locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getBeginOrderDateTime());
    }

    public String getOrderPreDateByLocale(String locale) {
        return GeneralUtils.getDateFormatterByLocale(locale, true).format(getPreOrderDateTime());
    }

}
