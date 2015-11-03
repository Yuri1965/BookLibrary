package com.epam.training.booklibrary.dao.utils;

import com.epam.training.booklibrary.datamodels.entity.*;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class factory which contains methods for filling of DAO of objects with the data obtained from a DB
 */
public class DAOUtils {
    /**
     * The method fills with data from a DB object of the class UserOrder
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class UserOrder
     * @throws SQLException
     */
    public static UserOrder setOrder(ResultSet rs) throws SQLException {
        UserOrder userOrder = new UserOrder();

        userOrder.setId(rs.getInt("id"));

        userOrder.setUserID(rs.getInt("userID"));
        userOrder.setFirstNameUser(rs.getString("firstNameUser"));
        userOrder.setLastNameUser(rs.getString("lastNameUser"));

        userOrder.setOrderTypeID(rs.getInt("orderTypeID"));
        userOrder.setOrderTypeNameRU(rs.getString("orderTypeNameRU"));
        userOrder.setOrderTypeNameEN(rs.getString("orderTypeNameEN"));

        userOrder.setBookID(rs.getInt("bookID"));
        userOrder.setBookName(rs.getString("bookName"));
        userOrder.setBookAuthorNameRU(rs.getString("bookAuthorNameRU"));
        userOrder.setBookAuthorNameEN(rs.getString("bookAuthorNameEN"));

        userOrder.setPreOrderDateTime(rs.getTimestamp("preOrderDateTime"));
        userOrder.setBeginOrderDateTime(rs.getTimestamp("beginOrderDateTime"));
        userOrder.setEndOrderDateTime(rs.getTimestamp("endOrderDateTime"));
        userOrder.setCloseOrder(rs.getBoolean("isCloseOrder"));

        return userOrder;
    }

    /**
     * The method fills with data from a DB object of the class UserExt
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class UserExt
     * @throws SQLException
     */
    public static UserExt setUserExt(ResultSet rs) throws SQLException {
        UserExt userExt = new UserExt();

        userExt.setId(rs.getInt("userID"));
        userExt.setUuid_user(rs.getString("uuidUser"));
        userExt.setName_user(rs.getString("nameUser"));
        userExt.setEmail_user(rs.getString("emailUser"));
        userExt.setPass_user("");
        userExt.setFirstName_user(rs.getString("firstNameUser"));
        userExt.setLastName_user(rs.getString("lastNameUser"));
        userExt.setReg_Date(rs.getTimestamp("regDateUser"));

        userExt.setBlocked(rs.getBoolean("isBlocked"));
        userExt.setBlockedDescription(rs.getString("blockedDescription"));

        UserRole userRole = new UserRole(rs.getInt("userRoleID"), rs.getInt("userID"), rs.getInt("roleID"));
        userExt.setUserRole(userRole);

        return userExt;
    }

    /**
     * The method fills with data from a DB object of the class Author
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class Author
     * @throws SQLException
     */
    public static Author setAuthor(ResultSet rs) throws SQLException {
        Author author = new Author();

        author.setId(rs.getInt("id"));
        author.setFioRU(rs.getString("fio_ru"));
        author.setFioEN(rs.getString("fio_en"));
        author.setBirthDay(rs.getDate("birthday"));

        return author;
    }

    /**
     * The method fills with data from a DB object of the class Book
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class Book
     * @throws SQLException
     */
    public static Book setBook(ResultSet rs) throws SQLException {
        Book book = new Book();

        book.setId(rs.getInt("id"));
        book.setName(rs.getString("name"));
        book.setIsbn(rs.getString("isbn"));
        book.setShortDescription(rs.getString("shortDescription"));
        book.setAuthorID(rs.getInt("authorID"));
        book.setGenreID(rs.getInt("genreID"));
        book.setPublisherID(rs.getInt("publisherID"));
        book.setPublishYear(rs.getInt("publishYear"));
        book.setNumberCopies(rs.getInt("numberCopies"));
        book.setCoverImage(rs.getBytes("coverImage"));

        Author author = new Author();
        author.setId(rs.getInt("authorID"));
        author.setFioRU(rs.getString("authorFIO_RU"));
        author.setFioEN(rs.getString("authorFIO_EN"));
        author.setBirthDay(rs.getDate("authorBirthday"));
        book.setBookAuthor(author);

        Publisher publisher = new Publisher();
        publisher.setId(rs.getInt("publisherID"));
        publisher.setNameRU(rs.getString("publisherNameRU"));
        publisher.setNameEN(rs.getString("publisherNameEN"));
        book.setBookPublisher(publisher);

        BookSection bookSection = new BookSection();
        bookSection.setId(rs.getInt("bookSectionID"));
        bookSection.setNameRU(rs.getString("bookSectionNameRU"));
        bookSection.setNameEN(rs.getString("bookSectionNameEN"));

        BookGenre bookGenre = new BookGenre();
        bookGenre.setId(rs.getInt("genreID"));
        bookGenre.setNameRU(rs.getString("bookGenreNameRU"));
        bookGenre.setNameEN(rs.getString("bookGenreNameRU"));
        bookGenre.setBookSectionID(rs.getInt("bookSectionID"));
        bookGenre.setBookSection(bookSection);
        book.setBookGenre(bookGenre);

        book.setDeleted(rs.getBoolean("isDeleted"));

        return book;
    }

    /**
     * The method fills with data from a DB object of the class BookGenre
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class BookGenre
     * @throws SQLException
     */
    public static BookGenre setBookGenre(ResultSet rs) throws SQLException {
        BookGenre bookGenre = new BookGenre();

        bookGenre.setId(rs.getInt("id"));
        bookGenre.setNameRU(rs.getString("name_ru"));
        bookGenre.setNameEN(rs.getString("name_en"));
        bookGenre.setBookSectionID(rs.getInt("bookSectionID"));

        BookSection bookSection = new BookSection();
        bookSection.setId(rs.getInt("bookSectionID"));
        bookSection.setNameRU(rs.getString("bookSectionNameRU"));
        bookSection.setNameEN(rs.getString("bookSectionNameEN"));

        bookGenre.setBookSection(bookSection);

        return bookGenre;
    }

    /**
     * The method fills with data from a DB object of the class BookSection
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class BookSection
     * @throws SQLException
     */
    public static BookSection setBookSection(ResultSet rs) throws SQLException {
        BookSection bookSection = new BookSection();

        bookSection.setId(rs.getInt("id"));
        bookSection.setNameRU(rs.getString("name_ru"));
        bookSection.setNameEN(rs.getString("name_en"));

        return bookSection;
    }

    /**
     * The method fills with data from a DB object of the class OrderType
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class OrderType
     * @throws SQLException
     */
    public static OrderType setOrderType(ResultSet rs) throws SQLException {
        OrderType orderType = new OrderType();

        orderType.setId(rs.getInt("id"));
        orderType.setNameRU(rs.getString("name_ru"));
        orderType.setNameEN(rs.getString("name_en"));

        return orderType;
    }

    /**
     * The method fills with data from a DB object of the class Publisher
     * @param rs (type of ResultSet) object which contains data from a DB
     * @return object of a class Publisher
     * @throws SQLException
     */
    public static Publisher setPublisher(ResultSet rs) throws SQLException {
        Publisher publisher = new Publisher();

        publisher.setId(rs.getInt("id"));
        publisher.setNameRU(rs.getString("name_ru"));
        publisher.setNameEN(rs.getString("name_en"));

        return publisher;
    }
}
